package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
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
import java.util.Calendar
import java.util.Locale
import kotlin.math.floor
import kotlin.math.roundToInt

enum class WeeksDisplayMode {
    NUMERICAL,
    GRAPHICAL
}

/**
 * Utility calculations for Life in Weeks
 */
object WeeksLivedCalculator {
    const val WEEKS_PER_YEAR = 52
    const val DEFAULT_LIFESPAN_YEARS = 90
    const val TOTAL_LIFETIME_WEEKS = DEFAULT_LIFESPAN_YEARS * WEEKS_PER_YEAR // 4680

    fun calculateWeeksLived(birthYear: Int, birthMonth: Int = 1, birthDay: Int = 1): Int {
        val birthCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, birthYear)
            set(Calendar.MONTH, (birthMonth - 1).coerceIn(0, 11))
            set(Calendar.DAY_OF_MONTH, birthDay.coerceIn(1, 31))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val now = Calendar.getInstance()
        val diffMillis = (now.timeInMillis - birthCal.timeInMillis).coerceAtLeast(0)
        val diffDays = diffMillis / (1000L * 60 * 60 * 24)
        val weeks = (diffDays / 7).toInt()
        return weeks.coerceIn(0, TOTAL_LIFETIME_WEEKS)
    }

    fun calculateAgeInDecimal(birthYear: Int, birthMonth: Int = 1, birthDay: Int = 1): Double {
        val weeks = calculateWeeksLived(birthYear, birthMonth, birthDay)
        return (weeks.toDouble() / WEEKS_PER_YEAR.toDouble())
    }
}

@Composable
fun WeeksLivedWidget(
    userProfile: UserProfileEntity?,
    onUpdateBirthDate: (birthYear: Int, birthMonth: Int, birthDay: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val birthYear = userProfile?.birthYear ?: 1996
    val birthMonth = userProfile?.birthMonth ?: 1
    val birthDay = userProfile?.birthDay ?: 1
    val totalLifetimeWeeks = (userProfile?.lifeExpectancyYears ?: 90) * WeeksLivedCalculator.WEEKS_PER_YEAR

    val weeksLived by remember(birthYear, birthMonth, birthDay) {
        derivedStateOf {
            WeeksLivedCalculator.calculateWeeksLived(birthYear, birthMonth, birthDay)
        }
    }

    val weeksRemaining = remember(weeksLived, totalLifetimeWeeks) {
        (totalLifetimeWeeks - weeksLived).coerceAtLeast(0)
    }

    val percentLived = remember(weeksLived, totalLifetimeWeeks) {
        (weeksLived.toFloat() / totalLifetimeWeeks.toFloat() * 100f).coerceIn(0f, 100f)
    }

    val ageDecimal = remember(birthYear, birthMonth, birthDay) {
        WeeksLivedCalculator.calculateAgeInDecimal(birthYear, birthMonth, birthDay)
    }

    var displayMode by remember { mutableStateOf(WeeksDisplayMode.GRAPHICAL) }
    var isExpanded by remember { mutableStateOf(false) }
    var showEditAgeDialog by remember { mutableStateOf(false) }

    // Pulsating animation for the current active week
    val infiniteTransition = rememberInfiniteTransition(label = "CurrentWeekGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GoldPrimary.copy(alpha = 0.45f), RoundedCornerShape(16.dp))
            .animateContentSize()
            .testTag("weeks_lived_widget"),
        colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Bar: Title, Mode Toggle Pill, Edit Age Icon, Expand/Collapse
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GoldLinearGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.HourglassTop,
                            contentDescription = "Memento Mori",
                            tint = RichBlack,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "LIFE IN WEEKS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp,
                            color = GoldPrimary
                        )
                        Text(
                            text = "${String.format(Locale.US, "%,d", weeksLived)} of 4,680 Weeks Lived",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }

                // Controls: Toggle Switch (Numerical vs Graphical) & Edit
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Segmented Toggle Pill
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Numerical Mode Tab
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (displayMode == WeeksDisplayMode.NUMERICAL) GoldPrimary else Color.Transparent)
                                    .clickable { displayMode = WeeksDisplayMode.NUMERICAL }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                    .testTag("toggle_numerical_age"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Numbers,
                                        contentDescription = "Numerical",
                                        tint = if (displayMode == WeeksDisplayMode.NUMERICAL) RichBlack else TextMuted,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Numerical",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (displayMode == WeeksDisplayMode.NUMERICAL) RichBlack else TextMuted
                                    )
                                }
                            }

                            // Graphical Mode Tab
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (displayMode == WeeksDisplayMode.GRAPHICAL) GoldPrimary else Color.Transparent)
                                    .clickable { displayMode = WeeksDisplayMode.GRAPHICAL }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                    .testTag("toggle_graphical_age"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.GridView,
                                        contentDescription = "Graphical",
                                        tint = if (displayMode == WeeksDisplayMode.GRAPHICAL) RichBlack else TextMuted,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Grid",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (displayMode == WeeksDisplayMode.GRAPHICAL) RichBlack else TextMuted
                                    )
                                }
                            }
                        }
                    }

                    // Edit Age Button
                    IconButton(
                        onClick = { showEditAgeDialog = true },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("edit_age_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Set Age or Birth Date",
                            tint = GoldLight,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Expand / Collapse Chevron
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("expand_weeks_grid_button")
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = GoldLight,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Motivational Text Line
            Text(
                text = "You've lived ~${String.format(Locale.US, "%,d", weeksLived)} weeks so far. There are 4,680 weeks in a 90-year lifetime.",
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Body Display depending on Numerical vs Graphical mode
            AnimatedContent(
                targetState = displayMode,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(150)) },
                label = "WeeksModeTransition"
            ) { mode ->
                when (mode) {
                    WeeksDisplayMode.NUMERICAL -> {
                        NumericalAgeView(
                            weeksLived = weeksLived,
                            weeksRemaining = weeksRemaining,
                            percentLived = percentLived,
                            ageDecimal = ageDecimal,
                            birthYear = birthYear,
                            onEditAge = { showEditAgeDialog = true }
                        )
                    }
                    WeeksDisplayMode.GRAPHICAL -> {
                        GraphicalAgeView(
                            weeksLived = weeksLived,
                            totalLifetimeWeeks = totalLifetimeWeeks,
                            isExpanded = isExpanded,
                            glowAlpha = glowAlpha,
                            onToggleExpand = { isExpanded = !isExpanded }
                        )
                    }
                }
            }
        }
    }

    // Edit Age / Birth Date Dialog
    if (showEditAgeDialog) {
        EditAgeDialog(
            currentBirthYear = birthYear,
            currentBirthMonth = birthMonth,
            currentBirthDay = birthDay,
            onDismiss = { showEditAgeDialog = false },
            onSave = { y, m, d ->
                onUpdateBirthDate(y, m, d)
                showEditAgeDialog = false
            }
        )
    }
}

