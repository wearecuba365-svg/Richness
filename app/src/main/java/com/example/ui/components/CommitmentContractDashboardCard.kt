package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommitmentContractEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.LocalIsDarkTheme

@Composable
fun CommitmentContractDashboardCard(
    contract: CommitmentContractEntity?,
    userProfile: UserProfileEntity?,
    onNavigateToContractScreen: () -> Unit,
    onCreateContract: () -> Unit,
    onUpdateProgress: (Long, Int) -> Unit,
    onCompleteContract: (CommitmentContractEntity) -> Unit,
    onRenewContract: (CommitmentContractEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val now = remember { System.currentTimeMillis() }

    if (contract == null) {
        // Empty State: Prompt user to formalize their Definite Chief Aim or create a commitment
        EmptyCommitmentDashboardCard(
            userProfile = userProfile,
            onCreateContract = onCreateContract,
            onViewHistory = onNavigateToContractScreen,
            modifier = modifier
        )
    } else {
        // Active Commitment Contract State
        ActiveCommitmentDashboardCard(
            contract = contract,
            userProfile = userProfile,
            now = now,
            onNavigateToContractScreen = onNavigateToContractScreen,
            onUpdateProgress = onUpdateProgress,
            onCompleteContract = onCompleteContract,
            onRenewContract = onRenewContract,
            modifier = modifier
        )
    }
}

@Composable
private fun ActiveCommitmentDashboardCard(
    contract: CommitmentContractEntity,
    userProfile: UserProfileEntity?,
    now: Long,
    onNavigateToContractScreen: () -> Unit,
    onUpdateProgress: (Long, Int) -> Unit,
    onCompleteContract: (CommitmentContractEntity) -> Unit,
    onRenewContract: (CommitmentContractEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val isDeadlineReached = contract.isDeadlineReached(now)
    val daysRemaining = contract.getDaysRemaining(now)

    var sliderProgress by remember(contract.id, contract.progressPercent) {
        mutableFloatStateOf(contract.progressPercent.toFloat())
    }
    var isEditingProgress by remember { mutableStateOf(false) }

    // Pulsing warning border if deadline reached
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val cardBorder = if (isDeadlineReached) {
        BorderStrokeModifier(1.5.dp, AmberBright.copy(alpha = pulseAlpha))
    } else {
        BorderStrokeModifier(1.dp, GoldPrimary.copy(alpha = 0.4f))
    }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_commitment_contract_card"),
        onClick = onNavigateToContractScreen,
        borderColor = if (isDeadlineReached) AmberBright.copy(alpha = pulseAlpha) else GoldPrimary.copy(alpha = 0.45f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Category Pill, Countdown Badge, and Full Screen Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📜",
                            fontSize = 14.sp
                        )
                    }

                    Column {
                        Text(
                            text = "COMMITMENT CONTRACT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Definite Chief Aim Covenant",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Countdown Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDeadlineReached) AmberAccent.copy(alpha = 0.2f) else GoldDark.copy(alpha = 0.35f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDeadlineReached) AmberBright.copy(alpha = 0.8f) else GoldPrimary.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isDeadlineReached) Icons.Default.Warning else Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = if (isDeadlineReached) AmberBright else GoldLight,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (isDeadlineReached) "DEADLINE REACHED" else {
                                if (daysRemaining == 0L) "DUE TODAY" else "$daysRemaining DAYS LEFT"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDeadlineReached) AmberBright else GoldLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Goal Statement in prominent display font
            Text(
                text = "“${contract.goalStatement}”",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                color = if (isDark) TextPrimary else RichBlack,
                lineHeight = 22.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (contract.whyItMatters.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Purpose: ${contract.whyItMatters}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Deadline date banner & progress percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Target: ${contract.getFormattedDeadline()}",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Progress:",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "${contract.progressPercent}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isDark) DarkCharcoal else Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = (contract.progressPercent / 100f).coerceIn(0f, 1f))
                        .height(8.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(GoldDark, GoldPrimary, GoldLight)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Progress Adjuster or Resolution Actions
            if (isDeadlineReached) {
                // Resolution Banner with Required Complete or Renew Actions
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = AmberAccent.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberBright.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "⚡ Deadline Arrived — Explicit Resolution Required",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberBright
                        )
                        Text(
                            text = "Did you fulfill this commitment, or are you calibrating the timeline to renew your covenant?",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onCompleteContract(contract) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldPrimary,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dashboard_complete_contract_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Complete (+150 XP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { onRenewContract(contract) },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = GoldLight
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.7f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dashboard_renew_contract_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Autorenew,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Renew (+50 XP)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                // Standard in-progress controls
                AnimatedVisibility(visible = isEditingProgress) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Adjust Manifestation Progress: ${sliderProgress.toInt()}%",
                                fontSize = 11.sp,
                                color = GoldPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Button(
                                onClick = {
                                    onUpdateProgress(contract.id, sliderProgress.toInt())
                                    isEditingProgress = false
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldPrimary,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Save", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Slider(
                            value = sliderProgress,
                            onValueChange = { sliderProgress = it },
                            valueRange = 0f..100f,
                            steps = 19, // 5% increments
                            colors = SliderDefaults.colors(
                                thumbColor = GoldPrimary,
                                activeTrackColor = GoldPrimary,
                                inactiveTrackColor = DarkCharcoal
                            ),
                            modifier = Modifier.testTag("dashboard_contract_progress_slider")
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { isEditingProgress = !isEditingProgress },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = GoldLight
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("dashboard_edit_progress_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isEditingProgress) "Close Slider" else "Update Progress",
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onNavigateToContractScreen() }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "View Formal Covenant",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyCommitmentDashboardCard(
    userProfile: UserProfileEntity?,
    onCreateContract: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val chiefAim = userProfile?.definiteChiefAim?.trim().orEmpty()
    val hasChiefAim = chiefAim.isNotBlank()

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dashboard_empty_commitment_contract_card"),
        borderColor = GoldPrimary.copy(alpha = 0.35f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = 0.15f))
                            .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📜", fontSize = 14.sp)
                    }

                    Column {
                        Text(
                            text = "COMMITMENT CONTRACT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = GoldPrimary,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Formalize Definite Chief Aim",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = GoldDark.copy(alpha = 0.25f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onViewHistory() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Archive",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (hasChiefAim) {
                Text(
                    text = "“$chiefAim”",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) TextPrimary else RichBlack,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Seal this Definite Chief Aim as an irrevocable formal covenant with a target deadline and accountability tracking.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            } else {
                Text(
                    text = "Turn your Definite Chief Aim into an unbreakable formal covenant with a clear target deadline, stakes, and progress tracking.",
                    fontSize = 13.sp,
                    color = if (isDark) TextPrimary else RichBlack,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onCreateContract,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = RichBlack
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_seal_contract_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (hasChiefAim) "Seal Chief Aim as Contract (+75 XP)" else "Draft Commitment Contract (+75 XP)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Helper class for conditional border
private data class BorderStrokeModifier(
    val width: androidx.compose.ui.unit.Dp,
    val color: Color
)
