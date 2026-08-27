package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.data.model.BadgeEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.ThinkAndGrowRichAffirmation
import com.example.data.model.UserProfileEntity
import com.example.data.model.VisionBoardItemEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.data.model.CommitmentContractEntity
import com.example.data.repository.RichesRepository
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.WeeklyDigestAggregator
import com.example.data.model.WeeklyProgressDigest
import com.example.ui.components.CommitmentContractDashboardCard
import com.example.ui.components.WeeklyDigestDashboardCard
import com.example.ui.components.WeeklyDigestShortcutBanner
import com.example.ui.components.AutoPatternDetectionSection
import com.example.ui.components.BrushedCard
import com.example.ui.components.CircularXpProgressRing
import com.example.ui.components.DailyAffirmationWidget
import com.example.ui.components.DailyHabitsTrackerSection
import com.example.ui.components.DailyVisionNudgeBanner
import com.example.ui.components.DecisionLogShortcutBanner
import com.example.ui.components.DecisionsDueToRevisitDashboardBanner
import com.example.ui.components.FearToActionShortcutBanner
import com.example.ui.components.GivingDashboardWidget
import com.example.ui.components.IncomeIdeaDashboardWidget
import com.example.ui.components.LeaderboardPreviewWidget
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.MoneyBlueprintDashboardWidget
import com.example.ui.components.MoneyMindsetDashboardWidget
import com.example.ui.components.STREAK_MILESTONES
import com.example.ui.components.StreakMilestoneInfo
import com.example.ui.components.ThirteenVaultsMasteryCard
import com.example.ui.components.TierBadgeChip
import com.example.ui.components.VisionBoardDashboardWidget
import com.example.ui.components.WealthGoalDashboardWidget
import com.example.ui.components.WealthMindsetProgressRing
import com.example.ui.components.WealthMindsetScoreCard
import com.example.ui.components.WeeksLivedWidget
import com.example.ui.components.luxurySharedBounds
import com.example.ui.components.luxurySharedElement
import com.example.util.VoiceMemoUiState
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DashboardScreen(
    userProfile: UserProfileEntity?,
    modules: List<ModuleEntity>,
    notebookEntries: List<NotebookEntryEntity>,
    badges: List<BadgeEntity>,
    habits: List<DailyHabitEntity> = emptyList(),
    todayHabitLogs: List<DailyHabitLogEntity> = emptyList(),
    allHabitLogs: List<DailyHabitLogEntity> = emptyList(),
    selectedHabitDateEpochDay: Long = RichesRepository.getTodayEpochDay(),
    streakAnimationTrigger: Long? = null,
    onCompleteDailyRitual: () -> Unit = {},
    onSelectHabitDate: (Long) -> Unit = {},
    onToggleHabit: (String) -> Unit = {},
    onOpenHabitDetail: (DailyHabitEntity) -> Unit = {},
    onAddNewHabit: () -> Unit = {},
    onMilestoneClick: (StreakMilestoneInfo) -> Unit = {},
    onSelectModule: (Int) -> Unit,
    onNavigateToModules: () -> Unit,
    onNavigateToNotebook: () -> Unit,
    onNavigateToAssessment: () -> Unit,
    onNavigateToChat: () -> Unit = {},
    onNavigateToMasterMindCircle: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onUpdateBirthDate: (Int, Int, Int) -> Unit = { _, _, _ -> },
    onOpenPaywall: () -> Unit,
    onAffirmQuote: ((ThinkAndGrowRichAffirmation) -> Unit)? = null,
    onSaveAffirmationToNotebook: ((ThinkAndGrowRichAffirmation) -> Unit)? = null,
    onSpeakAffirmation: ((String) -> Unit)? = null,
    voiceMemoUiState: VoiceMemoUiState = VoiceMemoUiState(),
    onStartVoiceRecording: () -> Boolean = { false },
    onStopVoiceRecording: () -> Unit = {},
    onPlayVoiceRecording: () -> Unit = {},
    onStopVoiceRecordingPlayback: () -> Unit = {},
    onDeleteVoiceRecording: () -> Unit = {},
    onOpenEditAim: () -> Unit = {},
    onCompleteAimAffirmation: () -> Unit = {},
    visionItems: List<VisionBoardItemEntity> = emptyList(),
    onNavigateToVisionBoard: () -> Unit = {},
    onStartVisionRitual: () -> Unit = {},
    onOpenFearReframe: () -> Unit = {},
    onOpenDecisionLog: () -> Unit = {},
    onNavigateToDecisionLog: () -> Unit = {},
    onOpenMoneyMindset: () -> Unit = {},
    onNavigateToMoneyBlueprint: () -> Unit = {},
    onRetakeMoneyBlueprint: () -> Unit = {},
    onRevisitDecision: (NotebookEntryEntity) -> Unit = {},
    onNavigateToSuccessLibrary: (String?, String?) -> Unit = { _, _ -> },
    onNavigateToIncomeIdeaExplorer: (String?, Boolean) -> Unit = { _, _ -> },
    wealthGoal: WealthGoalEntity? = null,
    wealthGoalLogs: List<WealthGoalLogEntity> = emptyList(),
    onOpenEditWealthGoal: () -> Unit = {},
    onOpenLogWealthContribution: () -> Unit = {},
    onNavigateToWealthGoalTracker: () -> Unit = {},
    givingGoal: GivingGoalEntity? = null,
    givingLogs: List<GivingLogEntity> = emptyList(),
    givingStreakWeeks: Int = 0,
    onOpenLogGiving: () -> Unit = {},
    onNavigateToGivingTracker: () -> Unit = {},
    mastermindCheckins: List<MastermindCheckinEntity> = emptyList(),
    commitmentContract: CommitmentContractEntity? = null,
    onNavigateToCommitmentContract: () -> Unit = {},
    onCreateCommitmentContract: () -> Unit = {},
    onUpdateCommitmentProgress: (Long, Int) -> Unit = { _, _ -> },
    onCompleteCommitmentContract: (CommitmentContractEntity) -> Unit = {},
    onRenewCommitmentContract: (CommitmentContractEntity) -> Unit = {},
    isWeeklyDigestDismissed: Boolean = false,
    onNavigateToWeeklyDigest: () -> Unit = {},
    onDismissWeeklyDigest: () -> Unit = {},
    onShareWeeklyDigest: (WeeklyProgressDigest) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    var selectedBadgeForModal by remember { mutableStateOf<BadgeEntity?>(null) }
    var isVisionNudgeDismissed by remember { mutableStateOf(false) }

    val currentWeeklyDigest = remember(
        userProfile, notebookEntries, allHabitLogs, habits,
        wealthGoal, wealthGoalLogs, givingGoal, givingLogs, badges, modules, mastermindCheckins
    ) {
        WeeklyDigestAggregator.compileDigest(
            weeksAgo = 0,
            userProfile = userProfile,
            notebookEntries = notebookEntries,
            allHabitLogs = allHabitLogs,
            habits = habits,
            wealthGoal = wealthGoal,
            wealthGoalLogs = wealthGoalLogs,
            givingGoal = givingGoal,
            givingLogs = givingLogs,
            badges = badges,
            modules = modules,
            mastermindCheckins = mastermindCheckins
        )
    }

    val dueDecisions = remember(notebookEntries) {
        val thirtyDaysMillis = 30L * 24 * 60 * 60 * 1000L
        notebookEntries.filter {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG &&
                    !it.isRevisited &&
                    (System.currentTimeMillis() - it.timestamp >= thirtyDaysMillis)
        }
    }

    val nextModule = modules.firstOrNull { it.isUnlocked && !it.isCompleted }
        ?: modules.firstOrNull { it.isUnlocked }
        ?: modules.firstOrNull()

    val nextTierThreshold = when (userProfile?.tierName?.lowercase()) {
        "legacy" -> 10000
        "sovereign" -> 7000
        "architect" -> 3500
        "builder" -> 1500
        else -> 500
    }

    val windowInfo = LocalWindowSizeInfo.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("dashboard_screen"),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item { Spacer(modifier = Modifier.height(6.dp)) }

            // Weekly Progress Digest Top Dashboard Card (Dismissible)
            if (!isWeeklyDigestDismissed) {
                item {
                    WeeklyDigestDashboardCard(
                        digest = currentWeeklyDigest,
                        onViewFullDigest = onNavigateToWeeklyDigest,
                        onDismiss = onDismissWeeklyDigest,
                        onShareImage = { onShareWeeklyDigest(currentWeeklyDigest) }
                    )
                }
            }

            if (windowInfo.isTabletOrFoldable) {
                // 30-Day Decision Revisit Alert Banner
                if (dueDecisions.isNotEmpty()) {
                    item {
                        DecisionsDueToRevisitDashboardBanner(
                            dueEntries = dueDecisions,
                            onRevisitEntry = onRevisitDecision
                        )
                    }
                }

                // --- ADAPTIVE 2-COLUMN TABLET / EXPANDED LAYOUT ---
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // LEFT COLUMN (Mindset, Protocol KPI, Streaks, Daily Rituals, Decree)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            SovereignGreetingCard(userProfile = userProfile)
                            if (!isVisionNudgeDismissed) {
                                DailyVisionNudgeBanner(
                                    userProfile = userProfile,
                                    onStart60sRitual = onStartVisionRitual,
                                    onDismissNudge = { isVisionNudgeDismissed = true }
                                )
                            }
                            WeeksLivedWidget(
                                userProfile = userProfile,
                                onUpdateBirthDate = onUpdateBirthDate
                            )
                            WealthGoalDashboardWidget(
                                goal = wealthGoal,
                                recentLogs = wealthGoalLogs,
                                onLogClick = onOpenLogWealthContribution,
                                onEditClick = onOpenEditWealthGoal,
                                onViewTrackerClick = onNavigateToWealthGoalTracker
                            )
                            GivingDashboardWidget(
                                goal = givingGoal,
                                logs = givingLogs,
                                streakWeeks = givingStreakWeeks,
                                onLogGivingClick = onOpenLogGiving,
                                onOpenTrackerClick = onNavigateToGivingTracker
                            )
                            VisionBoardDashboardWidget(
                                visionItems = visionItems,
                                userProfile = userProfile,
                                onOpenVisionBoard = onNavigateToVisionBoard,
                                onStartContemplation = onStartVisionRitual
                            )
                            WealthMindsetScoreCard(
                                userProfile = userProfile,
                                onRetakeAssessment = onNavigateToAssessment
                            )
                            KpiOverviewRow(
                                userProfile = userProfile,
                                nextTierThreshold = nextTierThreshold,
                                onRetakeAssessment = onNavigateToAssessment
                            )
                            DailyHabitsTrackerSection(
                                habits = habits,
                                todayHabitLogs = todayHabitLogs,
                                allHabitLogs = allHabitLogs,
                                selectedDateEpochDay = selectedHabitDateEpochDay,
                                userProfile = userProfile,
                                onSelectDate = onSelectHabitDate,
                                onToggleHabit = onToggleHabit,
                                onOpenHabitDetail = onOpenHabitDetail,
                                onAddNewHabit = onAddNewHabit,
                                onMilestoneClick = onMilestoneClick
                            )
                            StreaksMatrixCard(
                                userProfile = userProfile,
                                streakAnimationTrigger = streakAnimationTrigger,
                                onCompleteDailyRitual = onCompleteDailyRitual,
                                onMilestoneClick = onMilestoneClick
                            )
                            DailyAffirmationWidget(
                                userProfile = userProfile,
                                epochDay = selectedHabitDateEpochDay,
                                voiceMemoUiState = voiceMemoUiState,
                                onStartVoiceRecording = onStartVoiceRecording,
                                onStopVoiceRecording = onStopVoiceRecording,
                                onPlayVoiceRecording = onPlayVoiceRecording,
                                onStopVoiceRecordingPlayback = onStopVoiceRecordingPlayback,
                                onDeleteVoiceRecording = onDeleteVoiceRecording,
                                onOpenEditAim = onOpenEditAim,
                                onCompleteAimAffirmation = onCompleteAimAffirmation,
                                onAffirmToday = { affirmation ->
                                    if (onAffirmQuote != null) {
                                        onAffirmQuote(affirmation)
                                    } else {
                                        onCompleteDailyRitual()
                                    }
                                },
                                onSaveToNotebook = onSaveAffirmationToNotebook,
                                onSpeakAffirmation = onSpeakAffirmation
                            )
                            CommitmentContractDashboardCard(
                                contract = commitmentContract,
                                userProfile = userProfile,
                                onNavigateToContractScreen = onNavigateToCommitmentContract,
                                onCreateContract = onCreateCommitmentContract,
                                onUpdateProgress = onUpdateCommitmentProgress,
                                onCompleteContract = onCompleteCommitmentContract,
                                onRenewContract = onRenewCommitmentContract
                            )
                            DailySovereignDecreeCard(
                                onCompleteDailyRitual = onCompleteDailyRitual
                            )
                        }

                        // RIGHT COLUMN (AI Council, Mastermind Circle, Continue Ritual, 13 Vaults, Notebook, Badges)
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            MasterMindFastAccessCard(onNavigateToChat = onNavigateToChat)
                            MasterMindCircleFastAccessCard(onNavigateToMasterMindCircle = onNavigateToMasterMindCircle)
                            LeaderboardPreviewWidget(
                                userProfile = userProfile,
                                modules = modules,
                                onOpenLeaderboard = onNavigateToLeaderboard
                            )
                            ContinueRitualCard(
                                module = nextModule,
                                onSelectModule = onSelectModule,
                                onOpenPaywall = onOpenPaywall,
                                onViewAllModules = onNavigateToModules,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            Box(
                                modifier = Modifier.luxurySharedBounds(
                                    key = "thirteen_vaults_card",
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            ) {
                                ThirteenVaultsMasteryCard(
                                    modules = modules,
                                    isPaidUnlocked = userProfile?.isPaidUnlocked == true,
                                    onSelectModule = onSelectModule,
                                    onOpenPaywall = onOpenPaywall
                                )
                            }
                            MoneyBlueprintDashboardWidget(
                                userProfile = userProfile,
                                onOpenQuiz = onNavigateToMoneyBlueprint,
                                onRetakeQuiz = onRetakeMoneyBlueprint
                            )
                            IncomeIdeaDashboardWidget(
                                onExploreIdeas = { onNavigateToIncomeIdeaExplorer(null, false) }
                            )
                            MoneyMindsetDashboardWidget(
                                notebookEntries = notebookEntries,
                                onLogMoneyMoment = onOpenMoneyMindset,
                                onReviewMindsetLog = onNavigateToNotebook
                            )
                            AutoPatternDetectionSection(
                                entries = notebookEntries,
                                onOpenNewNote = onNavigateToNotebook,
                                onFilterByKeyword = { onNavigateToNotebook() },
                                onSelectEntry = { onNavigateToNotebook() }
                            )
                            DecisionLogShortcutBanner(
                                onOpenDecisionLog = onOpenDecisionLog,
                                onViewHistory = onNavigateToDecisionLog
                            )
                            FearToActionShortcutBanner(onOpenReframe = onOpenFearReframe)
                            WeeklyDigestShortcutBanner(onOpenDigest = onNavigateToWeeklyDigest)
                            RecentNotebookSection(
                                recentEntries = notebookEntries.take(2),
                                onNavigateToNotebook = onNavigateToNotebook
                            )
                            DashboardBadgesShowcaseSection(
                                badges = badges,
                                userProfile = userProfile,
                                onBadgeClick = { badge -> selectedBadgeForModal = badge },
                                onNavigateToProfile = onNavigateToProfile,
                                onNavigateToNotebook = onNavigateToNotebook,
                                onCompleteDailyRitual = onCompleteDailyRitual
                            )
                        }
                    }
                }
            } else {
                // 30-Day Decision Revisit Alert Banner
                if (dueDecisions.isNotEmpty()) {
                    item {
                        DecisionsDueToRevisitDashboardBanner(
                            dueEntries = dueDecisions,
                            onRevisitEntry = onRevisitDecision
                        )
                    }
                }

                // --- COMPACT PHONE SINGLE-COLUMN LAYOUT ---
                item { SovereignGreetingCard(userProfile = userProfile) }
                if (!isVisionNudgeDismissed) {
                    item {
                        DailyVisionNudgeBanner(
                            userProfile = userProfile,
                            onStart60sRitual = onStartVisionRitual,
                            onDismissNudge = { isVisionNudgeDismissed = true }
                        )
                    }
                }
                item {
                    WeeksLivedWidget(
                        userProfile = userProfile,
                        onUpdateBirthDate = onUpdateBirthDate
                    )
                }
                item {
                    WealthGoalDashboardWidget(
                        goal = wealthGoal,
                        recentLogs = wealthGoalLogs,
                        onLogClick = onOpenLogWealthContribution,
                        onEditClick = onOpenEditWealthGoal,
                        onViewTrackerClick = onNavigateToWealthGoalTracker
                    )
                }
                item {
                    GivingDashboardWidget(
                        goal = givingGoal,
                        logs = givingLogs,
                        streakWeeks = givingStreakWeeks,
                        onLogGivingClick = onOpenLogGiving,
                        onOpenTrackerClick = onNavigateToGivingTracker
                    )
                }
                item {
                    VisionBoardDashboardWidget(
                        visionItems = visionItems,
                        userProfile = userProfile,
                        onOpenVisionBoard = onNavigateToVisionBoard,
                        onStartContemplation = onStartVisionRitual
                    )
                }
                item {
                    WealthMindsetScoreCard(
                        userProfile = userProfile,
                        onRetakeAssessment = onNavigateToAssessment
                    )
                }
                item {
                    KpiOverviewRow(
                        userProfile = userProfile,
                        nextTierThreshold = nextTierThreshold,
                        onRetakeAssessment = onNavigateToAssessment
                    )
                }
                item {
                    DailyHabitsTrackerSection(
                        habits = habits,
                        todayHabitLogs = todayHabitLogs,
                        allHabitLogs = allHabitLogs,
                        selectedDateEpochDay = selectedHabitDateEpochDay,
                        userProfile = userProfile,
                        onSelectDate = onSelectHabitDate,
                        onToggleHabit = onToggleHabit,
                        onOpenHabitDetail = onOpenHabitDetail,
                        onAddNewHabit = onAddNewHabit,
                        onMilestoneClick = onMilestoneClick
                    )
                }
                item { MasterMindFastAccessCard(onNavigateToChat = onNavigateToChat) }
                item { MasterMindCircleFastAccessCard(onNavigateToMasterMindCircle = onNavigateToMasterMindCircle) }
                item {
                    LeaderboardPreviewWidget(
                        userProfile = userProfile,
                        modules = modules,
                        onOpenLeaderboard = onNavigateToLeaderboard
                    )
                }
                item {
                    DailyAffirmationWidget(
                        userProfile = userProfile,
                        epochDay = selectedHabitDateEpochDay,
                        voiceMemoUiState = voiceMemoUiState,
                        onStartVoiceRecording = onStartVoiceRecording,
                        onStopVoiceRecording = onStopVoiceRecording,
                        onPlayVoiceRecording = onPlayVoiceRecording,
                        onStopVoiceRecordingPlayback = onStopVoiceRecordingPlayback,
                        onDeleteVoiceRecording = onDeleteVoiceRecording,
                        onOpenEditAim = onOpenEditAim,
                        onCompleteAimAffirmation = onCompleteAimAffirmation,
                        onAffirmToday = { affirmation ->
                            if (onAffirmQuote != null) {
                                onAffirmQuote(affirmation)
                            } else {
                                onCompleteDailyRitual()
                            }
                        },
                        onSaveToNotebook = onSaveAffirmationToNotebook,
                        onSpeakAffirmation = onSpeakAffirmation
                    )
                }
                item {
                    CommitmentContractDashboardCard(
                        contract = commitmentContract,
                        userProfile = userProfile,
                        onNavigateToContractScreen = onNavigateToCommitmentContract,
                        onCreateContract = onCreateCommitmentContract,
                        onUpdateProgress = onUpdateCommitmentProgress,
                        onCompleteContract = onCompleteCommitmentContract,
                        onRenewContract = onRenewCommitmentContract
                    )
                }
                item {
                    StreaksMatrixCard(
                        userProfile = userProfile,
                        streakAnimationTrigger = streakAnimationTrigger,
                        onCompleteDailyRitual = onCompleteDailyRitual,
                        onMilestoneClick = onMilestoneClick
                    )
                }
                item {
                    ContinueRitualCard(
                        module = nextModule,
                        onSelectModule = onSelectModule,
                        onOpenPaywall = onOpenPaywall,
                        onViewAllModules = onNavigateToModules,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
                item {
                    Box(
                        modifier = Modifier.luxurySharedBounds(
                            key = "thirteen_vaults_card",
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    ) {
                        ThirteenVaultsMasteryCard(
                            modules = modules,
                            isPaidUnlocked = userProfile?.isPaidUnlocked == true,
                            onSelectModule = onSelectModule,
                            onOpenPaywall = onOpenPaywall
                        )
                    }
                }
                item {
                    DailySovereignDecreeCard(
                        onCompleteDailyRitual = onCompleteDailyRitual
                    )
                }
                item {
                    MoneyBlueprintDashboardWidget(
                        userProfile = userProfile,
                        onOpenQuiz = onNavigateToMoneyBlueprint,
                        onRetakeQuiz = onRetakeMoneyBlueprint
                    )
                }
                item {
                    MoneyMindsetDashboardWidget(
                        notebookEntries = notebookEntries,
                        onLogMoneyMoment = onOpenMoneyMindset,
                        onReviewMindsetLog = onNavigateToNotebook
                    )
                }
                item {
                    AutoPatternDetectionSection(
                        entries = notebookEntries,
                        onOpenNewNote = onNavigateToNotebook,
                        onFilterByKeyword = { onNavigateToNotebook() },
                        onSelectEntry = { onNavigateToNotebook() }
                    )
                }
                item {
                    DecisionLogShortcutBanner(
                        onOpenDecisionLog = onOpenDecisionLog,
                        onViewHistory = onNavigateToDecisionLog
                    )
                }
                item {
                    FearToActionShortcutBanner(
                        onOpenReframe = onOpenFearReframe
                    )
                }
                item {
                    WeeklyDigestShortcutBanner(
                        onOpenDigest = onNavigateToWeeklyDigest
                    )
                }
                item {
                    RecentNotebookSection(
                        recentEntries = notebookEntries.take(2),
                        onNavigateToNotebook = onNavigateToNotebook
                    )
                }
                item {
                    DashboardBadgesShowcaseSection(
                        badges = badges,
                        userProfile = userProfile,
                        onBadgeClick = { badge -> selectedBadgeForModal = badge },
                        onNavigateToProfile = onNavigateToProfile,
                        onNavigateToNotebook = onNavigateToNotebook,
                        onCompleteDailyRitual = onCompleteDailyRitual
                    )
                }
                item {
                    IncomeIdeaDashboardWidget(
                        onExploreIdeas = { onNavigateToIncomeIdeaExplorer(null, false) }
                    )
                }
                item {
                    SuccessStoryLibraryDashboardBanner(
                        onExplore = { onNavigateToSuccessLibrary(null, null) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // Badge Detail Dialog
        selectedBadgeForModal?.let { badge ->
            BadgeDetailDialog(
                badge = badge,
                onDismiss = { selectedBadgeForModal = null },
                onNavigateToNotebook = {
                    selectedBadgeForModal = null
                    onNavigateToNotebook()
                },
                onCompleteDailyRitual = {
                    selectedBadgeForModal = null
                    onCompleteDailyRitual()
                }
            )
        }
    }
}

@Composable
private fun MasterMindFastAccessCard(onNavigateToChat: () -> Unit) {
    BrushedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToChat() }
                .testTag("mastermind_council_fast_access"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GoldDark, GoldLight))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = RichBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "The Master Mind AI Council",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GoldLight
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(AmberAccent.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text("GEMINI", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AmberAccent)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Consult Napoleon Hill, Andrew Carnegie & Sovereign Mentors",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Consult Council",
                tint = GoldLight,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun MasterMindCircleFastAccessCard(onNavigateToMasterMindCircle: () -> Unit) {
    BrushedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToMasterMindCircle() }
                .testTag("mastermind_circle_fast_access"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Color(0xFF8B6B23), GoldPrimary, GoldLight))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Groups,
                        contentDescription = null,
                        tint = RichBlack,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Mastermind Circle",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GoldLight
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoldPrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text("COHORT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldLight)
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Accountability Cohort • Weekly Check-ins • Combined XP",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open Mastermind Circle",
                tint = GoldLight,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SovereignGreetingCard(userProfile: UserProfileEntity?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "DAILY SOVEREIGN RITUAL",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = GoldPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = userProfile?.name ?: "Sovereign Mind",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextPrimary
            )
        }
        TierBadgeChip(tier = userProfile?.tierName ?: "Novice")
    }
}

@Composable
private fun KpiOverviewRow(
    userProfile: UserProfileEntity?,
    nextTierThreshold: Int,
    onRetakeAssessment: () -> Unit
) {
    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circular XP Ring
            CircularXpProgressRing(
                currentXp = userProfile?.xpTotal ?: 0,
                nextTierThreshold = nextTierThreshold,
                tierName = userProfile?.tierName ?: "Novice",
                size = 96.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Protocol XP Progress Column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PROTOCOL XP TO NEXT TIER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "${userProfile?.xpTotal ?: 0}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = GoldLight
                    )
                    Text(
                        text = "/$nextTierThreshold XP",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 3.dp, start = 3.dp)
                    )
                }

                Text(
                    text = "Target: ${userProfile?.tierName ?: "Novice"} Advancement",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AmberAccent
                )

                val progress = ((userProfile?.xpTotal ?: 0).toFloat() / nextTierThreshold.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = GoldLight,
                    trackColor = SurfaceElevated
                )
            }
        }
    }
}

