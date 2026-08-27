package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.BadgeEntity
import com.example.data.model.CommitmentContractEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.data.model.IncomeIdea
import com.example.data.model.IncomeIdeaCategory
import com.example.data.model.IncomeIdeaLibraryData
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LeaderboardMember
import com.example.data.model.LeaderboardMetric
import com.example.data.model.LeaderboardTimeframe
import com.example.data.model.SovereignCommunityPeers
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.MastermindGroupEntity
import com.example.data.model.MastermindMemberEntity
import com.example.data.model.ModuleAudioScript
import com.example.data.model.ModuleCompletionCelebrationInfo
import com.example.data.model.ModuleCompletionReflectionData
import com.example.data.model.ModuleEntity
import com.example.data.model.ModuleReflectionPromptsProvider
import com.example.data.model.MoneyBlueprintQuizQuestions
import com.example.data.model.MoneyBlueprintResultEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.ShortLessonEntity
import com.example.data.model.SECTION_ACHIEVEMENTS
import com.example.data.model.SectionAchievementInfo
import com.example.data.model.ThinkAndGrowRichAffirmation
import com.example.data.model.UserProfileEntity
import com.example.data.model.VisionBoardItemEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity
import com.example.data.model.getSectionForModule
import com.example.data.model.isSectionCompleted
import com.example.data.remote.firebase.AuthUserState
import com.example.data.remote.firebase.CloudSyncState
import com.example.data.remote.firebase.CloudSyncStatus
import com.example.data.remote.firebase.FirebaseAuthManager
import com.example.data.remote.firebase.FirestoreSyncManager
import com.example.data.remote.gemini.AdvisorRole
import com.example.data.remote.gemini.ChatMessage
import com.example.data.remote.gemini.ChatSender
import com.example.data.remote.gemini.GeminiChatService
import com.example.data.remote.gemini.GeminiModelChoice
import com.example.data.repository.OfflineCacheStatus
import com.example.data.repository.RichesRepository
import com.example.ui.components.STREAK_MILESTONES
import com.example.ui.components.StreakMilestoneInfo
import com.example.ui.components.getMilestoneForDays
import com.example.ui.theme.AppThemeMode
import com.example.util.NotebookPdfExporter
import com.example.util.PdfExportResult
import com.example.util.TtsEngineManager
import com.example.util.TtsPlayerState
import com.example.data.model.WeeklyProgressDigest
import com.example.util.WeeklyDigestImageExporter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class ScreenRoute {
    object Landing : ScreenRoute()
    object Onboarding : ScreenRoute()
    object Assessment : ScreenRoute()
    data class MoneyBlueprint(val isRetake: Boolean = false) : ScreenRoute()
    object Dashboard : ScreenRoute()
    object ModulesPath : ScreenRoute()
    data class ModuleDetail(val moduleId: Int) : ScreenRoute()
    object MasterMindChat : ScreenRoute()
    object MasterMindCircle : ScreenRoute()
    object VisionBoard : ScreenRoute()
    object Notebook : ScreenRoute()
    object ProfileBadges : ScreenRoute()
    object AdminPanel : ScreenRoute()
    object WealthGoalTracker : ScreenRoute()
    data class SuccessStoryLibrary(
        val initialFigureId: String? = null,
        val initialPrinciple: String? = null
    ) : ScreenRoute()
    data class IncomeIdeaExplorer(
        val initialCategoryId: String? = null,
        val filterSavedOnly: Boolean = false
    ) : ScreenRoute()
    object GivingTracker : ScreenRoute()
    object Leaderboard : ScreenRoute()
    object DecisionLog : ScreenRoute()
    object WeeklyDigest : ScreenRoute()
    object CommitmentContract : ScreenRoute()
}

data class MoneyBlueprintQuizState(
    val currentStep: Int = 0,
    val answers: Map<Int, Int> = emptyMap(),
    val isRevealingResult: Boolean = false,
    val latestResult: MoneyBlueprintResultEntity? = null,
    val showHistoryDialog: Boolean = false
)

data class MindsetAssessmentState(
    val currentStep: Int = 0,
    val beliefScore: Int = 50,
    val disciplineScore: Int = 50,
    val desireScore: Int = 50,
    val persistenceScore: Int = 50,
    val identityScore: Int = 50,
    val isRevealingScore: Boolean = false
)

data class PdfExportUiState(
    val isExporting: Boolean = false,
    val exportResult: PdfExportResult? = null,
    val errorMessage: String? = null,
    val showExportDialog: Boolean = false
)

class RichesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RichesRepository
    private val geminiChatService = GeminiChatService()
    private val firebaseAuthManager = FirebaseAuthManager(application)
    private val firestoreSyncManager = FirestoreSyncManager()
    private val ttsEngineManager = TtsEngineManager(application)
    private val voiceMemoRecorder = com.example.util.VoiceMemoRecorder(application)
    private val shortLessonPlayerManager = com.example.util.ShortLessonPlayerManager(
        context = application,
        onLessonAutoCompleted = { lesson ->
            completeShortLesson(lesson.id)
        }
    )

    val ttsPlayerState: StateFlow<TtsPlayerState> = ttsEngineManager.playerState
    val voiceMemoUiState: StateFlow<com.example.util.VoiceMemoUiState> = voiceMemoRecorder.uiState
    val shortLessonPlayerState: StateFlow<com.example.util.ShortLessonPlayerState> = shortLessonPlayerManager.playerState

    val modules: StateFlow<List<ModuleEntity>>
    val allShortLessons: StateFlow<List<ShortLessonEntity>>
    val completedShortLessonsCount: StateFlow<Int>
    val userProfile: StateFlow<UserProfileEntity?>
    val notebookEntries: StateFlow<List<NotebookEntryEntity>>
    val badges: StateFlow<List<BadgeEntity>>
    val completedModulesCount: StateFlow<Int>
    val notebookEntriesCount: StateFlow<Int>
    val habits: StateFlow<List<DailyHabitEntity>>
    val allHabitLogs: StateFlow<List<DailyHabitLogEntity>>
    val todayHabitLogs: StateFlow<List<DailyHabitLogEntity>>

    // Mastermind Circles Flows
    val userMastermindGroup: StateFlow<MastermindGroupEntity?>
    val allMastermindGroups: StateFlow<List<MastermindGroupEntity>>
    val allMastermindMembers: StateFlow<List<MastermindMemberEntity>>
    val allMastermindCheckins: StateFlow<List<MastermindCheckinEntity>>
    val currentGroupMembers: StateFlow<List<MastermindMemberEntity>>
    val currentGroupWeeklyCheckins: StateFlow<List<MastermindCheckinEntity>>
    val currentGroupAllCheckins: StateFlow<List<MastermindCheckinEntity>>

    // Vision Board Flows & UI States
    val allVisionBoardItems: StateFlow<List<VisionBoardItemEntity>>
    private val _selectedVisionCategory = MutableStateFlow("all")
    val selectedVisionCategory: StateFlow<String> = _selectedVisionCategory.asStateFlow()

    private val _showAddVisionItemDialog = MutableStateFlow(false)
    val showAddVisionItemDialog: StateFlow<Boolean> = _showAddVisionItemDialog.asStateFlow()

    private val _editingVisionItem = MutableStateFlow<VisionBoardItemEntity?>(null)
    val editingVisionItem: StateFlow<VisionBoardItemEntity?> = _editingVisionItem.asStateFlow()

    // Wealth Goal Tracker Flows & UI States
    val primaryWealthGoal: StateFlow<WealthGoalEntity?>
    val wealthGoalLogs: StateFlow<List<WealthGoalLogEntity>>

    // Gratitude & Giving Tracker Flows & UI States
    val allGivingLogs: StateFlow<List<GivingLogEntity>>
    val givingGoal: StateFlow<GivingGoalEntity?>
    val givingLogsCount: StateFlow<Int>
    val givingStreakWeeks: StateFlow<Int>
    val givingBestStreakWeeks: StateFlow<Int>

    // Commitment Contracts Flows & UI States
    val allCommitmentContracts: StateFlow<List<CommitmentContractEntity>>
    val activeCommitmentContract: StateFlow<CommitmentContractEntity?>
    val activeCommitmentContracts: StateFlow<List<CommitmentContractEntity>>
    val completedCommitmentsCount: StateFlow<Int>

    private val _showCreateCommitmentDialog = MutableStateFlow(false)
    val showCreateCommitmentDialog: StateFlow<Boolean> = _showCreateCommitmentDialog.asStateFlow()
    val showCreateContractDialog: StateFlow<Boolean> = showCreateCommitmentDialog

    private val _showRenewCommitmentDialog = MutableStateFlow<CommitmentContractEntity?>(null)
    val showRenewCommitmentDialog: StateFlow<CommitmentContractEntity?> = _showRenewCommitmentDialog.asStateFlow()
    val contractForRenewal: StateFlow<CommitmentContractEntity?> = showRenewCommitmentDialog

    private val _showCompleteCommitmentDialog = MutableStateFlow<CommitmentContractEntity?>(null)
    val showCompleteCommitmentDialog: StateFlow<CommitmentContractEntity?> = _showCompleteCommitmentDialog.asStateFlow()
    val contractForCompletion: StateFlow<CommitmentContractEntity?> = showCompleteCommitmentDialog

    private val _showLogGivingDialog = MutableStateFlow(false)
    val showLogGivingDialog: StateFlow<Boolean> = _showLogGivingDialog.asStateFlow()

    private val _showGivingGoalDialog = MutableStateFlow(false)
    val showGivingGoalDialog: StateFlow<Boolean> = _showGivingGoalDialog.asStateFlow()

    private val _editingGivingLog = MutableStateFlow<GivingLogEntity?>(null)
    val editingGivingLog: StateFlow<GivingLogEntity?> = _editingGivingLog.asStateFlow()

    private val _givingCategoryFilter = MutableStateFlow("ALL")
    val givingCategoryFilter: StateFlow<String> = _givingCategoryFilter.asStateFlow()

    private val _isGivingAmountsHidden = MutableStateFlow(false)
    val isGivingAmountsHidden: StateFlow<Boolean> = _isGivingAmountsHidden.asStateFlow()

    private val _showEditWealthGoalDialog = MutableStateFlow(false)
    val showEditWealthGoalDialog: StateFlow<Boolean> = _showEditWealthGoalDialog.asStateFlow()

    private val _showLogContributionDialog = MutableStateFlow(false)
    val showLogContributionDialog: StateFlow<Boolean> = _showLogContributionDialog.asStateFlow()

    private val _isVisionContemplationActive = MutableStateFlow(false)
    val isVisionContemplationActive: StateFlow<Boolean> = _isVisionContemplationActive.asStateFlow()

    private val _visionContemplationSecondsRemaining = MutableStateFlow(60)
    val visionContemplationSecondsRemaining: StateFlow<Int> = _visionContemplationSecondsRemaining.asStateFlow()

    val authUserState: StateFlow<AuthUserState> = firebaseAuthManager.userState
    val cloudSyncState: StateFlow<CloudSyncState> = firestoreSyncManager.syncState

    private val sharedPrefs = application.getSharedPreferences("riches_theme_prefs", Context.MODE_PRIVATE)
    private val _themeMode = MutableStateFlow(
        AppThemeMode.fromString(sharedPrefs.getString("app_theme_mode", AppThemeMode.DARK.name))
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _isFloatingMoneyBubblesEnabled = MutableStateFlow(
        sharedPrefs.getBoolean("floating_money_bubbles_enabled", true)
    )
    val isFloatingMoneyBubblesEnabled: StateFlow<Boolean> = _isFloatingMoneyBubblesEnabled.asStateFlow()

    private val _offlineCacheStatus = MutableStateFlow(OfflineCacheStatus())
    val offlineCacheStatus: StateFlow<OfflineCacheStatus> = _offlineCacheStatus.asStateFlow()

    // Leaderboard state flows
    private val _leaderboardMetric = MutableStateFlow(LeaderboardMetric.XP)
    val leaderboardMetric: StateFlow<LeaderboardMetric> = _leaderboardMetric.asStateFlow()

    private val _leaderboardTimeframe = MutableStateFlow(LeaderboardTimeframe.THIS_WEEK)
    val leaderboardTimeframe: StateFlow<LeaderboardTimeframe> = _leaderboardTimeframe.asStateFlow()

    private val _currentScreen = MutableStateFlow<ScreenRoute>(ScreenRoute.Dashboard)
    val currentScreen: StateFlow<ScreenRoute> = _currentScreen.asStateFlow()

    private val _showPaywallModal = MutableStateFlow(false)
    val showPaywallModal: StateFlow<Boolean> = _showPaywallModal.asStateFlow()

    // Mastermind Circles UI States
    private val _showJoinCircleDialog = MutableStateFlow(false)
    val showJoinCircleDialog: StateFlow<Boolean> = _showJoinCircleDialog.asStateFlow()

    private val _showCreateCircleDialog = MutableStateFlow(false)
    val showCreateCircleDialog: StateFlow<Boolean> = _showCreateCircleDialog.asStateFlow()

    private val _showWeeklyCheckinDialog = MutableStateFlow(false)
    val showWeeklyCheckinDialog: StateFlow<Boolean> = _showWeeklyCheckinDialog.asStateFlow()

    private val _mastermindInviteCodeInput = MutableStateFlow("")
    val mastermindInviteCodeInput: StateFlow<String> = _mastermindInviteCodeInput.asStateFlow()

    // Habit Tracking UI States
    private val _selectedHabitDateEpochDay = MutableStateFlow(RichesRepository.getTodayEpochDay())
    val selectedHabitDateEpochDay: StateFlow<Long> = _selectedHabitDateEpochDay.asStateFlow()

    private val _activeHabitForModal = MutableStateFlow<DailyHabitEntity?>(null)
    val activeHabitForModal: StateFlow<DailyHabitEntity?> = _activeHabitForModal.asStateFlow()

    private val _showCustomHabitDialog = MutableStateFlow(false)
    val showCustomHabitDialog: StateFlow<Boolean> = _showCustomHabitDialog.asStateFlow()

    private val _showEditChiefAimDialog = MutableStateFlow(false)
    val showEditChiefAimDialog: StateFlow<Boolean> = _showEditChiefAimDialog.asStateFlow()

    private val _assessmentState = MutableStateFlow(MindsetAssessmentState())
    val assessmentState: StateFlow<MindsetAssessmentState> = _assessmentState.asStateFlow()

    // Money Blueprint Flows & Quiz UI States
    val allBlueprintResults: StateFlow<List<MoneyBlueprintResultEntity>>
    val latestBlueprintResult: StateFlow<MoneyBlueprintResultEntity?>
    private val _blueprintQuizState = MutableStateFlow(MoneyBlueprintQuizState())
    val blueprintQuizState: StateFlow<MoneyBlueprintQuizState> = _blueprintQuizState.asStateFlow()

    // Saved Income Idea Shortlist
    val savedIncomeIdeaIds: StateFlow<Set<String>>

    // Onboarding Step Logs & Telemetry
    val allOnboardingStepLogs: StateFlow<List<com.example.data.model.OnboardingStepLogEntity>>

    private val _notebookSearchQuery = MutableStateFlow("")
    val notebookSearchQuery: StateFlow<String> = _notebookSearchQuery.asStateFlow()

    private val _selectedNotebookTag = MutableStateFlow("All")
    val selectedNotebookTag: StateFlow<String> = _selectedNotebookTag.asStateFlow()

    private val _showNewNoteDialog = MutableStateFlow(false)
    val showNewNoteDialog: StateFlow<Boolean> = _showNewNoteDialog.asStateFlow()

    private val _showFearReframeDialog = MutableStateFlow(false)
    val showFearReframeDialog: StateFlow<Boolean> = _showFearReframeDialog.asStateFlow()

    private val _showDecisionLogDialog = MutableStateFlow(false)
    val showDecisionLogDialog: StateFlow<Boolean> = _showDecisionLogDialog.asStateFlow()

    private val _showMoneyMindsetDialog = MutableStateFlow(false)
    val showMoneyMindsetDialog: StateFlow<Boolean> = _showMoneyMindsetDialog.asStateFlow()

    private val _showPersistenceCheckDialog = MutableStateFlow(false)
    val showPersistenceCheckDialog: StateFlow<Boolean> = _showPersistenceCheckDialog.asStateFlow()

    private val _pendingPersistenceStreakType = MutableStateFlow("Daily Sovereign Ritual")
    val pendingPersistenceStreakType: StateFlow<String> = _pendingPersistenceStreakType.asStateFlow()

    private val _activeDecisionForRevisit = MutableStateFlow<NotebookEntryEntity?>(null)
    val activeDecisionForRevisit: StateFlow<NotebookEntryEntity?> = _activeDecisionForRevisit.asStateFlow()

    private val _activeNoteForEdit = MutableStateFlow<NotebookEntryEntity?>(null)
    val activeNoteForEdit: StateFlow<NotebookEntryEntity?> = _activeNoteForEdit.asStateFlow()

    private val _isAmbientSoundPlaying = MutableStateFlow(false)
    val isAmbientSoundPlaying: StateFlow<Boolean> = _isAmbientSoundPlaying.asStateFlow()

    private val _celebrationXpMessage = MutableStateFlow<String?>(null)
    val celebrationXpMessage: StateFlow<String?> = _celebrationXpMessage.asStateFlow()

    // Subtle Streak Animation Trigger Event
    private val _streakAnimationTrigger = MutableStateFlow<Long?>(null)
    val streakAnimationTrigger: StateFlow<Long?> = _streakAnimationTrigger.asStateFlow()

    // Streak Milestone Celebratory Animation State (3, 7, 14, 30 Days)
    private val _activeMilestoneCelebration = MutableStateFlow<StreakMilestoneInfo?>(null)
    val activeMilestoneCelebration: StateFlow<StreakMilestoneInfo?> = _activeMilestoneCelebration.asStateFlow()

    private val celebratedMilestoneDays = mutableSetOf<Int>()

    // Section Achievement Celebratory Animation & Notification States
    private val _activeSectionAchievementCelebration = MutableStateFlow<SectionAchievementInfo?>(null)
    val activeSectionAchievementCelebration: StateFlow<SectionAchievementInfo?> = _activeSectionAchievementCelebration.asStateFlow()

    // Module Completion Celebration State (Closing Reflection)
    private val _activeModuleCompletionCelebration = MutableStateFlow<ModuleCompletionCelebrationInfo?>(null)
    val activeModuleCompletionCelebration: StateFlow<ModuleCompletionCelebrationInfo?> = _activeModuleCompletionCelebration.asStateFlow()

    private val _sectionAchievementToast = MutableStateFlow<SectionAchievementInfo?>(null)
    val sectionAchievementToast: StateFlow<SectionAchievementInfo?> = _sectionAchievementToast.asStateFlow()

    private val celebratedSectionIds = mutableSetOf<Int>()

    // Weekly Progress Digest Dismissal State
    private fun getWeeklyDigestDismissKey(): String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val week = cal.get(Calendar.WEEK_OF_YEAR)
        return "weekly_digest_dismissed_${year}_${week}"
    }

    private val _isWeeklyDigestDismissed = MutableStateFlow(
        sharedPrefs.getBoolean(getWeeklyDigestDismissKey(), false)
    )
    val isWeeklyDigestDismissed: StateFlow<Boolean> = _isWeeklyDigestDismissed.asStateFlow()

    fun dismissWeeklyDigest() {
        val key = getWeeklyDigestDismissKey()
        sharedPrefs.edit().putBoolean(key, true).apply()
        _isWeeklyDigestDismissed.value = true
    }

    fun exportAndShareWeeklyDigest(context: Context, digest: WeeklyProgressDigest) {
        val result = WeeklyDigestImageExporter.exportAndShareDigestImage(
            context = context,
            digest = digest,
            userProfile = userProfile.value
        )
        result.onSuccess {
            showCelebration("Weekly Progress Digest Image Exported! 👑")
        }.onFailure { err ->
            showCelebration("Failed to export image: ${err.message ?: "Unknown error"}")
        }
    }

    fun triggerModuleCompletionCelebration(info: ModuleCompletionCelebrationInfo) {
        _activeModuleCompletionCelebration.value = info
    }

    fun dismissModuleCompletionCelebration() {
        _activeModuleCompletionCelebration.value = null
    }

    fun triggerMilestoneCelebration(milestone: StreakMilestoneInfo) {
        _activeMilestoneCelebration.value = milestone
    }

    fun triggerMilestoneCelebrationByDays(days: Int) {
        val m = getMilestoneForDays(days) ?: STREAK_MILESTONES.first()
        _activeMilestoneCelebration.value = m
    }

    fun dismissMilestoneCelebration() {
        _activeMilestoneCelebration.value = null
    }

    fun checkAndTriggerMilestoneCelebration(streak: Int) {
        val milestone = getMilestoneForDays(streak)
        if (milestone != null && !celebratedMilestoneDays.contains(streak)) {
            celebratedMilestoneDays.add(streak)
            _activeMilestoneCelebration.value = milestone
            showCelebration("🌟 Sovereign Milestone! ${milestone.title} (+${milestone.xpReward} XP)")
        }
    }

    fun triggerSectionAchievement(section: SectionAchievementInfo) {
        _activeSectionAchievementCelebration.value = section
    }

    fun dismissSectionAchievement() {
        _activeSectionAchievementCelebration.value = null
    }

    fun dismissSectionToast() {
        _sectionAchievementToast.value = null
    }

    private fun checkAndTriggerSectionAchievement(completedModuleId: Int) {
        val section = getSectionForModule(completedModuleId) ?: return
        if (celebratedSectionIds.contains(section.sectionId)) return

        val currentModules = modules.value
        val allSectionDone = section.moduleIds.all { id ->
            if (id == completedModuleId) true
            else currentModules.firstOrNull { it.id == id }?.isCompleted == true
        }

        if (allSectionDone) {
            celebratedSectionIds.add(section.sectionId)
            _activeSectionAchievementCelebration.value = section
            _sectionAchievementToast.value = section
            showCelebration("🏆 ACHIEVEMENT UNLOCKED! ${section.title} (+${section.xpReward} XP) ✨")
        }
    }

    // --- Gemini Chatbot States ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _selectedAdvisorRole = MutableStateFlow(AdvisorRole.NAPOLEON_HILL)
    val selectedAdvisorRole: StateFlow<AdvisorRole> = _selectedAdvisorRole.asStateFlow()

    private val _selectedGeminiModel = MutableStateFlow(GeminiModelChoice.GEMINI_FLASH)
    val selectedGeminiModel: StateFlow<GeminiModelChoice> = _selectedGeminiModel.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private val _pdfExportState = MutableStateFlow(PdfExportUiState())
    val pdfExportState: StateFlow<PdfExportUiState> = _pdfExportState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = RichesRepository(db.richesDao())

        modules = repository.allModules.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        userProfile = repository.userProfile.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        notebookEntries = repository.allNotebookEntries.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        badges = repository.allBadges.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        completedModulesCount = repository.completedModulesCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        allShortLessons = repository.allShortLessons.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        completedShortLessonsCount = repository.completedShortLessonsCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        notebookEntriesCount = repository.notebookEntriesCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        habits = repository.allHabits.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allHabitLogs = repository.allHabitLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        todayHabitLogs = _selectedHabitDateEpochDay.flatMapLatest { epochDay ->
            repository.getHabitLogsForDay(epochDay)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Mastermind Flows initialization
        userMastermindGroup = repository.userMastermindGroup.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        allMastermindGroups = repository.allMastermindGroups.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allMastermindMembers = repository.allMastermindMembers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allMastermindCheckins = repository.allMastermindCheckins.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allVisionBoardItems = repository.allVisionBoardItems.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        primaryWealthGoal = repository.primaryWealthGoal.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        wealthGoalLogs = repository.wealthGoalLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allBlueprintResults = repository.allMoneyBlueprintResults.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        latestBlueprintResult = repository.latestMoneyBlueprintResult.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        savedIncomeIdeaIds = repository.savedIncomeIdeaIds.map { it.toSet() }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

        allGivingLogs = repository.allGivingLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        givingGoal = repository.givingGoal.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        givingLogsCount = repository.givingLogsCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        givingStreakWeeks = allGivingLogs.map { logs ->
            RichesRepository.calculateGivingStreakWeeks(logs).first
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        givingBestStreakWeeks = allGivingLogs.map { logs ->
            RichesRepository.calculateGivingStreakWeeks(logs).second
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        currentGroupMembers = userMastermindGroup.flatMapLatest { group ->
            if (group != null) {
                repository.getGroupMembers(group.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        currentGroupWeeklyCheckins = userMastermindGroup.flatMapLatest { group ->
            if (group != null) {
                val cal = Calendar.getInstance()
                val currentWeek = cal.get(Calendar.WEEK_OF_YEAR)
                val currentYear = cal.get(Calendar.YEAR)
                repository.getGroupWeeklyCheckins(group.id, currentWeek, currentYear)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        currentGroupAllCheckins = userMastermindGroup.flatMapLatest { group ->
            if (group != null) {
                repository.getAllCheckinsForGroup(group.id)
            } else {
                flowOf(emptyList())
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allOnboardingStepLogs = repository.allOnboardingStepLogs.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allCommitmentContracts = repository.allCommitmentContracts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        activeCommitmentContract = repository.activeCommitmentContract.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

        activeCommitmentContracts = repository.activeCommitmentContracts.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        completedCommitmentsCount = repository.completedCommitmentsCount.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        viewModelScope.launch {
            repository.initializeIfNeeded()
            repository.recordDailyLogin()
            refreshOfflineCacheStatus()
            
            // Seed initial welcome message from Napoleon Hill
            _chatMessages.value = listOf(
                ChatMessage(
                    sender = ChatSender.MODEL,
                    text = "Greetings, Initiate. I am Napoleon Hill. You stand at the threshold of the Master Mind Council. Whatever the mind can conceive and believe, it can achieve. How may I direct your Definite Major Purpose today?",
                    advisorRole = AdvisorRole.NAPOLEON_HILL,
                    modelUsed = GeminiModelChoice.GEMINI_FLASH.displayName
                )
            )

            // Check onboarding state and pending persistence checks
            userProfile.collect { profile ->
                if (profile != null) {
                    if (!profile.hasCompletedOnboarding && _currentScreen.value == ScreenRoute.Dashboard) {
                        _currentScreen.value = ScreenRoute.Onboarding
                    }
                    if (profile.hasPendingPersistenceCheck && !_showPersistenceCheckDialog.value) {
                        _pendingPersistenceStreakType.value = profile.pendingPersistenceStreakType.ifBlank { "Daily Sovereign Ritual" }
                        _showPersistenceCheckDialog.value = true
                    }
                }
            }
        }
    }

    // --- Habit Tracking Methods ---

    fun selectHabitDate(epochDay: Long) {
        _selectedHabitDateEpochDay.value = epochDay
    }

    fun toggleHabit(habitId: String, durationMinutes: Int? = null, notes: String = "") {
        viewModelScope.launch {
            val dateEpochDay = _selectedHabitDateEpochDay.value
            val isNowCompleted = repository.toggleHabitCompletion(
                habitId = habitId,
                dateEpochDay = dateEpochDay,
                durationMinutes = durationMinutes,
                notes = notes
            )
            val habit = habits.value.firstOrNull { it.id == habitId }
            val title = habit?.title ?: "Ritual"
            if (isNowCompleted) {
                _streakAnimationTrigger.value = System.currentTimeMillis()
                val xp = habit?.xpReward ?: 30
                showCelebration("+$xp XP! Sealed '$title' in Room Ledger ✨")
                val profile = userProfile.value
                val streak = profile?.currentStreak ?: 1
                checkAndTriggerMilestoneCelebration(streak)
            } else {
                showCelebration("Unmarked '$title'")
            }
            refreshOfflineCacheStatus()
        }
    }

    fun logHabitWithReflection(
        habitId: String,
        durationMinutes: Int,
        notes: String,
        saveToNotebook: Boolean = false
    ) {
        viewModelScope.launch {
            val dateEpochDay = _selectedHabitDateEpochDay.value
            repository.logHabitWithReflection(
                habitId = habitId,
                dateEpochDay = dateEpochDay,
                durationMinutes = durationMinutes,
                notes = notes,
                saveToNotebook = saveToNotebook
            )
            val habit = habits.value.firstOrNull { it.id == habitId }
            val title = habit?.title ?: "Ritual"
            val xp = (habit?.xpReward ?: 30) + (if (saveToNotebook) 50 else 0)
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("+$xp XP! Logged '$title' with reflection ✨")
            val profile = userProfile.value
            val streak = profile?.currentStreak ?: 1
            checkAndTriggerMilestoneCelebration(streak)
            _activeHabitForModal.value = null
            refreshOfflineCacheStatus()
        }
    }

    fun openHabitDetailModal(habit: DailyHabitEntity?) {
        _activeHabitForModal.value = habit
    }

    fun closeHabitDetailModal() {
        _activeHabitForModal.value = null
    }

    fun setShowCustomHabitDialog(show: Boolean) {
        _showCustomHabitDialog.value = show
    }

    fun addCustomHabit(
        title: String,
        principle: String,
        description: String,
        category: String = "Mindset",
        iconKey: String = "affirmation",
        targetMinutes: Int = 15,
        xpReward: Int = 30
    ) {
        viewModelScope.launch {
            repository.addCustomHabit(
                title = title,
                principle = principle,
                description = description,
                category = category,
                iconKey = iconKey,
                targetMinutes = targetMinutes,
                xpReward = xpReward
            )
            showCelebration("Created Custom Ritual: $title ✨")
            _showCustomHabitDialog.value = false
        }
    }

    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
            showCelebration("Ritual Removed from Roster")
        }
    }

    fun refreshOfflineCacheStatus() {
        viewModelScope.launch {
            _offlineCacheStatus.value = repository.getOfflineCacheStatus()
        }
    }

    fun navigateTo(route: ScreenRoute) {
        _currentScreen.value = route
    }

    fun setPaywallVisible(visible: Boolean) {
        _showPaywallModal.value = visible
    }

    fun setNotebookSearchQuery(query: String) {
        _notebookSearchQuery.value = query
    }

    fun setSelectedNotebookTag(tag: String) {
        _selectedNotebookTag.value = tag
    }

    fun setShowNewNoteDialog(show: Boolean, note: NotebookEntryEntity? = null) {
        _activeNoteForEdit.value = note
        _showNewNoteDialog.value = show
    }

    fun toggleAmbientSound() {
        _isAmbientSoundPlaying.value = !_isAmbientSoundPlaying.value
    }

    fun updateAssessmentDimension(dimension: String, value: Int) {
        val current = _assessmentState.value
        _assessmentState.value = when (dimension) {
            "belief" -> current.copy(beliefScore = value)
            "discipline" -> current.copy(disciplineScore = value)
            "desire" -> current.copy(desireScore = value)
            "persistence" -> current.copy(persistenceScore = value)
            "identity" -> current.copy(identityScore = value)
            else -> current
        }
    }

    fun nextAssessmentStep() {
        val current = _assessmentState.value
        if (current.currentStep < 4) {
            _assessmentState.value = current.copy(currentStep = current.currentStep + 1)
        } else {
            // Completed all steps
            viewModelScope.launch {
                repository.completeMindsetAssessment(
                    belief = current.beliefScore,
                    discipline = current.disciplineScore,
                    desire = current.desireScore,
                    persistence = current.persistenceScore,
                    identity = current.identityScore
                )
                _assessmentState.value = current.copy(isRevealingScore = true)
                showCelebration("Assessment Complete! +250 XP Awarded")
                syncCloudNow()
            }
        }
    }

    fun prevAssessmentStep() {
        val current = _assessmentState.value
        if (current.currentStep > 0) {
            _assessmentState.value = current.copy(currentStep = current.currentStep - 1)
        }
    }

    // --- Money Blueprint Quiz Actions ---
    fun startMoneyBlueprintQuiz(isRetake: Boolean = false) {
        val initialMap = mutableMapOf<Int, Int>()
        MoneyBlueprintQuizQuestions.questions.forEach { q ->
            initialMap[q.id] = 50
        }
        _blueprintQuizState.value = MoneyBlueprintQuizState(
            currentStep = 0,
            answers = initialMap,
            isRevealingResult = false,
            latestResult = null
        )
        _currentScreen.value = ScreenRoute.MoneyBlueprint(isRetake)
    }

    fun answerBlueprintQuestion(questionId: Int, score: Int) {
        val currentAnswers = _blueprintQuizState.value.answers.toMutableMap()
        currentAnswers[questionId] = score
        _blueprintQuizState.value = _blueprintQuizState.value.copy(answers = currentAnswers)
    }

    fun nextBlueprintQuestion() {
        val current = _blueprintQuizState.value
        val questions = MoneyBlueprintQuizQuestions.questions
        if (current.currentStep < questions.size - 1) {
            _blueprintQuizState.value = current.copy(currentStep = current.currentStep + 1)
        } else {
            submitMoneyBlueprint()
        }
    }

    fun prevBlueprintQuestion() {
        val current = _blueprintQuizState.value
        if (current.currentStep > 0) {
            _blueprintQuizState.value = current.copy(currentStep = current.currentStep - 1)
        }
    }

    fun submitMoneyBlueprint() {
        val answers = _blueprintQuizState.value.answers
        val questions = MoneyBlueprintQuizQuestions.questions

        fun getAvgForCategory(categoryKey: String): Int {
            val qs = questions.filter { it.categoryKey == categoryKey }
            if (qs.isEmpty()) return 50
            val sum = qs.sumOf { answers[it.id] ?: 50 }
            return (sum / qs.size).coerceIn(0, 100)
        }

        val scarcity = getAvgForCategory(MoneyBlueprintResultEntity.PATTERN_SCARCITY)
        val guilt = getAvgForCategory(MoneyBlueprintResultEntity.PATTERN_GUILT)
        val fearFailure = getAvgForCategory(MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE)
        val fearJudgment = getAvgForCategory(MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT)
        val selfWorth = getAvgForCategory(MoneyBlueprintResultEntity.PATTERN_SELF_WORTH)

        viewModelScope.launch {
            val result = repository.completeMoneyBlueprintAssessment(
                scarcityScore = scarcity,
                guiltScore = guilt,
                fearFailureScore = fearFailure,
                fearJudgmentScore = fearJudgment,
                selfWorthScore = selfWorth
            )
            _blueprintQuizState.value = _blueprintQuizState.value.copy(
                isRevealingResult = true,
                latestResult = result
            )
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Money Blueprint Diagnosed! 🏛️ +250 XP Awarded")
            syncCloudNow()
        }
    }

    fun finishMoneyBlueprintReveal() {
        _blueprintQuizState.value = MoneyBlueprintQuizState()
        _currentScreen.value = ScreenRoute.Dashboard
    }

    fun setShowBlueprintHistoryDialog(show: Boolean) {
        _blueprintQuizState.value = _blueprintQuizState.value.copy(showHistoryDialog = show)
    }

    fun deleteBlueprintResult(id: Long) {
        viewModelScope.launch {
            repository.deleteMoneyBlueprintResult(id)
            showCelebration("Blueprint Entry Removed from History")
            syncCloudNow()
        }
    }

    fun saveBlueprintReflectionToNotebook(result: MoneyBlueprintResultEntity) {
        viewModelScope.launch {
            repository.addNotebookEntry(
                moduleId = null,
                moduleTitle = "Money Blueprint: ${result.primaryPatternTitle}",
                title = "Money Blueprint Diagnosis - ${result.primaryPatternTitle}",
                content = "${result.summaryInsight}\n\nKey Categories:\n• Scarcity: ${result.scarcityScore}%\n• Guilt: ${result.guiltScore}%\n• Fear of Loss: ${result.fearFailureScore}%\n• Fear of Judgment: ${result.fearJudgmentScore}%\n• Self-Worth / Imposter: ${result.selfWorthScore}%\n\nAction Commitment:\n${result.actionPledge}",
                promptQuestion = "What subconscious pattern am I transmuting today to elevate my wealth capacity?",
                tags = "Money Blueprint, Mindset, Limiting Beliefs, Diagnosis",
                isFavorite = true
            )
            showCelebration("Blueprint Inscribed in Sovereign Notebook! ✨ +50 XP")
            syncCloudNow()
        }
    }

    fun completeDailyRitual() {
        viewModelScope.launch {
            val success = repository.completeDailyRitual()
            if (success) {
                _streakAnimationTrigger.value = System.currentTimeMillis()
                showCelebration("Daily Ritual Sealed! 🔥 Streak Maintained & +50 XP")
                syncCloudNow()
            }
        }
    }

    fun triggerStreakAnimation() {
        _streakAnimationTrigger.value = System.currentTimeMillis()
    }

    fun completeLesson(moduleId: Int) {
        viewModelScope.launch {
            repository.completeLesson(moduleId)
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Vault #$moduleId Lecture Completed! +50 XP")
            checkAndTriggerSectionAchievement(moduleId)
            syncCloudNow()
        }
    }

    fun submitModuleCompletionReflection(
        moduleId: Int,
        answers: Map<String, String>,
        onSuccess: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            val module = modules.value.firstOrNull { it.id == moduleId }
            val entry = repository.submitModuleCompletionReflection(moduleId, answers)
            _streakAnimationTrigger.value = System.currentTimeMillis()

            if (module != null) {
                val xpAward = module.xpReward.takeIf { it > 0 } ?: 100
                _activeModuleCompletionCelebration.value = ModuleCompletionCelebrationInfo(
                    moduleId = module.id,
                    vaultOrder = module.order,
                    moduleTitle = module.title,
                    principleName = module.originalPrinciple,
                    xpEarned = xpAward,
                    completedTimestamp = System.currentTimeMillis()
                )
                showCelebration("Vault #${module.order} Conquered! Principle Sealed into Sovereign Memory ✨ +$xpAward XP")
                checkAndTriggerSectionAchievement(moduleId)
            } else {
                showCelebration("Module Reflection Inscribed & Sealed! +100 XP ✨")
            }
            syncCloudNow()
            onSuccess?.invoke()
        }
    }

    fun completeQuest(moduleId: Int) {
        viewModelScope.launch {
            repository.completeQuest(moduleId)
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Sovereign Quest Fulfilled! +100 XP")
            checkAndTriggerSectionAchievement(moduleId)
            syncCloudNow()
        }
    }

    fun saveWorksheet(moduleId: Int, answer1: String, answer2: String, answer3: String) {
        viewModelScope.launch {
            repository.saveWorksheet(moduleId, answer1, answer2, answer3)
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Worksheet Saved to Vault Archives! +50 XP")
            syncCloudNow()
        }
    }

    fun saveNotebookEntry(
        moduleId: Int?,
        moduleTitle: String,
        title: String,
        content: String,
        promptQuestion: String = "",
        tags: String = "Reflection",
        isFavorite: Boolean = false
    ) {
        viewModelScope.launch {
            repository.addNotebookEntry(
                moduleId = moduleId,
                moduleTitle = moduleTitle,
                title = title,
                content = content,
                promptQuestion = promptQuestion,
                tags = tags,
                isFavorite = isFavorite
            )
            _streakAnimationTrigger.value = System.currentTimeMillis()
            _showNewNoteDialog.value = false
            _activeNoteForEdit.value = null
            showCelebration("Reflection Inscribed in Permanent Notebook! +75 XP")
            syncCloudNow()
        }
    }

    fun updateNotebookEntry(entry: NotebookEntryEntity) {
        viewModelScope.launch {
            repository.updateNotebookEntry(entry)
            _showNewNoteDialog.value = false
            _activeNoteForEdit.value = null
            showCelebration("Notebook Entry Updated")
            syncCloudNow()
        }
    }

    fun deleteNotebookEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteNotebookEntry(id)
            syncCloudNow()
        }
    }

    fun toggleNoteFavorite(entry: NotebookEntryEntity) {
        viewModelScope.launch {
            repository.updateNotebookEntry(entry.copy(isFavorite = !entry.isFavorite))
        }
    }

    // --- Fear-to-Action Reframe Actions (Six Ghosts of Fear) ---
    fun openFearReframeDialog() {
        _showFearReframeDialog.value = true
    }

    fun closeFearReframeDialog() {
        _showFearReframeDialog.value = false
    }

    fun saveFearReframe(
        fearText: String,
        worstCaseText: String,
        actionTodayText: String,
        fearCategory: String = "Mindset",
        addToDailyHabits: Boolean = true
    ) {
        viewModelScope.launch {
            val (entryId, isFirstToday) = repository.saveFearReframeEntry(
                fearText = fearText,
                worstCaseText = worstCaseText,
                actionTodayText = actionTodayText,
                fearCategory = fearCategory,
                addToDailyHabits = addToDailyHabits
            )
            _showFearReframeDialog.value = false
            _streakAnimationTrigger.value = System.currentTimeMillis()
            if (isFirstToday) {
                showCelebration("Fear Transmuted to Action! 🔥 +75 XP Inscribed")
            } else {
                showCelebration("Fear Transmuted to Action! Inscribed in Ledger ✨")
            }
            refreshOfflineCacheStatus()
            syncCloudNow()
        }
    }

    fun toggleFearActionCompleted(entryId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleFearActionCompleted(entryId, isCompleted)
            val msg = if (isCompleted) "Action Sealed! Fear Neutralized ⚡" else "Action Marked Pending"
            showCelebration(msg)
            syncCloudNow()
        }
    }

    // --- Decision Log Actions (Decisiveness Habit & 30-Day Revisit) ---
    fun openDecisionLogDialog() {
        _showDecisionLogDialog.value = true
    }

    fun closeDecisionLogDialog() {
        _showDecisionLogDialog.value = false
    }

    fun openRevisitDecisionDialog(entry: NotebookEntryEntity) {
        _activeDecisionForRevisit.value = entry
    }

    fun closeRevisitDecisionDialog() {
        _activeDecisionForRevisit.value = null
    }

    fun saveDecisionLog(
        decisionText: String,
        confidenceLevel: Int = 3,
        customTimestamp: Long = System.currentTimeMillis(),
        rationale: String = ""
    ) {
        viewModelScope.launch {
            repository.saveDecisionLogEntry(
                decisionText = decisionText,
                confidenceLevel = confidenceLevel,
                customTimestamp = customTimestamp,
                rationale = rationale
            )
            _showDecisionLogDialog.value = false
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Decision Inscribed with Conviction! ⚡ +50 XP")
            refreshOfflineCacheStatus()
            syncCloudNow()
        }
    }

    fun revisitDecisionLog(
        entryId: Long,
        outcomeText: String,
        outcomeTag: String
    ) {
        viewModelScope.launch {
            val success = repository.revisitDecisionLog(
                entryId = entryId,
                outcomeText = outcomeText,
                outcomeTag = outcomeTag
            )
            if (success) {
                _activeDecisionForRevisit.value = null
                _streakAnimationTrigger.value = System.currentTimeMillis()
                showCelebration("30-Day Reflection Inscribed! 🎯 +25 XP")
                refreshOfflineCacheStatus()
                syncCloudNow()
            }
        }
    }

    // --- Persistence Streak Recovery Actions (Napoleon Hill Comeback Protocol) ---
    fun openPersistenceCheckDialog(streakType: String = "Daily Sovereign Ritual") {
        _pendingPersistenceStreakType.value = streakType
        _showPersistenceCheckDialog.value = true
    }

    fun closePersistenceCheckDialog() {
        _showPersistenceCheckDialog.value = false
        viewModelScope.launch {
            repository.dismissPendingPersistenceCheck()
        }
    }

    fun savePersistenceComeback(
        streakType: String,
        obstacle: String,
        tomorrowPlan: String
    ) {
        viewModelScope.launch {
            val (entryId, xpEarned) = repository.saveComebackEntry(
                streakType = streakType,
                obstacleText = obstacle,
                tomorrowPlanText = tomorrowPlan
            )
            _showPersistenceCheckDialog.value = false
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Comeback Inscribed! ⚡ +$xpEarned XP (Defeat is Temporary)")
            refreshOfflineCacheStatus()
            syncCloudNow()
        }
    }

    // --- Money Mindset Journal Actions (Daily Financial Consciousness & Pattern Tracking) ---
    fun openMoneyMindsetDialog() {
        _showMoneyMindsetDialog.value = true
    }

    fun closeMoneyMindsetDialog() {
        _showMoneyMindsetDialog.value = false
    }

    fun saveMoneyMindsetEntry(
        decisionType: String,
        actionText: String,
        emotion: String,
        beliefText: String,
        amount: String = "",
        promptQuestion: String = "What financial decision did you make today, and what belief or emotion was driving it?"
    ) {
        viewModelScope.launch {
            val (entryId, isFirstToday) = repository.saveMoneyMindsetEntry(
                decisionType = decisionType,
                actionText = actionText,
                emotion = emotion,
                beliefText = beliefText,
                amount = amount,
                promptQuestion = promptQuestion
            )
            _showMoneyMindsetDialog.value = false
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Money Moment Inscribed! 🪙 Mindset Pattern Sealed (+50 XP)")
            refreshOfflineCacheStatus()
            syncCloudNow()
        }
    }

    // --- PDF Export Actions ---
    fun openPdfExportDialog() {
        _pdfExportState.value = _pdfExportState.value.copy(showExportDialog = true, errorMessage = null)
    }

    fun dismissPdfExportDialog() {
        _pdfExportState.value = _pdfExportState.value.copy(showExportDialog = false)
    }

    fun closePdfExportDialog() {
        dismissPdfExportDialog()
    }

    fun exportNotebookToPdf(
        context: Context,
        entriesToExport: List<NotebookEntryEntity>? = null,
        customTitle: String = "THE SOVEREIGN RITUAL NOTEBOOK"
    ) {
        viewModelScope.launch {
            _pdfExportState.value = _pdfExportState.value.copy(isExporting = true, errorMessage = null)
            val list = entriesToExport ?: notebookEntries.value
            if (list.isEmpty()) {
                _pdfExportState.value = _pdfExportState.value.copy(
                    isExporting = false,
                    errorMessage = "No entries found in notebook to export."
                )
                return@launch
            }

            val result = NotebookPdfExporter.exportNotebookToPdf(
                context = context,
                entries = list,
                userProfile = userProfile.value,
                customTitle = customTitle
            )

            result.onSuccess { exportRes ->
                _pdfExportState.value = _pdfExportState.value.copy(
                    isExporting = false,
                    exportResult = exportRes,
                    showExportDialog = true
                )
                showCelebration("Sovereign Notebook PDF Exported! (${exportRes.totalEntries} entries, ${exportRes.pageCount} pages)")
            }.onFailure { err ->
                _pdfExportState.value = _pdfExportState.value.copy(
                    isExporting = false,
                    errorMessage = err.message ?: "Failed to generate PDF document."
                )
            }
        }
    }

    fun shareExportedPdf(context: Context) {
        val result = _pdfExportState.value.exportResult ?: return
        NotebookPdfExporter.sharePdfDocument(context, result)
    }

    fun viewExportedPdf(context: Context) {
        val result = _pdfExportState.value.exportResult ?: return
        NotebookPdfExporter.openPdfDocument(context, result)
    }

    fun unlockSovereignAccess() {
        viewModelScope.launch {
            repository.unlockAllPaidModules()
            _showPaywallModal.value = false
            showCelebration("Sovereign Protocol Unlocked! All 13 Vaults Active! +500 XP")
            syncCloudNow()
        }
    }

    // --- Gemini Multi-turn Chat Actions ---
    fun selectAdvisorRole(role: AdvisorRole) {
        _selectedAdvisorRole.value = role
    }

    fun selectGeminiModel(model: GeminiModelChoice) {
        _selectedGeminiModel.value = model
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank() || _isChatLoading.value) return

        val role = _selectedAdvisorRole.value
        val model = _selectedGeminiModel.value
        val profile = userProfile.value

        val userMsg = ChatMessage(
            sender = ChatSender.USER,
            text = text,
            advisorRole = role
        )

        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val result = geminiChatService.sendMessage(
                history = _chatMessages.value,
                userMessage = text,
                role = role,
                modelChoice = model,
                userMindsetScore = profile?.mindsetScore ?: 50,
                userTier = profile?.tierName ?: "Novice"
            )

            _isChatLoading.value = false

            result.onSuccess { reply ->
                val modelMsg = ChatMessage(
                    sender = ChatSender.MODEL,
                    text = reply,
                    advisorRole = role,
                    modelUsed = model.displayName
                )
                _chatMessages.value = _chatMessages.value + modelMsg
            }.onFailure { err ->
                val errorMsg = ChatMessage(
                    sender = ChatSender.MODEL,
                    text = "The transmission was interrupted: ${err.message}. Please consult again.",
                    advisorRole = role,
                    isError = true
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            }
        }
    }

    fun clearChatHistory() {
        val role = _selectedAdvisorRole.value
        _chatMessages.value = listOf(
            ChatMessage(
                sender = ChatSender.MODEL,
                text = "The slate is cleansed. I am ${role.displayName}. State your inquiry with definiteness of purpose.",
                advisorRole = role,
                modelUsed = _selectedGeminiModel.value.displayName
            )
        )
    }

    // --- Firebase Auth & Firestore Sync Actions ---
    fun signInWithGoogle() {
        viewModelScope.launch {
            val result = firebaseAuthManager.signInWithGoogle()
            result.onSuccess { authUser ->
                showCelebration("Authenticated via Google: ${authUser.displayName}")
                syncCloudNow()
            }.onFailure {
                showCelebration("Authentication note: running in local offline mode")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            firebaseAuthManager.signOut()
            showCelebration("Signed out from Cloud Session")
        }
    }

    fun syncCloudNow() {
        viewModelScope.launch {
            val authUser = authUserState.value
            val userId = authUser.uid ?: "local_initiate_user"
            firestoreSyncManager.syncUserData(
                userId = userId,
                profile = userProfile.value,
                modules = modules.value,
                notebookEntries = notebookEntries.value,
                badges = badges.value
            )
        }
    }

    fun openPaywall() {
        _showPaywallModal.value = true
    }

    fun closePaywall() {
        _showPaywallModal.value = false
    }

    fun startAssessment() {
        _assessmentState.value = MindsetAssessmentState()
        _currentScreen.value = ScreenRoute.Assessment
    }

    fun finishScoreReveal() {
        _assessmentState.value = MindsetAssessmentState()
        _currentScreen.value = ScreenRoute.Dashboard
    }

    // --- Onboarding Flow Handlers ---

    fun saveOnboardingStep(step: Int) {
        viewModelScope.launch {
            repository.saveOnboardingStep(step)
        }
    }

    fun saveOnboardingName(name: String) {
        viewModelScope.launch {
            repository.saveOnboardingName(name)
        }
    }

    fun saveOnboardingChiefAim(aim: String) {
        viewModelScope.launch {
            repository.saveOnboardingChiefAim(aim)
        }
    }

    fun completeOnboardingAndEnterDashboard() {
        viewModelScope.launch {
            repository.completeUnifiedOnboarding()
            _currentScreen.value = ScreenRoute.Dashboard
            showCelebration("🌟 Sovereign Initiation Complete! +100 XP Welcome Bonus ✨")
            syncCloudNow()
        }
    }

    fun resetOnboardingForTesting() {
        viewModelScope.launch {
            repository.resetOnboardingForDev()
            _assessmentState.value = MindsetAssessmentState()
            _currentScreen.value = ScreenRoute.Onboarding
            showCelebration("Onboarding flow reset to Step 1 for testing")
        }
    }

    fun devUnlockAll() {
        unlockSovereignAccess()
    }

    fun devResetProgress() {
        viewModelScope.launch {
            repository.manualAdminUpdate(
                xp = 150,
                tier = "Novice",
                mindsetScore = 50,
                isPaidUnlocked = false,
                role = "admin"
            )
            showCelebration("Progress Reset for Testing")
            syncCloudNow()
        }
    }

    fun devSetTier(tierName: String) {
        viewModelScope.launch {
            val current = userProfile.value
            val targetXp = when (tierName.lowercase()) {
                "legacy" -> 7500
                "sovereign" -> 4000
                "architect" -> 2000
                "builder" -> 800
                else -> 100
            }
            repository.manualAdminUpdate(
                xp = targetXp,
                tier = tierName,
                mindsetScore = current?.mindsetScore ?: 50,
                isPaidUnlocked = current?.isPaidUnlocked ?: false,
                role = current?.role ?: "admin"
            )
            showCelebration("Tier Switched to $tierName! Dynamic Gold Glow-Up Activated ✨")
            syncCloudNow()
        }
    }

    fun devAddXp(amount: Int) {
        viewModelScope.launch {
            val current = userProfile.value
            val newXp = (current?.xpTotal ?: 0) + amount
            val newTier = when {
                newXp >= 7000 -> "Legacy"
                newXp >= 3500 -> "Sovereign"
                newXp >= 1500 -> "Architect"
                newXp >= 500 -> "Builder"
                else -> "Novice"
            }
            repository.manualAdminUpdate(
                xp = newXp,
                tier = newTier,
                mindsetScore = current?.mindsetScore ?: 50,
                isPaidUnlocked = current?.isPaidUnlocked ?: false,
                role = current?.role ?: "user"
            )
            showCelebration("+$amount XP Injected! Tier: $newTier")
            syncCloudNow()
        }
    }

    fun unlockPaidAccess() {
        unlockSovereignAccess()
    }

    fun setSearchQuery(query: String) {
        setNotebookSearchQuery(query)
    }

    fun setSelectedTag(tag: String) {
        setSelectedNotebookTag(tag)
    }

    fun openNewNoteDialog() {
        setShowNewNoteDialog(true, null)
    }

    fun closeNoteDialog() {
        setShowNewNoteDialog(false, null)
    }

    fun openEditNoteDialog(note: NotebookEntryEntity) {
        setShowNewNoteDialog(true, note)
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        sharedPrefs.edit().putString("app_theme_mode", mode.name).apply()
        showCelebration("Theme set to ${mode.title} Mode ✨")
    }

    fun cycleThemeMode() {
        val nextMode = when (_themeMode.value) {
            AppThemeMode.DARK -> AppThemeMode.LIGHT
            AppThemeMode.LIGHT -> AppThemeMode.SYSTEM
            AppThemeMode.SYSTEM -> AppThemeMode.DARK
        }
        setThemeMode(nextMode)
    }

    fun toggleFloatingMoneyBubbles() {
        val newState = !_isFloatingMoneyBubblesEnabled.value
        _isFloatingMoneyBubblesEnabled.value = newState
        sharedPrefs.edit().putBoolean("floating_money_bubbles_enabled", newState).apply()
        showCelebration(if (newState) "Floating Gold Bubbles: Active ✨" else "Floating Gold Bubbles: Disabled (Reduced Motion)")
    }

    fun setFloatingMoneyBubblesEnabled(enabled: Boolean) {
        _isFloatingMoneyBubblesEnabled.value = enabled
        sharedPrefs.edit().putBoolean("floating_money_bubbles_enabled", enabled).apply()
    }

    fun toggleFavorite(entry: NotebookEntryEntity) {
        toggleNoteFavorite(entry)
    }

    // --- Text-to-Speech & Voice Transmutation Actions ---
    fun playAudioScript(script: ModuleAudioScript, startSentenceIndex: Int = 0) {
        ttsEngineManager.playScript(script, startSentenceIndex)
        showCelebration("Playing: ${script.title} 🎧")
    }

    fun resumeAudio() {
        ttsEngineManager.resume()
    }

    fun pauseAudio() {
        ttsEngineManager.pause()
    }

    fun stopAudio() {
        ttsEngineManager.stop()
    }

    fun nextAudioSentence() {
        ttsEngineManager.nextSentence()
    }

    fun previousAudioSentence() {
        ttsEngineManager.previousSentence()
    }

    fun seekAudioSentence(index: Int) {
        ttsEngineManager.seekToSentence(index)
    }

    fun setAudioSpeechRate(rate: Float) {
        ttsEngineManager.setSpeechRate(rate)
    }

    fun setAudioSpeechPitch(pitch: Float) {
        ttsEngineManager.setSpeechPitch(pitch)
    }

    fun toggleAudioAmbientSound() {
        ttsEngineManager.toggleAmbientSound()
    }

    fun saveAffirmationToNotebook(script: ModuleAudioScript) {
        saveNotebookEntry(
            moduleId = script.moduleId,
            moduleTitle = script.moduleTitle,
            title = "Audio Affirmation: ${script.title}",
            content = script.textToSpeak,
            promptQuestion = "Spoken Transmutation Affirmation for ${script.principleName}",
            tags = "Affirmation, Spoken Word, Vault ${script.moduleId}",
            isFavorite = true
        )
        showCelebration("Affirmation Inscribed in Sovereign Notebook! 📜✨")
    }

    // --- Short Lessons Masterclass Actions ---
    fun playShortLesson(lesson: ShortLessonEntity) {
        // Pause general TTS if playing
        ttsEngineManager.stop()
        shortLessonPlayerManager.playLesson(lesson)
        showCelebration("Playing: ${lesson.title} 🎧")
    }

    fun resumeShortLesson() {
        shortLessonPlayerManager.resume()
    }

    fun pauseShortLesson() {
        shortLessonPlayerManager.pause()
    }

    fun stopShortLesson() {
        shortLessonPlayerManager.stop()
    }

    fun seekShortLesson(seconds: Int) {
        shortLessonPlayerManager.seekTo(seconds)
        val active = shortLessonPlayerState.value.activeLesson
        if (active != null) {
            viewModelScope.launch {
                repository.updateLessonProgress(active.id, seconds)
            }
        }
    }

    fun seekShortLessonRelative(deltaSeconds: Int) {
        shortLessonPlayerManager.seekRelative(deltaSeconds)
        val active = shortLessonPlayerState.value.activeLesson
        val cur = shortLessonPlayerState.value.currentPositionSeconds
        if (active != null) {
            viewModelScope.launch {
                repository.updateLessonProgress(active.id, cur)
            }
        }
    }

    fun setShortLessonSpeed(speed: Float) {
        shortLessonPlayerManager.setSpeed(speed)
    }

    fun toggleShortLessonVideoMode() {
        shortLessonPlayerManager.toggleVideoMode()
    }

    fun toggleShortLessonTranscript() {
        shortLessonPlayerManager.toggleTranscript()
    }

    fun toggleShortLessonChapters() {
        shortLessonPlayerManager.toggleChapters()
    }

    fun toggleShortLessonAmbient() {
        shortLessonPlayerManager.toggleAmbient()
    }

    fun completeShortLesson(lessonId: String) {
        viewModelScope.launch {
            val awarded = repository.completeShortLesson(lessonId)
            if (awarded) {
                _streakAnimationTrigger.value = System.currentTimeMillis()
                showCelebration("Masterclass Lesson Completed! +35 XP ✨")
                syncCloudNow()
            }
        }
    }

    fun toggleLessonCompletion(lesson: ShortLessonEntity) {
        viewModelScope.launch {
            if (lesson.isCompleted) {
                repository.resetLessonCompletion(lesson.id)
                showCelebration("Lesson marked as uncompleted")
            } else {
                completeShortLesson(lesson.id)
            }
            syncCloudNow()
        }
    }

    fun saveShortLessonToNotebook(lesson: ShortLessonEntity) {
        saveNotebookEntry(
            moduleId = lesson.moduleId,
            moduleTitle = "Vault ${lesson.moduleId} Masterclass",
            title = "Lesson Takeaways: ${lesson.title}",
            content = "Key Takeaway:\n${lesson.keyTakeaway}\n\nCore Insights:\n${lesson.description}\n\nChapter Markers:\n${lesson.keyBulletPoints}",
            promptQuestion = "How will you apply the lesson '${lesson.title}' to your Definite Chief Aim?",
            tags = "Short Lesson, ${if (lesson.isVideo) "Video" else "Audio"}, Vault ${lesson.moduleId}",
            isFavorite = true
        )
        showCelebration("Lesson Notes Inscribed in Sovereign Notebook! 📜✨")
    }

    fun speakAffirmation(text: String) {
        ttsEngineManager.speakRawText(text)
        showCelebration("Speaking Daily Affirmation 🎧")
    }

    fun affirmDailyPrinciple(affirmation: ThinkAndGrowRichAffirmation) {
        viewModelScope.launch {
            repository.completeDailyRitual()
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("Affirmed: ${affirmation.principleName}! +30 XP & Streak Maintained 🔥")
            syncCloudNow()
        }
    }

    fun saveAffirmationQuoteToNotebook(affirmation: ThinkAndGrowRichAffirmation) {
        saveNotebookEntry(
            moduleId = if (affirmation.principleNumber > 0) affirmation.principleNumber else 0,
            moduleTitle = affirmation.principleName,
            title = "Daily Principle: ${affirmation.principleName}",
            content = "\"${affirmation.quote}\"\n\n— ${affirmation.author}, ${affirmation.source}\n\nMorning Transmutation Practice:\n${affirmation.actionPractice}",
            promptQuestion = "How will you embody ${affirmation.principleName} in your actions today?",
            tags = "Affirmation, Daily Principle, Think and Grow Rich",
            isFavorite = true
        )
        showCelebration("Quote Inscribed in Permanent Notebook! 📜✨")
    }

    fun updateUserBirthDate(birthYear: Int, birthMonth: Int = 1, birthDay: Int = 1, lifeExpectancyYears: Int = 90) {
        viewModelScope.launch {
            repository.updateUserBirthDate(birthYear, birthMonth, birthDay, lifeExpectancyYears)
            showCelebration("Life Timeline Synchronized ⏳")
        }
    }

    fun setUserAgeInYears(age: Int) {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val calculatedBirthYear = (currentYear - age).coerceIn(1900, currentYear)
        updateUserBirthDate(calculatedBirthYear, 1, 1, 90)
    }

    fun openEditChiefAimDialog() {
        _showEditChiefAimDialog.value = true
    }

    fun closeEditChiefAimDialog() {
        _showEditChiefAimDialog.value = false
    }

    fun updateDefiniteChiefAim(aim: String) {
        viewModelScope.launch {
            repository.updateDefiniteChiefAim(aim)
            _showEditChiefAimDialog.value = false
            showCelebration("Definite Chief Aim Inscribed & Sealed ✨")
            syncCloudNow()
        }
    }

    fun completeDailyAffirmation() {
        viewModelScope.launch {
            val (success, streak) = repository.completeDailyAffirmation()
            if (success) {
                _streakAnimationTrigger.value = System.currentTimeMillis()
                showCelebration("Daily Aim Affirmed! 🔥 $streak-Day Affirmation Streak & +50 XP ✨")
                syncCloudNow()
            }
        }
    }

    // --- Voice Memo Operations ---
    fun startVoiceMemoRecording(): Boolean {
        return voiceMemoRecorder.startRecording()
    }

    fun stopVoiceMemoRecording() {
        val path = voiceMemoRecorder.stopRecording()
        if (path != null) {
            viewModelScope.launch {
                repository.updateAffirmationAudioMemo(path)
                showCelebration("Voice Memo Inscribed to Storage 🎙️✨")
            }
        }
    }

    fun playVoiceMemo() {
        voiceMemoRecorder.startPlaying()
    }

    fun stopVoiceMemo() {
        voiceMemoRecorder.stopPlaying()
    }

    fun deleteVoiceMemo() {
        voiceMemoRecorder.deleteRecording()
        viewModelScope.launch {
            repository.updateAffirmationAudioMemo(null)
            showCelebration("Voice Memo Cleared")
        }
    }

    // --- Mastermind Circle Operations ---

    fun setShowJoinCircleDialog(show: Boolean) {
        _showJoinCircleDialog.value = show
    }

    fun setShowCreateCircleDialog(show: Boolean) {
        _showCreateCircleDialog.value = show
    }

    fun setShowWeeklyCheckinDialog(show: Boolean) {
        _showWeeklyCheckinDialog.value = show
    }

    fun setMastermindInviteCodeInput(code: String) {
        _mastermindInviteCodeInput.value = code
    }

    fun joinMastermindByInviteCode(code: String) {
        viewModelScope.launch {
            val (success, message) = repository.joinGroupByInviteCode(code)
            showCelebration(message)
            if (success) {
                _showJoinCircleDialog.value = false
                _mastermindInviteCodeInput.value = ""
                syncCloudNow()
            }
        }
    }

    fun autoMatchMastermindCircle() {
        viewModelScope.launch {
            val profile = userProfile.value
            val tier = profile?.tierName ?: "Builder"
            val (success, message) = repository.autoMatchCircle(tier)
            showCelebration(message)
            if (success) {
                _showJoinCircleDialog.value = false
                syncCloudNow()
            }
        }
    }

    fun createMastermindCircle(name: String, motto: String, targetTier: String) {
        viewModelScope.launch {
            val (success, message) = repository.createMastermindCircle(name, motto, targetTier)
            showCelebration(message)
            if (success) {
                _showCreateCircleDialog.value = false
                syncCloudNow()
            }
        }
    }

    fun leaveMastermindCircle() {
        viewModelScope.launch {
            repository.leaveCurrentCircle()
            showCelebration("Departed Mastermind Circle")
            syncCloudNow()
        }
    }

    fun submitWeeklyCheckin(goalTitle: String, status: String, note: String) {
        viewModelScope.launch {
            val currentGroup = userMastermindGroup.value
            if (currentGroup == null) {
                showCelebration("Join a Mastermind Circle first")
                return@launch
            }
            val xpGain = repository.submitWeeklyCheckin(
                groupId = currentGroup.id,
                goalTitle = goalTitle,
                status = status,
                note = note
            )
            if (xpGain > 0) {
                _showWeeklyCheckinDialog.value = false
                _streakAnimationTrigger.value = System.currentTimeMillis()
                showCelebration("Weekly Check-in Inscribed! 🔥 +$xpGain XP to Circle ✨")
                syncCloudNow()
            }
        }
    }

    fun toggleCheckinReaction(checkinId: String, reactionType: String) {
        viewModelScope.launch {
            repository.toggleCheckinReaction(checkinId, reactionType)
        }
    }

    // ==========================================
    // VISION BOARD ACTIONS
    // ==========================================

    fun setSelectedVisionCategory(category: String) {
        _selectedVisionCategory.value = category
    }

    fun setShowAddVisionItemDialog(show: Boolean) {
        _showAddVisionItemDialog.value = show
        if (!show) {
            _editingVisionItem.value = null
        }
    }

    fun setEditingVisionItem(item: VisionBoardItemEntity?) {
        _editingVisionItem.value = item
        _showAddVisionItemDialog.value = (item != null)
    }

    fun addVisionBoardItem(
        title: String,
        category: String,
        imageUrl: String,
        targetTimeline: String,
        affirmation: String
    ) {
        viewModelScope.launch {
            repository.addVisionBoardItem(
                title = title,
                category = category,
                imageUrl = imageUrl,
                targetTimeline = targetTimeline,
                affirmation = affirmation
            )
            _showAddVisionItemDialog.value = false
            _editingVisionItem.value = null
            showCelebration("Goal Inscribed on Vision Board! ✨ +15 XP")
            syncCloudNow()
        }
    }

    fun updateVisionBoardItem(
        id: Long,
        title: String,
        category: String,
        imageUrl: String,
        targetTimeline: String,
        affirmation: String
    ) {
        viewModelScope.launch {
            val existing = allVisionBoardItems.value.firstOrNull { it.id == id }
            val updated = existing?.copy(
                title = title.trim(),
                category = category.trim(),
                imageUrl = imageUrl.trim(),
                targetTimeline = targetTimeline.trim(),
                affirmation = affirmation.trim()
            ) ?: VisionBoardItemEntity(
                id = id,
                title = title.trim(),
                category = category.trim(),
                imageUrl = imageUrl.trim(),
                targetTimeline = targetTimeline.trim(),
                affirmation = affirmation.trim()
            )
            repository.updateVisionBoardItem(updated)
            _showAddVisionItemDialog.value = false
            _editingVisionItem.value = null
            showCelebration("Vision Inscription Updated ✨")
            syncCloudNow()
        }
    }

    fun deleteVisionBoardItem(id: Long) {
        viewModelScope.launch {
            repository.deleteVisionBoardItem(id)
            showCelebration("Item Removed from Vision Board")
            syncCloudNow()
        }
    }

    fun toggleVisionItemPin(id: Long, isPinned: Boolean) {
        viewModelScope.launch {
            repository.toggleVisionBoardItemPin(id, isPinned)
        }
    }

    fun reorderVisionBoardItems(items: List<VisionBoardItemEntity>) {
        viewModelScope.launch {
            repository.reorderVisionBoardItems(items)
        }
    }

    fun startVisionContemplationRitual() {
        _isVisionContemplationActive.value = true
        _visionContemplationSecondsRemaining.value = 60
    }

    fun tickVisionContemplation() {
        if (_visionContemplationSecondsRemaining.value > 0) {
            _visionContemplationSecondsRemaining.value -= 1
        }
    }

    fun cancelVisionContemplation() {
        _isVisionContemplationActive.value = false
        _visionContemplationSecondsRemaining.value = 60
    }

    fun completeDailyVisionRitual() {
        viewModelScope.launch {
            _isVisionContemplationActive.value = false
            _visionContemplationSecondsRemaining.value = 60
            val xpGain = repository.completeDailyVisionContemplation()
            if (xpGain > 0) {
                _streakAnimationTrigger.value = System.currentTimeMillis()
                showCelebration("Subconscious Vision Ritual Complete! 🔥 +$xpGain XP Inscribed ✨")
                syncCloudNow()
            } else {
                showCelebration("Vision Contemplation Complete ✨ (Daily XP already claimed)")
            }
        }
    }

    // ==========================================
    // WEALTH GOAL TRACKER ACTIONS
    // ==========================================

    fun openEditWealthGoalDialog() {
        _showEditWealthGoalDialog.value = true
    }

    fun closeEditWealthGoalDialog() {
        _showEditWealthGoalDialog.value = false
    }

    fun openLogContributionDialog() {
        _showLogContributionDialog.value = true
    }

    fun closeLogContributionDialog() {
        _showLogContributionDialog.value = false
    }

    fun updateWealthGoalSettings(
        title: String,
        targetAmount: Double,
        startingAmount: Double,
        targetDateEpochMillis: Long,
        currencySymbol: String = "$",
        category: String = "Financial Sovereignty",
        servicePledge: String = ""
    ) {
        viewModelScope.launch {
            repository.updateWealthGoalSettings(
                goalId = 1,
                title = title,
                targetAmount = targetAmount,
                startingAmount = startingAmount,
                targetDateEpochMillis = targetDateEpochMillis,
                currencySymbol = currencySymbol,
                category = category,
                servicePledge = servicePledge
            )
            _showEditWealthGoalDialog.value = false
            showCelebration("Definite Wealth Target Inscribed & Sealed ✨")
            syncCloudNow()
        }
    }

    fun logWealthContribution(
        amount: Double,
        isMilestoneOnly: Boolean,
        title: String,
        note: String,
        saveToNotebook: Boolean = false
    ) {
        viewModelScope.launch {
            val logId = repository.logWealthContribution(
                goalId = 1,
                amount = amount,
                isMilestoneOnly = isMilestoneOnly,
                title = title,
                note = note,
                saveToNotebook = saveToNotebook
            )
            _showLogContributionDialog.value = false
            if (isMilestoneOnly) {
                showCelebration("★ Wealth Milestone Inscribed! +35 XP ✨")
            } else {
                val goal = primaryWealthGoal.value
                val symbol = goal?.currencySymbol ?: "$"
                showCelebration("💰 +$symbol${String.format(java.util.Locale.US, "%,.2f", amount)} Inflow Inscribed! +35 XP ✨")
            }
            syncCloudNow()
        }
    }

    fun deleteWealthGoalLog(logId: Long) {
        viewModelScope.launch {
            repository.deleteWealthGoalLog(logId, 1)
            showCelebration("Entry Removed & Balance Recalibrated")
            syncCloudNow()
        }
    }

    // ==========================================
    // INCOME IDEA EXPLORER ACTIONS
    // ==========================================

    fun toggleSaveIncomeIdea(ideaId: String) {
        viewModelScope.launch {
            val wasSaved = repository.toggleSaveIncomeIdea(ideaId)
            val idea = IncomeIdeaLibraryData.getIdeaById(ideaId)
            val title = idea?.title ?: "Income Idea"
            if (wasSaved) {
                showCelebration("★ Saved '$title' to your Idea Shortlist! ✨")
            } else {
                showCelebration("Removed '$title' from your Idea Shortlist")
            }
        }
    }

    fun saveIncomeIdeaToNotebook(idea: IncomeIdea) {
        viewModelScope.launch {
            val noteContent = buildString {
                appendLine("💡 **INCOME VEHICLE EXPLORATION: ${idea.title.uppercase()}**")
                appendLine("• Category: ${idea.category.title}")
                appendLine("• Effort Level: ${idea.effortLevel.label} (${idea.effortLevel.description})")
                appendLine("• Capital Required: ${idea.capitalRequired}")
                appendLine("• Time to Launch: ${idea.timeToFirstRevenue}")
                appendLine("• Scalability Ceiling: ${idea.scalabilityRating}")
                appendLine()
                appendLine("🏛️ **PRINCIPLE ALIGNMENT: ${idea.linkedPrinciple.uppercase()}**")
                appendLine(idea.linkedPrincipleRationale)
                appendLine()
                appendLine("📋 **TACTICAL EXECUTION STEPS:**")
                idea.keySteps.forEachIndexed { index, step ->
                    appendLine("${index + 1}. $step")
                }
                appendLine()
                appendLine("⚡ **PREREQUISITES & SKILLS:**")
                idea.prerequisites.forEach { prereq ->
                    appendLine("• $prereq")
                }
                appendLine()
                appendLine("⚖️ **STRATEGIC PROS & CONSIDERATIONS:**")
                appendLine("Pros:")
                idea.pros.forEach { pro -> appendLine("  + $pro") }
                appendLine("Considerations:")
                idea.considerations.forEach { con -> appendLine("  - $con") }
                appendLine()
                appendLine("🎯 **SOVEREIGN CONTEMPLATION PROMPT:**")
                appendLine(idea.notebookPrompt)
                appendLine()
                appendLine("✍️ **MY ACTION PLAN & INSIGHTS:**")
                appendLine("[Inscribed from Income Idea Explorer. Detail your customized implementation strategy here...]")
            }

            repository.addNotebookEntry(
                moduleId = idea.linkedModuleId,
                moduleTitle = "Income Idea: ${idea.title}",
                title = "Income Idea Exploration: ${idea.title}",
                content = noteContent,
                promptQuestion = idea.notebookPrompt,
                tags = "Income Idea, ${idea.category.title}, ${idea.linkedPrinciple}, Wealth",
                isFavorite = true
            )
            showCelebration("📜 Inscribed '${idea.title}' in Sovereign Notebook! +30 XP ✨")
            syncCloudNow()
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsEngineManager.shutdown()
        voiceMemoRecorder.release()
        shortLessonPlayerManager.release()
    }

    private fun showCelebration(message: String) {
        _celebrationXpMessage.value = message
    }

    fun clearCelebration() {
        _celebrationXpMessage.value = null
    }

    // ==========================================
    // GRATITUDE & GIVING ACTIONS
    // ==========================================

    fun setShowLogGivingDialog(show: Boolean, logToEdit: GivingLogEntity? = null) {
        _editingGivingLog.value = logToEdit
        _showLogGivingDialog.value = show
    }

    fun openLogGivingDialog(logToEdit: GivingLogEntity? = null) {
        setShowLogGivingDialog(true, logToEdit)
    }

    fun closeLogGivingDialog() {
        setShowLogGivingDialog(false, null)
    }

    fun openEditGivingLogDialog(log: GivingLogEntity) {
        setShowLogGivingDialog(true, log)
    }

    fun setShowGivingGoalDialog(show: Boolean) {
        _showGivingGoalDialog.value = show
    }

    fun openGivingGoalDialog() {
        setShowGivingGoalDialog(true)
    }

    fun closeGivingGoalDialog() {
        setShowGivingGoalDialog(false)
    }

    fun setGivingCategoryFilter(category: String) {
        _givingCategoryFilter.value = category
    }

    fun toggleGivingAmountsHidden() {
        _isGivingAmountsHidden.value = !_isGivingAmountsHidden.value
    }

    fun toggleHideGivingAmounts() {
        toggleGivingAmountsHidden()
    }

    fun logGivingAct(
        title: String,
        amount: Double?,
        currencySymbol: String = "$",
        category: String = GivingLogEntity.CATEGORY_CHARITY,
        recipientName: String = "",
        note: String = "",
        saveToNotebook: Boolean = false,
        customTimestamp: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            repository.logGivingAct(
                title = title,
                amount = amount,
                currencySymbol = currencySymbol,
                category = category,
                recipientName = recipientName,
                note = note,
                saveToNotebook = saveToNotebook,
                customTimestamp = customTimestamp
            )
            _showLogGivingDialog.value = false
            _editingGivingLog.value = null
            _streakAnimationTrigger.value = System.currentTimeMillis()
            showCelebration("✨ Logged Act of Benevolence! +35 XP circulating abundance")
            syncCloudNow()
        }
    }

    fun updateGivingGoalSettings(
        goalType: String,
        targetAmount: Double,
        targetPercentage: Double,
        targetActsCount: Int,
        currencySymbol: String = "$",
        serviceMotto: String = ""
    ) {
        viewModelScope.launch {
            repository.updateGivingGoalSettings(
                goalType = goalType,
                targetAmount = targetAmount,
                targetPercentage = targetPercentage,
                targetActsCount = targetActsCount,
                currencySymbol = currencySymbol,
                serviceMotto = serviceMotto
            )
            _showGivingGoalDialog.value = false
            showCelebration("🌟 Giving & Tithing Target Updated! +25 XP")
            syncCloudNow()
        }
    }

    fun deleteGivingLog(id: Long) {
        viewModelScope.launch {
            repository.deleteGivingLog(id)
            showCelebration("Giving record removed")
            syncCloudNow()
        }
    }

    fun updateGivingLog(log: GivingLogEntity) {
        viewModelScope.launch {
            repository.updateGivingLog(log)
            _showLogGivingDialog.value = false
            _editingGivingLog.value = null
            showCelebration("Giving record updated")
            syncCloudNow()
        }
    }

    // ==========================================
    // LEADERBOARD & PRIVACY ACTIONS
    // ==========================================

    fun setLeaderboardMetric(metric: LeaderboardMetric) {
        _leaderboardMetric.value = metric
    }

    fun setLeaderboardTimeframe(timeframe: LeaderboardTimeframe) {
        _leaderboardTimeframe.value = timeframe
    }

    fun setLeaderboardOptIn(isOptedIn: Boolean) {
        viewModelScope.launch {
            repository.updateLeaderboardOptIn(isOptedIn)
            if (isOptedIn) {
                showCelebration("🌟 Leaderboard Standings Enabled! Visible to Mastermind Guild")
            } else {
                showCelebration("🔒 Privacy Mode: Excluded from public Leaderboard rankings")
            }
            syncCloudNow()
        }
    }

    fun toggleLeaderboardOptIn() {
        val currentOptIn = userProfile.value?.isLeaderboardOptedIn ?: true
        setLeaderboardOptIn(!currentOptIn)
    }

    // ==========================================
    // COMMITMENT CONTRACT ACTIONS
    // ==========================================

    fun openCreateCommitmentDialog() {
        _showCreateCommitmentDialog.value = true
    }

    fun openCreateContractDialog() = openCreateCommitmentDialog()

    fun closeCreateCommitmentDialog() {
        _showCreateCommitmentDialog.value = false
    }

    fun closeCreateContractDialog() = closeCreateCommitmentDialog()

    fun openRenewCommitmentDialog(contract: CommitmentContractEntity) {
        _showRenewCommitmentDialog.value = contract
    }

    fun openRenewContractDialog(contract: CommitmentContractEntity) = openRenewCommitmentDialog(contract)

    fun closeRenewCommitmentDialog() {
        _showRenewCommitmentDialog.value = null
    }

    fun closeRenewContractDialog() = closeRenewCommitmentDialog()

    fun openCompleteCommitmentDialog(contract: CommitmentContractEntity) {
        _showCompleteCommitmentDialog.value = contract
    }

    fun openCompleteContractDialog(contract: CommitmentContractEntity) = openCompleteCommitmentDialog(contract)

    fun closeCompleteCommitmentDialog() {
        _showCompleteCommitmentDialog.value = null
    }

    fun closeCompleteContractDialog() = closeCompleteCommitmentDialog()

    fun createCommitmentContract(
        goalStatement: String,
        whyItMatters: String,
        deadlineMillis: Long,
        signatureName: String,
        title: String = "Definite Chief Aim Covenant"
    ) {
        viewModelScope.launch {
            repository.createCommitmentContract(
                goalStatement = goalStatement,
                whyItMatters = whyItMatters,
                deadlineMillis = deadlineMillis,
                signatureName = signatureName,
                title = title
            )
            _showCreateCommitmentDialog.value = false
            showCelebration("📜 SOVEREIGN COMMITMENT SEALED! (+75 XP) ⚜️")
            syncCloudNow()
        }
    }

    fun updateCommitmentProgress(contractId: Long, progressPercent: Int) {
        viewModelScope.launch {
            repository.updateCommitmentProgress(contractId, progressPercent)
            if (progressPercent == 100) {
                showCelebration("🎯 100% Progress Reached! Ready to Seal as Completed.")
            }
        }
    }

    fun completeCommitmentContract(
        contractId: Long,
        completionNotes: String = ""
    ) {
        viewModelScope.launch {
            repository.completeCommitmentContract(
                contractId = contractId,
                completionNotes = completionNotes
            )
            _showCompleteCommitmentDialog.value = null
            showCelebration("👑 COMMITMENT FULFILLED & TRANSCENDED! (+150 XP) 🏆")
            syncCloudNow()
        }
    }

    fun renewCommitmentContract(
        contractId: Long,
        newDeadlineMillis: Long,
        renewalNotes: String = ""
    ) {
        viewModelScope.launch {
            repository.renewCommitmentContract(
                contractId = contractId,
                newDeadlineMillis = newDeadlineMillis,
                renewalNotes = renewalNotes
            )
            _showRenewCommitmentDialog.value = null
            showCelebration("🔄 COVENANT RENEWED & EXTENDED! (+50 XP) 🛡️")
            syncCloudNow()
        }
    }

    fun deleteCommitmentContract(contractId: Long) {
        viewModelScope.launch {
            repository.deleteCommitmentContract(contractId)
            showCelebration("Commitment removed from archive")
            syncCloudNow()
        }
    }
}