/**
 * Numerical Stats View: Compact KPI cards with percentage bar and quote
 */
@Composable
private fun NumericalAgeView(
    weeksLived: Int,
    weeksRemaining: Int,
    percentLived: Float,
    ageDecimal: Double,
    birthYear: Int,
    onEditAge: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // High-contrast stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Weeks Lived Card
            Surface(
                color = RichBlack,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "WEEKS LIVED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format(Locale.US, "%,d", weeksLived),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "${String.format(Locale.US, "%.1f", percentLived)}% of 90 yrs",
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }

            // Weeks Remaining Card
            Surface(
                color = RichBlack,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberAccent.copy(alpha = 0.3f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "WEEKS REMAINING",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberAccent,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format(Locale.US, "%,d", weeksRemaining),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = GoldLight
                    )
                    Text(
                        text = "To build your legacy",
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }

            // Exact Age Card
            Surface(
                color = RichBlack,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CHRONO AGE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        letterSpacing = 0.6.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${String.format(Locale.US, "%.1f", ageDecimal)} yrs",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Born in $birthYear",
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Lifetime Progress Bar
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "90-Year Life Progress",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Text(
                    text = "${String.format(Locale.US, "%.1f", percentLived)}%",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = RichBlack,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(percentLived / 100f)
                        .background(GoldLinearGradient)
                )
            }
        }

        // Stoic Wisdom Quote
        Text(
            text = "“Time is the only true capital that any human being has to invest.” — Napoleon Hill",
            fontSize = 10.sp,
            color = GoldLight.copy(alpha = 0.8f),
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        )
    }
}

/**
 * Graphical Grid View: 90 Rows × 52 Columns (4,680 week blocks)
 * Renders cleanly in compact mode (high density canvas preview) or expanded interactive grid.
 */
