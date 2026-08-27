package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.RichesRepository
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DailyHabitsTrackerSection(
    habits: List<DailyHabitEntity>,
    todayHabitLogs: List<DailyHabitLogEntity>,
    allHabitLogs: List<DailyHabitLogEntity>,
    selectedDateEpochDay: Long,
    userProfile: UserProfileEntity?,
    onSelectDate: (Long) -> Unit,
    onToggleHabit: (String) -> Unit,
    onOpenHabitDetail: (DailyHabitEntity) -> Unit,
    onAddNewHabit: () -> Unit,
    onMilestoneClick: (StreakMilestoneInfo) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val todayEpochDay = remember { RichesRepository.getTodayEpochDay() }
    val isViewingToday = selectedDateEpochDay == todayEpochDay

    val completedHabitIds = remember(todayHabitLogs) {
        todayHabitLogs.map { it.habitId }.toSet()
    }

    val completedCount = habits.count { it.id in completedHabitIds }
    val totalCount = habits.size
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    val totalXpToday = remember(todayHabitLogs) {
        todayHabitLogs.sumOf { it.xpEarned }
    }

    val totalMinutesToday = remember(todayHabitLogs) {
        todayHabitLogs.sumOf { it.durationMinutes }
    }

    val categories = listOf("All", "Mindset", "Knowledge", "Spiritual", "Action")
    val filteredHabits = remember(habits, selectedCategory) {
        if (selectedCategory == "All") habits else habits.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val currentStreak = userProfile?.currentStreak ?: 1
    val bestStreak = userProfile?.bestStreak ?: 1

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        Color(0xFFD4AF37).copy(alpha = 0.5f),
                        Color(0xFF8C6D23).copy(alpha = 0.2f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .testTag("daily_habits_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF14120E).copy(alpha = 0.96f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD4AF37).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "DAILY RITUALS & HABITS",
                            color = Color(0xFFFFD700),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Think and Grow Rich Daily Protocol",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Add Custom Ritual Button
                    IconButton(
                        onClick = onAddNewHabit,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD4AF37).copy(alpha = 0.12f))
                            .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), CircleShape)
                            .testTag("add_custom_habit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Custom Ritual",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- VISUAL STREAK COUNTER & MILESTONES (3, 7, 14, 30 DAYS) ---
            HabitStreakMilestoneBar(
                currentStreak = currentStreak,
                bestStreak = bestStreak,
                todayCompletedHabitsCount = completedCount,
                totalHabitsCount = totalCount,
                onMilestoneClick = onMilestoneClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            // --- 7-DAY CALENDAR DATE SELECTOR STRIP ---
            SevenDayDateSelectorStrip(
                selectedDateEpochDay = selectedDateEpochDay,
                todayEpochDay = todayEpochDay,
                allHabitLogs = allHabitLogs,
                totalHabitsCount = habits.size,
                onSelectDate = onSelectDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- DAILY PROGRESS SUMMARY BANNER ---
            DailyProgressMetricBanner(
                completedCount = completedCount,
                totalCount = totalCount,
                progressFraction = progressFraction,
                totalXpToday = totalXpToday,
                totalMinutesToday = totalMinutesToday,
                isViewingToday = isViewingToday,
                selectedDateEpochDay = selectedDateEpochDay,
                streak = userProfile?.currentStreak ?: 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- CATEGORY PILL FILTERS ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    val activeColor = when (category) {
                        "Mindset" -> Color(0xFFFFD700)
                        "Knowledge" -> Color(0xFF64B5F6)
                        "Spiritual" -> Color(0xFFCE93D8)
                        "Action" -> Color(0xFFFF8A65)
                        else -> Color(0xFFD4AF37)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) activeColor.copy(alpha = 0.22f)
                                else Color.White.copy(alpha = 0.05f)
                            )
                            .border(
                                1.dp,
                                if (isSelected) activeColor.copy(alpha = 0.7f)
                                else Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) activeColor else Color(0xFFB0A89A),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- HABITS LIST ---
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                filteredHabits.forEach { habit ->
                    val isCompleted = habit.id in completedHabitIds
                    val habitLog = todayHabitLogs.firstOrNull { it.habitId == habit.id }

                    HabitItemCard(
                        habit = habit,
                        isCompleted = isCompleted,
                        habitLog = habitLog,
                        onToggle = { onToggleHabit(habit.id) },
                        onCardClick = { onOpenHabitDetail(habit) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SevenDayDateSelectorStrip(
    selectedDateEpochDay: Long,
    todayEpochDay: Long,
    allHabitLogs: List<DailyHabitLogEntity>,
    totalHabitsCount: Int,
    onSelectDate: (Long) -> Unit
) {
    // Generate the last 7 days ending with today
    val days = remember(todayEpochDay) {
        (6 downTo 0).map { offset -> todayEpochDay - offset }
    }

    val logsCountByDay = remember(allHabitLogs) {
        allHabitLogs.groupBy { it.dateEpochDay }.mapValues { it.value.size }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { epochDay ->
            val isSelected = epochDay == selectedDateEpochDay
            val isToday = epochDay == todayEpochDay
            val count = logsCountByDay[epochDay] ?: 0
            val isFullCompleted = totalHabitsCount > 0 && count >= totalHabitsCount
            val hasActivity = count > 0

            val cal = remember(epochDay) {
                Calendar.getInstance().apply {
                    timeInMillis = epochDay * (24 * 60 * 60 * 1000L)
                }
            }
            val dayName = remember(epochDay) {
                SimpleDateFormat("EEE", Locale.US).format(cal.time).take(1).uppercase()
            }
            val dayNumber = remember(epochDay) {
                cal.get(Calendar.DAY_OF_MONTH).toString()
            }

            val bgModifier = if (isSelected) {
                Modifier.background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFD4AF37).copy(alpha = 0.35f), Color(0xFF8C6D23).copy(alpha = 0.2f))
                    )
                )
            } else if (hasActivity) {
                Modifier.background(Color(0xFFD4AF37).copy(alpha = 0.08f))
            } else {
                Modifier.background(Color.White.copy(alpha = 0.03f))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(bgModifier)
                    .border(
                        1.dp,
                        when {
                            isSelected -> Color(0xFFFFD700)
                            isToday -> Color(0xFFD4AF37).copy(alpha = 0.5f)
                            hasActivity -> Color(0xFFD4AF37).copy(alpha = 0.2f)
                            else -> Color.White.copy(alpha = 0.06f)
                        },
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onSelectDate(epochDay) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayName,
                        color = if (isSelected) Color(0xFFFFD700) else Color(0xFF8E867A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = dayNumber,
                        color = if (isSelected) Color.White else if (isToday) Color(0xFFFFD700) else Color(0xFFD2C7B8),
                        fontSize = 13.sp,
                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Indicator dot
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isFullCompleted -> Color(0xFFFFD700)
                                    hasActivity -> Color(0xFF4CAF50)
                                    isSelected -> Color(0xFFD4AF37).copy(alpha = 0.5f)
                                    else -> Color.Transparent
                                }
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyProgressMetricBanner(
    completedCount: Int,
    totalCount: Int,
    progressFraction: Float,
    totalXpToday: Int,
    totalMinutesToday: Int,
    isViewingToday: Boolean,
    selectedDateEpochDay: Long,
    streak: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "habit_progress"
    )

    val dateLabel = remember(selectedDateEpochDay, isViewingToday) {
        if (isViewingToday) "Today's Protocol"
        else RichesRepository.formatDisplayDate(selectedDateEpochDay)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF262016),
                        Color(0xFF1B160F)
                    )
                )
            )
            .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left circular progress indicator
            Box(
                modifier = Modifier.size(54.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White.copy(alpha = 0.08f),
                    strokeWidth = 5.dp
                )
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFD700),
                    strokeWidth = 5.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$completedCount/$totalCount",
                        color = Color(0xFFFFD700),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = dateLabel,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (progressFraction >= 1f && totalCount > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF4CAF50).copy(alpha = 0.2f))
                                .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "100% COMPLETE",
                                color = Color(0xFF81C784),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (completedCount == 0) "Begin your morning autosuggestion & visualization"
                    else if (completedCount == totalCount) "Mastery achieved! Infinite Intelligence is aligned ✨"
                    else "${totalCount - completedCount} rituals remaining for complete daily transmutation",
                    color = Color(0xFFC0B5A3),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right XP & Minutes Badge
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFD4AF37).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "+$totalXpToday XP",
                            color = Color(0xFFFFD700),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (totalMinutesToday > 0) {
                    Text(
                        text = "$totalMinutesToday mins logged",
                        color = Color(0xFF9E9484),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitItemCard(
    habit: DailyHabitEntity,
    isCompleted: Boolean,
    habitLog: DailyHabitLogEntity?,
    onToggle: () -> Unit,
    onCardClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val categoryColor = when (habit.category.lowercase()) {
        "mindset" -> Color(0xFFFFD700)
        "knowledge" -> Color(0xFF64B5F6)
        "spiritual" -> Color(0xFFCE93D8)
        "action" -> Color(0xFFFF8A65)
        else -> Color(0xFFD4AF37)
    }

    val iconVector = getHabitIconVector(habit.iconKey)

    val cardBg = if (isCompleted) {
        Color(0xFF1E1A14).copy(alpha = 0.95f)
    } else {
        Color(0xFF161410).copy(alpha = 0.85f)
    }

    val borderColor by animateColorAsState(
        targetValue = if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.5f)
        else Color.White.copy(alpha = 0.08f),
        label = "border_color"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(cardBg)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onCardClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.15f)
                        else categoryColor.copy(alpha = 0.12f)
                    )
                    .border(
                        1.dp,
                        if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.5f)
                        else categoryColor.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = habit.title,
                    tint = if (isCompleted) Color(0xFF81C784) else categoryColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Main Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = habit.title,
                        color = if (isCompleted) Color.White else Color(0xFFEDE6DA),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = habit.principle,
                        color = categoryColor.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "•",
                        color = Color.White.copy(alpha = 0.25f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "${habit.targetMinutes}m target",
                        color = Color(0xFF9E9484),
                        fontSize = 11.sp
                    )
                    Text(
                        text = "•",
                        color = Color.White.copy(alpha = 0.25f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "+${habit.xpReward} XP",
                        color = Color(0xFFFFD700),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isCompleted && habitLog?.notes?.isNotBlank() == true) {
                    Text(
                        text = "\"${habitLog.notes}\"",
                        color = Color(0xFFB5AC9E),
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Interactive Golden Seal Toggle Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) Brush.radialGradient(
                            listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                        ) else Brush.radialGradient(
                            listOf(Color(0xFF2E2619), Color(0xFF19140B))
                        )
                    )
                    .border(
                        1.5.dp,
                        if (isCompleted) Color(0xFF81C784)
                        else Color(0xFFD4AF37).copy(alpha = 0.5f),
                        CircleShape
                    )
                    .clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToggle()
                    }
                    .testTag("habit_toggle_${habit.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Diamond,
                        contentDescription = "Tap to Complete",
                        tint = Color(0xFFD4AF37).copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// --- RITUAL EXECUTION & GUIDED TIMER MODAL ---

@Composable
fun RitualDetailModal(
    habit: DailyHabitEntity,
    isCompleted: Boolean,
    habitLog: DailyHabitLogEntity?,
    isAmbientSoundPlaying: Boolean,
    onToggleAmbientSound: () -> Unit,
    onCompleteWithReflection: (durationMinutes: Int, notes: String, saveToNotebook: Boolean) -> Unit,
    onToggleQuick: () -> Unit,
    onDismiss: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var reflectionNote by remember { mutableStateOf(habitLog?.notes ?: "") }
    var saveToNotebook by remember { mutableStateOf(false) }

    // Timer states
    val targetSeconds = remember(habit) { habit.targetMinutes * 60 }
    var remainingSeconds by remember { mutableIntStateOf(targetSeconds) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
        if (remainingSeconds == 0) {
            isTimerRunning = false
        }
    }

    val elapsedMinutes = remember(remainingSeconds, targetSeconds) {
        maxOf(1, ((targetSeconds - remainingSeconds) / 60) + if ((targetSeconds - remainingSeconds) % 60 > 0) 1 else 0)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            listOf(Color(0xFFFFD700).copy(alpha = 0.6f), Color(0xFF8C6D23).copy(alpha = 0.2f))
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF14120E))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFD4AF37).copy(alpha = 0.15f))
                                .border(1.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = habit.category.uppercase(),
                                color = Color(0xFFFFD700),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = Color(0xFFC0B5A3)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = habit.title,
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Principle: ${habit.principle}",
                        color = Color(0xFFFFD700),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Description & Instructions
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.04f))
                            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = habit.description,
                            color = Color(0xFFD5CCBE),
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- GUIDED FOCUS TIMER ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1A13)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFFD4AF37).copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Timer,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Ritual Focus Chamber",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Ambient sound toggle
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            if (isAmbientSoundPlaying) Color(0xFFD4AF37).copy(alpha = 0.2f)
                                            else Color.White.copy(alpha = 0.05f)
                                        )
                                        .clickable { onToggleAmbientSound() }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isAmbientSoundPlaying) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                                            contentDescription = null,
                                            tint = if (isAmbientSoundPlaying) Color(0xFFFFD700) else Color(0xFF8E867A),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (isAmbientSoundPlaying) "Binaural ON" else "Ambient",
                                            color = if (isAmbientSoundPlaying) Color(0xFFFFD700) else Color(0xFF8E867A),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Timer Display
                            val minutesStr = String.format(Locale.US, "%02d", remainingSeconds / 60)
                            val secondsStr = String.format(Locale.US, "%02d", remainingSeconds % 60)

                            Text(
                                text = "$minutesStr:$secondsStr",
                                color = if (isTimerRunning) Color(0xFFFFD700) else Color.White,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Timer Controls
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { isTimerRunning = !isTimerRunning },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isTimerRunning) Color(0xFFD32F2F) else Color(0xFFD4AF37)
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isTimerRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isTimerRunning) "Pause" else "Begin Ritual",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        isTimerRunning = false
                                        remainingSeconds = targetSeconds
                                    },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "Reset Timer",
                                        tint = Color(0xFFC0B5A3),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- REFLECTION JOURNAL ENTRY ---
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.EditNote,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Ritual Realizations & Auto-Suggestion Note",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = reflectionNote,
                            onValueChange = { reflectionNote = it },
                            placeholder = {
                                Text(
                                    "Log mental impressions, affirmations, or insights experienced during this session...",
                                    color = Color(0xFF6E665A),
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFD700),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color(0xFFEDE6DA),
                                focusedContainerColor = Color(0xFF100E0A),
                                unfocusedContainerColor = Color(0xFF100E0A)
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Checkbox to save to Notebook
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { saveToNotebook = !saveToNotebook }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = saveToNotebook,
                                onCheckedChange = { saveToNotebook = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFFFFD700),
                                    uncheckedColor = Color(0xFF8E867A),
                                    checkmarkColor = Color.Black
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Save to Sovereign Notebook (+50 bonus XP)",
                                color = Color(0xFFD5CCBE),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onCompleteWithReflection(
                                    elapsedMinutes,
                                    reflectionNote,
                                    saveToNotebook
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("seal_ritual_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD700)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isCompleted) "Update Reflection (+XP)" else "Seal Ritual (+${habit.xpReward + (if (saveToNotebook) 50 else 0)} XP)",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- CUSTOM RITUAL CREATION DIALOG ---

@Composable
fun CreateCustomRitualDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, principle: String, description: String, category: String, iconKey: String, targetMinutes: Int, xpReward: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var principle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Mindset") }
    var targetMinutes by remember { mutableIntStateOf(15) }
    var iconKey by remember { mutableStateOf("affirmation") }

    val categories = listOf("Mindset", "Knowledge", "Spiritual", "Action")
    val icons = listOf(
        "visualization" to Icons.Filled.Visibility,
        "reading" to Icons.AutoMirrored.Filled.MenuBook,
        "meditation" to Icons.Filled.SelfImprovement,
        "affirmation" to Icons.Filled.Psychology,
        "transmutation" to Icons.Filled.Bolt,
        "mastermind" to Icons.Filled.Groups
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .border(
                        1.dp,
                        Brush.linearGradient(
                            listOf(Color(0xFFFFD700).copy(alpha = 0.6f), Color(0xFF8C6D23).copy(alpha = 0.2f))
                        ),
                        RoundedCornerShape(22.dp)
                    ),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF14120E))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Forge Custom Ritual",
                            color = Color(0xFFFFD700),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = Color(0xFFC0B5A3)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Ritual Title", color = Color(0xFFD5CCBE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. 5:00 AM Definite Purpose Incantation", color = Color(0xFF6E665A), fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = customFieldColors()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Associated Principle", color = Color(0xFFD5CCBE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = principle,
                        onValueChange = { principle = it },
                        placeholder = { Text("e.g. Auto-Suggestion, Imagination, Decision", color = Color(0xFF6E665A), fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = customFieldColors()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Category", color = Color(0xFFD5CCBE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = category == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) Color(0xFFD4AF37).copy(alpha = 0.25f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { category = cat }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) Color(0xFFFFD700) else Color(0xFFC0B5A3),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Select Icon", color = Color(0xFFD5CCBE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        icons.forEach { (key, iconVec) ->
                            val isSelected = iconKey == key
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) Color(0xFFD4AF37).copy(alpha = 0.3f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.1f),
                                        CircleShape
                                    )
                                    .clickable { iconKey = key },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = iconVec,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFFFFD700) else Color(0xFFC0B5A3),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Target Duration", color = Color(0xFFD5CCBE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 15, 30, 45).forEach { mins ->
                            val isSelected = targetMinutes == mins
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) Color(0xFFD4AF37).copy(alpha = 0.25f)
                                        else Color.White.copy(alpha = 0.05f)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) Color(0xFFFFD700) else Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { targetMinutes = mins }
                                    .padding(vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${mins}m",
                                    color = if (isSelected) Color(0xFFFFD700) else Color(0xFFC0B5A3),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Ritual Instructions & Guidance", color = Color(0xFFD5CCBE), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Outline how this ritual is executed...", color = Color(0xFF6E665A), fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = customFieldColors()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(
                                    title,
                                    if (principle.isBlank()) "Auto-Suggestion" else principle,
                                    if (description.isBlank()) "Daily non-negotiable sovereign habit." else description,
                                    category,
                                    iconKey,
                                    targetMinutes,
                                    30
                                )
                            }
                        },
                        enabled = title.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            disabledContainerColor = Color(0xFF332B1A)
                        )
                    ) {
                        Text(
                            text = "Seal New Sovereign Ritual",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun customFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFFFD700),
    unfocusedBorderColor = Color.White.copy(alpha = 0.12f),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color(0xFFEDE6DA),
    focusedContainerColor = Color(0xFF100E0A),
    unfocusedContainerColor = Color(0xFF100E0A)
)

private fun getHabitIconVector(iconKey: String): ImageVector {
    return when (iconKey.lowercase()) {
        "visualization" -> Icons.Filled.Visibility
        "reading" -> Icons.AutoMirrored.Filled.MenuBook
        "meditation" -> Icons.Filled.SelfImprovement
        "affirmation" -> Icons.Filled.Psychology
        "transmutation" -> Icons.Filled.Bolt
        "mastermind" -> Icons.Filled.Groups
        "gratitude" -> Icons.Filled.Star
        else -> Icons.Filled.AutoAwesome
    }
}
