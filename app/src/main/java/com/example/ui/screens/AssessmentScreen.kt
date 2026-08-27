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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.UserProfileEntity
import com.example.ui.components.BrushedCard
import com.example.ui.components.TierBadgeChip
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
import com.example.ui.viewmodel.MindsetAssessmentState

@Composable
fun AssessmentScreen(
    state: MindsetAssessmentState,
    userProfile: UserProfileEntity?,
    onUpdateDimension: (String, Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onFinishReveal: () -> Unit,
    onExploreVault0: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("assessment_screen")
    ) {
        if (state.isRevealingScore) {
            ScoreRevealView(
                userProfile = userProfile,
                onEnterDashboard = onFinishReveal,
                onExploreVault0 = onExploreVault0
            )
        } else {
            AssessmentQuizView(
                state = state,
                onUpdateDimension = onUpdateDimension,
                onNext = onNext,
                onPrev = onPrev
            )
        }
    }
}

@Composable
private fun AssessmentQuizView(
    state: MindsetAssessmentState,
    onUpdateDimension: (String, Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    val scrollState = rememberScrollState()

    val dimensions = listOf(
        AssessmentQuestionData(
            id = "belief",
            dimensionName = "1. UNCONVINCED vs UNSHAKEABLE BELIEF",
            principle = "Faith & Autosuggestion",
            question = "When you set an ambitious financial target, what percentage of your subconscious mind genuinely expects it to materialize without doubt?",
            lowLabel = "20% (Frequent self-doubt)",
            highLabel = "100% (Complete conviction)",
            options = listOf(
                Pair("I often second-guess whether I deserve or can reach great wealth.", 30),
                Pair("I believe it's possible with enough effort, but market doubt creeps in.", 60),
                Pair("I operate with certainty; setbacks are merely logistical adjustments.", 90)
            )
        ),
        AssessmentQuestionData(
            id = "discipline",
            dimensionName = "2. DAILY DISCIPLINE & NON-NEGOTIABLES",
            principle = "Organized Planning & Habit",
            question = "How consistently do you execute your high-leverage creative rituals before engaging with reactive distractions (email, social feeds, news)?",
            lowLabel = "Reactive & Inconsistent",
            highLabel = "Inviolable Sovereign Ritual",
            options = listOf(
                Pair("My schedule is frequently dictated by external demands and impulses.", 35),
                Pair("I have structured routines 3-4 days a week, but lose momentum on weekends.", 65),
                Pair("My morning focus block and daily decrees are non-negotiable every single day.", 95)
            )
        ),
        AssessmentQuestionData(
            id = "desire",
            dimensionName = "3. CLARITY OF DEFINITE DESIRE",
            principle = "Definiteness of Purpose",
            question = "Have you written down the exact monetary figure, the exact date, and the exact value you pledge to deliver in return?",
            lowLabel = "Vague Financial Hope",
            highLabel = "Definite Major Purpose (DMP)",
            options = listOf(
                Pair("I just want 'financial freedom' or 'plenty of money'.", 25),
                Pair("I have rough figures in mind, but haven't written the exact pledge.", 60),
                Pair("I have an exact, written Definite Major Purpose that I review daily.", 95)
            )
        ),
        AssessmentQuestionData(
            id = "persistence",
            dimensionName = "4. PERSISTENCE AGAINST FRICTION",
            principle = "The Long Game",
            question = "When an enterprise faces severe financial or logistical defeat, how long does it take for your resolve to rebound?",
            lowLabel = "Prolonged Paralysis",
            highLabel = "Instant Strategic Pivot",
            options = listOf(
                Pair("Major setbacks derail my focus for weeks or cause me to pivot entirely.", 30),
                Pair("I experience disappointment for a few days, then slowly rebuild.", 65),
                Pair("I treat temporary defeat as essential market data and recalibrate within hours.", 95)
            )
        ),
        AssessmentQuestionData(
            id = "identity",
            dimensionName = "5. FINANCIAL IDENTITY & CEILING",
            principle = "Subconscious Mind & Self-Image",
            question = "Deep within your subconscious self-image, what annual financial output feels naturally congruent with who you are right now?",
            lowLabel = "Scarcity / Subsistence",
            highLabel = "Sovereign Abundance",
            options = listOf(
                Pair("Struggling with imposter syndrome when quoting high prices or managing capital.", 30),
                Pair("Comfortable with steady middle-class or moderate high-income brackets.", 65),
                Pair("Completely aligned with commanding 7+ figure sovereign asset accumulation.", 95)
            )
        )
    )

    val currentQ = dimensions[state.currentStep]
    val currentValue = when (currentQ.id) {
        "belief" -> state.beliefScore
        "discipline" -> state.disciplineScore
        "desire" -> state.desireScore
        "persistence" -> state.persistenceScore
        "identity" -> state.identityScore
        else -> 50
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.currentStep > 0) {
                IconButton(onClick = onPrev) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldLight
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }

            Text(
                text = "QUESTION ${state.currentStep + 1} OF 5",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldPrimary
            )

            Text(
                text = "${((state.currentStep + 1) * 20)}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted
            )
        }

        // Progress bar
        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((state.currentStep + 1) / 5f)
                    .height(4.dp)
                    .background(GoldLinearGradient)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Question Card
        BrushedCard {
            Text(
                text = currentQ.dimensionName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Principle: ${currentQ.principle}",
                fontSize = 12.sp,
                color = GoldLight,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = currentQ.question,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Score Slider
            Text(
                text = "Current Calibration: $currentValue / 100",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight
            )

            Slider(
                value = currentValue.toFloat(),
                onValueChange = { onUpdateDimension(currentQ.id, it.toInt()) },
                valueRange = 10f..100f,
                steps = 8,
                colors = SliderDefaults.colors(
                    thumbColor = GoldLight,
                    activeTrackColor = GoldPrimary,
                    inactiveTrackColor = SurfaceElevated
                ),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = currentQ.lowLabel, fontSize = 10.sp, color = TextMuted)
                Text(text = currentQ.highLabel, fontSize = 10.sp, color = TextMuted)
            }
        }

        // Quick Preset Option Cards
        Text(
            text = "OR SELECT THE CLOSEST MATCH:",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.8.sp
        )

        currentQ.options.forEach { (optionText, optScore) ->
            val isSelected = currentValue == optScore
            Surface(
                color = if (isSelected) SurfaceElevated else DarkCharcoal,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) GoldPrimary else DarkBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUpdateDimension(currentQ.id, optScore) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, if (isSelected) GoldLight else TextMuted, CircleShape)
                            .background(if (isSelected) GoldPrimary else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = RichBlack,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = optionText,
                        fontSize = 12.sp,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next / Submit Button
        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = AmberAccent,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("assessment_next_button")
        ) {
            Text(
                text = if (state.currentStep == 4) "GENERATE WEALTH MINDSET SCORE" else "NEXT DIMENSION",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ScoreRevealView(
    userProfile: UserProfileEntity?,
    onEnterDashboard: () -> Unit,
    onExploreVault0: () -> Unit
) {
    val scrollState = rememberScrollState()
    val score = userProfile?.mindsetScore ?: 55
    val tier = userProfile?.tierName ?: "Builder"

    val animatedScore by animateFloatAsState(
        targetValue = score.toFloat(),
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "score_anim"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Text(
                text = "ASSESSMENT COMPLETE • +250 XP",
                color = GoldLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        Text(
            text = "Your Wealth Mindset Diagnosis",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        // Large Mindset Score Ring
        Box(
            modifier = Modifier
                .size(180.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 12.dp.toPx()
                val diameter = size.width - strokeWidth
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                val arcSize = Size(diameter, diameter)

                // Background Track
                drawArc(
                    color = SurfaceElevated,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Glowing Gold Score Arc
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(GoldDark, GoldPrimary, GoldLight, AmberBright)
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * (animatedScore / 100f),
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${animatedScore.toInt()}",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    color = GoldLight
                )
                Text(
                    text = "MINDSET SCORE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
            }
        }

        // Assigned Tier Card
        BrushedCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ASSIGNED TIER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$tier Tier",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GoldLight
                    )
                }
                TierBadgeChip(tier = tier)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = when (tier.lowercase()) {
                    "legacy" -> "Apex congruence achieved. You possess rare psychological alignment across all 5 wealth vectors."
                    "sovereign" -> "High sovereign command. Your daily discipline and autosuggestion are compounding rapidly."
                    "architect" -> "Strong strategic framework. Refining subconscious programming and mastermind synergy will unlock sovereign scale."
                    "builder" -> "Clear foundation established. Elimination of secondary distractions and definite purpose required."
                    else -> "Baseline calibration. Commencing Vault 0 will systematically rewire scarcity patterns."
                },
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = TextSecondary
            )
        }

        // 5-Dimension Diagnostic Breakdown
        BrushedCard {
            Text(
                text = "5-AXIS DIAGNOSTIC BREAKDOWN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            val breakdown = listOf(
                Pair("Belief & Conviction (Faith)", userProfile?.beliefScore ?: 50),
                Pair("Daily Discipline & Ritual", userProfile?.disciplineScore ?: 50),
                Pair("Clarity of Definite Desire", userProfile?.desireScore ?: 50),
                Pair("Persistence Against Defeat", userProfile?.persistenceScore ?: 50),
                Pair("Financial Self-Image & Ceiling", userProfile?.identityScore ?: 50)
            )

            breakdown.forEach { (axis, valScore) ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = axis, fontSize = 12.sp, color = TextPrimary)
                        Text(text = "$valScore / 100", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AmberAccent)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(valScore / 100f)
                                .height(6.dp)
                                .background(GoldLinearGradient)
                        )
                    }
                }
            }
        }

        // Next Action Buttons
        Button(
            onClick = onExploreVault0,
            colors = ButtonDefaults.buttonColors(
                containerColor = AmberAccent,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("enter_vault0_button")
        ) {
            Icon(
                imageVector = Icons.Filled.LockOpen,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ENTER VAULT 0: THE FIRST VAULT (FREE)",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp
            )
        }

        OutlinedButton(
            onClick = onEnterDashboard,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("enter_dashboard_button")
        ) {
            Text(
                text = "PROCEED TO SOVEREIGN DASHBOARD",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private data class AssessmentQuestionData(
    val id: String,
    val dimensionName: String,
    val principle: String,
    val question: String,
    val lowLabel: String,
    val highLabel: String,
    val options: List<Pair<String, Int>>
)
