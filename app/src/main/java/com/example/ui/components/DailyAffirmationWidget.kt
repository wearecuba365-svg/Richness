package com.example.ui.components

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.ThinkAndGrowRichAffirmation
import com.example.data.model.ThinkAndGrowRichQuotes
import com.example.data.model.UserProfileEntity
import com.example.data.repository.RichesRepository
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.VoiceMemoState
import com.example.util.VoiceMemoUiState
import kotlinx.coroutines.launch

/**
 * Daily Affirmation Widget matching the Dark/Gold Luxury Sovereign aesthetic.
 * Integrates:
 * 1. User's "Definite Chief Aim" personal goal statement with in-app reminder & editing.
 * 2. Voice memo recording & playback of the spoken aim (with TTS fallback).
 * 3. Daily ritual streak tracking (Affirmation Streak) + XP rewards.
 * 4. Gentle non-punitive "Streak broken — start fresh today" messaging on missed days.
 * 5. Compact expandable banner / card layout with Think and Grow Rich principles.
 */
@Composable
fun DailyAffirmationWidget(
    modifier: Modifier = Modifier,
    userProfile: UserProfileEntity? = null,
    epochDay: Long = RichesRepository.getTodayEpochDay(),
    voiceMemoUiState: VoiceMemoUiState = VoiceMemoUiState(),
    onStartVoiceRecording: () -> Boolean = { false },
    onStopVoiceRecording: () -> Unit = {},
    onPlayVoiceRecording: () -> Unit = {},
    onStopVoiceRecordingPlayback: () -> Unit = {},
    onDeleteVoiceRecording: () -> Unit = {},
    onOpenEditAim: () -> Unit = {},
    onCompleteAimAffirmation: () -> Unit = {},
    onAffirmToday: ((ThinkAndGrowRichAffirmation) -> Unit)? = null,
    onSaveToNotebook: ((ThinkAndGrowRichAffirmation) -> Unit)? = null,
    onSpeakAffirmation: ((String) -> Unit)? = null
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val todayEpoch = remember { RichesRepository.getTodayEpochDay() }
    val lastAffirmationDay = userProfile?.lastAffirmationEpochDay ?: 0L
    val isCompletedToday = lastAffirmationDay == todayEpoch
    val streakCount = userProfile?.affirmationStreak ?: 0
    val chiefAim = userProfile?.definiteChiefAim?.trim().orEmpty()
    val hasChiefAim = chiefAim.isNotBlank()

    // Gentle streak broken condition: had an active streak, but missed yesterday and hasn't affirmed today yet
    val isStreakBroken = lastAffirmationDay in 1 until (todayEpoch - 1) && !isCompletedToday

    // Widget Expansion State (Compact vs Full Ritual)
    var isExpanded by remember { mutableStateOf(true) }
    var activeTab by remember { mutableStateOf(0) } // 0 = Definite Chief Aim, 1 = Classical Principle Quote

    // Morning deterministic quote for current epochDay
    val dailyMorningQuote = remember(epochDay) {
        ThinkAndGrowRichQuotes.getDailyQuote(epochDay)
    }

    var currentAffirmation by remember(dailyMorningQuote.id) {
        mutableStateOf(dailyMorningQuote)
    }

    var isQuoteAffirmedForToday by remember(currentAffirmation.id, epochDay) {
        mutableStateOf(false)
    }

    var isPracticeExpanded by remember { mutableStateOf(false) }

    // Rotation animation for quote shuffle icon
    val rotationAnim = remember { Animatable(0f) }

    // Pulsing aura animation for the gold card border
    val infiniteTransition = rememberInfiniteTransition(label = "gold_aura")
    val borderGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_glow"
    )

    val micPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mic_pulse"
    )

    val goldBorderBrush = Brush.linearGradient(
        colors = listOf(
            GoldLight.copy(alpha = borderGlowAlpha),
            GoldPrimary,
            AmberBright.copy(alpha = borderGlowAlpha),
            GoldDark,
            GoldLight.copy(alpha = borderGlowAlpha)
        )
    )

    // Microphone Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onStartVoiceRecording()
        } else {
            Toast.makeText(context, "Microphone permission is required to record voice memo", Toast.LENGTH_SHORT).show()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, goldBorderBrush, RoundedCornerShape(22.dp))
            .testTag("daily_affirmation_widget"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RichBlack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SurfaceElevated.copy(alpha = 0.95f),
                            DarkCharcoal.copy(alpha = 0.98f),
                            RichBlack
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // --- HEADER: BADGE, STREAK & COMPACT TOGGLE ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(listOf(AmberBright, GoldDark))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FormatQuote,
                                contentDescription = "Daily Affirmation",
                                tint = RichBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "DAILY AFFIRMATION RITUAL",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight,
                                    letterSpacing = 1.1.sp
                                )
                            }
                            Text(
                                text = if (isCompletedToday) "Ritual Sealed for Today ✨" else "Daily Transmutation Decree",
                                fontSize = 10.sp,
                                color = if (isCompletedToday) SuccessGreen else TextMuted
                            )
                        }
                    }

                    // Streak Pill Badge & Expand/Collapse Toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Affirmation Streak Counter Badge
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isCompletedToday) GoldDark.copy(alpha = 0.4f) else DarkCharcoal,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isCompletedToday) GoldPrimary else DarkBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .testTag("affirmation_streak_badge")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = if (streakCount > 0) AmberBright else TextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "$streakCount d",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (streakCount > 0) GoldLight else TextMuted
                                )
                            }
                        }

                        // Compact/Expand Toggle Button
                        IconButton(
                            onClick = { isExpanded = !isExpanded },
                            modifier = Modifier.size(30.dp).testTag("toggle_affirmation_expand_button")
                        ) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = GoldLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Gentle "Streak Broken" Encouraging Banner (Only shown if missed day)
                if (isStreakBroken) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(AmberAccent.copy(alpha = 0.15f))
                            .border(1.dp, AmberAccent.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = AmberBright,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Streak broken — start fresh today. Definite purpose knows no permanent defeat.",
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                color = GoldLight
                            )
                        }
                    }
                }

                // --- TAB SELECTOR (Definite Chief Aim / Classical Principle) ---
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkCharcoal.copy(alpha = 0.8f))
                            .padding(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1.1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (activeTab == 0) GoldDark.copy(alpha = 0.6f) else Color.Transparent)
                                .clickable { activeTab = 0 }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Diamond,
                                    contentDescription = null,
                                    tint = if (activeTab == 0) GoldLight else TextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Definite Chief Aim",
                                    fontSize = 11.sp,
                                    fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (activeTab == 0) GoldLight else TextMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (activeTab == 1) GoldDark.copy(alpha = 0.6f) else Color.Transparent)
                                .clickable { activeTab = 1 }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Lightbulb,
                                    contentDescription = null,
                                    tint = if (activeTab == 1) GoldLight else TextMuted,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Principle Quote",
                                    fontSize = 11.sp,
                                    fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (activeTab == 1) GoldLight else TextMuted
                                )
                            }
                        }
                    }
                }

                // --- COMPACT VIEW SUMMARY (when collapsed) ---
                if (!isExpanded) {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (hasChiefAim) {
                        Text(
                            text = "\"$chiefAim\"",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            maxLines = 2,
                            color = TextSecondary,
                            modifier = Modifier.clickable { isExpanded = true }
                        )
                    } else {
                        Text(
                            text = "Tap to inscribe your personal Definite Chief Aim...",
                            fontSize = 12.sp,
                            color = AmberBright,
                            modifier = Modifier.clickable { isExpanded = true }
                        )
                    }
                }

                // --- EXPANDED TAB CONTENT ---
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        if (activeTab == 0) {
                            // ==========================================
                            // TAB 0: DEFINITE CHIEF AIM STATEMENT RITUAL
                            // ==========================================
                            Spacer(modifier = Modifier.height(14.dp))

                            if (hasChiefAim) {
                                // User has written their Chief Aim
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DarkCharcoal.copy(alpha = 0.5f))
                                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                                        .padding(14.dp)
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "“",
                                                    fontSize = 32.sp,
                                                    fontFamily = FontFamily.Serif,
                                                    fontWeight = FontWeight.Bold,
                                                    color = GoldPrimary
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "MY TRANSMUTATION STATEMENT",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AmberBright,
                                                    letterSpacing = 1.sp
                                                )
                                            }

                                            // Edit Button
                                            IconButton(
                                                onClick = onOpenEditAim,
                                                modifier = Modifier.size(28.dp).testTag("edit_chief_aim_button")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Edit,
                                                    contentDescription = "Edit Statement",
                                                    tint = GoldLight,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = chiefAim,
                                            fontFamily = FontFamily.Serif,
                                            fontSize = 14.sp,
                                            lineHeight = 22.sp,
                                            color = TextPrimary,
                                            fontStyle = FontStyle.Normal,
                                            modifier = Modifier.testTag("chief_aim_display_text")
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Read aloud twice daily (morning & night)",
                                                fontSize = 10.sp,
                                                color = TextMuted,
                                                fontStyle = FontStyle.Italic
                                            )

                                            // Copy Aim Button
                                            IconButton(
                                                onClick = {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                    val clip = ClipData.newPlainText("Definite Chief Aim", chiefAim)
                                                    clipboard.setPrimaryClip(clip)
                                                    Toast.makeText(context, "Chief Aim copied ✨", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(26.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.ContentCopy,
                                                    contentDescription = "Copy Statement",
                                                    tint = TextMuted,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // No Aim configured yet -> Invitation Card
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(DarkCharcoal.copy(alpha = 0.6f))
                                        .border(1.dp, AmberBright.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Diamond,
                                            contentDescription = null,
                                            tint = AmberBright,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Formulate Your Definite Chief Aim",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldLight,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Establish your major life goal statement in writing to begin the daily morning transmutation ritual.",
                                            fontSize = 11.sp,
                                            lineHeight = 16.sp,
                                            color = TextSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = onOpenEditAim,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = GoldPrimary,
                                                contentColor = RichBlack
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("inscribe_chief_aim_prompt_button")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.EditNote,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Inscribe Chief Aim",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // --- VOICE MEMO RECORD & PLAYBACK SECTION ---
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkCharcoal.copy(alpha = 0.7f))
                                    .border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                                    .padding(12.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Filled.Mic,
                                                contentDescription = null,
                                                tint = if (voiceMemoUiState.state == VoiceMemoState.RECORDING) AmberBright else GoldLight,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Spoken Voice Affirmation",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        }

                                        // TTS Speak Aloud Button
                                        if (onSpeakAffirmation != null && hasChiefAim) {
                                            Text(
                                                text = "Listen via TTS 🎧",
                                                fontSize = 10.sp,
                                                color = GoldLight,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier
                                                    .clickable {
                                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                        onSpeakAffirmation("My Definite Chief Aim: $chiefAim")
                                                    }
                                                    .testTag("tts_chief_aim_button")
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Voice Recording Controls
                                    when (voiceMemoUiState.state) {
                                        VoiceMemoState.RECORDING -> {
                                            // Actively Recording
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                            .clip(CircleShape)
                                                            .background(Color.Red.copy(alpha = micPulseAlpha))
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    val sec = voiceMemoUiState.durationSeconds
                                                    Text(
                                                        text = "Recording: ${sec / 60}:${(sec % 60).toString().padStart(2, '0')}",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.Red
                                                    )
                                                }

                                                Button(
                                                    onClick = onStopVoiceRecording,
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Red,
                                                        contentColor = Color.White
                                                    ),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                    modifier = Modifier.testTag("stop_recording_button")
                                                ) {
                                                    Icon(Icons.Filled.Stop, contentDescription = "Stop", modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Save Memo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }

                                        VoiceMemoState.PLAYING -> {
                                            // Actively Playing Back Voice Memo
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Filled.VolumeUp, contentDescription = null, tint = AmberBright, modifier = Modifier.size(14.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("Playing Voice Memo...", fontSize = 11.sp, color = AmberBright)
                                                    }

                                                    OutlinedButton(
                                                        onClick = onStopVoiceRecordingPlayback,
                                                        shape = RoundedCornerShape(8.dp),
                                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Icon(Icons.Filled.Pause, contentDescription = "Pause", modifier = Modifier.size(13.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("Stop", fontSize = 10.sp)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                LinearProgressIndicator(
                                                    progress = {
                                                        if (voiceMemoUiState.durationSeconds > 0)
                                                            voiceMemoUiState.currentPositionSeconds.toFloat() / voiceMemoUiState.durationSeconds.toFloat()
                                                        else 0f
                                                    },
                                                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                                    color = GoldPrimary,
                                                    trackColor = RichBlack
                                                )
                                            }
                                        }

                                        else -> {
                                            // Idle state: Can either record or play back existing memo
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (voiceMemoUiState.hasRecording) {
                                                    // Has existing voice memo
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        Button(
                                                            onClick = onPlayVoiceRecording,
                                                            colors = ButtonDefaults.buttonColors(
                                                                containerColor = GoldDark,
                                                                contentColor = GoldLight
                                                            ),
                                                            shape = RoundedCornerShape(8.dp),
                                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                            modifier = Modifier.testTag("play_memo_button")
                                                        ) {
                                                            Icon(Icons.Filled.PlayArrow, contentDescription = null, modifier = Modifier.size(14.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text("Play Memo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                                        }

                                                        IconButton(
                                                            onClick = onDeleteVoiceRecording,
                                                            modifier = Modifier.size(28.dp).testTag("delete_memo_button")
                                                        ) {
                                                            Icon(Icons.Filled.Delete, contentDescription = "Delete Memo", tint = TextMuted, modifier = Modifier.size(14.dp))
                                                        }
                                                    }
                                                } else {
                                                    Text(
                                                        text = "Record yourself reading your Aim aloud",
                                                        fontSize = 11.sp,
                                                        color = TextMuted
                                                    )
                                                }

                                                // Record Button
                                                OutlinedButton(
                                                    onClick = {
                                                        val hasPermission = ContextCompat.checkSelfPermission(
                                                            context,
                                                            Manifest.permission.RECORD_AUDIO
                                                        ) == PackageManager.PERMISSION_GRANTED

                                                        if (hasPermission) {
                                                            onStartVoiceRecording()
                                                        } else {
                                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                                        }
                                                    },
                                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                    modifier = Modifier.testTag("record_voice_memo_button")
                                                ) {
                                                    Icon(Icons.Filled.Mic, contentDescription = null, modifier = Modifier.size(13.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (voiceMemoUiState.hasRecording) "Re-record" else "Record Voice",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // --- "DONE FOR TODAY" ACTION BUTTON ---
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onCompleteAimAffirmation()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCompletedToday) GoldDark else GoldPrimary,
                                    contentColor = if (isCompletedToday) GoldLight else RichBlack
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("done_for_today_button")
                            ) {
                                Icon(
                                    imageVector = if (isCompletedToday) Icons.Filled.CheckCircle else Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (isCompletedToday) GoldLight else RichBlack,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isCompletedToday) "Done for today ✨ (+50 XP Claimed)" else "Done for today (+50 XP)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompletedToday) GoldLight else RichBlack
                                )
                            }

                        } else {
                            // ==========================================
                            // TAB 1: THINK & GROW RICH PHILOSOPHY QUOTE
                            // ==========================================
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = GoldDark.copy(alpha = 0.35f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                        .testTag("affirmation_principle_badge")
                                ) {
                                    Text(
                                        text = currentAffirmation.principleName.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldLight,
                                        letterSpacing = 0.8.sp
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    // Speak Button
                                    if (onSpeakAffirmation != null) {
                                        IconButton(
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                val textToSpeak = "${currentAffirmation.principleName}. ${currentAffirmation.quote}. Napoleon Hill."
                                                onSpeakAffirmation(textToSpeak)
                                            },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.VolumeUp,
                                                contentDescription = "Listen",
                                                tint = GoldLight.copy(alpha = 0.8f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    // Shuffle Button
                                    IconButton(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            coroutineScope.launch {
                                                rotationAnim.animateTo(
                                                    targetValue = rotationAnim.value + 360f,
                                                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                                                )
                                            }
                                            currentAffirmation = ThinkAndGrowRichQuotes.getRandomQuote(currentAffirmation.id)
                                            isQuoteAffirmedForToday = false
                                        },
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Refresh,
                                            contentDescription = "Shuffle",
                                            tint = AmberBright,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .rotate(rotationAnim.value)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Animated Quote Text
                            AnimatedContent(
                                targetState = currentAffirmation,
                                transitionSpec = {
                                    (fadeIn(animationSpec = tween(350)) + expandVertically()) togetherWith
                                            (fadeOut(animationSpec = tween(200)) + shrinkVertically())
                                },
                                label = "quote_transition"
                            ) { affirmation ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = "“",
                                            fontSize = 34.sp,
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldPrimary.copy(alpha = 0.6f),
                                            modifier = Modifier.padding(end = 4.dp)
                                        )

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = affirmation.quote,
                                                fontFamily = FontFamily.Serif,
                                                fontSize = 14.sp,
                                                lineHeight = 22.sp,
                                                color = TextPrimary,
                                                fontStyle = FontStyle.Italic
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = "— ${affirmation.author}, Think and Grow Rich",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = GoldLight,
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Morning Transmutation Practice
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkCharcoal.copy(alpha = 0.7f))
                                    .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                                    .clickable { isPracticeExpanded = !isPracticeExpanded }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = AmberBright, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Morning Practice", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                        }
                                        Text(text = if (isPracticeExpanded) "Close ▲" else "Expand ▼", fontSize = 10.sp, color = GoldPrimary)
                                    }

                                    AnimatedVisibility(
                                        visible = isPracticeExpanded,
                                        enter = expandVertically() + fadeIn(),
                                        exit = shrinkVertically() + fadeOut()
                                    ) {
                                        Column(modifier = Modifier.padding(top = 6.dp)) {
                                            Text(text = currentAffirmation.actionPractice, fontSize = 11.sp, lineHeight = 16.sp, color = TextSecondary)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Action Buttons (Inscribe to Notebook & Affirm Quote)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onSaveToNotebook?.invoke(currentAffirmation)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                    modifier = Modifier.weight(1f).height(42.dp)
                                ) {
                                    Icon(Icons.Filled.EditNote, contentDescription = null, tint = GoldLight, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Inscribe Note", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }

                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isQuoteAffirmedForToday = true
                                        onAffirmToday?.invoke(currentAffirmation)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isQuoteAffirmedForToday) GoldDark else GoldPrimary,
                                        contentColor = RichBlack
                                    ),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                                    modifier = Modifier.weight(1.3f).height(42.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isQuoteAffirmedForToday) Icons.Filled.CheckCircle else Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = if (isQuoteAffirmedForToday) GoldLight else RichBlack,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isQuoteAffirmedForToday) "Affirmed (+30 XP)" else "Affirm Principle (+30 XP)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isQuoteAffirmedForToday) GoldLight else RichBlack
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
