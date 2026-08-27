package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AudioScriptType
import com.example.data.model.ModuleAudioScript
import com.example.data.model.ModuleEntity
import com.example.data.model.ModuleReflectionPrompt
import com.example.data.model.ModuleReflectionPromptsProvider
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.ShortLessonEntity
import com.example.data.model.SuccessFigure
import com.example.data.repository.ModuleAudioScriptsProvider
import com.example.data.repository.SuccessStoryLibraryData
import com.example.ui.components.BrushedCard
import com.example.ui.components.ModuleAudioPlayerCard
import com.example.ui.components.ModuleLessonsHeaderBadge
import com.example.ui.components.ModuleVaultCircularProgressRing
import com.example.ui.components.PrincipleFigureCrossLinkCard
import com.example.ui.components.ShortLessonInlinePlayerCard
import com.example.ui.components.ShortLessonItemCard
import com.example.ui.components.SuccessFigureDetailDialog
import com.example.ui.components.TierBadgeChip
import com.example.ui.components.luxurySharedBounds
import com.example.ui.components.luxurySharedElement
import com.example.util.ShortLessonPlayerState
import com.example.util.TtsPlayerState
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.MutedGold
import com.example.ui.theme.NightBlack
import com.example.ui.theme.PureBlack
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ModuleDetailScreen(
    module: ModuleEntity,
    moduleShortLessons: List<ShortLessonEntity> = emptyList(),
    shortLessonPlayerState: ShortLessonPlayerState? = null,
    ttsPlayerState: TtsPlayerState? = null,
    existingReflections: List<NotebookEntryEntity> = emptyList(),
    onSubmitModuleReflection: ((Int, Map<String, String>, (() -> Unit)?) -> Unit)? = null,
    onBack: () -> Unit,
    onCompleteLesson: (Int) -> Unit,
    onCompleteQuest: (Int) -> Unit,
    onSaveWorksheet: (Int, String, String, String) -> Unit,
    onSaveNotebookReflection: (Int, String, String, String, String, String, Boolean) -> Unit,
    onPlayShortLesson: (ShortLessonEntity) -> Unit = {},
    onResumeShortLesson: () -> Unit = {},
    onPauseShortLesson: () -> Unit = {},
    onSeekShortLesson: (Int) -> Unit = {},
    onSeekShortLessonRelative: (Int) -> Unit = {},
    onSetShortLessonSpeed: (Float) -> Unit = {},
    onToggleShortLessonVideoMode: () -> Unit = {},
    onToggleShortLessonTranscript: () -> Unit = {},
    onToggleShortLessonChapters: () -> Unit = {},
    onToggleShortLessonAmbient: () -> Unit = {},
    onSaveShortLessonToNotebook: (ShortLessonEntity) -> Unit = {},
    onToggleLessonCompletion: (ShortLessonEntity) -> Unit = {},
    onCompleteShortLesson: (String) -> Unit = {},
    onPlayAudioScript: (ModuleAudioScript) -> Unit = {},
    onResumeAudio: () -> Unit = {},
    onPauseAudio: () -> Unit = {},
    onStopAudio: () -> Unit = {},
    onNextAudioSentence: () -> Unit = {},
    onPreviousAudioSentence: () -> Unit = {},
    onSeekAudioSentence: (Int) -> Unit = {},
    onSetAudioRate: (Float) -> Unit = {},
    onSetAudioPitch: (Float) -> Unit = {},
    onToggleAudioAmbient: () -> Unit = {},
    onSaveAffirmationToNotebook: (ModuleAudioScript) -> Unit = {},
    onNavigateToSuccessLibrary: ((String?, String?) -> Unit)? = null,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedFigureForModal by remember { mutableStateOf<SuccessFigure?>(null) }
    var showIncompleteReflectionWarningDialog by remember { mutableStateOf(false) }
    val tabs = listOf("Lessons", "Voice & Lecture", "Manuscript", "Worksheet", "Quest", "Notebook", "Reflection")

    val handleBackClick = {
        if (!module.isCompleted) {
            showIncompleteReflectionWarningDialog = true
        } else {
            onBack()
        }
    }

    val moduleScripts = remember(module.id) {
        val scripts = ModuleAudioScriptsProvider.getScriptsForModule(module.id).toMutableList()
        // Also add full manuscript excerpt as an audio script option!
        if (scripts.none { it.type == AudioScriptType.MANUSCRIPT }) {
            scripts.add(
                ModuleAudioScript(
                    id = "vault_${module.id}_manuscript",
                    moduleId = module.id,
                    moduleTitle = module.title,
                    principleName = module.originalPrinciple,
                    title = "${module.excerptTitle} (Manuscript)",
                    type = AudioScriptType.MANUSCRIPT,
                    description = "Full narrated recitation of Napoleon Hill's original philosophical excerpt.",
                    textToSpeak = module.excerptText,
                    estimatedDurationSeconds = 90
                )
            )
        }
        scripts
    }

    var selectedScript by remember(module.id) {
        mutableStateOf(moduleScripts.firstOrNull() ?: ModuleAudioScriptsProvider.getDefaultScriptForModule(module.id))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("module_detail_screen")
    ) {
        // --- TOP MODULE HEADER ---
        Surface(
            color = DarkCharcoal,
            modifier = Modifier
                .fillMaxWidth()
                .luxurySharedBounds(
                    key = "module_card_${module.id}",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .border(1.dp, DarkBorder, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = handleBackClick,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GoldLight
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier.luxurySharedElement(
                            key = "module_ring_${module.id}",
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    ) {
                        ModuleVaultCircularProgressRing(
                            isUnlocked = module.isUnlocked,
                            isCompleted = module.isCompleted,
                            order = module.order,
                            size = 38.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "VAULT ${module.order} • ${module.originalPrinciple.uppercase()}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = module.title,
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
                    }

                    if (module.isCompleted) {
                        Surface(
                            color = SuccessGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "CONQUERED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "+${module.xpReward} XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stepped Navigation Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = DarkCharcoal,
                    contentColor = GoldLight,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GoldLight,
                            height = 2.5.dp
                        )
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isReflection = index == 6
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (isReflection) {
                                        Icon(
                                            imageVector = if (module.isCompleted) Icons.Default.CheckCircle else Icons.Default.SelfImprovement,
                                            contentDescription = null,
                                            tint = if (selectedTab == index) GoldLight else if (module.isCompleted) SuccessGreen else AmberAccent,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                    Text(
                                        text = if (isReflection) "Reflection ✦" else title,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedTab == index) GoldLight else if (isReflection && !module.isCompleted) AmberAccent else TextMuted
                                    )
                                }
                            },
                            modifier = Modifier.testTag("tab_$title")
                        )
                    }
                }
            }
        }

        // --- TAB CONTENT BODY ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> ShortLessonsTabContent(
                    module = module,
                    lessons = moduleShortLessons,
                    playerState = shortLessonPlayerState,
                    onPlayLesson = onPlayShortLesson,
                    onResumeLesson = onResumeShortLesson,
                    onPauseLesson = onPauseShortLesson,
                    onSeekLesson = onSeekShortLesson,
                    onSeekLessonRelative = onSeekShortLessonRelative,
                    onSetSpeed = onSetShortLessonSpeed,
                    onToggleVideoMode = onToggleShortLessonVideoMode,
                    onToggleTranscript = onToggleShortLessonTranscript,
                    onToggleChapters = onToggleShortLessonChapters,
                    onToggleAmbient = onToggleShortLessonAmbient,
                    onSaveToNotebook = onSaveShortLessonToNotebook,
                    onToggleCompletion = onToggleLessonCompletion,
                    onMarkCompleted = onCompleteShortLesson,
                    onNextTab = { selectedTab = 1 }
                )
                1 -> LectureTabContent(
                    module = module,
                    availableScripts = moduleScripts,
                    selectedScript = selectedScript,
                    onSelectScript = { selectedScript = it },
                    ttsPlayerState = ttsPlayerState,
                    onPlayAudioScript = onPlayAudioScript,
                    onResumeAudio = onResumeAudio,
                    onPauseAudio = onPauseAudio,
                    onStopAudio = onStopAudio,
                    onNextAudioSentence = onNextAudioSentence,
                    onPreviousAudioSentence = onPreviousAudioSentence,
                    onSeekAudioSentence = onSeekAudioSentence,
                    onSetAudioRate = onSetAudioRate,
                    onSetAudioPitch = onSetAudioPitch,
                    onToggleAudioAmbient = onToggleAudioAmbient,
                    onSaveAffirmationToNotebook = onSaveAffirmationToNotebook,
                    onComplete = { onCompleteLesson(module.id) },
                    onNextTab = { selectedTab = 2 }
                )
                2 -> ManuscriptTabContent(
                    module = module,
                    onListenManuscript = {
                        val manuscriptScript = moduleScripts.firstOrNull { it.type == AudioScriptType.MANUSCRIPT }
                            ?: ModuleAudioScript(
                                id = "vault_${module.id}_manuscript",
                                moduleId = module.id,
                                moduleTitle = module.title,
                                principleName = module.originalPrinciple,
                                title = "${module.excerptTitle} (Manuscript)",
                                type = AudioScriptType.MANUSCRIPT,
                                description = "Narrated recitation of Napoleon Hill's original philosophical manuscript.",
                                textToSpeak = module.excerptText,
                                estimatedDurationSeconds = 90
                            )
                        selectedScript = manuscriptScript
                        onPlayAudioScript(manuscriptScript)
                        selectedTab = 1 // Switch to Voice tab to view teleprompter
                    },
                    onOpenFigure = { figure -> selectedFigureForModal = figure },
                    onNextTab = { selectedTab = 3 }
                )
                3 -> WorksheetTabContent(
                    module = module,
                    onSaveWorksheet = { f1, f2, f3 -> onSaveWorksheet(module.id, f1, f2, f3) },
                    onNextTab = { selectedTab = 4 }
                )
                4 -> QuestTabContent(
                    module = module,
                    onCompleteQuest = { onCompleteQuest(module.id) },
                    onNextTab = { selectedTab = 5 }
                )
                5 -> NotebookTabContent(
                    module = module,
                    onSaveReflection = { title, content ->
                        onSaveNotebookReflection(
                            module.id,
                            module.title,
                            title,
                            content,
                            module.notebookPrompt,
                            "Vault ${module.order}, ${module.originalPrinciple}",
                            true
                        )
                    },
                    onNextTab = { selectedTab = 6 }
                )
                6 -> ModuleCompletionReflectionTabContent(
                    module = module,
                    existingReflections = existingReflections.filter { it.moduleId == module.id },
                    onSubmitReflection = { answers ->
                        onSubmitModuleReflection?.invoke(module.id, answers, null)
                    }
                )
            }
        }

        // Incomplete Reflection Gating Warning Dialog
        if (showIncompleteReflectionWarningDialog) {
            IncompleteReflectionGatingDialog(
                module = module,
                onCompleteReflection = {
                    showIncompleteReflectionWarningDialog = false
                    selectedTab = 6
                },
                onLeaveAnyway = {
                    showIncompleteReflectionWarningDialog = false
                    onBack()
                }
            )
        }

        selectedFigureForModal?.let { figure ->
            SuccessFigureDetailDialog(
                figure = figure,
                onDismiss = { selectedFigureForModal = null },
                onNavigateToVault = { vaultId ->
                    selectedFigureForModal = null
                    onNavigateToSuccessLibrary?.invoke(figure.id, null)
                },
                onSaveToNotebook = { title, content, tags ->
                    onSaveNotebookReflection(
                        module.id,
                        module.title,
                        title,
                        content,
                        "Historical Titan Case Study: ${figure.name}",
                        tags,
                        false
                    )
                }
            )
        }
    }
}

