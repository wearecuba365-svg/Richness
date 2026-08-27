package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CompleteCommitmentContractDialog
import com.example.ui.components.CreateCommitmentContractDialog
import com.example.ui.components.CreateCustomRitualDialog
import com.example.ui.components.EditDefiniteChiefAimDialog
import com.example.ui.components.EditWealthGoalDialog
import com.example.ui.components.FearToActionReframeDialog
import com.example.ui.components.FloatingMiniAudioPlayerBar
import com.example.ui.components.FloatingMiniLessonPlayerBar
import com.example.ui.components.GivingGoalSettingsDialog
import com.example.ui.components.LogContributionDialog
import com.example.ui.components.LogGivingActDialog
import com.example.ui.components.LuxuryDecelerateEasing
import com.example.ui.components.LuxuryScaffold
import com.example.ui.components.MoneyMindsetLogDialog
import com.example.ui.components.PaywallBottomSheet
import com.example.ui.components.ModuleCompletionCelebrationDialog
import com.example.ui.components.PersistenceCheckDialog
import com.example.ui.components.RenewCommitmentContractDialog
import com.example.ui.components.RitualDetailModal
import com.example.ui.components.SectionAchievementCelebrationDialog
import com.example.ui.components.SectionAchievementToastBanner
import com.example.ui.components.StreakMilestoneCelebrationDialog
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AssessmentScreen
import com.example.ui.screens.CommitmentContractScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DecisionLogScreen
import com.example.ui.screens.GivingTrackerScreen
import com.example.ui.screens.IncomeIdeaExplorerScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MasterMindChatScreen
import com.example.ui.screens.MasterMindCircleScreen
import com.example.ui.screens.MoneyBlueprintQuizScreen
import com.example.ui.screens.ModuleDetailScreen
import com.example.ui.screens.ModulesPathScreen
import com.example.ui.screens.NotebookScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileBadgesScreen
import com.example.ui.screens.SuccessStoryLibraryScreen
import com.example.ui.screens.VisionBoardScreen
import com.example.ui.screens.WealthGoalTrackerScreen
import com.example.ui.screens.WeeklyDigestScreen
import com.example.ui.theme.TheRichesProtocolTheme
import com.example.ui.viewmodel.RichesViewModel
import com.example.ui.viewmodel.ScreenRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val viewModel: RichesViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return RichesViewModel(context.applicationContext as Application) as T
                    }
                }
            )
            val themeMode by viewModel.themeMode.collectAsState()

            TheRichesProtocolTheme(themeMode = themeMode) {
                RichesAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RichesAppContent(viewModel: RichesViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val modules by viewModel.modules.collectAsState()
    val notebookEntries by viewModel.notebookEntries.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val assessmentState by viewModel.assessmentState.collectAsState()
    val celebrationMessage by viewModel.celebrationXpMessage.collectAsState()
    val isPaywallOpen by viewModel.showPaywallModal.collectAsState()
    val isAmbientSoundPlaying by viewModel.isAmbientSoundPlaying.collectAsState()
    val searchQuery by viewModel.notebookSearchQuery.collectAsState()
    val selectedTag by viewModel.selectedNotebookTag.collectAsState()
    val showNewNoteDialog by viewModel.showNewNoteDialog.collectAsState()
    val activeNoteForEdit by viewModel.activeNoteForEdit.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val pdfExportState by viewModel.pdfExportState.collectAsState()

    // TTS Audio Player State
    val ttsPlayerState by viewModel.ttsPlayerState.collectAsState()
    val allShortLessons by viewModel.allShortLessons.collectAsState()
    val shortLessonPlayerState by viewModel.shortLessonPlayerState.collectAsState()

    // Gemini Chat State
    val chatMessages by viewModel.chatMessages.collectAsState()
    val selectedAdvisorRole by viewModel.selectedAdvisorRole.collectAsState()
    val selectedGeminiModel by viewModel.selectedGeminiModel.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()

    // Daily Habit Tracking State
    val habits by viewModel.habits.collectAsState()
    val todayHabitLogs by viewModel.todayHabitLogs.collectAsState()
    val allHabitLogs by viewModel.allHabitLogs.collectAsState()
    val selectedHabitDateEpochDay by viewModel.selectedHabitDateEpochDay.collectAsState()
    val activeHabitForModal by viewModel.activeHabitForModal.collectAsState()
    val showCustomHabitDialog by viewModel.showCustomHabitDialog.collectAsState()
    val showEditChiefAimDialog by viewModel.showEditChiefAimDialog.collectAsState()
    val voiceMemoUiState by viewModel.voiceMemoUiState.collectAsState()
    val activeMilestoneCelebration by viewModel.activeMilestoneCelebration.collectAsState()
    val activeModuleCompletionCelebration by viewModel.activeModuleCompletionCelebration.collectAsState()
    val activeSectionAchievementCelebration by viewModel.activeSectionAchievementCelebration.collectAsState()
    val sectionAchievementToast by viewModel.sectionAchievementToast.collectAsState()

    // Mastermind Circles State
    val allMastermindGroups by viewModel.allMastermindGroups.collectAsState()
    val userMastermindGroup by viewModel.userMastermindGroup.collectAsState()
    val currentGroupMembers by viewModel.currentGroupMembers.collectAsState()
    val currentGroupWeeklyCheckins by viewModel.currentGroupWeeklyCheckins.collectAsState()
    val currentGroupAllCheckins by viewModel.currentGroupAllCheckins.collectAsState()
    val allMastermindMembers by viewModel.allMastermindMembers.collectAsState()
    val allMastermindCheckins by viewModel.allMastermindCheckins.collectAsState()
    val showJoinCircleDialog by viewModel.showJoinCircleDialog.collectAsState()
    val showCreateCircleDialog by viewModel.showCreateCircleDialog.collectAsState()
    val showWeeklyCheckinDialog by viewModel.showWeeklyCheckinDialog.collectAsState()
    val mastermindInviteCodeInput by viewModel.mastermindInviteCodeInput.collectAsState()

    // Leaderboard State
    val leaderboardMetric by viewModel.leaderboardMetric.collectAsState()
    val leaderboardTimeframe by viewModel.leaderboardTimeframe.collectAsState()

    // Vision Board State
    val visionItems by viewModel.allVisionBoardItems.collectAsState()

    // Fear Reframe State
    val showFearReframeDialog by viewModel.showFearReframeDialog.collectAsState()

    // Decision Log State
    val showDecisionLogDialog by viewModel.showDecisionLogDialog.collectAsState()
    val activeDecisionForRevisit by viewModel.activeDecisionForRevisit.collectAsState()

    // Money Mindset Journal State
    val showMoneyMindsetDialog by viewModel.showMoneyMindsetDialog.collectAsState()

    // Money Blueprint Quiz State
    val blueprintQuizState by viewModel.blueprintQuizState.collectAsState()
    val allBlueprintResults by viewModel.allBlueprintResults.collectAsState()

    // Persistence Streak Recovery State
    val showPersistenceCheckDialog by viewModel.showPersistenceCheckDialog.collectAsState()
    val pendingPersistenceStreakType by viewModel.pendingPersistenceStreakType.collectAsState()

    // Wealth Goal Tracker State
    val primaryWealthGoal by viewModel.primaryWealthGoal.collectAsState()
    val wealthGoalLogs by viewModel.wealthGoalLogs.collectAsState()
    val showEditWealthGoalDialog by viewModel.showEditWealthGoalDialog.collectAsState()
    val showLogContributionDialog by viewModel.showLogContributionDialog.collectAsState()

    // Gratitude & Giving Tracker State
    val givingGoal by viewModel.givingGoal.collectAsState()
    val allGivingLogs by viewModel.allGivingLogs.collectAsState()
    val givingStreakWeeks by viewModel.givingStreakWeeks.collectAsState()
    val givingBestStreakWeeks by viewModel.givingBestStreakWeeks.collectAsState()
    val showLogGivingDialog by viewModel.showLogGivingDialog.collectAsState()
    val showGivingGoalDialog by viewModel.showGivingGoalDialog.collectAsState()
    val editingGivingLog by viewModel.editingGivingLog.collectAsState()
    val givingCategoryFilter by viewModel.givingCategoryFilter.collectAsState()
    val isGivingAmountsHidden by viewModel.isGivingAmountsHidden.collectAsState()

    // Saved Income Ideas State
    val savedIncomeIdeaIds by viewModel.savedIncomeIdeaIds.collectAsState()

    // Onboarding Telemetry Logs
    val allOnboardingStepLogs by viewModel.allOnboardingStepLogs.collectAsState()

    // Commitment Contract State
    val activeCommitmentContract by viewModel.activeCommitmentContract.collectAsState()
    val allCommitmentContracts by viewModel.allCommitmentContracts.collectAsState()
    val showCreateContractDialog by viewModel.showCreateContractDialog.collectAsState()
    val contractForRenewal by viewModel.contractForRenewal.collectAsState()
    val contractForCompletion by viewModel.contractForCompletion.collectAsState()

    // Firebase Auth & Cloud Sync State
    val authUserState by viewModel.authUserState.collectAsState()
    val cloudSyncState by viewModel.cloudSyncState.collectAsState()
    val streakAnimationTrigger by viewModel.streakAnimationTrigger.collectAsState()
    val isFloatingMoneyBubblesEnabled by viewModel.isFloatingMoneyBubblesEnabled.collectAsState()
    val isWeeklyDigestDismissed by viewModel.isWeeklyDigestDismissed.collectAsState()

    val isPaidUnlocked = userProfile?.isPaidUnlocked ?: false

    LuxuryScaffold(
        currentRoute = currentScreen,
        userProfile = userProfile,
        celebrationMessage = celebrationMessage,
        isAmbientSoundPlaying = isAmbientSoundPlaying,
        onNavigate = { route -> viewModel.navigateTo(route) },
        onToggleAmbientSound = { viewModel.toggleAmbientSound() },
        onCycleTheme = { viewModel.cycleThemeMode() },
        onSelectTheme = { mode -> viewModel.setThemeMode(mode) },
        isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
        onToggleFloatingMoneyBubbles = { viewModel.toggleFloatingMoneyBubbles() },
        onOpenPaywall = { viewModel.openPaywall() },
        onClearCelebration = { viewModel.clearCelebration() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        val isEnteringDetail = targetState is ScreenRoute.ModuleDetail
                        val isExitingDetail = initialState is ScreenRoute.ModuleDetail

                        if (isEnteringDetail) {
                            (slideInHorizontally(
                                animationSpec = tween(450, easing = LuxuryDecelerateEasing),
                                initialOffsetX = { fullWidth -> fullWidth / 4 }
                            ) + fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.94f, animationSpec = tween(450, easing = LuxuryDecelerateEasing)))
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(350, easing = LuxuryDecelerateEasing),
                                        targetOffsetX = { fullWidth -> -fullWidth / 6 }
                                    ) + fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.96f, animationSpec = tween(350))
                                )
                        } else if (isExitingDetail) {
                            (slideInHorizontally(
                                animationSpec = tween(450, easing = LuxuryDecelerateEasing),
                                initialOffsetX = { fullWidth -> -fullWidth / 6 }
                            ) + fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.96f, animationSpec = tween(450, easing = LuxuryDecelerateEasing)))
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(350, easing = LuxuryDecelerateEasing),
                                        targetOffsetX = { fullWidth -> fullWidth / 4 }
                                    ) + fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.94f, animationSpec = tween(350))
                                )
                        } else {
                            (fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) + scaleIn(initialScale = 0.98f, animationSpec = tween(350)))
                                .togetherWith(fadeOut(animationSpec = tween(250, easing = FastOutSlowInEasing)) + scaleOut(targetScale = 0.98f, animationSpec = tween(250)))
                        }
                    },
                    label = "screen_transition"
                ) { targetScreen ->
                    when (targetScreen) {
                        ScreenRoute.Landing -> {
                            LandingScreen(
                                onStartAssessment = { viewModel.startAssessment() },
                                onExploreVault0 = { viewModel.navigateTo(ScreenRoute.ModuleDetail(0)) },
                                onEnterDashboard = { viewModel.navigateTo(ScreenRoute.Dashboard) }
                            )
                        }

                        ScreenRoute.Onboarding -> {
                            OnboardingScreen(
                                userProfile = userProfile,
                                assessmentState = assessmentState,
                                onSaveStep = { step -> viewModel.saveOnboardingStep(step) },
                                onSaveName = { name -> viewModel.saveOnboardingName(name) },
                                onSaveChiefAim = { aim -> viewModel.saveOnboardingChiefAim(aim) },
                                onUpdateAssessmentDimension = { dim, score -> viewModel.updateAssessmentDimension(dim, score) },
                                onNextAssessmentStep = { viewModel.nextAssessmentStep() },
                                onPrevAssessmentStep = { viewModel.prevAssessmentStep() },
                                onFinishOnboarding = { viewModel.completeOnboardingAndEnterDashboard() }
                            )
                        }

                        ScreenRoute.Assessment -> {
                            AssessmentScreen(
                                state = assessmentState,
                                userProfile = userProfile,
                                onUpdateDimension = { dim, score -> viewModel.updateAssessmentDimension(dim, score) },
                                onNext = { viewModel.nextAssessmentStep() },
                                onPrev = { viewModel.prevAssessmentStep() },
                                onFinishReveal = { viewModel.finishScoreReveal() },
                                onExploreVault0 = {
                                    viewModel.finishScoreReveal()
                                    viewModel.navigateTo(ScreenRoute.ModuleDetail(0))
                                }
                            )
                        }

                        ScreenRoute.Dashboard -> {
                            DashboardScreen(
                                userProfile = userProfile,
                                modules = modules,
                                notebookEntries = notebookEntries,
                                badges = badges,
                                habits = habits,
                                todayHabitLogs = todayHabitLogs,
                                allHabitLogs = allHabitLogs,
                                selectedHabitDateEpochDay = selectedHabitDateEpochDay,
                                streakAnimationTrigger = streakAnimationTrigger,
                                onCompleteDailyRitual = { viewModel.completeDailyRitual() },
                                onSelectHabitDate = { epochDay -> viewModel.selectHabitDate(epochDay) },
                                onToggleHabit = { habitId -> viewModel.toggleHabit(habitId) },
                                onOpenHabitDetail = { habit -> viewModel.openHabitDetailModal(habit) },
                                onAddNewHabit = { viewModel.setShowCustomHabitDialog(true) },
                                onMilestoneClick = { milestone -> viewModel.triggerMilestoneCelebration(milestone) },
                                onSelectModule = { id -> viewModel.navigateTo(ScreenRoute.ModuleDetail(id)) },
                                onNavigateToModules = { viewModel.navigateTo(ScreenRoute.ModulesPath) },
                                onNavigateToNotebook = { viewModel.navigateTo(ScreenRoute.Notebook) },
                                onNavigateToAssessment = { viewModel.startAssessment() },
                                onNavigateToChat = { viewModel.navigateTo(ScreenRoute.MasterMindChat) },
                                onNavigateToMasterMindCircle = { viewModel.navigateTo(ScreenRoute.MasterMindCircle) },
                                onNavigateToLeaderboard = { viewModel.navigateTo(ScreenRoute.Leaderboard) },
                                onNavigateToProfile = { viewModel.navigateTo(ScreenRoute.ProfileBadges) },
                                onUpdateBirthDate = { y, m, d -> viewModel.updateUserBirthDate(y, m, d) },
                                onOpenPaywall = { viewModel.openPaywall() },
                                onAffirmQuote = { affirmation -> viewModel.affirmDailyPrinciple(affirmation) },
                                onSaveAffirmationToNotebook = { affirmation -> viewModel.saveAffirmationQuoteToNotebook(affirmation) },
                                onSpeakAffirmation = { text -> viewModel.speakAffirmation(text) },
                                voiceMemoUiState = voiceMemoUiState,
                                onStartVoiceRecording = { viewModel.startVoiceMemoRecording() },
                                onStopVoiceRecording = { viewModel.stopVoiceMemoRecording() },
                                onPlayVoiceRecording = { viewModel.playVoiceMemo() },
                                onStopVoiceRecordingPlayback = { viewModel.stopVoiceMemo() },
                                onDeleteVoiceRecording = { viewModel.deleteVoiceMemo() },
                                onOpenEditAim = { viewModel.openEditChiefAimDialog() },
                                onCompleteAimAffirmation = { viewModel.completeDailyAffirmation() },
                                visionItems = visionItems,
                                onNavigateToVisionBoard = { viewModel.navigateTo(ScreenRoute.VisionBoard) },
                                onStartVisionRitual = {
                                    viewModel.navigateTo(ScreenRoute.VisionBoard)
                                    viewModel.startVisionContemplationRitual()
                                },
                                onOpenFearReframe = { viewModel.openFearReframeDialog() },
                                onOpenDecisionLog = { viewModel.openDecisionLogDialog() },
                                onNavigateToDecisionLog = { viewModel.navigateTo(ScreenRoute.DecisionLog) },
                                onOpenMoneyMindset = { viewModel.openMoneyMindsetDialog() },
                                onNavigateToMoneyBlueprint = { viewModel.startMoneyBlueprintQuiz() },
                                onRetakeMoneyBlueprint = { viewModel.startMoneyBlueprintQuiz(isRetake = true) },
                                onRevisitDecision = { entry -> viewModel.openRevisitDecisionDialog(entry) },
                                onNavigateToSuccessLibrary = { figureId, principle ->
                                    viewModel.navigateTo(ScreenRoute.SuccessStoryLibrary(figureId, principle))
                                },
                                onNavigateToIncomeIdeaExplorer = { catId, savedOnly ->
                                    viewModel.navigateTo(ScreenRoute.IncomeIdeaExplorer(catId, savedOnly))
                                },
                                wealthGoal = primaryWealthGoal,
                                wealthGoalLogs = wealthGoalLogs,
                                onOpenEditWealthGoal = { viewModel.openEditWealthGoalDialog() },
                                onOpenLogWealthContribution = { viewModel.openLogContributionDialog() },
                                onNavigateToWealthGoalTracker = { viewModel.navigateTo(ScreenRoute.WealthGoalTracker) },
                                givingGoal = givingGoal,
                                givingLogs = allGivingLogs,
                                givingStreakWeeks = givingStreakWeeks,
                                onOpenLogGiving = { viewModel.openLogGivingDialog() },
                                onNavigateToGivingTracker = { viewModel.navigateTo(ScreenRoute.GivingTracker) },
                                mastermindCheckins = allMastermindCheckins,
                                commitmentContract = activeCommitmentContract,
                                onNavigateToCommitmentContract = { viewModel.navigateTo(ScreenRoute.CommitmentContract) },
                                onCreateCommitmentContract = { viewModel.openCreateContractDialog() },
                                onUpdateCommitmentProgress = { contractId, newProgress ->
                                    viewModel.updateCommitmentProgress(contractId, newProgress)
                                },
                                onCompleteCommitmentContract = { contract ->
                                    viewModel.openCompleteContractDialog(contract)
                                },
                                onRenewCommitmentContract = { contract ->
                                    viewModel.openRenewContractDialog(contract)
                                },
                                isWeeklyDigestDismissed = isWeeklyDigestDismissed,
                                onNavigateToWeeklyDigest = { viewModel.navigateTo(ScreenRoute.WeeklyDigest) },
                                onDismissWeeklyDigest = { viewModel.dismissWeeklyDigest() },
                                onShareWeeklyDigest = { digest -> viewModel.exportAndShareWeeklyDigest(context, digest) },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        }

                        ScreenRoute.ModulesPath -> {
                            ModulesPathScreen(
                                modules = modules,
                                isPaidUnlocked = isPaidUnlocked,
                                onSelectModule = { id -> viewModel.navigateTo(ScreenRoute.ModuleDetail(id)) },
                                onOpenPaywall = { viewModel.openPaywall() },
                                onOpenSectionAchievement = { section -> viewModel.triggerSectionAchievement(section) },
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        }

                        is ScreenRoute.ModuleDetail -> {
                            val currentModule = modules.firstOrNull { it.id == targetScreen.moduleId }
                                ?: modules.firstOrNull()

                            if (currentModule != null) {
                                val moduleLessons = allShortLessons.filter { it.moduleId == currentModule.id }
                                ModuleDetailScreen(
                                    module = currentModule,
                                    moduleShortLessons = moduleLessons,
                                    shortLessonPlayerState = shortLessonPlayerState,
                                    ttsPlayerState = ttsPlayerState,
                                    existingReflections = notebookEntries,
                                    onSubmitModuleReflection = { moduleId, answers, onSuccess ->
                                        viewModel.submitModuleCompletionReflection(moduleId, answers, onSuccess)
                                    },
                                    onBack = { viewModel.navigateTo(ScreenRoute.ModulesPath) },
                                    onCompleteLesson = { id -> viewModel.completeLesson(id) },
                                    onCompleteQuest = { id -> viewModel.completeQuest(id) },
                                    onSaveWorksheet = { id, f1, f2, f3 -> viewModel.saveWorksheet(id, f1, f2, f3) },
                                    onSaveNotebookReflection = { modId, modTitle, title, content, prompt, tags, isFav ->
                                        viewModel.saveNotebookEntry(modId, modTitle, title, content, prompt, tags, isFav)
                                    },
                                    onPlayShortLesson = { lesson -> viewModel.playShortLesson(lesson) },
                                    onResumeShortLesson = { viewModel.resumeShortLesson() },
                                    onPauseShortLesson = { viewModel.pauseShortLesson() },
                                    onSeekShortLesson = { sec -> viewModel.seekShortLesson(sec) },
                                    onSeekShortLessonRelative = { delta -> viewModel.seekShortLessonRelative(delta) },
                                    onSetShortLessonSpeed = { speed -> viewModel.setShortLessonSpeed(speed) },
                                    onToggleShortLessonVideoMode = { viewModel.toggleShortLessonVideoMode() },
                                    onToggleShortLessonTranscript = { viewModel.toggleShortLessonTranscript() },
                                    onToggleShortLessonChapters = { viewModel.toggleShortLessonChapters() },
                                    onToggleShortLessonAmbient = { viewModel.toggleShortLessonAmbient() },
                                    onSaveShortLessonToNotebook = { lesson -> viewModel.saveShortLessonToNotebook(lesson) },
                                    onToggleLessonCompletion = { lesson -> viewModel.toggleLessonCompletion(lesson) },
                                    onCompleteShortLesson = { lessonId -> viewModel.completeShortLesson(lessonId) },
                                    onPlayAudioScript = { script -> viewModel.playAudioScript(script) },
                                    onResumeAudio = { viewModel.resumeAudio() },
                                    onPauseAudio = { viewModel.pauseAudio() },
                                    onStopAudio = { viewModel.stopAudio() },
                                    onNextAudioSentence = { viewModel.nextAudioSentence() },
                                    onPreviousAudioSentence = { viewModel.previousAudioSentence() },
                                    onSeekAudioSentence = { index -> viewModel.seekAudioSentence(index) },
                                    onSetAudioRate = { rate -> viewModel.setAudioSpeechRate(rate) },
                                    onSetAudioPitch = { pitch -> viewModel.setAudioSpeechPitch(pitch) },
                                    onToggleAudioAmbient = { viewModel.toggleAmbientSound() },
                                    onSaveAffirmationToNotebook = { script -> viewModel.saveAffirmationToNotebook(script) },
                                    onNavigateToSuccessLibrary = { figureId, principle ->
                                        viewModel.navigateTo(ScreenRoute.SuccessStoryLibrary(figureId, principle))
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedContent
                                )
                            }
                        }

                        ScreenRoute.MasterMindChat -> {
                            MasterMindChatScreen(
                                messages = chatMessages,
                                selectedRole = selectedAdvisorRole,
                                selectedModel = selectedGeminiModel,
                                isLoading = isChatLoading,
                                userProfile = userProfile,
                                onSelectRole = { role -> viewModel.selectAdvisorRole(role) },
                                onSelectModel = { model -> viewModel.selectGeminiModel(model) },
                                onSendMessage = { text -> viewModel.sendChatMessage(text) },
                                onClearChat = { viewModel.clearChatHistory() },
                                onSaveToNotebook = { title, content ->
                                    viewModel.saveNotebookEntry(
                                        moduleId = null,
                                        moduleTitle = "Master Mind AI Insight",
                                        title = title,
                                        content = content,
                                        tags = "AI Decree,MasterMind"
                                    )
                                }
                            )
                        }

                        ScreenRoute.VisionBoard -> {
                            VisionBoardScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) }
                            )
                        }

                        ScreenRoute.Notebook -> {
                            NotebookScreen(
                                entries = notebookEntries,
                                searchQuery = searchQuery,
                                selectedTag = selectedTag,
                                showNewNoteDialog = showNewNoteDialog,
                                activeNoteForEdit = activeNoteForEdit,
                                showFearReframeDialog = showFearReframeDialog,
                                showDecisionLogDialog = showDecisionLogDialog,
                                activeDecisionForRevisit = activeDecisionForRevisit,
                                showMoneyMindsetDialog = showMoneyMindsetDialog,
                                pdfExportState = pdfExportState,
                                userProfile = userProfile,
                                onSearchChange = { viewModel.setSearchQuery(it) },
                                onTagSelect = { viewModel.setSelectedTag(it) },
                                onOpenNewNote = { viewModel.openNewNoteDialog() },
                                onCloseDialog = { viewModel.closeNoteDialog() },
                                onOpenMoneyMindsetDialog = { viewModel.openMoneyMindsetDialog() },
                                onCloseMoneyMindsetDialog = { viewModel.closeMoneyMindsetDialog() },
                                onSaveMoneyMindset = { type, action, emotion, belief, amount, prompt ->
                                    viewModel.saveMoneyMindsetEntry(type, action, emotion, belief, amount, prompt)
                                },
                                onOpenFearReframe = { viewModel.openFearReframeDialog() },
                                onCloseFearReframe = { viewModel.closeFearReframeDialog() },
                                onSaveFearReframe = { fear, worst, action, cat, createHabit ->
                                    viewModel.saveFearReframe(fear, worst, action, cat, createHabit)
                                },
                                onToggleFearActionCompleted = { id, isDone ->
                                    viewModel.toggleFearActionCompleted(id, isDone)
                                },
                                onOpenDecisionLog = { viewModel.openDecisionLogDialog() },
                                onCloseDecisionLog = { viewModel.closeDecisionLogDialog() },
                                onSaveDecisionLog = { decision, confidence, dateMillis, rationale ->
                                    viewModel.saveDecisionLog(decision, confidence, dateMillis, rationale)
                                },
                                onOpenRevisitDecision = { entry -> viewModel.openRevisitDecisionDialog(entry) },
                                onCloseRevisitDecision = { viewModel.closeRevisitDecisionDialog() },
                                onSaveRevisitDecision = { id, outcomeText, outcomeTag ->
                                    viewModel.revisitDecisionLog(id, outcomeText, outcomeTag)
                                },
                                onSaveNote = { modId, modTitle, title, content, prompt, tags, isFav ->
                                    viewModel.saveNotebookEntry(modId, modTitle, title, content, prompt, tags, isFav)
                                },
                                onDeleteNote = { id -> viewModel.deleteNotebookEntry(id) },
                                onToggleFavorite = { entry -> viewModel.toggleFavorite(entry) },
                                onEditNote = { entry -> viewModel.openEditNoteDialog(entry) },
                                onOpenExportDialog = { viewModel.openPdfExportDialog() },
                                onDismissExportDialog = { viewModel.closePdfExportDialog() },
                                onExportPdf = { selectedEntries, customTitle ->
                                    viewModel.exportNotebookToPdf(context, selectedEntries, customTitle)
                                },
                                onSharePdf = { viewModel.shareExportedPdf(context) },
                                onViewPdf = { viewModel.viewExportedPdf(context) }
                            )
                        }

                        ScreenRoute.ProfileBadges -> {
                            ProfileBadgesScreen(
                                userProfile = userProfile,
                                badges = badges,
                                modules = modules,
                                notebookEntries = notebookEntries,
                                authUserState = authUserState,
                                cloudSyncState = cloudSyncState,
                                themeMode = themeMode,
                                isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
                                onSignInGoogle = { viewModel.signInWithGoogle() },
                                onSignOut = { viewModel.signOut() },
                                onSyncCloud = { viewModel.syncCloudNow() },
                                onSetThemeMode = { mode -> viewModel.setThemeMode(mode) },
                                onToggleFloatingMoneyBubbles = { viewModel.toggleFloatingMoneyBubbles() },
                                onUpdateBirthDate = { y, m, d -> viewModel.updateUserBirthDate(y, m, d) },
                                onNavigateToAssessment = { viewModel.startAssessment() },
                                onNavigateToAdmin = { viewModel.navigateTo(ScreenRoute.AdminPanel) },
                                onNavigateToMoneyBlueprint = { viewModel.startMoneyBlueprintQuiz() },
                                onOpenPaywall = { viewModel.openPaywall() },
                                onNavigateToNotebookExport = {
                                    viewModel.navigateTo(ScreenRoute.Notebook)
                                    viewModel.openPdfExportDialog()
                                },
                                onOpenSectionAchievement = { section -> viewModel.triggerSectionAchievement(section) },
                                onEditChiefAim = { viewModel.openEditChiefAimDialog() },
                                wealthGoal = primaryWealthGoal,
                                onEditWealthGoal = { viewModel.openEditWealthGoalDialog() },
                                onNavigateToWealthGoalTracker = { viewModel.navigateTo(ScreenRoute.WealthGoalTracker) },
                                onToggleLeaderboardOptIn = { isOptedIn -> viewModel.setLeaderboardOptIn(isOptedIn) },
                                onNavigateToLeaderboard = { viewModel.navigateTo(ScreenRoute.Leaderboard) }
                            )
                        }

                        ScreenRoute.MasterMindCircle -> {
                            MasterMindCircleScreen(
                                userProfile = userProfile,
                                userGroup = userMastermindGroup,
                                allGroups = allMastermindGroups,
                                groupMembers = currentGroupMembers,
                                weeklyCheckins = currentGroupWeeklyCheckins,
                                allGroupCheckins = currentGroupAllCheckins,
                                showJoinDialog = showJoinCircleDialog,
                                showCreateDialog = showCreateCircleDialog,
                                showWeeklyCheckinDialog = showWeeklyCheckinDialog,
                                inviteCodeInput = mastermindInviteCodeInput,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onNavigateToLeaderboard = { viewModel.navigateTo(ScreenRoute.Leaderboard) },
                                onSetShowJoinDialog = { viewModel.setShowJoinCircleDialog(it) },
                                onSetShowCreateDialog = { viewModel.setShowCreateCircleDialog(it) },
                                onSetShowWeeklyCheckinDialog = { viewModel.setShowWeeklyCheckinDialog(it) },
                                onSetInviteCodeInput = { viewModel.setMastermindInviteCodeInput(it) },
                                onJoinByInviteCode = { viewModel.joinMastermindByInviteCode(it) },
                                onAutoMatch = { viewModel.autoMatchMastermindCircle() },
                                onCreateCircle = { name, motto, tier -> viewModel.createMastermindCircle(name, motto, tier) },
                                onLeaveCircle = { viewModel.leaveMastermindCircle() },
                                onSubmitWeeklyCheckin = { goal, status, note -> viewModel.submitWeeklyCheckin(goal, status, note) },
                                onToggleReaction = { checkinId, reaction -> viewModel.toggleCheckinReaction(checkinId, reaction) }
                            )
                        }

                        ScreenRoute.AdminPanel -> {
                            AdminPanelScreen(
                                userProfile = userProfile,
                                modules = modules,
                                mastermindGroups = allMastermindGroups,
                                mastermindMembers = allMastermindMembers,
                                mastermindCheckins = allMastermindCheckins,
                                onboardingLogs = allOnboardingStepLogs,
                                themeMode = themeMode,
                                isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onUnlockAllModules = { viewModel.devUnlockAll() },
                                onResetAllProgress = { viewModel.devResetProgress() },
                                onResetOnboarding = { viewModel.resetOnboardingForTesting() },
                                onAddXp = { amount -> viewModel.devAddXp(amount) },
                                onSetThemeMode = { mode -> viewModel.setThemeMode(mode) },
                                onToggleFloatingMoneyBubbles = { viewModel.toggleFloatingMoneyBubbles() },
                                onSetTier = { tier -> viewModel.devSetTier(tier) }
                            )
                        }

                        ScreenRoute.Leaderboard -> {
                            LeaderboardScreen(
                                userProfile = userProfile,
                                modules = modules,
                                selectedMetric = leaderboardMetric,
                                selectedTimeframe = leaderboardTimeframe,
                                onSelectMetric = { metric -> viewModel.setLeaderboardMetric(metric) },
                                onSelectTimeframe = { timeframe -> viewModel.setLeaderboardTimeframe(timeframe) },
                                onToggleOptIn = { viewModel.toggleLeaderboardOptIn() },
                                onNavigateBack = { viewModel.navigateTo(ScreenRoute.Dashboard) }
                            )
                        }

                        is ScreenRoute.SuccessStoryLibrary -> {
                            SuccessStoryLibraryScreen(
                                initialFigureId = targetScreen.initialFigureId,
                                initialPrinciple = targetScreen.initialPrinciple,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onNavigateToVault = { vaultId ->
                                    viewModel.navigateTo(ScreenRoute.ModuleDetail(vaultId))
                                },
                                onSaveToNotebook = { title, content, tags ->
                                    viewModel.saveNotebookEntry(
                                        moduleId = null,
                                        moduleTitle = "Success Story Library Case Study",
                                        title = title,
                                        content = content,
                                        tags = tags
                                    )
                                }
                            )
                        }

                        ScreenRoute.WealthGoalTracker -> {
                            WealthGoalTrackerScreen(
                                goal = primaryWealthGoal,
                                logs = wealthGoalLogs,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onSaveGoal = { title, targetAmount, startingAmount, targetDateEpochMillis, currencySymbol, category, servicePledge ->
                                    viewModel.updateWealthGoalSettings(title, targetAmount, startingAmount, targetDateEpochMillis, currencySymbol, category, servicePledge)
                                },
                                onLogContribution = { amount, isMilestoneOnly, title, note, saveToNotebook ->
                                    viewModel.logWealthContribution(amount, isMilestoneOnly, title, note, saveToNotebook)
                                },
                                onDeleteLog = { logId ->
                                    viewModel.deleteWealthGoalLog(logId)
                                },
                                onNavigateToIncomeIdeaExplorer = {
                                    viewModel.navigateTo(ScreenRoute.IncomeIdeaExplorer())
                                }
                            )
                        }

                        ScreenRoute.GivingTracker -> {
                            GivingTrackerScreen(
                                goal = givingGoal,
                                logs = allGivingLogs,
                                streakWeeks = givingStreakWeeks,
                                bestStreakWeeks = givingBestStreakWeeks,
                                selectedCategoryFilter = givingCategoryFilter,
                                isAmountsHidden = isGivingAmountsHidden,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onLogGivingAct = { title, amount, currencySymbol, category, recipientName, note, saveToNotebook ->
                                    viewModel.logGivingAct(
                                        title = title,
                                        amount = amount,
                                        currencySymbol = currencySymbol,
                                        category = category,
                                        recipientName = recipientName,
                                        note = note,
                                        saveToNotebook = saveToNotebook
                                    )
                                },
                                onUpdateGivingGoal = { goalType, targetAmount, targetPercentage, targetActsCount, currencySymbol, serviceMotto ->
                                    viewModel.updateGivingGoalSettings(
                                        goalType = goalType,
                                        targetAmount = targetAmount,
                                        targetPercentage = targetPercentage,
                                        targetActsCount = targetActsCount,
                                        currencySymbol = currencySymbol,
                                        serviceMotto = serviceMotto
                                    )
                                },
                                onDeleteLog = { id -> viewModel.deleteGivingLog(id) },
                                onUpdateLog = { log -> viewModel.updateGivingLog(log) },
                                onSetCategoryFilter = { cat -> viewModel.setGivingCategoryFilter(cat) },
                                onToggleAmountsHidden = { viewModel.toggleHideGivingAmounts() }
                            )
                        }

                        is ScreenRoute.IncomeIdeaExplorer -> {
                            IncomeIdeaExplorerScreen(
                                savedIdeaIds = savedIncomeIdeaIds,
                                initialCategoryId = targetScreen.initialCategoryId,
                                initialFilterSavedOnly = targetScreen.filterSavedOnly,
                                onToggleSave = { idea -> viewModel.toggleSaveIncomeIdea(idea.id) },
                                onInscribeInNotebook = { idea -> viewModel.saveIncomeIdeaToNotebook(idea) },
                                onNavigateToModule = { moduleId ->
                                    viewModel.navigateTo(ScreenRoute.ModuleDetail(moduleId))
                                },
                                onNavigateBack = { viewModel.navigateTo(ScreenRoute.Dashboard) }
                            )
                        }

                        is ScreenRoute.MoneyBlueprint -> {
                            MoneyBlueprintQuizScreen(
                                state = blueprintQuizState,
                                historyList = allBlueprintResults,
                                onAnswerChanged = { qId, score -> viewModel.answerBlueprintQuestion(qId, score) },
                                onNext = { viewModel.nextBlueprintQuestion() },
                                onPrev = { viewModel.prevBlueprintQuestion() },
                                onFinish = { viewModel.finishMoneyBlueprintReveal() },
                                onBackToDashboard = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onShowHistory = { viewModel.setShowBlueprintHistoryDialog(true) },
                                onHideHistory = { viewModel.setShowBlueprintHistoryDialog(false) },
                                onDeleteHistoryItem = { id -> viewModel.deleteBlueprintResult(id) },
                                onRetakeQuiz = { viewModel.startMoneyBlueprintQuiz(isRetake = true) },
                                onSaveToNotebook = { result -> viewModel.saveBlueprintReflectionToNotebook(result) },
                                onNavigateToModule = { moduleId -> viewModel.navigateTo(ScreenRoute.ModuleDetail(moduleId)) },
                                onOpenMoneyMindsetJournal = { viewModel.openMoneyMindsetDialog() },
                                onOpenFearReframe = { viewModel.openFearReframeDialog() },
                                onOpenMastermindChat = { viewModel.navigateTo(ScreenRoute.MasterMindChat) }
                            )
                        }

                        ScreenRoute.DecisionLog -> {
                            DecisionLogScreen(
                                notebookEntries = notebookEntries,
                                userProfile = userProfile,
                                showDecisionLogDialog = showDecisionLogDialog,
                                activeDecisionForRevisit = activeDecisionForRevisit,
                                onOpenDecisionLog = { viewModel.openDecisionLogDialog() },
                                onCloseDecisionLog = { viewModel.closeDecisionLogDialog() },
                                onSaveDecisionLog = { decision, confidence, dateMillis, rationale ->
                                    viewModel.saveDecisionLog(decision, confidence, dateMillis, rationale)
                                },
                                onOpenRevisitDecision = { entry -> viewModel.openRevisitDecisionDialog(entry) },
                                onCloseRevisitDecision = { viewModel.closeRevisitDecisionDialog() },
                                onSaveRevisitDecision = { id, outcomeText, outcomeTag ->
                                    viewModel.revisitDecisionLog(id, outcomeText, outcomeTag)
                                },
                                onEditDecision = { entry -> viewModel.openEditNoteDialog(entry) },
                                onDeleteDecision = { id -> viewModel.deleteNotebookEntry(id) },
                                onToggleFavorite = { entry -> viewModel.toggleFavorite(entry) },
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) }
                            )
                        }

                        ScreenRoute.WeeklyDigest -> {
                            WeeklyDigestScreen(
                                userProfile = userProfile,
                                notebookEntries = notebookEntries,
                                allHabitLogs = allHabitLogs,
                                habits = habits,
                                wealthGoal = primaryWealthGoal,
                                wealthGoalLogs = wealthGoalLogs,
                                givingGoal = givingGoal,
                                givingLogs = allGivingLogs,
                                badges = badges,
                                modules = modules,
                                mastermindCheckins = allMastermindCheckins,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) },
                                onShareDigestImage = { digest ->
                                    viewModel.exportAndShareWeeklyDigest(context, digest)
                                }
                            )
                        }

                        ScreenRoute.CommitmentContract -> {
                            CommitmentContractScreen(
                                viewModel = viewModel,
                                onBack = { viewModel.navigateTo(ScreenRoute.Dashboard) }
                            )
                        }
                    }
                }
            }

            // Luxury Paywall Modal Bottom Sheet
            if (isPaywallOpen) {
                PaywallBottomSheet(
                    onDismiss = { viewModel.closePaywall() },
                    onUnlock = { viewModel.unlockPaidAccess() }
                )
            }

            // Think and Grow Rich Daily Ritual Execution / Reflection Modal
            if (activeHabitForModal != null) {
                val habit = activeHabitForModal!!
                val isCompleted = todayHabitLogs.any { it.habitId == habit.id }
                val habitLog = todayHabitLogs.firstOrNull { it.habitId == habit.id }

                RitualDetailModal(
                    habit = habit,
                    isCompleted = isCompleted,
                    habitLog = habitLog,
                    isAmbientSoundPlaying = isAmbientSoundPlaying,
                    onToggleAmbientSound = { viewModel.toggleAmbientSound() },
                    onCompleteWithReflection = { duration, notes, saveToNb ->
                        viewModel.logHabitWithReflection(
                            habitId = habit.id,
                            durationMinutes = duration,
                            notes = notes,
                            saveToNotebook = saveToNb
                        )
                    },
                    onToggleQuick = { viewModel.toggleHabit(habit.id) },
                    onDismiss = { viewModel.closeHabitDetailModal() }
                )
            }

            // Custom Ritual Creation Modal Dialog
            if (showCustomHabitDialog) {
                CreateCustomRitualDialog(
                    onDismiss = { viewModel.setShowCustomHabitDialog(false) },
                    onSave = { title, principle, desc, cat, icon, duration, xp ->
                        viewModel.addCustomHabit(
                            title = title,
                            principle = principle,
                            description = desc,
                            category = cat,
                            iconKey = icon,
                            targetMinutes = duration,
                            xpReward = xp
                        )
                    }
                )
            }

            // Definite Chief Aim Setup & Edit Dialog
            if (showEditChiefAimDialog) {
                EditDefiniteChiefAimDialog(
                    initialStatement = userProfile?.definiteChiefAim ?: "",
                    onDismiss = { viewModel.closeEditChiefAimDialog() },
                    onSave = { newAim -> viewModel.updateDefiniteChiefAim(newAim) }
                )
            }

            // Wealth Goal Target Configuration Dialog
            if (showEditWealthGoalDialog) {
                EditWealthGoalDialog(
                    currentGoal = primaryWealthGoal ?: com.example.data.model.WealthGoalEntity(),
                    onDismiss = { viewModel.closeEditWealthGoalDialog() },
                    onSave = { title, targetAmount, startingAmount, targetDateEpochMillis, currencySymbol, category, servicePledge ->
                        viewModel.updateWealthGoalSettings(title, targetAmount, startingAmount, targetDateEpochMillis, currencySymbol, category, servicePledge)
                        viewModel.closeEditWealthGoalDialog()
                    }
                )
            }

            // Wealth Goal Inflow & Milestone Contribution Dialog
            if (showLogContributionDialog) {
                LogContributionDialog(
                    goal = primaryWealthGoal ?: com.example.data.model.WealthGoalEntity(),
                    onDismiss = { viewModel.closeLogContributionDialog() },
                    onLog = { amount, isMilestoneOnly, title, note, saveToNotebook ->
                        viewModel.logWealthContribution(amount, isMilestoneOnly, title, note, saveToNotebook)
                        viewModel.closeLogContributionDialog()
                    }
                )
            }

            // Gratitude & Giving Act Quick-Entry Dialog
            if (showLogGivingDialog) {
                LogGivingActDialog(
                    initialLog = editingGivingLog,
                    onDismiss = { viewModel.closeLogGivingDialog() },
                    onSave = { title, amount, currencySymbol, category, recipientName, note, saveToNotebook ->
                        if (editingGivingLog != null) {
                            viewModel.updateGivingLog(
                                editingGivingLog!!.copy(
                                    title = title,
                                    amount = amount,
                                    currencySymbol = currencySymbol,
                                    category = category,
                                    recipientName = recipientName,
                                    note = note
                                )
                            )
                        } else {
                            viewModel.logGivingAct(
                                title = title,
                                amount = amount,
                                currencySymbol = currencySymbol,
                                category = category,
                                recipientName = recipientName,
                                note = note,
                                saveToNotebook = saveToNotebook
                            )
                        }
                        viewModel.closeLogGivingDialog()
                    }
                )
            }

            // Giving Goal Settings & Tithing Target Configuration Dialog
            if (showGivingGoalDialog) {
                GivingGoalSettingsDialog(
                    currentGoal = givingGoal,
                    onDismiss = { viewModel.closeGivingGoalDialog() },
                    onSaveGoal = { goalType, targetAmount, targetPercentage, targetActsCount, currencySymbol, serviceMotto ->
                        viewModel.updateGivingGoalSettings(
                            goalType = goalType,
                            targetAmount = targetAmount,
                            targetPercentage = targetPercentage,
                            targetActsCount = targetActsCount,
                            currencySymbol = currencySymbol,
                            serviceMotto = serviceMotto
                        )
                        viewModel.closeGivingGoalDialog()
                    }
                )
            }

            // Money Mindset Journal Quick-Entry Dialog
            if (showMoneyMindsetDialog) {
                MoneyMindsetLogDialog(
                    onDismiss = { viewModel.closeMoneyMindsetDialog() },
                    onSave = { decisionType, actionText, emotion, beliefText, amount, promptQuestion ->
                        viewModel.saveMoneyMindsetEntry(decisionType, actionText, emotion, beliefText, amount, promptQuestion)
                    }
                )
            }

            // Fear-to-Action 3-Step Guided Reframe & History Modal
            if (showFearReframeDialog) {
                FearToActionReframeDialog(
                    pastEntries = notebookEntries,
                    onDismiss = { viewModel.closeFearReframeDialog() },
                    onSaveReframe = { fearText, worstCaseText, actionTodayText, fearCategory, addToDailyHabits ->
                        viewModel.saveFearReframe(fearText, worstCaseText, actionTodayText, fearCategory, addToDailyHabits)
                        viewModel.closeFearReframeDialog()
                    },
                    onToggleActionCompleted = { entryId, isDone ->
                        viewModel.toggleFearActionCompleted(entryId, isDone)
                    },
                    onDeleteEntry = { entryId ->
                        viewModel.deleteNotebookEntry(entryId)
                    }
                )
            }

            // Streak Milestone Celebratory Animation Modal Dialog (3, 7, 14, 30 Days)
            if (activeMilestoneCelebration != null) {
                StreakMilestoneCelebrationDialog(
                    milestone = activeMilestoneCelebration!!,
                    onDismiss = { viewModel.dismissMilestoneCelebration() }
                )
            }

            // Module Completion Reflection Celebratory Animation Modal Dialog (Monk Motif & Vault Conquered)
            if (activeModuleCompletionCelebration != null) {
                ModuleCompletionCelebrationDialog(
                    info = activeModuleCompletionCelebration!!,
                    onDismiss = { viewModel.dismissModuleCompletionCelebration() }
                )
            }

            // Persistence Streak Recovery (Comeback Protocol) Dialog
            if (showPersistenceCheckDialog) {
                PersistenceCheckDialog(
                    initialStreakType = pendingPersistenceStreakType,
                    onDismiss = { viewModel.closePersistenceCheckDialog() },
                    onLogComeback = { streakType, obstacle, plan ->
                        viewModel.savePersistenceComeback(streakType, obstacle, plan)
                    },
                    onExplorePersistenceTitans = {
                        viewModel.closePersistenceCheckDialog()
                        viewModel.navigateTo(ScreenRoute.SuccessStoryLibrary(initialPrinciple = "Persistence"))
                    }
                )
            }

            // Section Achievement Celebratory Animation Modal Dialog (Section I, II, III, IV)
            if (activeSectionAchievementCelebration != null) {
                SectionAchievementCelebrationDialog(
                    section = activeSectionAchievementCelebration!!,
                    modules = modules,
                    onDismiss = { viewModel.dismissSectionAchievement() }
                )
            }

            // Section Achievement Toast Notification (Slide-in)
            if (sectionAchievementToast != null && activeSectionAchievementCelebration == null) {
                SectionAchievementToastBanner(
                    section = sectionAchievementToast!!,
                    onViewBadge = {
                        val sec = sectionAchievementToast!!
                        viewModel.dismissSectionToast()
                        viewModel.triggerSectionAchievement(sec)
                    },
                    onDismiss = { viewModel.dismissSectionToast() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }

            // Commitment Contract Creation Dialog
            if (showCreateContractDialog) {
                CreateCommitmentContractDialog(
                    userProfile = userProfile,
                    onDismiss = { viewModel.closeCreateContractDialog() },
                    onConfirm = { goalStatement, whyItMatters, deadlineMillis, signatureName ->
                        viewModel.createCommitmentContract(
                            goalStatement = goalStatement,
                            whyItMatters = whyItMatters,
                            deadlineMillis = deadlineMillis,
                            signatureName = signatureName
                        )
                    }
                )
            }

            // Commitment Contract Renewal Dialog
            if (contractForRenewal != null) {
                RenewCommitmentContractDialog(
                    contract = contractForRenewal!!,
                    onDismiss = { viewModel.closeRenewContractDialog() },
                    onConfirm = { newDeadlineMillis, renewalNotes ->
                        viewModel.renewCommitmentContract(
                            contractId = contractForRenewal!!.id,
                            newDeadlineMillis = newDeadlineMillis,
                            renewalNotes = renewalNotes
                        )
                    }
                )
            }

            // Commitment Contract Completion Dialog
            if (contractForCompletion != null) {
                CompleteCommitmentContractDialog(
                    contract = contractForCompletion!!,
                    onDismiss = { viewModel.closeCompleteContractDialog() },
                    onConfirm = { completionNotes ->
                        viewModel.completeCommitmentContract(
                            contractId = contractForCompletion!!.id,
                            completionNotes = completionNotes
                        )
                    }
                )
            }

            // Floating Mini Audio Player Bar (visible when audio is active/paused across screens)
            if (ttsPlayerState.currentScript != null && currentScreen !is ScreenRoute.ModuleDetail) {
                FloatingMiniAudioPlayerBar(
                    playerState = ttsPlayerState,
                    onResume = { viewModel.resumeAudio() },
                    onPause = { viewModel.pauseAudio() },
                    onStop = { viewModel.stopAudio() },
                    onOpenDetail = { modId ->
                        viewModel.navigateTo(ScreenRoute.ModuleDetail(modId))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 76.dp)
                )
            }

            // Floating Mini Short Lesson Player Bar (visible when short lesson is active/paused across screens)
            if (shortLessonPlayerState.activeLesson != null && currentScreen !is ScreenRoute.ModuleDetail) {
                FloatingMiniLessonPlayerBar(
                    playerState = shortLessonPlayerState,
                    onResume = { viewModel.resumeShortLesson() },
                    onPause = { viewModel.pauseShortLesson() },
                    onStop = { viewModel.stopShortLesson() },
                    onOpenModule = { modId ->
                        viewModel.navigateTo(ScreenRoute.ModuleDetail(modId))
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 76.dp)
                )
            }
        }
    }
}
