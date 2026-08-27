package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MoneyBlueprintQuestion
import com.example.data.model.MoneyBlueprintQuizQuestions
import com.example.data.model.MoneyBlueprintResultEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.MoneyBlueprintHistoryDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MoneyBlueprintQuizState

@Composable
fun MoneyBlueprintQuizScreen(
    state: MoneyBlueprintQuizState,
    historyList: List<MoneyBlueprintResultEntity>,
    onAnswerChanged: (questionId: Int, score: Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onFinish: () -> Unit,
    onBackToDashboard: () -> Unit,
    onShowHistory: () -> Unit,
    onHideHistory: () -> Unit,
    onDeleteHistoryItem: (Long) -> Unit,
    onRetakeQuiz: () -> Unit,
    onSaveToNotebook: (MoneyBlueprintResultEntity) -> Unit,
    onNavigateToModule: (Int) -> Unit,
    onOpenMoneyMindsetJournal: () -> Unit,
    onOpenFearReframe: () -> Unit,
    onOpenMastermindChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) RichBlack else Color(0xFFF7F5F0)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
            .testTag("money_blueprint_screen")
    ) {
        if (state.isRevealingResult && state.latestResult != null) {
            MoneyBlueprintResultView(
                result = state.latestResult,
                onFinish = onFinish,
                onRetake = onRetakeQuiz,
                onShowHistory = onShowHistory,
                onSaveToNotebook = { onSaveToNotebook(state.latestResult) },
                onNavigateToModule = onNavigateToModule,
                onOpenMoneyMindsetJournal = onOpenMoneyMindsetJournal,
                onOpenFearReframe = onOpenFearReframe,
                onOpenMastermindChat = onOpenMastermindChat
            )
        } else {
            MoneyBlueprintQuestionView(
                currentStep = state.currentStep,
                answers = state.answers,
                onAnswerChanged = onAnswerChanged,
                onNext = onNext,
                onPrev = onPrev,
                onCancel = onBackToDashboard,
                onShowHistory = onShowHistory
            )
        }

        if (state.showHistoryDialog) {
            MoneyBlueprintHistoryDialog(
                historyList = historyList,
                onDismiss = onHideHistory,
                onRetakeQuiz = onRetakeQuiz,
                onDeleteEntry = onDeleteHistoryItem
            )
        }
    }
}