@Composable
private fun StreaksMatrixCard(
    userProfile: UserProfileEntity?,
    streakAnimationTrigger: Long? = null,
    onCompleteDailyRitual: () -> Unit = {},
    onMilestoneClick: (StreakMilestoneInfo) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val currentStreak = userProfile?.currentStreak ?: 1
    val learningStreak = userProfile?.learningStreak ?: 1
    val journalStreak = userProfile?.journalStreak ?: 1
    val perfectWeeks = userProfile?.perfectWeeksCount ?: 0

    // Animation states for subtle luxury celebration
    val badgeScale = remember { Animatable(1f) }
    val haloScale = remember { Animatable(1f) }
    val haloAlpha = remember { Animatable(0f) }
    val particlesProgress = remember { Animatable(0f) }
    val cardGlowAlpha = remember { Animatable(0f) }
    var showCelebrationBanner by remember { mutableStateOf(false) }

    // Ambient resting ember flicker animation
    val infiniteTransition = rememberInfiniteTransition(label = "ember_flicker")
    val emberPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ember_scale"
    )
    val emberRotation by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ember_rot"
    )

    // Trigger effect when streak increases or daily ritual is completed
    LaunchedEffect(streakAnimationTrigger, currentStreak) {
        if (streakAnimationTrigger != null) {
            showCelebrationBanner = true
            // Run coordinated subtle luxury celebration
            cardGlowAlpha.snapTo(0.65f)
            haloScale.snapTo(0.5f)
            haloAlpha.snapTo(0.9f)
            particlesProgress.snapTo(0f)

            // Parallel animations
            launch {
                badgeScale.animateTo(
                    1.28f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                badgeScale.animateTo(1.0f, animationSpec = tween(350))
            }
            launch {
                haloScale.animateTo(1.9f, animationSpec = tween(1100, easing = FastOutSlowInEasing))
            }
            launch {
                haloAlpha.animateTo(0f, animationSpec = tween(1100, easing = FastOutSlowInEasing))
            }
            launch {
                particlesProgress.animateTo(1f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
            }
            launch {
                cardGlowAlpha.animateTo(0f, animationSpec = tween(1400))
            }

            kotlinx.coroutines.delay(3600)
            showCelebrationBanner = false
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = if (cardGlowAlpha.value > 0.05f) 1.5.dp else 1.dp,
                    brush = if (cardGlowAlpha.value > 0.05f) {
                        Brush.horizontalGradient(
                            listOf(
                                GoldDark,
                                AmberBright.copy(alpha = cardGlowAlpha.value.coerceIn(0f, 1f)),
                                GoldLight,
                                GoldDark
                            )
                        )
                    } else {
                        Brush.linearGradient(listOf(DarkBorder, DarkBorder))
                    },
                    shape = RoundedCornerShape(20.dp)
                )
                .testTag("streaks_matrix_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCharcoal)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header row with subtle animated counter badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(AmberAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Whatshot,
                                contentDescription = null,
                                tint = AmberBright,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RITUAL STREAK DISCIPLINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                    }

                    // --- Subtle Luxury Animated Counter Badge ---
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = badgeScale.value
                                scaleY = badgeScale.value
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        AmberAccent.copy(alpha = 0.25f),
                                        GoldDark.copy(alpha = 0.4f)
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = AmberBright.copy(alpha = 0.6f + (cardGlowAlpha.value * 0.4f)),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onCompleteDailyRitual()
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Expanding Halo Ring during celebration
                        if (haloAlpha.value > 0.01f) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .graphicsLayer {
                                        scaleX = haloScale.value
                                        scaleY = haloScale.value
                                        alpha = haloAlpha.value
                                    }
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(
                                                AmberBright.copy(alpha = 0.7f),
                                                GoldLight.copy(alpha = 0.4f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                        }

                        // Sparkling Micro Particles during celebration
                        if (particlesProgress.value in 0.01f..0.99f) {
                            val progress = particlesProgress.value
                            val radiusPx = progress * 32f
                            val particleAlpha = (1f - progress).coerceIn(0f, 1f)

                            Box(modifier = Modifier.size(24.dp)) {
                                val angles = listOf(0.0, 60.0, 120.0, 180.0, 240.0, 300.0)
                                for (angle in angles) {
                                    val rad = Math.toRadians(angle)
                                    val xOffset = (cos(rad) * radiusPx).toFloat()
                                    val yOffset = (sin(rad) * radiusPx).toFloat()
                                    Box(
                                        modifier = Modifier
                                            .offset(x = xOffset.dp, y = yOffset.dp)
                                            .size(4.dp)
                                            .alpha(particleAlpha)
                                            .clip(CircleShape)
                                            .background(GoldLight)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Streak Fire",
                                tint = AmberBright,
                                modifier = Modifier
                                    .size(16.dp)
                                    .scale(emberPulse)
                                    .rotate(emberRotation)
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            // Rolling Number Transition
                            AnimatedContent(
                                targetState = currentStreak,
                                transitionSpec = {
                                    (slideInVertically(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    ) { fullHeight -> -fullHeight } + fadeIn())
                                        .togetherWith(
                                            slideOutVertically(
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            ) { fullHeight -> fullHeight } + fadeOut()
                                        )
                                },
                                label = "streak_number_roll"
                            ) { streakNumber ->
                                Text(
                                    text = "$streakNumber Days",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberBright,
                                    fontFamily = FontFamily.Serif
                                )
                            }
                        }
                    }
                }

                // Subtle celebration banner when ritual is sealed
                AnimatedVisibility(
                    visible = showCelebrationBanner,
                    enter = fadeIn(tween(250)) + slideInVertically { -it / 2 },
                    exit = fadeOut(tween(350)) + slideOutVertically { -it / 2 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            GoldDark.copy(alpha = 0.4f),
                                            AmberAccent.copy(alpha = 0.35f),
                                            GoldDark.copy(alpha = 0.4f)
                                        )
                                    )
                                )
                                .border(1.dp, GoldLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Daily Ritual Sealed • Flame Transmuted to Day $currentStreak! (+50 XP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GoldLight
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Streak Dimension Items (Daily Login, Learning, Journaling, Perfect Week, Comebacks)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    StreakItem(
                        modifier = Modifier.weight(1f),
                        label = "Daily Login",
                        count = currentStreak,
                        icon = Icons.Filled.LocalFireDepartment,
                        color = AmberBright,
                        isPulsing = cardGlowAlpha.value > 0.05f
                    )
                    StreakItem(
                        modifier = Modifier.weight(1f),
                        label = "Learning",
                        count = learningStreak,
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        color = GoldPrimary,
                        isPulsing = cardGlowAlpha.value > 0.05f
                    )
                    StreakItem(
                        modifier = Modifier.weight(1f),
                        label = "Journaling",
                        count = journalStreak,
                        icon = Icons.Filled.EditNote,
                        color = GoldLight,
                        isPulsing = cardGlowAlpha.value > 0.05f
                    )
                    StreakItem(
                        modifier = Modifier.weight(1f),
                        label = "Perfect Wk",
                        count = perfectWeeks,
                        icon = Icons.Filled.WorkspacePremium,
                        color = Color(0xFFFFD700),
                        isPulsing = cardGlowAlpha.value > 0.05f
                    )
                    StreakItem(
                        modifier = Modifier.weight(1f),
                        label = "Comebacks",
                        count = userProfile?.comebacksCount ?: 0,
                        icon = Icons.Filled.Bolt,
                        color = AmberAccent,
                        isPulsing = cardGlowAlpha.value > 0.05f
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Milestone Badges Pathway Strip (3, 7, 14, 30 Days)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .border(1.dp, GoldDark.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    STREAK_MILESTONES.forEach { milestone ->
                        val isReached = currentStreak >= milestone.days
                        val isNextTarget = !isReached && (STREAK_MILESTONES.firstOrNull { currentStreak < it.days }?.days == milestone.days)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when {
                                        isReached -> Color(0xFFD4AF37).copy(alpha = 0.18f)
                                        isNextTarget -> Color(0xFFFFA000).copy(alpha = 0.12f)
                                        else -> Color.White.copy(alpha = 0.04f)
                                    }
                                )
                                .border(
                                    1.dp,
                                    when {
                                        isReached -> Color(0xFFFFD700).copy(alpha = 0.7f)
                                        isNextTarget -> Color(0xFFFFA000).copy(alpha = 0.5f)
                                        else -> Color.White.copy(alpha = 0.08f)
                                    },
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { onMilestoneClick(milestone) }
                                .padding(horizontal = 7.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isReached) Icons.Filled.CheckCircle else milestone.iconVector,
                                contentDescription = milestone.title,
                                tint = if (isReached) Color(0xFFFFD700) else if (isNextTarget) Color(0xFFFFB300) else Color(0xFF8C7D68),
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "${milestone.days}D",
                                fontSize = 10.sp,
                                fontWeight = if (isReached || isNextTarget) FontWeight.Bold else FontWeight.Medium,
                                color = if (isReached) Color(0xFFFFD700) else if (isNextTarget) Color(0xFFFFE082) else Color(0xFF9E9180)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Daily Ritual Seal Action Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    SurfaceElevated,
                                    DarkCharcoal
                                )
                            )
                        )
                        .border(1.dp, GoldPrimary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onCompleteDailyRitual()
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("seal_daily_ritual_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(AmberBright, GoldDark)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Whatshot,
                                    contentDescription = null,
                                    tint = RichBlack,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Daily Discipline Protocol",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Maintain momentum & unlock sovereign status",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldLight.copy(alpha = 0.15f))
                                .border(1.dp, GoldLight.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Seal Ritual (+50 XP)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StreakItem(
    label: String,
    count: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    isPulsing: Boolean = false
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(count, isPulsing) {
        if (isPulsing) {
            scale.animateTo(
                1.18f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scale.animateTo(1.0f, animationSpec = tween(300))
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(SurfaceElevated)
                .border(
                    width = if (isPulsing) 1.5.dp else 1.dp,
                    color = color.copy(alpha = if (isPulsing) 0.9f else 0.45f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Rolling Number Transition
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                (slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) { -it } + fadeIn())
                    .togetherWith(
                        slideOutVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ) { it } + fadeOut()
                    )
            },
            label = "streak_item_roll"
        ) { currentCount ->
            Text(
                text = "$currentCount",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary
            )
        }

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ContinueRitualCard(
    module: ModuleEntity?,
    onSelectModule: (Int) -> Unit,
    onOpenPaywall: () -> Unit,
    onViewAllModules: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    if (module == null) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .luxurySharedBounds(
                key = "module_card_${module.id}",
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
            .border(1.5.dp, GoldLight, RoundedCornerShape(20.dp))
            .clickable {
                if (module.isUnlocked) {
                    onSelectModule(module.id)
                } else {
                    onOpenPaywall()
                }
            }
            .testTag("continue_ritual_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCharcoal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = AmberAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "TODAY'S ASSIGNED VAULT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "+${module.xpReward} XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Vault ${module.order}: ${module.title}",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary,
                modifier = Modifier.luxurySharedElement(
                    key = "module_title_${module.id}",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )

            Text(
                text = "Principle: ${module.originalPrinciple}",
                fontSize = 12.sp,
                color = GoldPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = module.subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (module.isUnlocked) {
                            onSelectModule(module.id)
                        } else {
                            onOpenPaywall()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (module.isUnlocked) AmberAccent else SurfaceElevated,
                        contentColor = if (module.isUnlocked) RichBlack else GoldLight
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("continue_step_button")
                ) {
                    Icon(
                        imageVector = if (module.isUnlocked) Icons.Filled.PlayArrow else Icons.Filled.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (module.isUnlocked) "CONTINUE LESSON" else "UNLOCK VAULT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "View All 13 Vaults →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GoldLight,
                    modifier = Modifier
                        .clickable { onViewAllModules() }
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun DailySovereignDecreeCard(
    onCompleteDailyRitual: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    var isExpanded by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe_scale"
    )

    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(GoldDark),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "DAILY SOVEREIGN DECREE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    letterSpacing = 0.8.sp
                )
            }

            Text(
                text = if (isExpanded) "Hide Focus" else "Focus Mode",
                fontSize = 11.sp,
                color = GoldPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "\"I command full definiteness of purpose. My thoughts are transmuted into tangible wealth through sustained daily persistence and unshakeable conviction.\"",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCompleteDailyRitual()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberAccent,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("affirm_decree_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AFFIRM DECREE (+50 XP)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceElevated, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "4-7-8 Sovereign Calibrating Breath",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .scale(breatheScale)
                        .clip(CircleShape)
                        .background(GoldLinearGradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SelfImprovement,
                        contentDescription = "Breathing",
                        tint = RichBlack,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Inhale Strength • Hold Intention • Exhale Hesitation",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun RecentNotebookSection(
    recentEntries: List<NotebookEntryEntity>,
    onNavigateToNotebook: () -> Unit
) {
    BrushedCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SOVEREIGN NOTEBOOK",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 1.sp
            )

            Text(
                text = "Open Notebook →",
                fontSize = 11.sp,
                color = AmberAccent,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onNavigateToNotebook() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (recentEntries.isEmpty()) {
            Text(
                text = "No reflections inscribed yet. Complete a vault module or inscribe your daily financial intentions in your Sovereign Notebook.",
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        } else {
            recentEntries.forEach { entry ->
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onNavigateToNotebook() }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = entry.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            if (entry.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG) {
                                Surface(
                                    color = GoldPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (entry.isRevisited) "REVISITED ✓" else "DECISION (${entry.confidenceLevel}/5 ★)",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldLight,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            } else if (entry.entryType == NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME) {
                                Surface(
                                    color = AmberAccent.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (entry.isActionCompleted) "ACTION DONE ✓" else "ACTION PENDING ⚡",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AmberBright,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = entry.tags,
                                    fontSize = 9.sp,
                                    color = GoldPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when {
                                entry.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG && entry.decisionText.isNotBlank() ->
                                    "Decision: ${entry.decisionText}"
                                entry.entryType == NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME && entry.actionTodayText.isNotBlank() ->
                                    "Action: ${entry.actionTodayText}"
                                else -> entry.content
                            },
                            fontSize = 11.sp,
                            color = TextSecondary,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardBadgesShowcaseSection(
    badges: List<BadgeEntity>,
    userProfile: UserProfileEntity?,
    onBadgeClick: (BadgeEntity) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToNotebook: () -> Unit,
    onCompleteDailyRitual: () -> Unit
) {
    val unlockedBadges = badges.filter { it.isUnlocked }
    val unlockedCount = unlockedBadges.size
    val totalCount = badges.size.coerceAtLeast(1)
    val overallProgress = (unlockedCount.toFloat() / totalCount.toFloat()).coerceIn(0f, 1f)

    val consistentRitualistBadge = badges.firstOrNull { it.id == "badge_consistent_ritualist" }
        ?: BadgeEntity(
            id = "badge_consistent_ritualist",
            title = "Consistent Ritualist",
            description = "Completed 7 days of daily entries and rituals to forge an unbreakable mindset.",
            iconKey = "consistent_ritualist",
            isUnlocked = false,
            tierRequired = "Builder",
            progress = (userProfile?.currentStreak ?: 0).coerceAtMost(7),
            maxProgress = 7,
            category = "Ritual & Streak",
            xpReward = 200
        )

    BrushedCard {
        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(GoldDark.copy(alpha = 0.3f))
                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = "Achievements",
                        tint = GoldLight,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "SOVEREIGN TROPHY VAULT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$unlockedCount of $totalCount Crests Unlocked",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }

            Surface(
                color = DarkCharcoal,
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.clickable { onNavigateToProfile() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View Vault",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- OVERALL PROGRESS BAR ---
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mastery Completion",
                    fontSize = 9.sp,
                    color = TextMuted
                )
                Text(
                    text = "${(overallProgress * 100).toInt()}%",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            LinearProgressIndicator(
                progress = { overallProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = GoldPrimary,
                trackColor = DarkBorder
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- FEATURED MILESTONE: CONSISTENT RITUALIST ---
        Surface(
            color = DarkCharcoal,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (consistentRitualistBadge.isUnlocked) GoldPrimary.copy(alpha = 0.6f) else AmberAccent.copy(alpha = 0.35f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBadgeClick(consistentRitualistBadge) }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (consistentRitualistBadge.isUnlocked) GoldDark.copy(alpha = 0.5f) else AmberBright.copy(alpha = 0.15f)
                                )
                                .border(
                                    1.dp,
                                    if (consistentRitualistBadge.isUnlocked) GoldPrimary else AmberBright,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (consistentRitualistBadge.isUnlocked) Icons.Filled.WorkspacePremium else Icons.Filled.LocalFireDepartment,
                                contentDescription = "Consistent Ritualist",
                                tint = if (consistentRitualistBadge.isUnlocked) GoldLight else AmberBright,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Consistent Ritualist",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = if (consistentRitualistBadge.isUnlocked) GoldPrimary.copy(alpha = 0.2f) else AmberAccent.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "+${consistentRitualistBadge.xpReward} XP",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (consistentRitualistBadge.isUnlocked) GoldLight else AmberBright,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "7-Day Daily Entries & Rituals Protocol",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    if (consistentRitualistBadge.isUnlocked) {
                        Surface(
                            color = GoldPrimary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "ACHIEVED",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "${consistentRitualistBadge.progress} / 7 Days",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberBright
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Progress indicator for Consistent Ritualist
                val ritualProgress = (consistentRitualistBadge.progress.toFloat() / 7f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { ritualProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (consistentRitualistBadge.isUnlocked) GoldPrimary else AmberBright,
                    trackColor = DarkBorder
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!consistentRitualistBadge.isUnlocked) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onNavigateToNotebook,
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Inscribe Entry",
                                fontSize = 10.sp,
                                color = GoldLight
                            )
                        }

                        Button(
                            onClick = onCompleteDailyRitual,
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldPrimary,
                                contentColor = RichBlack
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Seal Ritual",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- BADGES HORIZONTAL SCROLL VAULT ---
        Text(
            text = "EXPLORE TROPHY CRESTS (TAP TO INSPECT)",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(badges) { badge ->
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (badge.isUnlocked) GoldPrimary.copy(alpha = 0.5f) else DarkBorder
                    ),
                    modifier = Modifier
                        .width(108.dp)
                        .clickable { onBadgeClick(badge) }
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    if (badge.isUnlocked) GoldDark.copy(alpha = 0.4f) else DarkCharcoal
                                )
                                .border(
                                    1.dp,
                                    if (badge.isUnlocked) GoldLight else TextMuted.copy(alpha = 0.3f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (badge.isUnlocked) getBadgeIconVector(badge.iconKey) else Icons.Filled.Lock,
                                contentDescription = badge.title,
                                tint = if (badge.isUnlocked) GoldLight else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = badge.title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (badge.isUnlocked) TextPrimary else TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 1
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        if (badge.isUnlocked) {
                            Text(
                                text = "UNLOCKED",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        } else if (badge.maxProgress > 1) {
                            Text(
                                text = "${badge.progress}/${badge.maxProgress}",
                                fontSize = 8.sp,
                                color = AmberBright
                            )
                        } else {
                            Text(
                                text = "${badge.tierRequired} Tier",
                                fontSize = 8.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgeDetailDialog(
    badge: BadgeEntity,
    onDismiss: () -> Unit,
    onNavigateToNotebook: () -> Unit,
    onCompleteDailyRitual: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val unlockedDateStr = badge.unlockedAt?.let { dateFormat.format(Date(it)) } ?: "In Progress"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCharcoal,
        tonalElevation = 6.dp,
        shape = RoundedCornerShape(16.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(
                            if (badge.isUnlocked) GoldDark.copy(alpha = 0.5f) else DarkBorder
                        )
                        .border(
                            2.dp,
                            if (badge.isUnlocked) GoldLight else DarkBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (badge.isUnlocked) getBadgeIconVector(badge.iconKey) else Icons.Filled.Lock,
                        contentDescription = badge.title,
                        tint = if (badge.isUnlocked) GoldLight else TextMuted,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = badge.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = GoldPrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badge.category.uppercase(),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = AmberAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "+${badge.xpReward} XP",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberBright,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = badge.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Status", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = if (badge.isUnlocked) "UNLOCKED ($unlockedDateStr)" else "LOCKED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (badge.isUnlocked) GoldLight else TextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Required Tier", fontSize = 10.sp, color = TextMuted)
                            Text(
                                text = "${badge.tierRequired} Tier",
                                fontSize = 10.sp,
                                color = TextPrimary
                            )
                        }

                        if (badge.maxProgress > 1) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Requirement Progress", fontSize = 10.sp, color = TextMuted)
                                Text(
                                    text = "${badge.progress} / ${badge.maxProgress}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (badge.isUnlocked) GoldLight else AmberBright
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { (badge.progress.toFloat() / badge.maxProgress.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = if (badge.isUnlocked) GoldPrimary else AmberBright,
                                trackColor = DarkBorder
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (!badge.isUnlocked && badge.id == "badge_consistent_ritualist") {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCompleteDailyRitual()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = RichBlack)
                ) {
                    Text("Seal Daily Ritual", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            } else if (!badge.isUnlocked && (badge.id == "badge_first_reflection" || badge.id == "badge_prolific_scribe")) {
                Button(
                    onClick = onNavigateToNotebook,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = RichBlack)
                ) {
                    Text("Write Entry", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            } else {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary, contentColor = RichBlack)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss", color = TextMuted, fontSize = 11.sp)
            }
        }
    )
}

private fun getBadgeIconVector(iconKey: String): ImageVector {
    return when (iconKey.lowercase()) {
        "consistent_ritualist" -> Icons.Filled.LocalFireDepartment
        "notebook" -> Icons.AutoMirrored.Filled.MenuBook
        "scribe" -> Icons.Filled.EditNote
        "diagnosis" -> Icons.Filled.Psychology
        "vault" -> Icons.Filled.Shield
        "streak" -> Icons.Filled.Whatshot
        "ignition" -> Icons.Filled.Bolt
        "mastermind" -> Icons.Filled.Stars
        "transmutation" -> Icons.Filled.Diamond
        "fortress" -> Icons.Filled.Shield
        "apex" -> Icons.Filled.WorkspacePremium
        else -> Icons.Filled.WorkspacePremium
    }
}

@Composable
fun SuccessStoryLibraryDashboardBanner(
    onExplore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background.red < 0.5f
    val goldColor = if (isDark) GoldLight else GoldDark
    val primaryText = if (isDark) TextPrimary else com.example.ui.theme.LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else com.example.ui.theme.LightTextSecondary
    val cardBorder = if (isDark) DarkBorder else com.example.ui.theme.LightBorder

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onExplore() }
            .testTag("dashboard_success_library_banner")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        AmberAccent.copy(alpha = if (isDark) 0.35f else 0.25f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.2.dp, goldColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "HISTORICAL TITANS LIBRARY",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp,
                            color = primaryText
                        )
                        Text(
                            text = "16 Real-World Principle Case Studies",
                            fontSize = 11.sp,
                            color = secondaryText
                        )
                    }
                }

                Surface(
                    color = AmberAccent.copy(alpha = if (isDark) 0.15f else 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(0.6.dp, goldColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "16 PROFILES",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        color = if (isDark) AmberBright else GoldDark,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = "Explore how titans like Carnegie, Edison, Ford, Musk, and Buffett applied the 13 sovereign principles to transmute desires into physical empires.",
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = secondaryText
            )

            // Horizontal preview chips of notable figures
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Carnegie", "Edison", "Ford", "Musk", "Buffett").forEach { titan ->
                    Surface(
                        color = if (isDark) SurfaceElevated else com.example.ui.theme.LightElevated,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, cardBorder)
                    ) {
                        Text(
                            text = titan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = goldColor,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Browse Titans Library",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = goldColor,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}


