package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShortLessonEntity
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.ShortLessonPlayerState

/**
 * Header badge summarizing short lesson completion progress for a given module.
 */
@Composable
fun ModuleLessonsHeaderBadge(
    lessons: List<ShortLessonEntity>,
    modifier: Modifier = Modifier
) {
    if (lessons.isEmpty()) return

    val completedCount = lessons.count { it.isCompleted }
    val totalCount = lessons.size
    val allCompleted = completedCount == totalCount && totalCount > 0
    val totalXp = lessons.sumOf { it.xpReward }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("module_lessons_header_badge"),
        shape = RoundedCornerShape(12.dp),
        color = if (allCompleted) GoldPrimary.copy(alpha = 0.15f) else DarkCharcoal.copy(alpha = 0.9f),
        border = BorderStroke(
            1.dp,
            if (allCompleted) GoldPrimary else DarkBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (allCompleted) GoldPrimary else GoldDark.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (allCompleted) Icons.Filled.CheckCircle else Icons.Filled.Headphones,
                        contentDescription = null,
                        tint = if (allCompleted) RichBlack else GoldLight,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = if (allCompleted) "ALL LESSONS MASTERED" else "SHORT LESSONS PROGRESS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = if (allCompleted) GoldLight else TextMuted
                    )
                    Text(
                        text = "$completedCount of $totalCount Lessons Watched",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
            }

            // XP badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = GoldPrimary.copy(alpha = 0.2f),
                border = BorderStroke(0.8.dp, GoldPrimary.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "+$totalXp XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Individual Short Lesson list item card.
 */
@Composable
fun ShortLessonItemCard(
    lesson: ShortLessonEntity,
    isActive: Boolean,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressFraction = if (lesson.durationSeconds > 0) {
        (lesson.lastPlaybackPositionSeconds.toFloat() / lesson.durationSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(if (isActive) 6.dp else 2.dp, RoundedCornerShape(14.dp))
            .clickable { onPlayClick() }
            .testTag("lesson_card_${lesson.id}"),
        shape = RoundedCornerShape(14.dp),
        color = if (isActive) SurfaceElevated else DarkCard,
        border = BorderStroke(
            if (isActive) 1.5.dp else 0.8.dp,
            if (isActive) GoldPrimary else if (lesson.isCompleted) GoldPrimary.copy(alpha = 0.4f) else DarkBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Media Type Icon Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isActive && isPlaying) {
                                Brush.linearGradient(listOf(GoldPrimary, GoldDark))
                            } else if (lesson.isCompleted) {
                                Brush.linearGradient(listOf(GoldDark.copy(alpha = 0.6f), DarkCharcoal))
                            } else {
                                Brush.linearGradient(listOf(SurfaceElevated, DarkCharcoal))
                            }
                        )
                        .border(
                            1.dp,
                            if (isActive) GoldLight else DarkBorder,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isActive && isPlaying) {
                        ShortLessonMiniEqualizer(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(
                            imageVector = if (lesson.isVideo) Icons.Filled.Videocam else Icons.Filled.Headphones,
                            contentDescription = if (lesson.isVideo) "Video" else "Audio",
                            tint = if (isActive) RichBlack else if (lesson.isCompleted) GoldLight else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and Metadata
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Type pill
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (lesson.isVideo) Color(0xFF3B2D54) else Color(0xFF1E3A34),
                            border = BorderStroke(0.5.dp, if (lesson.isVideo) Color(0xFF9D7BFF) else Color(0xFF4EBA97))
                        ) {
                            Text(
                                text = if (lesson.isVideo) "VIDEO" else "AUDIO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (lesson.isVideo) Color(0xFFD4BFFF) else Color(0xFFA5F0D9),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = lesson.durationText,
                            fontSize = 11.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "• +${lesson.xpReward} XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GoldLight
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = lesson.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) GoldLight else TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = lesson.subtitle,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Play / Watched Action Button
                if (lesson.isCompleted) {
                    IconButton(
                        onClick = onToggleCompleted,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("lesson_completed_check_${lesson.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Completed",
                            tint = GoldPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = onPlayClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .testTag("lesson_play_btn_${lesson.id}")
                    ) {
                        Icon(
                            imageVector = if (isActive && isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Play Lesson",
                            tint = GoldLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // In-progress playback indicator (if partially listened/watched and not completed)
            if (!lesson.isCompleted && progressFraction > 0.05f) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = GoldPrimary,
                        trackColor = DarkBorder,
                    )
                    Text(
                        text = "${(progressFraction * 100).toInt()}% watched",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

/**
 * Inline Luxury Audio & Video Player Card with interactive controls, scrubber, and expandable tabs.
 */
@Composable
fun ShortLessonInlinePlayerCard(
    playerState: ShortLessonPlayerState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeek: (Int) -> Unit,
    onSeekRelative: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onToggleVideoMode: () -> Unit,
    onToggleTranscript: () -> Unit,
    onToggleChapters: () -> Unit,
    onToggleAmbient: () -> Unit,
    onSaveToNotebook: (ShortLessonEntity) -> Unit,
    onMarkCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lesson = playerState.activeLesson ?: return
    val isPlaying = playerState.isPlaying
    var speedMenuExpanded by remember { mutableStateOf(false) }

    val currentSeconds = playerState.currentPositionSeconds
    val totalSeconds = playerState.durationSeconds.coerceAtLeast(1)
    val sliderValue = (currentSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(18.dp))
            .testTag("short_lesson_inline_player_card"),
        shape = RoundedCornerShape(18.dp),
        color = DarkCharcoal,
        border = BorderStroke(1.2.dp, GoldPrimary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- TOP BAR: Header, Mode Switcher & Speed ---
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
                            .background(GoldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (playerState.isVideoMode) Icons.Filled.Videocam else Icons.Filled.Headphones,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (playerState.isVideoMode) "CINEMATIC VIDEO MASTERCLASS" else "AUDIO TRANSMUTATION LECTURE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = GoldLight
                        )
                        Text(
                            text = lesson.instructorName,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Video / Audio mode toggle button
                    IconButton(
                        onClick = onToggleVideoMode,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("toggle_video_audio_mode_btn")
                    ) {
                        Icon(
                            imageVector = if (playerState.isVideoMode) Icons.Filled.Headphones else Icons.Filled.Videocam,
                            contentDescription = "Switch Mode",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Ambient Theta Synth toggle
                    IconButton(
                        onClick = onToggleAmbient,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("toggle_ambient_theta_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Radio,
                            contentDescription = "Ambient Theta",
                            tint = if (playerState.isAmbientThetaEnabled) GoldLight else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Playback Speed Selector
                    Box {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SurfaceElevated,
                            border = BorderStroke(0.6.dp, DarkBorder),
                            modifier = Modifier.clickable { speedMenuExpanded = true }
                        ) {
                            Text(
                                text = "${playerState.playbackSpeed}x",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = speedMenuExpanded,
                            onDismissRequest = { speedMenuExpanded = false }
                        ) {
                            listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                                DropdownMenuItem(
                                    text = { Text("${speed}x") },
                                    onClick = {
                                        onSetSpeed(speed)
                                        speedMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- CENTER MEDIA STAGE (Cinematic Video or Audio Waveform) ---
            if (playerState.isVideoMode) {
                ShortLessonVideoStage(
                    lesson = lesson,
                    isPlaying = isPlaying,
                    currentPositionSeconds = currentSeconds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                )
            } else {
                ShortLessonAudioStage(
                    lesson = lesson,
                    isPlaying = isPlaying,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title & Subtitle
            Text(
                text = lesson.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = lesson.subtitle,
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- SCRUBBER / SEEK BAR ---
            Slider(
                value = sliderValue,
                onValueChange = { frac ->
                    val targetSec = (frac * totalSeconds).toInt()
                    onSeek(targetSec)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .testTag("short_lesson_scrubber_slider"),
                colors = SliderDefaults.colors(
                    thumbColor = GoldPrimary,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = DarkBorder
                )
            )

            // Elapsed and Remaining Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatSecondsToTime(currentSeconds),
                    fontSize = 11.sp,
                    color = GoldLight,
                    fontWeight = FontWeight.SemiBold
                )
                val remainingSeconds = (totalSeconds - currentSeconds).coerceAtLeast(0)
                Text(
                    text = "-${formatSecondsToTime(remainingSeconds)}",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- HERO PLAYBACK CONTROLS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Seek Back 15s
                IconButton(
                    onClick = { onSeekRelative(-15) },
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("seek_back_15s_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastRewind,
                        contentDescription = "Rewind 15 Seconds",
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Hero Play/Pause Button
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GoldLight, GoldDark)))
                        .clickable { if (isPlaying) onPause() else onPlay() }
                        .testTag("lesson_player_play_pause_hero_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = RichBlack,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Seek Forward 15s
                IconButton(
                    onClick = { onSeekRelative(15) },
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("seek_fwd_15s_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.FastForward,
                        contentDescription = "Fast Forward 15 Seconds",
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- ACTION TABS: Chapters, Transcript, Save Takeaways, Mark Completed ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chapters Toggle
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (playerState.showChapters) GoldPrimary.copy(alpha = 0.2f) else SurfaceElevated,
                    border = BorderStroke(0.8.dp, if (playerState.showChapters) GoldPrimary else DarkBorder),
                    modifier = Modifier.clickable { onToggleChapters() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FormatListNumbered,
                            contentDescription = null,
                            tint = if (playerState.showChapters) GoldLight else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Chapters",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (playerState.showChapters) GoldLight else TextSecondary
                        )
                    }
                }

                // Transcript Toggle
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (playerState.showTranscript) GoldPrimary.copy(alpha = 0.2f) else SurfaceElevated,
                    border = BorderStroke(0.8.dp, if (playerState.showTranscript) GoldPrimary else DarkBorder),
                    modifier = Modifier.clickable { onToggleTranscript() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notes,
                            contentDescription = null,
                            tint = if (playerState.showTranscript) GoldLight else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Transcript",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (playerState.showTranscript) GoldLight else TextSecondary
                        )
                    }
                }

                // Save to Notebook
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SurfaceElevated,
                    border = BorderStroke(0.8.dp, DarkBorder),
                    modifier = Modifier.clickable { onSaveToNotebook(lesson) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.BookmarkAdd,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Save Note",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Completed status
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (lesson.isCompleted) GoldPrimary.copy(alpha = 0.25f) else SurfaceElevated,
                    border = BorderStroke(0.8.dp, if (lesson.isCompleted) GoldPrimary else DarkBorder),
                    modifier = Modifier.clickable { onMarkCompleted(lesson.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (lesson.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = if (lesson.isCompleted) GoldPrimary else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (lesson.isCompleted) "Completed" else "Mark Done",
                            fontSize = 11.sp,
                            fontWeight = if (lesson.isCompleted) FontWeight.Bold else FontWeight.Normal,
                            color = if (lesson.isCompleted) GoldLight else TextSecondary
                        )
                    }
                }
            }

            // --- EXPANDABLE CHAPTERS LIST ---
            AnimatedVisibility(visible = playerState.showChapters) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "LESSON CHAPTERS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    lesson.chapterList.forEach { chapterLine ->
                        val parts = chapterLine.split(" ", limit = 2)
                        val timestamp = parts.getOrNull(0) ?: "00:00"
                        val title = parts.getOrNull(1) ?: chapterLine
                        val sec = parseTimeToSeconds(timestamp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSeek(sec) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = timestamp,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary,
                                modifier = Modifier.width(44.dp)
                            )
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // --- EXPANDABLE TRANSCRIPT / KEY TAKEAWAY ---
            AnimatedVisibility(visible = playerState.showTranscript) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(SurfaceElevated, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "KEY TAKEAWAY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = lesson.keyTakeaway,
                        fontSize = 12.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "FULL TRANSCRIPT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lesson.transcript,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

/**
 * Animated visualizer stage for Video mode with cinematic animated slides and dynamic subtitles.
 */
@Composable
fun ShortLessonVideoStage(
    lesson: ShortLessonEntity,
    isPlaying: Boolean,
    currentPositionSeconds: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "video_stage_anim")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = if (isPlaying) 1.05f else 1.0f,
        animationSpec = infiniteRepeatable(tween(2400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isPlaying) 0.6f else 0.2f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Reverse),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        GoldDark.copy(alpha = glowAlpha),
                        SurfaceElevated,
                        RichBlack
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Gold Geometric Crown / Crest Insignia
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.15f))
                    .border(1.dp, GoldPrimary.copy(alpha = glowAlpha), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.SlowMotionVideo,
                    contentDescription = null,
                    tint = GoldLight,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = lesson.title.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Principle of Transmutation • Napoleon Hill Philosophy",
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle ticker / Current Key point
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = RichBlack.copy(alpha = 0.75f),
                border = BorderStroke(0.5.dp, DarkBorder)
            ) {
                Text(
                    text = if (isPlaying) "▶ \"${lesson.keyTakeaway.take(65)}...\"" else "❚❚ Paused • Tap Play to Stream Lecture",
                    fontSize = 10.sp,
                    color = if (isPlaying) GoldLight else TextMuted,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Animated visualizer stage for Audio mode with golden waveform equalizer.
 */
@Composable
fun ShortLessonAudioStage(
    lesson: ShortLessonEntity,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceElevated,
                        RichBlack
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            ShortLessonEqualizer(isPlaying = isPlaying, modifier = Modifier.height(40.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "THETA HARMONIC AUDIO STREAM",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = GoldLight
            )

            Text(
                text = lesson.instructorName,
                fontSize = 11.sp,
                color = TextMuted
            )
        }
    }
}

/**
 * Multi-bar animated waveform for active short lesson playback.
 */
@Composable
fun ShortLessonEqualizer(isPlaying: Boolean, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "equalizer_anim")

    val b1 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = if (isPlaying) 32f else 6f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b1"
    )
    val b2 by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = if (isPlaying) 38f else 12f,
        animationSpec = infiniteRepeatable(tween(420, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b2"
    )
    val b3 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = if (isPlaying) 26f else 8f,
        animationSpec = infiniteRepeatable(tween(280, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b3"
    )
    val b4 by infiniteTransition.animateFloat(
        initialValue = 14f,
        targetValue = if (isPlaying) 40f else 14f,
        animationSpec = infiniteRepeatable(tween(480, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b4"
    )
    val b5 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = if (isPlaying) 22f else 4f,
        animationSpec = infiniteRepeatable(tween(320, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "b5"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(b1, b3, b5, b2, b4, b1, b5, b3, b4, b2, b5, b1, b3).forEach { barHeight ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(barHeight.dp)
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

/**
 * Compact mini equalizer for list items.
 */
@Composable
fun ShortLessonMiniEqualizer(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "mini_eq_anim")

    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(tween(300, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 16f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(260, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(h1, h2, h3).forEach { h ->
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(h.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(RichBlack)
            )
        }
    }
}

/**
 * Floating mini lesson player bar visible when navigating across screens.
 */
@Composable
fun FloatingMiniLessonPlayerBar(
    playerState: ShortLessonPlayerState,
    onResume: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onOpenModule: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lesson = playerState.activeLesson ?: return
    val isPlaying = playerState.isPlaying
    val isVisible = playerState.activeLesson != null

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clickable { onOpenModule(lesson.moduleId) }
                .testTag("floating_mini_lesson_player_bar"),
            color = DarkCharcoal,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GoldPrimary),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini Waveform
                ShortLessonEqualizer(isPlaying = isPlaying, modifier = Modifier.size(width = 28.dp, height = 20.dp))

                Spacer(modifier = Modifier.width(10.dp))

                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lesson.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (isPlaying) "Playing • ${lesson.durationText}" else "Paused • Tap to resume",
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

private fun formatSecondsToTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

private fun parseTimeToSeconds(timeStr: String): Int {
    return try {
        val parts = timeStr.trim().split(":")
        if (parts.size == 2) {
            val min = parts[0].toInt()
            val sec = parts[1].toInt()
            (min * 60) + sec
        } else {
            0
        }
    } catch (_: Exception) {
        0
    }
}