@Composable
fun MoneyBlueprintQuestionView(
    currentStep: Int,
    answers: Map<Int, Int>,
    onAnswerChanged: (questionId: Int, score: Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onCancel: () -> Unit,
    onShowHistory: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textPrimaryColor = if (isDark) TextPrimary else Color(0xFF1E1B18)
    val textSecColor = if (isDark) TextSecondary else Color(0xFF635948)
    val cardBg = if (isDark) DarkCharcoal else Color.White
    val cardBorderColor = if (isDark) DarkBorder else Color(0xFFE2D6BC)

    val questions = MoneyBlueprintQuizQuestions.questions
    val safeStep = currentStep.coerceIn(0, questions.size - 1)
    val question = questions[safeStep]
    val currentScore = answers[question.id] ?: 50

    val progress = (safeStep + 1).toFloat() / questions.size.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "quiz_progress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Top Navigation & Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.testTag("exit_blueprint_quiz_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textSecColor
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "MONEY BLUEPRINT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Question ${safeStep + 1} of ${questions.size}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimaryColor
                )
            }

            IconButton(
                onClick = onShowHistory,
                modifier = Modifier.testTag("blueprint_quiz_history_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "History",
                    tint = goldAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(if (isDark) SurfaceElevated else Color(0xFFE5DECE))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(GoldLinearGradient)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Category Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(GoldDark.copy(alpha = 0.35f))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Psychology,
                contentDescription = null,
                tint = goldAccent,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = question.categoryName.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = goldAccent,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Question Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("question_card_${question.id}")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = question.questionText,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 25.sp,
                    color = textPrimaryColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = question.contextHint,
                    fontSize = 12.sp,
                    color = textSecColor,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Score Calibration Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your Resonance:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textSecColor
                    )
                    Text(
                        text = "$currentScore%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            currentScore <= 25 -> SuccessGreen
                            currentScore <= 60 -> AmberAccent
                            else -> GoldLight
                        }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Slider(
                    value = currentScore.toFloat(),
                    onValueChange = { onAnswerChanged(question.id, it.toInt()) },
                    valueRange = 0f..100f,
                    steps = 3, // 0, 25, 50, 75, 100
                    colors = SliderDefaults.colors(
                        thumbColor = goldAccent,
                        activeTrackColor = GoldPrimary,
                        inactiveTrackColor = cardBorderColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("slider_question_${question.id}")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = question.lowLabel,
                        fontSize = 10.sp,
                        color = textSecColor
                    )
                    Text(
                        text = question.highLabel,
                        fontSize = 10.sp,
                        color = textSecColor
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "SELECT LEVEL:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option Preset Buttons (5 discrete choices)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    question.options.forEach { (label, value) ->
                        val isSelected = currentScore == value
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) GoldDark.copy(alpha = 0.3f) else (if (isDark) SurfaceElevated.copy(alpha = 0.5f) else Color(0xFFF9F7F2)),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) GoldPrimary else cardBorderColor.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAnswerChanged(question.id, value) }
                                .testTag("option_${question.id}_$value")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isSelected) 5.dp else 1.5.dp,
                                            color = if (isSelected) GoldPrimary else textSecColor,
                                            shape = CircleShape
                                        )
                                        .background(if (isSelected) RichBlack else Color.Transparent)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) goldAccent else textPrimaryColor
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Row (Prev / Next)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (safeStep > 0) {
                OutlinedButton(
                    onClick = onPrev,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("blueprint_quiz_prev_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = textSecColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PREVIOUS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textSecColor
                    )
                }
            }

            Button(
                onClick = onNext,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberAccent,
                    contentColor = RichBlack
                ),
                modifier = Modifier
                    .weight(if (safeStep > 0) 1.5f else 1f)
                    .height(48.dp)
                    .testTag("blueprint_quiz_next_button")
            ) {
                Text(
                    text = if (safeStep == questions.size - 1) "DIAGNOSE BLUEPRINT ⚡" else "NEXT QUESTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun MoneyBlueprintResultView(
    result: MoneyBlueprintResultEntity,
    onFinish: () -> Unit,
    onRetake: () -> Unit,
    onShowHistory: () -> Unit,
    onSaveToNotebook: () -> Unit,
    onNavigateToModule: (Int) -> Unit,
    onOpenMoneyMindsetJournal: () -> Unit,
    onOpenFearReframe: () -> Unit,
    onOpenMastermindChat: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textPrimaryColor = if (isDark) TextPrimary else Color(0xFF1E1B18)
    val textSecColor = if (isDark) TextSecondary else Color(0xFF635948)
    val cardBg = if (isDark) DarkCharcoal else Color.White
    val cardBorderColor = if (isDark) DarkBorder else Color(0xFFE2D6BC)

    val animatedLimitation by animateFloatAsState(
        targetValue = result.overallLimitationScore.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "animated_limitation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .testTag("blueprint_result_view")
    ) {
        // Diagnosis Badge
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(GoldDark.copy(alpha = 0.4f))
                    .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = goldAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MONEY BLUEPRINT DIAGNOSIS • +250 XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Large Ring & Score
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            LimitationScoreGauge(
                score = animatedLimitation.toInt(),
                primaryGold = GoldPrimary,
                goldLight = goldAccent
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Primary Limitation Blueprint Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("primary_pattern_card")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRIMARY SUBCONSCIOUS PATTERN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = GoldDark.copy(alpha = 0.3f),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = "HIGH IMPACT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = result.primaryPatternTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = textPrimaryColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = result.summaryInsight,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = textSecColor
                )
            }
        }

        // Secondary Limitation Blueprint Card (if applicable)
        if (result.secondaryPatternTitle.isNotBlank() && result.secondaryPatternTitle != result.primaryPatternTitle) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SECONDARY INFLUENCE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = textSecColor,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = result.secondaryPatternTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = textPrimaryColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 5-Axis Limiting Belief Breakdown
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "5-AXIS BELIEF CALIBRATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                LimitationCategoryBar(title = "Scarcity & Zero-Sum Mindset", score = result.scarcityScore)
                Spacer(modifier = Modifier.height(10.dp))
                LimitationCategoryBar(title = "Guilt Around Wealth & Deservedness", score = result.guiltScore)
                Spacer(modifier = Modifier.height(10.dp))
                LimitationCategoryBar(title = "Fear of Financial Loss & Failure", score = result.fearFailureScore)
                Spacer(modifier = Modifier.height(10.dp))
                LimitationCategoryBar(title = "Fear of Judgment & Social Rejection", score = result.fearJudgmentScore)
                Spacer(modifier = Modifier.height(10.dp))
                LimitationCategoryBar(title = "Self-Worth Tied to Money / Imposter", score = result.selfWorthScore)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recommended Masterclass Modules & Feature Protocol
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.School,
                        contentDescription = null,
                        tint = goldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RECOMMENDED TRANSMUTATION PATH",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Parse module recommendations
                val moduleIds = result.recommendedModuleIds.split(",").mapNotNull { it.trim().toIntOrNull() }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    moduleIds.forEach { modId ->
                        val (vaultName, principleName) = when (modId) {
                            0 -> "Vault 0" to "The Introduction & Master Key"
                            1 -> "Vault 1" to "Desire: The Starting Point of All Achievement"
                            2 -> "Vault 2" to "Faith: Visualizing & Believing in Attainment"
                            3 -> "Vault 3" to "Auto-Suggestion: The Medium for Influencing Subconscious"
                            4 -> "Vault 4" to "Specialized Knowledge: Personal Experiences or Observations"
                            5 -> "Vault 5" to "Imagination: The Workshop of the Mind"
                            6 -> "Vault 6" to "Organized Planning: The Crystallization of Desire into Action"
                            7 -> "Vault 7" to "Decision: The Mastery of Procrastination"
                            8 -> "Vault 8" to "Persistence: The Sustained Effort Necessary to Induce Faith"
                            9 -> "Vault 9" to "Power of the Master Mind: The Driving Force"
                            10 -> "Vault 10" to "Sex Transmutation: The Mystery of Creative Energy"
                            11 -> "Vault 11" to "The Subconscious Mind: The Connecting Link"
                            12 -> "Vault 12" to "The Brain: A Broadcasting and Receiving Station"
                            13 -> "Vault 13" to "The Sixth Sense: The Temple of Wisdom"
                            else -> "Vault $modId" to "Mastery Chapter $modId"
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDark) SurfaceElevated else Color(0xFFF7F5F0),
                            border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToModule(modId) }
                                .testTag("recommended_module_$modId")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = vaultName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = goldAccent
                                    )
                                    Text(
                                        text = principleName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textPrimaryColor
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open",
                                    tint = goldAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Recommended Feature Action Button
                val (featureTitle, featureAction) = when (result.recommendedFeatureKey) {
                    "money_mindset_journal" -> "Open Money Mindset Journal" to onOpenMoneyMindsetJournal
                    "fear_reframe" -> "Open Fear Reframe Protocol" to onOpenFearReframe
                    "mastermind_chat" -> "Consult Mastermind AI Counsel" to onOpenMastermindChat
                    else -> "Open Money Mindset Journal" to onOpenMoneyMindsetJournal
                }

                Button(
                    onClick = featureAction,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldDark,
                        contentColor = GoldLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("recommended_feature_action_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = featureTitle.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Action Pledge Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) GoldDark.copy(alpha = 0.25f) else Color(0xFFFAF3E5)
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "DAILY TRANSMUTATION PLEDGE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = result.actionPledge,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    lineHeight = 19.sp,
                    color = textPrimaryColor
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Retake Cadence Recommendation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "RETAKE SCHEDULE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Re-evaluate in 30-60 days to measure mental reprogramming.",
                    fontSize = 11.sp,
                    color = textSecColor
                )
            }

            OutlinedButton(
                onClick = onShowHistory,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                modifier = Modifier.testTag("result_view_history_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = textSecColor,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "HISTORY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = textSecColor
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons: Inscribe in Notebook & Return to Dashboard
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = onSaveToNotebook,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_blueprint_to_notebook_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.BookmarkBorder,
                    contentDescription = null,
                    tint = goldAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INSCRIBE IN SOVEREIGN NOTEBOOK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldAccent,
                    letterSpacing = 0.5.sp
                )
            }

            Button(
                onClick = onFinish,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmberAccent,
                    contentColor = RichBlack
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("finish_blueprint_button")
            ) {
                Text(
                    text = "SEAL & RETURN TO DASHBOARD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun LimitationScoreGauge(
    score: Int,
    primaryGold: Color,
    goldLight: Color
) {
    val isDark = LocalIsDarkTheme.current
    val arcColor = when {
        score < 35 -> SuccessGreen
        score < 65 -> AmberAccent
        else -> goldLight
    }
    val statusTitle = when {
        score < 35 -> "HIGH SOVEREIGN FLOW"
        score < 65 -> "MODERATE RESISTANCE"
        else -> "SIGNIFICANT CEILING"
    }

    Box(
        modifier = Modifier.size(170.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val strokeWidth = 12.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            val arcSize = Size(diameter, diameter)

            // Background Track
            drawArc(
                color = if (isDark) Color(0xFF22201D) else Color(0xFFE5DECE),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Progress
            val sweep = (score / 100f) * 260f
            drawArc(
                color = arcColor,
                startAngle = 140f,
                sweepAngle = sweep.coerceIn(0f, 260f),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$score%",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = if (isDark) TextPrimary else Color(0xFF1E1B18)
            )
            Text(
                text = statusTitle,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = arcColor,
                letterSpacing = 0.5.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LimitationCategoryBar(
    title: String,
    score: Int
) {
    val isDark = LocalIsDarkTheme.current
    val barColor = when {
        score < 35 -> SuccessGreen
        score < 65 -> AmberAccent
        else -> GoldPrimary
    }
    val textPrimaryColor = if (isDark) TextPrimary else Color(0xFF1E1B18)
    val textSecColor = if (isDark) TextSecondary else Color(0xFF635948)
    val trackBg = if (isDark) SurfaceElevated else Color(0xFFEBE6DC)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = textPrimaryColor
            )
            Text(
                text = "$score%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = barColor
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { (score / 100f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = trackBg
        )
    }
}
