package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrushedCard
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.TierBadgeChip
import com.example.ui.theme.AmberAccent
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
import com.example.ui.viewmodel.ScreenRoute

@Composable
fun LandingScreen(
    onStartAssessment: () -> Unit,
    onExploreVault0: () -> Unit,
    onEnterDashboard: () -> Unit
) {
    val windowInfo = LocalWindowSizeInfo.current

    if (windowInfo.isTabletOrFoldable) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("landing_screen_list")
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // --- HERO SECTION ---
            item {
                HeroCinematicSection(
                    onStartAssessment = onStartAssessment,
                    onExploreVault0 = onExploreVault0
                )
            }

            // --- 2-COLUMN FEATURE GRIDS ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        VaultsTeaserSection()
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        TierProgressionSection()
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        DailyRitualFrameworkSection()
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        HistoricalTitansProofSection()
                    }
                }
            }

            item {
                PlatformMetricsSection()
            }

            item {
                BottomCtaSection(
                    onStartAssessment = onStartAssessment,
                    onEnterDashboard = onEnterDashboard
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("landing_screen_list")
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Top Spacer
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // --- HERO SECTION ---
            item {
                HeroCinematicSection(
                    onStartAssessment = onStartAssessment,
                    onExploreVault0 = onExploreVault0
                )
            }

            // --- THE 13 VAULTS PATH (TEASER) ---
            item {
                VaultsTeaserSection()
            }

            // --- WHAT YOU'LL BECOME (TIER PROGRESSION) ---
            item {
                TierProgressionSection()
            }

            // --- THE DAILY RITUAL PROTOCOL ---
            item {
                DailyRitualFrameworkSection()
            }

            // --- TITANS PROOF SECTION ---
            item {
                HistoricalTitansProofSection()
            }

            // --- WEALTH MINDSET DATA & METRICS ---
            item {
                PlatformMetricsSection()
            }

            // --- BOTTOM CTA CARD ---
            item {
                BottomCtaSection(
                    onStartAssessment = onStartAssessment,
                    onEnterDashboard = onEnterDashboard
                )
            }

            // Bottom padding
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun HeroCinematicSection(
    onStartAssessment: () -> Unit,
    onExploreVault0: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aura")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sovereign Monk / Meditating Aura Visual Canvas
        Box(
            modifier = Modifier
                .size(170.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val baseRadius = size.width / 2.5f

                // Outer Glowing Aura Rings
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoldLight.copy(alpha = 0.35f), Color.Transparent),
                        center = center,
                        radius = baseRadius * pulse
                    ),
                    radius = baseRadius * pulse,
                    center = center
                )

                drawCircle(
                    color = GoldPrimary.copy(alpha = 0.25f),
                    radius = baseRadius * 0.85f,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )

                drawCircle(
                    color = AmberAccent.copy(alpha = 0.4f),
                    radius = baseRadius * 0.65f,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // Central Sovereign Emblem / Meditating Figure Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(GoldLinearGradient)
                    .border(2.dp, GoldLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Psychology,
                    contentDescription = "Sovereign Mind",
                    tint = RichBlack,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
            Text(
                text = "TRANSMUTATION OVER ACCUMULATION",
                color = GoldLight,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Master Your Mind.\nCommand Your Wealth.",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            textAlign = TextAlign.Center,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "A single-path, gamified transformation ritual built on the timeless wealth-mindset code of Think and Grow Rich — reframed for modern sovereign architects.",
            fontFamily = FontFamily.SansSerif,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Dual CTA Buttons
        Button(
            onClick = onStartAssessment,
            colors = ButtonDefaults.buttonColors(
                containerColor = AmberAccent,
                contentColor = RichBlack
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("start_assessment_cta")
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "START FREE MINDSET ASSESSMENT",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onExploreVault0,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldLight),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("explore_vault0_cta")
        ) {
            Icon(
                imageVector = Icons.Filled.LockOpen,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = GoldLight
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "EXPLORE VAULT 0 (FREE INITIATION)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun VaultsTeaserSection() {
    BrushedCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "THE 13 VAULTS",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = GoldLight
                )
                Text(
                    text = "One sequential path. Zero distractions.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            TierBadgeChip(tier = "Sovereign")
        }

        Spacer(modifier = Modifier.height(16.dp))

        val previewVaults = listOf(
            Triple("Vault 0 (Free)", "The First Vault", "Money / Wealth Context Baseline"),
            Triple("Vault 1", "The Ignition", "Definite Major Purpose & Obsession"),
            Triple("Vault 2", "Unshakeable Belief", "Autosuggestion & Mental Inducement"),
            Triple("Vault 3", "Mind Programming", "Daily Subconscious Mirror Command"),
            Triple("Vault 4-13", "The Sovereign Arsenal", "Mastermind, Transmutation & Sixth Sense")
        )

        previewVaults.forEach { (tag, title, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = desc,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun TierProgressionSection() {
    BrushedCard {
        Text(
            text = "IDENTITY EVOLUTION: 5 TIERS",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = GoldLight
        )
        Text(
            text = "Track your Wealth Mindset Score and unlock cosmetic profile flair.",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        val tiers = listOf(
            Pair("Novice", "0 – 499 XP • Establishing the baseline"),
            Pair("Builder", "500 – 1,499 XP • Crafting Definite Purpose"),
            Pair("Architect", "1,500 – 3,499 XP • Mastermind & Strategic leverage"),
            Pair("Sovereign", "3,500 – 6,999 XP • Transmuted energy & subconscious command"),
            Pair("Legacy", "7,000+ XP • Sixth Sense mastery & apex alignment")
        )

        tiers.forEach { (name, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = desc,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
                TierBadgeChip(tier = name)
            }
        }
    }
}

@Composable
private fun DailyRitualFrameworkSection() {
    BrushedCard {
        Text(
            text = "THE DAILY RITUAL FORMULA",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = GoldLight
        )
        Text(
            text = "Every module is engineered for daily compound transformation.",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        val steps = listOf(
            Triple("1. Cinematic Lecture", "12-18 min deep philosophical synthesis", Icons.Filled.WorkspacePremium),
            Triple("2. Manuscript Excerpt", "Original distilled principles & key takeaways", Icons.Filled.AutoAwesome),
            Triple("3. Action Worksheet", "3-step interactive template persisted to DB", Icons.Filled.TrendingUp),
            Triple("4. Sovereign Quest", "Real-world friction-breaking challenge", Icons.Filled.Shield),
            Triple("5. Notebook Reflection", "Deep evening subconscious programming", Icons.Filled.Psychology)
        )

        steps.forEach { (title, subtitle, icon) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoricalTitansProofSection() {
    BrushedCard {
        Text(
            text = "HISTORICAL TITANS PROOF",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = GoldLight
        )
        Text(
            text = "How the world's most dominant wealth builders applied these principles.",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        val titans = listOf(
            Pair("Andrew Carnegie", "Mastermind principle: Orchestrated 50+ steel specialists to create the world's first billion-dollar enterprise."),
            Pair("Thomas Edison", "Persistence & Subconscious: Tested 10,000 filaments without accepting temporary defeat as failure."),
            Pair("Henry Ford", "Definiteness of Decision: Held resolute unyielding decisions on the V8 engine despite expert opposition."),
            Pair("Modern Innovators", "Specialized knowledge & Transmutation: Channeling relentless obsessive drive into systemic breakthroughs.")
        )

        titans.forEach { (name, principle) ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .background(SurfaceElevated, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldLight
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = principle,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun PlatformMetricsSection() {
    BrushedCard {
        Text(
            text = "SOVEREIGN METRICS & DATA",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = GoldLight
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    text = "84.2%",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GoldLight
                )
                Text(
                    text = "Assessment Completion",
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    text = "+24.8",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AmberAccent
                )
                Text(
                    text = "Avg Mindset Score Lift",
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    text = "92.4%",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = GoldLight
                )
                Text(
                    text = "7-Day Streak Retention",
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun BottomCtaSection(
    onStartAssessment: () -> Unit,
    onEnterDashboard: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, GoldLight, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Diamond,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Begin Your Daily Transformation",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Take the 2-minute Mindset Assessment to receive your Wealth Score and unlock Vault 0 immediately.",
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartAssessment,
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
                    text = "START FREE ASSESSMENT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onEnterDashboard,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go Directly to Dashboard", fontSize = 12.sp)
            }
        }
    }
}