@Composable
private fun GraphicalAgeView(
    weeksLived: Int,
    totalLifetimeWeeks: Int,
    isExpanded: Boolean,
    glowAlpha: Float,
    onToggleExpand: () -> Unit
) {
    var selectedWeekInfo by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Compact vs Full Canvas Grid
        if (!isExpanded) {
            // Compact Canvas Preview (90 years represented as dense rows)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(RichBlack)
                    .border(1.dp, DarkBorder, RoundedCornerShape(8.dp))
                    .clickable { onToggleExpand() }
                    .padding(6.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val cols = 52
                    val rows = 90
                    val colSpacing = 0.5f
                    val rowSpacing = 0.5f

                    val blockWidth = (canvasWidth - (cols - 1) * colSpacing) / cols
                    val blockHeight = (canvasHeight - (rows - 1) * rowSpacing) / rows

                    var weekIndex = 0

                    for (r in 0 until rows) {
                        for (c in 0 until cols) {
                            val x = c * (blockWidth + colSpacing)
                            val y = r * (blockHeight + rowSpacing)

                            when {
                                weekIndex < weeksLived -> {
                                    // Past week: filled gold
                                    drawRect(
                                        color = GoldPrimary,
                                        topLeft = Offset(x, y),
                                        size = Size(blockWidth, blockHeight)
                                    )
                                }
                                weekIndex == weeksLived -> {
                                    // Current week: glowing amber
                                    drawRect(
                                        color = AmberBright.copy(alpha = glowAlpha),
                                        topLeft = Offset(x, y),
                                        size = Size(blockWidth, blockHeight)
                                    )
                                }
                                else -> {
                                    // Future week: subtle dark surface
                                    drawRect(
                                        color = DarkBorder.copy(alpha = 0.6f),
                                        topLeft = Offset(x, y),
                                        size = Size(blockWidth, blockHeight)
                                    )
                                }
                            }
                            weekIndex++
                        }
                    }
                }
            }

            // Compact Legend & Tap to Expand hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Legend
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(GoldPrimary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Weeks Lived", fontSize = 9.sp, color = TextMuted)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(AmberAccent)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Current Week", fontSize = 9.sp, color = AmberAccent)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .border(0.5.dp, DarkBorder, RoundedCornerShape(1.dp))
                                .background(SurfaceElevated)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Future", fontSize = 9.sp, color = TextMuted)
                    }
                }

                Text(
                    text = "Tap to expand full grid ▾",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    modifier = Modifier.clickable { onToggleExpand() }
                )
            }
        } else {
            // Expanded Interactive Canvas Grid (Higher height with decade labels 0, 10, 20... 90)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(RichBlack)
                    .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "90-YEAR MASTER LIFE GRID (52 WEEKS / YR)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary,
                        letterSpacing = 0.8.sp
                    )

                    Text(
                        text = selectedWeekInfo ?: "Touch any row for decade details",
                        fontSize = 9.sp,
                        color = if (selectedWeekInfo != null) AmberAccent else TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // High-resolution Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val cols = 52
                                val rows = 90
                                val colSpacing = 1.0f
                                val rowSpacing = 1.2f
                                val blockWidth = (size.width - (cols - 1) * colSpacing) / cols
                                val blockHeight = (size.height - (rows - 1) * rowSpacing) / rows

                                val col = (offset.x / (blockWidth + colSpacing)).toInt().coerceIn(0, 51)
                                val row = (offset.y / (blockHeight + rowSpacing)).toInt().coerceIn(0, 89)
                                val tappedWeek = row * 52 + col
                                val status = when {
                                    tappedWeek < weeksLived -> "Completed"
                                    tappedWeek == weeksLived -> "Current Present Week"
                                    else -> "Future Week"
                                }
                                selectedWeekInfo = "Age $row, Wk ${col + 1} (#${tappedWeek + 1}): $status"
                            }
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        val cols = 52
                        val rows = 90
                        val colSpacing = 1.0f
                        val rowSpacing = 1.2f

                        val blockWidth = (canvasWidth - (cols - 1) * colSpacing) / cols
                        val blockHeight = (canvasHeight - (rows - 1) * rowSpacing) / rows

                        var weekIndex = 0

                        for (r in 0 until rows) {
                            val isDecade = (r % 10 == 0)
                            for (c in 0 until cols) {
                                val x = c * (blockWidth + colSpacing)
                                val y = r * (blockHeight + rowSpacing)

                                when {
                                    weekIndex < weeksLived -> {
                                        drawRoundRect(
                                            color = GoldPrimary,
                                            topLeft = Offset(x, y),
                                            size = Size(blockWidth, blockHeight),
                                            cornerRadius = CornerRadius(1f, 1f)
                                        )
                                    }
                                    weekIndex == weeksLived -> {
                                        drawRoundRect(
                                            color = AmberBright.copy(alpha = glowAlpha),
                                            topLeft = Offset(x, y),
                                            size = Size(blockWidth, blockHeight),
                                            cornerRadius = CornerRadius(1f, 1f)
                                        )
                                    }
                                    else -> {
                                        drawRoundRect(
                                            color = if (isDecade) DarkBorder.copy(alpha = 0.9f) else SurfaceElevated.copy(alpha = 0.5f),
                                            topLeft = Offset(x, y),
                                            size = Size(blockWidth, blockHeight),
                                            cornerRadius = CornerRadius(1f, 1f)
                                        )
                                    }
                                }
                                weekIndex++
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Decade milestones guide
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Age 0", "Age 18", "Age 30", "Age 50", "Age 70", "Age 90").forEach { label ->
                        Text(text = label, fontSize = 8.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = onToggleExpand) {
                        Icon(imageVector = Icons.Filled.ExpandLess, contentDescription = null, tint = GoldLight, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Collapse Grid", fontSize = 11.sp, color = GoldLight)
                    }
                }
            }
        }
    }
}

