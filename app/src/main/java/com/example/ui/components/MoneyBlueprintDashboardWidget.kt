package com.example.ui.components

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.data.repository.RichesRepository
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MoneyBlueprintDashboardWidget(
    userProfile: UserProfileEntity?,
    onOpenQuiz: () -> Unit,
    onRetakeQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textPrimaryColor = if (isDark) TextPrimary else Color(0xFF1E1B18)
    val textSecColor = if (isDark) TextSecondary else Color(0xFF635948)
    val cardBg = if (isDark) DarkCharcoal else Color.White
    val cardBorderColor = if (isDark) DarkBorder else Color(0xFFE2D6BC)

    val pattern = userProfile?.primaryBlueprintPattern
    val hasDiagnosis = !pattern.isNullOrBlank()
    val todayEpoch = RichesRepository.getTodayEpochDay()
    val lastEpoch = userProfile?.lastBlueprintEpochDay ?: 0L
    val daysSince = if (lastEpoch > 0) (todayEpoch - lastEpoch).coerceAtLeast(0) else 0
    val daysUntilRetake = (45 - daysSince).coerceAtLeast(0)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(
            width = if (hasDiagnosis) 1.2.dp else 1.dp,
            color = if (hasDiagnosis) GoldPrimary.copy(alpha = 0.7f) else cardBorderColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("money_blueprint_dashboard_widget")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(GoldDark.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = goldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "MONEY BLUEPRINT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (hasDiagnosis) "Subconscious Architecture" else "Limiting Beliefs Diagnosis",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = textPrimaryColor
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (hasDiagnosis) SuccessGreen.copy(alpha = 0.15f) else GoldDark.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (hasDiagnosis) SuccessGreen.copy(alpha = 0.4f) else GoldPrimary.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = if (hasDiagnosis) "DIAGNOSED" else "+250 XP",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (hasDiagnosis) SuccessGreen else AmberAccent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (hasDiagnosis) {
                // Active Diagnosis View
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDark) SurfaceElevated else Color(0xFFF7F5F0),
                    border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "PRIMARY PATTERN",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pattern ?: "",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = textPrimaryColor
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (daysSince == 0L) "Diagnosed today • Retake recommended in 45 days"
                                   else "Diagnosed $daysSince days ago • Retake in $daysUntilRetake days",
                            fontSize = 10.sp,
                            color = textSecColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetakeQuiz,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("retake_blueprint_widget_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = null,
                            tint = textSecColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RETAKE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = textSecColor
                        )
                    }

                    Button(
                        onClick = onOpenQuiz,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberAccent,
                            contentColor = RichBlack
                        ),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(40.dp)
                            .testTag("view_blueprint_widget_button")
                    ) {
                        Text(
                            text = "VIEW BLUEPRINT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            } else {
                // Pending Diagnostic Flow
                Text(
                    text = "Discover the subconscious money patterns (scarcity, guilt, fear of loss, fear of judgment, or self-worth) determining your financial velocity.",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = textSecColor
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onOpenQuiz,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberAccent,
                        contentColor = RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("start_blueprint_quiz_widget_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TAKE MONEY BLUEPRINT QUIZ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
