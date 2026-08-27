package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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
import com.example.ui.theme.GoldAura
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldMetallic
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
import com.example.ui.viewmodel.MindsetAssessmentState
import kotlin.math.cos
import kotlin.math.sin

private const val TOTAL_ONBOARDING_STEPS = 5

@Composable
fun OnboardingScreen(
    userProfile: UserProfileEntity?,
    assessmentState: MindsetAssessmentState,
    onSaveStep: (Int) -> Unit,
    onSaveName: (String) -> Unit,
    onSaveChiefAim: (String) -> Unit,
    onUpdateAssessmentDimension: (String, Int) -> Unit,
    onNextAssessmentStep: () -> Unit,
    onPrevAssessmentStep: () -> Unit,
    onFinishOnboarding: () -> Unit
) {
    // Determine the active step (1 to 5), resuming from where the user left off
    val initialStep = (userProfile?.onboardingStep ?: 1).coerceIn(1, TOTAL_ONBOARDING_STEPS)
    var currentStep by remember(userProfile?.onboardingStep) { mutableIntStateOf(initialStep) }

    // Local form state synced with user profile
    var inputName by remember(userProfile?.name) {
        mutableStateOf(userProfile?.name?.takeIf { it != "Sovereign Initiate" } ?: "")
    }
    var inputChiefAim by remember(userProfile?.definiteChiefAim) {
        mutableStateOf(userProfile?.definiteChiefAim.orEmpty())
    }

    // When step changes, persist to repository
    fun navigateToStep(step: Int) {
        val bounded = step.coerceIn(1, TOTAL_ONBOARDING_STEPS)
        currentStep = bounded
        onSaveStep(bounded)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RichBlack)
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp)
        ) {
            // --- TOP PROGRESS & STEPPER HEADER ---
            OnboardingTopHeader(
                currentStep = currentStep,
                totalSteps = TOTAL_ONBOARDING_STEPS,
                canGoBack = currentStep > 1,
                onBack = {
                    if (currentStep > 1) {
                        navigateToStep(currentStep - 1)
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- STEP CONTENT CONTAINER ---
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally(tween(350)) { fullWidth -> fullWidth / 2 } + fadeIn(tween(300)))
                                .togetherWith(slideOutHorizontally(tween(250)) { fullWidth -> -fullWidth / 3 } + fadeOut(tween(200)))
                        } else {
                            (slideInHorizontally(tween(350)) { fullWidth -> -fullWidth / 2 } + fadeIn(tween(300)))
                                .togetherWith(slideOutHorizontally(tween(250)) { fullWidth -> fullWidth / 3 } + fadeOut(tween(200)))
                        }
                    },
                    label = "onboarding_step_transition"
                ) { step ->
                    when (step) {
                        1 -> OnboardingWelcomeStep(
                            onProceed = {
                                navigateToStep(2)
                            }
                        )

                        2 -> OnboardingNameStep(
                            name = inputName,
                            onNameChange = { inputName = it },
                            onProceed = {
                                val trimmed = inputName.trim()
                                if (trimmed.isNotBlank()) {
                                    onSaveName(trimmed)
                                    navigateToStep(3)
                                }
                            }
                        )

                        3 -> OnboardingChiefAimStep(
                            aim = inputChiefAim,
                            onAimChange = { inputChiefAim = it },
                            onProceed = {
                                val trimmed = inputChiefAim.trim()
                                if (trimmed.isNotBlank()) {
                                    onSaveChiefAim(trimmed)
                                    navigateToStep(4)
                                }
                            }
                        )

                        4 -> OnboardingMindsetStep(
                            state = assessmentState,
                            userProfile = userProfile,
                            onUpdateDimension = onUpdateAssessmentDimension,
                            onNext = onNextAssessmentStep,
                            onPrev = onPrevAssessmentStep,
                            onProceedToPreview = {
                                navigateToStep(5)
                            }
                        )

                        5 -> OnboardingUnlockPreviewStep(
                            userProfile = userProfile,
                            onEnterDashboard = onFinishOnboarding
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// STEPPER TOP HEADER
// -----------------------------------------------------------------------------

@Composable
private fun OnboardingTopHeader(
    currentStep: Int,
    totalSteps: Int,
    canGoBack: Boolean,
    onBack: () -> Unit
) {
    val stepTitles = listOf(
        "The Sacred Calling",
        "Sovereign Identity",
        "Definite Chief Aim",
        "Mindset Assessment",
        "Sovereign Arsenal"
    )
    val stepTitle = stepTitles.getOrElse(currentStep - 1) { "Initiation" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (canGoBack) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("onboarding_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Step",
                        tint = GoldLight
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "STEP $currentStep OF $totalSteps",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = GoldPrimary
                )
                Text(
                    text = stepTitle.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
            }

            Text(
                text = "${(currentStep * 100) / totalSteps}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MutedGold,
                modifier = Modifier.padding(end = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 5-Segmented Gold Progress Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in 1..totalSteps) {
                val isCompleted = i < currentStep
                val isCurrent = i == currentStep
                val animatedProgress by animateFloatAsState(
                    targetValue = if (isCompleted || isCurrent) 1f else 0f,
                    animationSpec = tween(400, easing = FastOutSlowInEasing),
                    label = "segment_$i"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(DarkCharcoal)
                ) {
                    if (animatedProgress > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress)
                                .height(5.dp)
                                .background(
                                    if (isCurrent) {
                                        Brush.horizontalGradient(listOf(GoldDark, GoldLight))
                                    } else {
                                        Brush.horizontalGradient(listOf(GoldPrimary, AmberAccent))
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// STEP 1: WELCOME SCREEN (MONK MOTIF & PURPOSE)
// -----------------------------------------------------------------------------

@Composable
private fun OnboardingWelcomeStep(
    onProceed: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("onboarding_welcome_step"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Monk Motif Visual Medallion
        MonkSacredEmblem(modifier = Modifier.size(190.dp))

        // Title & Mission Statement
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "THINK & GROW RICH",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = GoldPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "The Sovereign Wealth Protocol",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Transmute your burning desire into financial sovereignty through the 13 immutable principles of Napoleon Hill. A sacred daily practice of unwavering purpose, autosuggestion, and Master Mind alliance.",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // 3 Key Philosophy Pillars
        BrushedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "THE THREE PILLARS OF SOVEREIGNTY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = GoldLight
            )
            Spacer(modifier = Modifier.height(10.dp))

            PillarRow(
                icon = Icons.Filled.Diamond,
                title = "Definite Chief Aim",
                desc = "Declare an exact target, date, and value pledge with zero ambiguity."
            )
            Spacer(modifier = Modifier.height(8.dp))
            PillarRow(
                icon = Icons.Filled.RecordVoiceOver,
                title = "Subconscious Autosuggestion",
                desc = "Daily morning & evening spoken decrees that rewire your financial ceiling."
            )
            Spacer(modifier = Modifier.height(8.dp))
            PillarRow(
                icon = Icons.Filled.Psychology,
                title = "Napoleon Hill AI Mastermind",
                desc = "Direct counsel from history's wealth titans powered by Gemini intelligence."
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Action Button
        Button(
            onClick = onProceed,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_welcome_proceed_button")
        ) {
            Text(
                text = "BEGIN YOUR INITIATION →",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun PillarRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = GoldPrimary.copy(alpha = 0.12f),
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
            modifier = Modifier.size(34.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GoldLight,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = TextSecondary
            )
        }
    }
}

// -----------------------------------------------------------------------------
// STEP 2: NAME ENTRY (SOVEREIGN IDENTITY)
// -----------------------------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OnboardingNameStep(
    name: String,
    onNameChange: (String) -> Unit,
    onProceed: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isValid = name.trim().isNotBlank()

    val suggestions = listOf("Sovereign Initiate", "Architect of Destiny", "Titan", "Mastermind")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("onboarding_name_step"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Avatar Medallion
        Surface(
            color = NightBlack,
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(2.dp, GoldPrimary),
            shadowElevation = 8.dp,
            modifier = Modifier.size(90.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(GoldPrimary.copy(alpha = 0.2f), PureBlack)
                        )
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Identity",
                    tint = GoldLight,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "SOVEREIGN IDENTITY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = GoldPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "How shall the Master Mind address you?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Every great fortune begins with the declaration of identity. Inscribe your name or sovereign moniker.",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }

        BrushedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Your Full Name / Display Moniker", color = TextMuted) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = GoldPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_name_input")
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "QUICK TITLES",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                suggestions.forEach { title ->
                    Surface(
                        color = if (name == title) GoldPrimary.copy(alpha = 0.2f) else DarkCharcoal,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (name == title) GoldPrimary else DarkBorder
                        ),
                        modifier = Modifier.clickable { onNameChange(title) }
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            color = if (name == title) GoldLight else TextSecondary,
                            fontWeight = if (name == title) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onProceed,
            enabled = isValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack,
                disabledContainerColor = DarkCharcoal,
                disabledContentColor = TextMuted
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_name_proceed_button")
        ) {
            Text(
                text = "CONFIRM IDENTITY (Step 2 of 5) →",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// -----------------------------------------------------------------------------
// STEP 3: DEFINITE CHIEF AIM (DMP & AFFIRMATION DECREE)
// -----------------------------------------------------------------------------

@Composable
private fun OnboardingChiefAimStep(
    aim: String,
    onAimChange: (String) -> Unit,
    onProceed: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isValid = aim.trim().length >= 8

    val templates = listOf(
        "By December 31, I will possess $500,000 in liquid capital, in return for which I will deliver the highest quality software and technology leadership.",
        "By next year, I will build an unshakeable $1,000,000 enterprise by solving critical problems with relentless persistence and integrity.",
        "I will master sovereign wealth creation and accumulate $250,000 in cash flow by providing unmatched client value and disciplined daily execution."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("onboarding_chief_aim_step"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "PRINCIPLE 1: DEFINITENESS OF PURPOSE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = GoldPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Inscribe Your Definite Chief Aim",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "This single statement will automatically become your Daily Affirmation Decree for daily morning and evening recitations.",
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Napoleon Hill Quote Card
        Surface(
            color = NightBlack,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatQuote,
                    contentDescription = null,
                    tint = AmberAccent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "\"There is one quality which one must possess to win, and that is definiteness of purpose, the knowledge of what one wants, and a burning desire to possess it.\"",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = GoldChampagne,
                    lineHeight = 17.sp
                )
            }
        }

        // Multi-line Input Box
        BrushedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "YOUR DEFINITE MAJOR STATEMENT",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldLight
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = aim,
                onValueChange = onAimChange,
                placeholder = {
                    Text(
                        text = "By [Date], I will accumulate [Target Amount/Asset] in exchange for delivering [Exact Service/Value]...",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                },
                minLines = 3,
                maxLines = 6,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldPrimary,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = GoldPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_chief_aim_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "CHOOSE A STARTER BLUEPRINT:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(6.dp))

            templates.forEach { t ->
                Surface(
                    color = DarkCharcoal,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable { onAimChange(t) }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = t,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onProceed,
            enabled = isValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack,
                disabledContainerColor = DarkCharcoal,
                disabledContentColor = TextMuted
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_chief_aim_proceed_button")
        ) {
            Text(
                text = "SEAL CHIEF AIM (Step 3 of 5) →",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// -----------------------------------------------------------------------------
// STEP 4: FIRST MINDSET ASSESSMENT
// -----------------------------------------------------------------------------

@Composable
private fun OnboardingMindsetStep(
    state: MindsetAssessmentState,
    userProfile: UserProfileEntity?,
    onUpdateDimension: (String, Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onProceedToPreview: () -> Unit
) {
    if (state.isRevealingScore) {
        OnboardingScoreRevealView(
            userProfile = userProfile,
            onProceed = onProceedToPreview
        )
    } else {
        OnboardingMindsetQuizView(
            state = state,
            onUpdateDimension = onUpdateDimension,
            onNext = onNext,
            onPrev = onPrev
        )
    }
}

@Composable
private fun OnboardingMindsetQuizView(
    state: MindsetAssessmentState,
    onUpdateDimension: (String, Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    val scrollState = rememberScrollState()

    val questions = listOf(
        OnboardingQuestionData(
            id = "belief",
            dimensionName = "1. FAITH & BELIEF CERTAINTY",
            principle = "Faith & Autosuggestion",
            question = "When you set an ambitious financial target, what percentage of your subconscious mind genuinely expects it to materialize without doubt?",
            lowLabel = "Frequent Self-Doubt",
            highLabel = "Absolute Conviction",
            options = listOf(
                Pair("I often second-guess whether I deserve or can reach great wealth.", 30),
                Pair("I believe it's possible with enough effort, but market doubt creeps in.", 60),
                Pair("I operate with certainty; setbacks are merely logistical adjustments.", 90)
            )
        ),
        OnboardingQuestionData(
            id = "discipline",
            dimensionName = "2. DAILY DISCIPLINE & NON-NEGOTIABLES",
            principle = "Organized Planning & Habit",
            question = "How consistently do you execute your high-leverage creative rituals before engaging with reactive distractions (email, social feeds)?",
            lowLabel = "Reactive & Inconsistent",
            highLabel = "Inviolable Daily Ritual",
            options = listOf(
                Pair("My schedule is frequently dictated by external demands and impulses.", 35),
                Pair("I have structured routines 3-4 days a week, but lose momentum.", 65),
                Pair("My morning focus block and daily decrees are non-negotiable every single day.", 95)
            )
        ),
        OnboardingQuestionData(
            id = "desire",
            dimensionName = "3. CLARITY OF DEFINITE DESIRE",
            principle = "Definiteness of Purpose",
            question = "Have you written down the exact monetary figure, the exact date, and the exact value you pledge to deliver in return?",
            lowLabel = "Vague Financial Hope",
            highLabel = "Definite Major Purpose (DMP)",
            options = listOf(
                Pair("I just want general financial freedom or more money.", 25),
                Pair("I have rough figures in mind, but haven't finalized the exact pledge.", 60),
                Pair("I have an exact, written Definite Major Purpose that I review daily.", 95)
            )
        ),
        OnboardingQuestionData(
            id = "persistence",
            dimensionName = "4. PERSISTENCE AGAINST FRICTION",
            principle = "The Long Game",
            question = "When an enterprise faces severe financial or logistical defeat, how long does it take for your resolve to rebound?",
            lowLabel = "Prolonged Paralysis",
            highLabel = "Instant Strategic Pivot",
            options = listOf(
                Pair("Major setbacks derail my focus for weeks or cause me to quit.", 30),
                Pair("I experience disappointment for a few days, then slowly rebuild.", 65),
                Pair("I treat temporary defeat as essential market data and recalibrate immediately.", 95)
            )
        ),
        OnboardingQuestionData(
            id = "identity",
            dimensionName = "5. FINANCIAL IDENTITY & CEILING",
            principle = "Subconscious Self-Image",
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

    val currentQ = questions[state.currentStep.coerceIn(0, 4)]
    val currentScore = when (currentQ.id) {
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
            .verticalScroll(scrollState)
            .testTag("onboarding_mindset_quiz"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Question subheader
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (state.currentStep > 0) {
                IconButton(onClick = onPrev, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Question",
                        tint = GoldLight
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(36.dp))
            }

            Text(
                text = "DIMENSION ${state.currentStep + 1} OF 5",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldPrimary
            )

            Text(
                text = "${(state.currentStep + 1) * 20}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MutedGold
            )
        }

        // Active Question Card
        BrushedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = GoldPrimary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = currentQ.principle.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = currentQ.dimensionName,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldLight
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = currentQ.question,
                fontSize = 13.5.sp,
                lineHeight = 19.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SELECT YOUR CURRENT REALITY:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(8.dp))

            currentQ.options.forEach { (label, value) ->
                val isSelected = currentScore == value
                Surface(
                    color = if (isSelected) GoldPrimary.copy(alpha = 0.18f) else DarkCharcoal,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isSelected) GoldPrimary else DarkBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onUpdateDimension(currentQ.id, value) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) GoldPrimary else DarkCharcoal)
                                .border(1.dp, if (isSelected) GoldLight else TextMuted, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(RichBlack)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = label,
                            fontSize = 12.5.sp,
                            lineHeight = 16.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) GoldLight else TextSecondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Next Question or Complete Button
        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_mindset_next_button")
        ) {
            Text(
                text = if (state.currentStep == 4) "CALCULATE MINDSET PROTOCOL →" else "NEXT DIMENSION (${state.currentStep + 1}/5) →",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun OnboardingScoreRevealView(
    userProfile: UserProfileEntity?,
    onProceed: () -> Unit
) {
    val scrollState = rememberScrollState()
    val score = userProfile?.mindsetScore ?: 70
    val tier = userProfile?.tierName ?: "Builder"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("onboarding_score_reveal"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = SuccessGreen.copy(alpha = 0.15f),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MINDSET CALIBRATED (+250 XP)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }
        }

        // Radial Score Display
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = (size.minDimension / 2f) - 14.dp.toPx()

                // Background track
                drawCircle(
                    color = DarkCharcoal,
                    radius = radius,
                    center = center,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )

                // Foreground progress
                val sweep = (score / 100f) * 360f
                drawArc(
                    brush = Brush.sweepGradient(listOf(GoldDark, GoldLight, AmberAccent, GoldPrimary)),
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$score",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldLight,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "SOVEREIGN SCORE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
            }
        }

        TierBadgeChip(tier = tier)

        Text(
            text = "Your initial baseline is established. As you complete daily Vault lessons, morning decrees, and notebook reflections, your score and tier will progress towards Legacy Sovereign.",
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        // 5 Dimensions Mini Bars
        BrushedCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "DIMENSIONAL BREAKDOWN",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = GoldLight
            )
            Spacer(modifier = Modifier.height(10.dp))

            DimensionBarRow(name = "Belief & Faith", value = userProfile?.beliefScore ?: 50)
            Spacer(modifier = Modifier.height(6.dp))
            DimensionBarRow(name = "Daily Discipline", value = userProfile?.disciplineScore ?: 50)
            Spacer(modifier = Modifier.height(6.dp))
            DimensionBarRow(name = "Clarity of Desire", value = userProfile?.desireScore ?: 50)
            Spacer(modifier = Modifier.height(6.dp))
            DimensionBarRow(name = "Persistence Index", value = userProfile?.persistenceScore ?: 50)
            Spacer(modifier = Modifier.height(6.dp))
            DimensionBarRow(name = "Identity Ceiling", value = userProfile?.identityScore ?: 50)
        }

        Button(
            onClick = onProceed,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_score_proceed_button")
        ) {
            Text(
                text = "CONTINUE TO ARSENAL PREVIEW (Step 4 of 5) →",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun DimensionBarRow(name: String, value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.width(110.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(DarkCharcoal)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value / 100f)
                    .height(6.dp)
                    .background(Brush.horizontalGradient(listOf(GoldDark, GoldLight)))
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "$value%",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = GoldLight,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.End
        )
    }
}

// -----------------------------------------------------------------------------
// STEP 5: "HERE'S WHAT YOU'LL UNLOCK" FEATURE PREVIEW
// -----------------------------------------------------------------------------

@Composable
private fun OnboardingUnlockPreviewStep(
    userProfile: UserProfileEntity?,
    onEnterDashboard: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .testTag("onboarding_unlock_preview_step"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            color = GoldPrimary.copy(alpha = 0.15f),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LockOpen,
                    contentDescription = null,
                    tint = GoldLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "INITIATION COMPLETE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Your Sovereign Arsenal is Prepared",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = GoldLight,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Here is what has been configured and unlocked for your daily protocol:",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // 4 Key Feature Cards
        FeatureUnlockCard(
            icon = Icons.Filled.RecordVoiceOver,
            title = "Daily Affirmation & Decree",
            badge = "ACTIVE & POPULATED",
            oneLiner = "Your Definite Chief Aim is loaded into the daily morning & evening spoken decree engine with audio recitation."
        )

        FeatureUnlockCard(
            icon = Icons.Filled.MenuBook,
            title = "Sovereign Transmutation Notebook",
            badge = "READY",
            oneLiner = "Reflective journaling, idea capture, and exportable PDF codex directly saved to local SQLite ledger."
        )

        FeatureUnlockCard(
            icon = Icons.Filled.Psychology,
            title = "Napoleon Hill AI Mastermind",
            badge = "6 TITANS UNLOCKED",
            oneLiner = "Engage in direct strategic dialogues with simulated advisors (Napoleon Hill, Andrew Carnegie, Marcus Aurelius)."
        )

        FeatureUnlockCard(
            icon = Icons.Filled.Diamond,
            title = "13 Principle Vaults & Income Blueprints",
            badge = "14 VAULTS AVAILABLE",
            oneLiner = "Structured chapters, audio manuscripts, interactive worksheets, and curated high-leverage wealth blueprints."
        )

        // Account Profile Snapshot Chip
        BrushedCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SOVEREIGN CREDENTIALS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = GoldLight
                    )
                    Text(
                        text = userProfile?.name ?: "Sovereign Initiate",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                TierBadgeChip(tier = userProfile?.tierName ?: "Builder")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Grand CTA Button
        Button(
            onClick = onEnterDashboard,
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("onboarding_enter_dashboard_button")
        ) {
            Text(
                text = "ENTER SOVEREIGN DASHBOARD →",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FeatureUnlockCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    badge: String,
    oneLiner: String
) {
    Surface(
        color = NightBlack,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = GoldPrimary.copy(alpha = 0.15f),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        color = GoldPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = oneLiner,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SACRED MONK VISUAL EMBLEM
// -----------------------------------------------------------------------------

@Composable
private fun MonkSacredEmblem(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "monk_aura")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sunburst_rotation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Rotating Sacred Sunburst Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.65f * pulseGlow }
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            rotate(rotation, center) {
                val rayCount = 24
                for (i in 0 until rayCount) {
                    val angle = (i * 360f / rayCount) * (Math.PI / 180f)
                    val startX = center.x + (radius * 0.45f) * cos(angle).toFloat()
                    val startY = center.y + (radius * 0.45f) * sin(angle).toFloat()
                    val endX = center.x + (radius * 0.95f) * cos(angle).toFloat()
                    val endY = center.y + (radius * 0.95f) * sin(angle).toFloat()

                    drawLine(
                        brush = Brush.linearGradient(
                            listOf(GoldPrimary.copy(alpha = 0.7f), Color.Transparent),
                            start = Offset(startX, startY),
                            end = Offset(endX, endY)
                        ),
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = if (i % 2 == 0) 2.5.dp.toPx() else 1.2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Concentric Sacred Rings
            drawCircle(
                color = GoldPrimary.copy(alpha = 0.4f),
                radius = radius * 0.85f,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = GoldChampagne.copy(alpha = 0.6f),
                radius = radius * 0.55f,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Central Monk Contemplation Medallion
        Surface(
            color = NightBlack,
            shape = CircleShape,
            border = androidx.compose.foundation.BorderStroke(2.dp, GoldPrimary),
            shadowElevation = 12.dp,
            modifier = Modifier.size(96.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF2A200B),
                                NightBlack,
                                PureBlack
                            )
                        )
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.SelfImprovement,
                    contentDescription = "Monk Contemplation",
                    tint = GoldLight,
                    modifier = Modifier.size(54.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// DATA HELPER
// -----------------------------------------------------------------------------

private data class OnboardingQuestionData(
    val id: String,
    val dimensionName: String,
    val principle: String,
    val question: String,
    val lowLabel: String,
    val highLabel: String,
    val options: List<Pair<String, Int>>
)