@Composable
private fun LectureTabContent(
    module: ModuleEntity,
    availableScripts: List<ModuleAudioScript>,
    selectedScript: ModuleAudioScript?,
    onSelectScript: (ModuleAudioScript) -> Unit,
    ttsPlayerState: TtsPlayerState?,
    onPlayAudioScript: (ModuleAudioScript) -> Unit,
    onResumeAudio: () -> Unit,
    onPauseAudio: () -> Unit,
    onStopAudio: () -> Unit,
    onNextAudioSentence: () -> Unit,
    onPreviousAudioSentence: () -> Unit,
    onSeekAudioSentence: (Int) -> Unit,
    onSetAudioRate: (Float) -> Unit,
    onSetAudioPitch: (Float) -> Unit,
    onToggleAudioAmbient: () -> Unit,
    onSaveAffirmationToNotebook: (ModuleAudioScript) -> Unit,
    onComplete: () -> Unit,
    onNextTab: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- REAL TEXT-TO-SPEECH AUDIO PLAYER INTERFACE ---
        ModuleAudioPlayerCard(
            playerState = ttsPlayerState ?: TtsPlayerState(currentScript = selectedScript),
            availableScripts = availableScripts,
            onSelectScript = onSelectScript,
            onPlay = onPlayAudioScript,
            onResume = onResumeAudio,
            onPause = onPauseAudio,
            onStop = onStopAudio,
            onNextSentence = onNextAudioSentence,
            onPreviousSentence = onPreviousAudioSentence,
            onSeekSentence = onSeekAudioSentence,
            onSetRate = onSetAudioRate,
            onSetPitch = onSetAudioPitch,
            onToggleAmbient = onToggleAudioAmbient,
            onSaveToNotebook = onSaveAffirmationToNotebook
        )

        // Masterclass Overview Card
        BrushedCard {
            Text(
                text = "MASTERCLASS OVERVIEW",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = module.videoTitle,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = GoldLight
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = module.subtitle,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )
        }

        // Completion Action Card
        BrushedCard {
            Text(
                text = "LESSON COMPLETION PROTOCOL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Recite the spoken affirmations or complete the audio meditation, then mark complete to record XP and advance.",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onComplete()
                    onNextTab()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (module.isCompleted) SuccessGreen else AmberAccent,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("complete_lesson_button")
            ) {
                Icon(
                    imageVector = if (module.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.WorkspacePremium,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (module.isCompleted) "LESSON CONQUERED • READ MANUSCRIPT" else "MARK COMPLETED (+${module.xpReward} XP)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ManuscriptTabContent(
    module: ModuleEntity,
    onListenManuscript: () -> Unit = {},
    onOpenFigure: (SuccessFigure) -> Unit = {},
    onNextTab: () -> Unit
) {
    val scrollState = rememberScrollState()
    val matchingFigures = remember(module.id) {
        SuccessStoryLibraryData.getFiguresForPrinciple(module.id).ifEmpty {
            SuccessStoryLibraryData.getFiguresForPrincipleName(module.title)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quick Listen Bar
        Surface(
            color = DarkCharcoal,
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Read Aloud",
                        tint = GoldLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Audio Synthesizer Ready",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Listen aloud to chapter manuscript",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                Button(
                    onClick = onListenManuscript,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Read Aloud",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Excerpt Reader Card
        BrushedCard {
            Text(
                text = "THE MANUSCRIPT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = module.excerptTitle,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = GoldLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = module.excerptText,
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp,
                lineHeight = 24.sp,
                color = TextPrimary
            )
        }

        // Key Takeaways Card
        BrushedCard {
            Text(
                text = "CORE SOVEREIGN PRINCIPLES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = module.keyTakeaways,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = TextSecondary
            )
        }

        // Historical Titan Case Study Cross-Link
        if (matchingFigures.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "HISTORICAL TITAN CASE STUDY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = GoldLight
                )
                matchingFigures.forEach { figure ->
                    PrincipleFigureCrossLinkCard(
                        figure = figure,
                        onClick = { onOpenFigure(figure) }
                    )
                }
            }
        }

        Button(
            onClick = onNextTab,
            colors = ButtonDefaults.buttonColors(
                containerColor = AmberAccent,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "PROCEED TO ACTION WORKSHEET",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun WorksheetTabContent(
    module: ModuleEntity,
    onSaveWorksheet: (String, String, String) -> Unit,
    onNextTab: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    var field1 by remember(module.id) { mutableStateOf(module.savedField1) }
    var field2 by remember(module.id) { mutableStateOf(module.savedField2) }
    var field3 by remember(module.id) { mutableStateOf(module.savedField3) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BrushedCard {
            Text(
                text = "ACTIONABLE TEMPLATE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = module.templateTitle,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = GoldLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = module.templatePrompt,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field 1
            Text(text = module.templateFieldLabel1, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = field1,
                onValueChange = { field1 = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("worksheet_field_1"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldLight,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 2
            Text(text = module.templateFieldLabel2, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = field2,
                onValueChange = { field2 = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("worksheet_field_2"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldLight,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 3
            Text(text = module.templateFieldLabel3, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = field3,
                onValueChange = { field3 = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("worksheet_field_3"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldLight,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSaveWorksheet(field1, field2, field3)
                    onNextTab()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberAccent,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_worksheet_button")
            ) {
                Icon(imageVector = Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SAVE WORKSHEET TO VAULT (+50 XP)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuestTabContent(
    module: ModuleEntity,
    onCompleteQuest: () -> Unit,
    onNextTab: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BrushedCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SOVEREIGN ACTION QUEST",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberAccent,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "+100 XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = module.questTitle,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = GoldLight
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = module.questDescription,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                color = SurfaceElevated,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = module.questActionPrompt,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCompleteQuest()
                    onNextTab()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (module.isQuestCompleted) SuccessGreen else AmberAccent,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("complete_quest_button")
            ) {
                Icon(
                    imageVector = if (module.isQuestCompleted) Icons.Filled.CheckCircle else Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (module.isQuestCompleted) "QUEST CONQUERED • CONTINUE" else "CONFIRM QUEST COMPLETED (+100 XP)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun NotebookTabContent(
    module: ModuleEntity,
    onSaveReflection: (String, String) -> Unit,
    onNextTab: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    var reflectionTitle by remember { mutableStateOf("Vault ${module.order} Reflection: ${module.title}") }
    var reflectionText by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BrushedCard {
            Text(
                text = "VAULT RITUAL NOTEBOOK PROMPT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldPrimary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = module.notebookPrompt,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = GoldLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Reflection Title", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = reflectionTitle,
                onValueChange = { reflectionTitle = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldLight,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Your Reflection & Subconscious Command", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = reflectionText,
                onValueChange = { reflectionText = it },
                placeholder = { Text("Inscribe your unyielding thoughts, realizations, and strategic pledges here...", color = TextMuted, fontSize = 12.sp) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("notebook_reflection_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldLight,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (reflectionText.isNotBlank()) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSaveReflection(reflectionTitle, reflectionText)
                        isSaved = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSaved) SuccessGreen else AmberAccent,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_reflection_button")
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Filled.Check else Icons.Filled.EditNote,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isSaved) "INSCRIBED IN NOTEBOOK (+75 XP)" else "INSCRIBE IN NOTEBOOK (+75 XP)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        // Proceed to Module Closing Reflection Gate
        Button(
            onClick = onNextTab,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("notebook_proceed_to_reflection_gate_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.SelfImprovement, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "PROCEED TO CLOSING REFLECTION GATE →",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.6.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Module Completion Reflection Tab Content
 * The mandatory final gate requiring 1-3 tailored prompts before
 * marking the module complete and unlocking the next vault.
 */
@Composable
private fun ModuleCompletionReflectionTabContent(
    module: ModuleEntity,
    existingReflections: List<NotebookEntryEntity>,
    onSubmitReflection: (Map<String, String>) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val prompts = remember(module.id) {
        ModuleReflectionPromptsProvider.getPromptsForModule(module.id)
    }

    // Load any existing reflection response if available
    val latestModuleReflection = remember(existingReflections) {
        existingReflections.filter { it.entryType == NotebookEntryEntity.ENTRY_TYPE_MODULE_REFLECTION }
            .maxByOrNull { it.timestamp }
    }

    // State for each prompt answer
    val promptAnswers = remember(module.id, prompts) {
        androidx.compose.runtime.mutableStateMapOf<String, String>().apply {
            prompts.forEach { prompt ->
                this[prompt.id] = ""
            }
        }
    }

    var isSubmitting by remember { mutableStateOf(false) }
    var submittedSuccessfully by remember { mutableStateOf(module.isCompleted) }

    val allPromptsAnswered = prompts.all { prompt ->
        (promptAnswers[prompt.id] ?: "").trim().isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("module_completion_reflection_tab"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- STATUS & GATING BANNER ---
        if (module.isCompleted || submittedSuccessfully) {
            Surface(
                color = SuccessGreen.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(SuccessGreen.copy(alpha = 0.2f), CircleShape)
                            .border(1.5.dp, SuccessGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "VAULT ${module.order} SEALED & CONQUERED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Your closing reflection is permanently inscribed. You may update your insights or re-seal your commitments anytime.",
                            fontSize = 12.sp,
                            color = TextPrimary.copy(alpha = 0.9f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        } else {
            Surface(
                color = AmberBright.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(AmberBright.copy(alpha = 0.18f), CircleShape)
                            .border(1.5.dp, AmberAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = AmberBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "FINAL COMPLETION GATE • CLOSING REFLECTION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberBright,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Answer these reflective prompts to seal this principle into your memory, complete Vault #${module.order}, and unlock the next principle.",
                            fontSize = 12.sp,
                            color = TextPrimary.copy(alpha = 0.9f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // --- REFLECTIVE PROMPTS CARDS ---
        prompts.forEachIndexed { index, prompt ->
            val currentAnswer = promptAnswers[prompt.id] ?: ""

            BrushedCard(
                modifier = Modifier.testTag("reflection_prompt_card_${prompt.id}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GoldPrimary.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "PROMPT ${index + 1} OF ${prompts.size}",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = MutedGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${currentAnswer.length} chars",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Prompt Question
                Text(
                    text = prompt.promptText,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = GoldLight
                )

                if (prompt.helperHint.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prompt.helperHint,
                        fontSize = 11.sp,
                        color = MutedGold.copy(alpha = 0.85f),
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Response Input
                OutlinedTextField(
                    value = currentAnswer,
                    onValueChange = { promptAnswers[prompt.id] = it },
                    placeholder = {
                        Text(
                            text = "Inscribe your truthful reflection here...",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .testTag("reflection_input_${prompt.id}"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldLight,
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = PureBlack.copy(alpha = 0.4f),
                        unfocusedContainerColor = PureBlack.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // --- SUBMISSION ACTION ---
        Button(
            onClick = {
                if (allPromptsAnswered) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    isSubmitting = true
                    onSubmitReflection(promptAnswers.toMap())
                    submittedSuccessfully = true
                    isSubmitting = false
                }
            },
            enabled = allPromptsAnswered && !isSubmitting,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (module.isCompleted || submittedSuccessfully) SuccessGreen else AmberAccent,
                contentColor = RichBlack,
                disabledContainerColor = DarkCharcoal,
                disabledContentColor = TextMuted
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_module_reflection_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (module.isCompleted || submittedSuccessfully) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (module.isCompleted || submittedSuccessfully) {
                        "UPDATE & RE-SEAL REFLECTION (+100 XP)"
                    } else {
                        "SUBMIT CLOSING REFLECTION & SEAL VAULT (+100 XP)"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 0.8.sp
                )
            }
        }

        if (!allPromptsAnswered) {
            Text(
                text = "Please provide your reflection for each prompt above to unlock module completion.",
                fontSize = 11.sp,
                color = MutedGold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- PAST REFLECTIONS HISTORY (ARCHIVES) ---
        val moduleReflectionsHistory = existingReflections.filter {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_MODULE_REFLECTION ||
            (it.moduleId == module.id && it.tags.contains("Module Reflection", ignoreCase = true))
        }.sortedByDescending { it.timestamp }

        if (moduleReflectionsHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = DarkCharcoal.copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "MODULE REFLECTION ARCHIVES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${moduleReflectionsHistory.size} Inscribed",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }

                    moduleReflectionsHistory.forEach { item ->
                        Surface(
                            color = PureBlack.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldLight
                                    )
                                    val dateStr = remember(item.timestamp) {
                                        java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault())
                                            .format(java.util.Date(item.timestamp))
                                    }
                                    Text(
                                        text = dateStr,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }

                                if (item.promptQuestion.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.promptQuestion,
                                        fontSize = 11.sp,
                                        color = MutedGold.copy(alpha = 0.8f),
                                        fontStyle = FontStyle.Italic
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = item.content,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

/**
 * Incomplete Reflection Gating Warning Dialog
 * Shows when the user attempts to navigate away before completing the mandatory reflection.
 */
@Composable
private fun IncompleteReflectionGatingDialog(
    module: ModuleEntity,
    onCompleteReflection: () -> Unit,
    onLeaveAnyway: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Dialog(
        onDismissRequest = onLeaveAnyway,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.5.dp,
                    color = AmberAccent.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(22.dp)
                )
                .testTag("incomplete_reflection_gating_dialog"),
            shape = RoundedCornerShape(22.dp),
            color = NightBlack
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Monk Contemplation Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(AmberBright.copy(alpha = 0.15f), CircleShape)
                        .border(2.dp, AmberAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = "Monk Contemplation",
                        tint = AmberBright,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "CLOSING REFLECTION REQUIRED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberBright,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Complete this reflection to finish ${module.title}",
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Quizzes and lessons alone are not enough. Inscribing your closing reflection is the final gate required to complete this Vault, earn +100 XP, and unlock the next principle.",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = TextPrimary.copy(alpha = 0.85f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Complete Reflection Now Button
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onCompleteReflection()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberAccent,
                        contentColor = RichBlack
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("complete_reflection_now_btn")
                ) {
                    Icon(imageVector = Icons.Default.SelfImprovement, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "COMPLETE REFLECTION NOW",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Leave For Now Button
                OutlinedButton(
                    onClick = onLeaveAnyway,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MutedGold
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("leave_for_now_btn")
                ) {
                    Text(
                        text = "Leave for Now (Vault Remains Incomplete)",
                        fontSize = 11.sp,
                        color = MutedGold
                    )
                }
            }
        }
    }
}

@Composable
fun ShortLessonsTabContent(
    module: ModuleEntity,
    lessons: List<ShortLessonEntity>,
    playerState: ShortLessonPlayerState?,
    onPlayLesson: (ShortLessonEntity) -> Unit,
    onResumeLesson: () -> Unit,
    onPauseLesson: () -> Unit,
    onSeekLesson: (Int) -> Unit,
    onSeekLessonRelative: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onToggleVideoMode: () -> Unit,
    onToggleTranscript: () -> Unit,
    onToggleChapters: () -> Unit,
    onToggleAmbient: () -> Unit,
    onSaveToNotebook: (ShortLessonEntity) -> Unit,
    onToggleCompletion: (ShortLessonEntity) -> Unit,
    onMarkCompleted: (String) -> Unit,
    onNextTab: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("short_lessons_tab_content")
    ) {
        // Module Lessons Completion Header Badge
        ModuleLessonsHeaderBadge(lessons = lessons)

        Spacer(modifier = Modifier.height(14.dp))

        // If a lesson is active in player, show the Inline Player Card
        if (playerState?.activeLesson != null && playerState.activeLesson.moduleId == module.id) {
            ShortLessonInlinePlayerCard(
                playerState = playerState,
                onPlay = onResumeLesson,
                onPause = onPauseLesson,
                onSeek = onSeekLesson,
                onSeekRelative = onSeekLessonRelative,
                onSetSpeed = onSetSpeed,
                onToggleVideoMode = onToggleVideoMode,
                onToggleTranscript = onToggleTranscript,
                onToggleChapters = onToggleChapters,
                onToggleAmbient = onToggleAmbient,
                onSaveToNotebook = onSaveToNotebook,
                onMarkCompleted = onMarkCompleted
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section Title: Available Masterclass Lessons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "LESSONS & CLIPS (5-10 MIN)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 1.sp
            )
            Text(
                text = "${lessons.size} Available",
                fontSize = 11.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // List of Short Lesson Cards
        lessons.forEach { lesson ->
            val isActive = playerState?.activeLesson?.id == lesson.id
            val isPlaying = isActive && (playerState?.isPlaying == true)

            ShortLessonItemCard(
                lesson = lesson,
                isActive = isActive,
                isPlaying = isPlaying,
                onPlayClick = { onPlayLesson(lesson) },
                onToggleCompleted = { onToggleCompletion(lesson) }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next Tab: Continue to Voice & Manuscript button
        Button(
            onClick = onNextTab,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("lessons_continue_to_lecture_btn"),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "CONTINUE TO VAULT LECTURE →",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