/**
 * Luxury Dialog for updating user age / birth year / date of birth
 */
@Composable
fun EditAgeDialog(
    currentBirthYear: Int,
    currentBirthMonth: Int,
    currentBirthDay: Int,
    onDismiss: () -> Unit,
    onSave: (birthYear: Int, birthMonth: Int, birthDay: Int) -> Unit
) {
    val currentCalendarYear = Calendar.getInstance().get(Calendar.YEAR)
    val calculatedCurrentAge = (currentCalendarYear - currentBirthYear).coerceIn(1, 100)

    var ageSliderValue by remember { mutableFloatStateOf(calculatedCurrentAge.toFloat()) }
    var birthYearText by remember { mutableStateOf(currentBirthYear.toString()) }
    var birthMonthText by remember { mutableStateOf(currentBirthMonth.toString()) }
    var birthDayText by remember { mutableStateOf(currentBirthDay.toString()) }
    var isDirectDateMode by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCharcoal,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Synchronize Life Timeline",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = GoldLight
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close", tint = TextMuted)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Configure your age or exact date of birth to calculate your 4,680-week timeline accurately.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )

                // Mode switch: Quick Age Slider vs Exact Date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { isDirectDateMode = false },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (!isDirectDateMode) GoldPrimary.copy(alpha = 0.15f) else Color.Transparent
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (!isDirectDateMode) GoldPrimary else DarkBorder
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text(
                            text = "Quick Age (${ageSliderValue.roundToInt()} yrs)",
                            fontSize = 11.sp,
                            color = if (!isDirectDateMode) GoldLight else TextMuted
                        )
                    }

                    OutlinedButton(
                        onClick = { isDirectDateMode = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isDirectDateMode) GoldPrimary.copy(alpha = 0.15f) else Color.Transparent
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDirectDateMode) GoldPrimary else DarkBorder
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text(
                            text = "Exact Birth Date",
                            fontSize = 11.sp,
                            color = if (isDirectDateMode) GoldLight else TextMuted
                        )
                    }
                }

                if (!isDirectDateMode) {
                    // Quick Age Slider
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RichBlack, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${ageSliderValue.roundToInt()} Years Old",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = GoldLight
                        )
                        Text(
                            text = "Born ~${currentCalendarYear - ageSliderValue.roundToInt()} • ~${(ageSliderValue * 52).roundToInt()} weeks lived",
                            fontSize = 11.sp,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = ageSliderValue,
                            onValueChange = {
                                ageSliderValue = it
                                birthYearText = (currentCalendarYear - it.roundToInt()).toString()
                            },
                            valueRange = 10f..90f,
                            steps = 79,
                            colors = SliderDefaults.colors(
                                thumbColor = GoldPrimary,
                                activeTrackColor = GoldPrimary,
                                inactiveTrackColor = DarkBorder
                            )
                        )
                    }
                } else {
                    // Exact Date Input (Year, Month, Day)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RichBlack, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "ENTER BIRTH DATE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary,
                            letterSpacing = 0.8.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Birth Year
                            OutlinedTextField(
                                value = birthYearText,
                                onValueChange = { birthYearText = it.take(4) },
                                label = { Text("Year (YYYY)", fontSize = 10.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = DarkBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1.4f)
                            )

                            // Birth Month
                            OutlinedTextField(
                                value = birthMonthText,
                                onValueChange = { birthMonthText = it.take(2) },
                                label = { Text("Month (1-12)", fontSize = 10.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = DarkBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1.1f)
                            )

                            // Birth Day
                            OutlinedTextField(
                                value = birthDayText,
                                onValueChange = { birthDayText = it.take(2) },
                                label = { Text("Day (1-31)", fontSize = 10.sp) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GoldPrimary,
                                    unfocusedBorderColor = DarkBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                singleLine = true,
                                modifier = Modifier.weight(1.1f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!isDirectDateMode) {
                        val chosenAge = ageSliderValue.roundToInt()
                        val computedYear = currentCalendarYear - chosenAge
                        onSave(computedYear, 1, 1)
                    } else {
                        val y = birthYearText.toIntOrNull() ?: (currentCalendarYear - ageSliderValue.roundToInt())
                        val m = (birthMonthText.toIntOrNull() ?: 1).coerceIn(1, 12)
                        val d = (birthDayText.toIntOrNull() ?: 1).coerceIn(1, 31)
                        onSave(y.coerceIn(1900, currentCalendarYear), m, d)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Save Timeline", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted, fontSize = 12.sp)
            }
        }
    )
}
