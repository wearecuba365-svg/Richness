package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioScriptType
import com.example.data.model.ModuleAudioScript
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.TtsPlaybackStatus
import com.example.util.TtsPlayerState

@Composable
fun ModuleAudioPlayerCard(
    playerState: TtsPlayerState,
    availableScripts: List<ModuleAudioScript>,
    onSelectScript: (ModuleAudioScript) -> Unit,
    onPlay: (ModuleAudioScript) -> Unit,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onNextSentence: () -> Unit,
    onPreviousSentence: () -> Unit,
    onSeekSentence: (Int) -> Unit,
    onSetRate: (Float) -> Unit,
    onSetPitch: (Float) -> Unit,
    onToggleAmbient: () -> Unit,
    onSaveToNotebook: (ModuleAudioScript) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeScript = playerState.currentScript ?: availableScripts.firstOrNull()
    val isPlaying = playerState.status == TtsPlaybackStatus.PLAYING
    val isPaused = playerState.status == TtsPlaybackStatus.PAUSED
    val isIdle = playerState.status == TtsPlaybackStatus.IDLE || playerState.status == TtsPlaybackStatus.COMPLETED

    var showControlsDrawer by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, GoldPrimary.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .testTag("module_audio_player_card"),
        colors = CardDefaults.cardColors(containerColor = RichBlack),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Top Bar: Audio Type Badge & Script Switcher Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .border(1.dp, GoldLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Headphones,
                            contentDescription = "Voice Reader",
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "VOICE TRANSMUTATION ENGINE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Napoleon Hill Voice Synthesizer",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }

                // Ambient Tone Toggle Button
                Surface(
                    color = if (playerState.isAmbientSoundEnabled) AmberAccent.copy(alpha = 0.2f) else DarkBorder.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (playerState.isAmbientSoundEnabled) AmberAccent else DarkBorder
                    ),
                    modifier = Modifier.clickable { onToggleAmbient() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GraphicEq,
                            contentDescription = "Ambient Synth",
                            tint = if (playerState.isAmbientSoundEnabled) AmberAccent else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (playerState.isAmbientSoundEnabled) "THETA 6Hz ON" else "AMBIENT OFF",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (playerState.isAmbientSoundEnabled) AmberAccent else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Script Selection Tabs / Chips
            if (availableScripts.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availableScripts.forEach { script ->
                        val isSelected = activeScript?.id == script.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectScript(script) },
                            label = {
                                Text(
                                    text = script.type.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldPrimary.copy(alpha = 0.25f),
                                selectedLabelColor = GoldLight,
                                containerColor = SurfaceElevated,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = DarkBorder,
                                selectedBorderColor = GoldLight,
                                borderWidth = 1.dp
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("script_chip_${script.id}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Script Active Title & Description
            if (activeScript != null) {
                Text(
                    text = activeScript.title,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activeScript.description,
                    fontSize = 11.sp,
                    color = TextMuted,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Waveform Visualizer & Sentence Teleprompter Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceElevated)
                    .border(1.dp, if (isPlaying) GoldLight.copy(alpha = 0.6f) else DarkBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated Waveform Bars
                    WaveformEqualizer(isPlaying = isPlaying)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sentence Teleprompter Highlight View
                    val spokenText = if (isPlaying || isPaused) {
                        playerState.currentSentenceText.ifBlank { activeScript?.textToSpeak ?: "" }
                    } else {
                        activeScript?.textToSpeak ?: "Select an affirmation or meditation script to listen."
                    }

                    Text(
                        text = "\"$spokenText\"",
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = if (isPlaying) FontWeight.SemiBold else FontWeight.Normal,
                        fontFamily = FontFamily.Serif,
                        color = if (isPlaying) GoldLight else TextPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sentence Progress Indicator
                    val currentIdx = if (isPlaying || isPaused) playerState.currentSentenceIndex + 1 else 0
                    val totalSentences = activeScript?.sentences?.size ?: 1
                    Text(
                        text = if (isPlaying || isPaused) "Phase $currentIdx of $totalSentences • ${activeScript?.backgroundTheme}" else "Ready for Audio Recitation • Est. ${activeScript?.estimatedDurationSeconds ?: 45}s",
                        fontSize = 10.sp,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Slider
            val sentenceCount = (activeScript?.sentences?.size ?: 1).coerceAtLeast(1)
            val currentProgress = if (sentenceCount > 1) {
                (playerState.currentSentenceIndex.toFloat() / (sentenceCount - 1).toFloat()).coerceIn(0f, 1f)
            } else 0f

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${playerState.currentSentenceIndex + 1}/$sentenceCount",
                    fontSize = 10.sp,
                    color = TextMuted,
                    modifier = Modifier.width(36.dp)
                )

                Slider(
                    value = currentProgress,
                    onValueChange = { frac ->
                        val targetIdx = (frac * (sentenceCount - 1)).toInt().coerceIn(0, sentenceCount - 1)
                        onSeekSentence(targetIdx)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = GoldLight,
                        activeTrackColor = GoldPrimary,
                        inactiveTrackColor = DarkBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_player_slider")
                )

                Text(
                    text = "${activeScript?.estimatedDurationSeconds ?: 45}s",
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.End,
                    modifier = Modifier.width(36.dp)
                )
            }

            // --- PRIMARY AUDIO CONTROLS ROW ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind / Previous Sentence
                IconButton(
                    onClick = onPreviousSentence,
                    enabled = isPlaying || isPaused,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Previous Sentence",
                        tint = if (isPlaying || isPaused) GoldLight else TextMuted
                    )
                }

                // Main Play / Pause Button
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(GoldLinearGradient)
                        .clickable {
                            if (activeScript != null) {
                                when {
                                    isPlaying -> onPause()
                                    isPaused -> onResume()
                                    else -> onPlay(activeScript)
                                }
                            }
                        }
                        .testTag("audio_player_main_play_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = RichBlack,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Next Sentence
                IconButton(
                    onClick = onNextSentence,
                    enabled = isPlaying || isPaused,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next Sentence",
                        tint = if (isPlaying || isPaused) GoldLight else TextMuted
                    )
                }

                // Stop Button
                IconButton(
                    onClick = onStop,
                    enabled = isPlaying || isPaused,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Stop,
                        contentDescription = "Stop",
                        tint = if (isPlaying || isPaused) AmberAccent else TextMuted
                    )
                }

                // Audio Settings / Tuning Toggle
                IconButton(
                    onClick = { showControlsDrawer = !showControlsDrawer },
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Tuning",
                        tint = if (showControlsDrawer) GoldLight else TextSecondary
                    )
                }
            }

            // Quick Actions & Tuning Drawer
            AnimatedVisibility(
                visible = showControlsDrawer,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkCharcoal)
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "VOICE SPEED & PITCH CONTROLS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Speed Selector Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Speed:", fontSize = 11.sp, color = TextSecondary)
                        listOf(0.75f to "0.75x", 1.0f to "1.0x", 1.25f to "1.25x", 1.5f to "1.5x").forEach { (rate, label) ->
                            val isSelected = (playerState.speechRate - rate).let { it > -0.05f && it < 0.05f }
                            Surface(
                                color = if (isSelected) GoldPrimary else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { onSetRate(rate) }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) RichBlack else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Pitch Selector Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Tone:", fontSize = 11.sp, color = TextSecondary)
                        listOf(0.85f to "Deep", 1.0f to "Sovereign", 1.2f to "Bright").forEach { (pitch, label) ->
                            val isSelected = (playerState.speechPitch - pitch).let { it > -0.05f && it < 0.05f }
                            Surface(
                                color = if (isSelected) GoldPrimary else SurfaceElevated,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { onSetPitch(pitch) }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) RichBlack else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Save to Sovereign Notebook Button
                    if (activeScript != null) {
                        Button(
                            onClick = { onSaveToNotebook(activeScript) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceElevated,
                                contentColor = GoldLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.BookmarkAdd,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Save Affirmation to Notebook",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WaveformEqualizer(isPlaying: Boolean, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform_anim")

    val h1 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = if (isPlaying) 28f else 6f,
        animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = if (isPlaying) 36f else 10f,
        animationSpec = infiniteRepeatable(tween(300, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = if (isPlaying) 22f else 4f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = if (isPlaying) 32f else 8f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h4"
    )
    val h5 by infiniteTransition.animateFloat(
        initialValue = 5f,
        targetValue = if (isPlaying) 26f else 5f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h5"
    )

    Row(
        modifier = modifier.height(36.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(h1, h3, h2, h5, h4, h2, h3, h1, h4, h2, h5, h3, h1).forEach { h ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(h.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isPlaying) {
                            Brush.verticalGradient(listOf(GoldLight, GoldDark))
                        } else {
                            Brush.verticalGradient(listOf(DarkBorder, DarkBorder))
                        }
                    )
            )
        }
    }
}

@Composable
fun FloatingMiniAudioPlayerBar(
    playerState: TtsPlayerState,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val script = playerState.currentScript ?: return
    val isPlaying = playerState.status == TtsPlaybackStatus.PLAYING
    val isVisible = playerState.status == TtsPlaybackStatus.PLAYING || playerState.status == TtsPlaybackStatus.PAUSED

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { onOpenDetail(script.moduleId) }
                .testTag("floating_mini_audio_player_bar"),
            color = DarkCharcoal,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini Waveform
                WaveformEqualizer(isPlaying = isPlaying, modifier = Modifier.size(width = 30.dp, height = 24.dp))

                Spacer(modifier = Modifier.width(10.dp))

                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = script.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (isPlaying) "Playing • ${script.principleName}" else "Paused • Tap to resume",
                        fontSize = 10.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                // Play / Pause Button
                IconButton(
                    onClick = { if (isPlaying) onPause() else onResume() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = GoldLight
                    )
                }

                // Close / Stop Button
                IconButton(
                    onClick = onStop,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Stop",
                        tint = TextMuted
                    )
                }
            }
        }
    }
}
