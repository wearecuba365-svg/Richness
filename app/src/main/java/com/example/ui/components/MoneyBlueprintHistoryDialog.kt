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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.MoneyBlueprintResultEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoneyBlueprintHistoryDialog(
    historyList: List<MoneyBlueprintResultEntity>,
    onDismiss: () -> Unit,
    onRetakeQuiz: () -> Unit,
    onDeleteEntry: (Long) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else Color(0xFFFBF9F5)
    val cardBorderColor = if (isDark) DarkBorder else Color(0xFFE2D6BC)
    val textPrimaryColor = if (isDark) TextPrimary else Color(0xFF1E1B18)
    val textSecColor = if (isDark) TextSecondary else Color(0xFF635948)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = surfaceColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("blueprint_history_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(GoldDark.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null,
                                tint = goldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "BLUEPRINT HISTORY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Subconscious Evolution",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = textPrimaryColor
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_blueprint_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = textSecColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Track how your limiting money beliefs transmute over time through daily autosuggestion and sovereign study.",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = textSecColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (historyList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDark) SurfaceElevated.copy(alpha = 0.5f) else Color(0xFFF3EEE3)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.Psychology,
                                contentDescription = null,
                                tint = goldAccent.copy(alpha = 0.6f),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No diagnostic records logged yet.",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimaryColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Complete the quiz to generate your first baseline.",
                                fontSize = 11.sp,
                                color = textSecColor
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(historyList, key = { it.id }) { item ->
                            val sdf = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.US)
                            val dateStr = sdf.format(Date(item.timestamp))

                            Surface(
                                color = if (isDark) SurfaceElevated else Color.White,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, cardBorderColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dateStr,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = goldAccent
                                        )
                                        IconButton(
                                            onClick = { onDeleteEntry(item.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DeleteOutline,
                                                contentDescription = "Delete",
                                                tint = textSecColor,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = item.primaryPatternTitle,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        color = textPrimaryColor
                                    )

                                    if (item.secondaryPatternTitle.isNotBlank()) {
                                        Text(
                                            text = "Secondary: ${item.secondaryPatternTitle}",
                                            fontSize = 11.sp,
                                            color = textSecColor
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Overall Limitation Index:",
                                            fontSize = 10.sp,
                                            color = textSecColor
                                        )
                                        Text(
                                            text = "${item.overallLimitationScore}%",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when {
                                                item.overallLimitationScore < 35 -> SuccessGreen
                                                item.overallLimitationScore < 65 -> AmberAccent
                                                else -> GoldLight
                                            }
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    LinearProgressIndicator(
                                        progress = { (item.overallLimitationScore / 100f).coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = when {
                                            item.overallLimitationScore < 35 -> SuccessGreen
                                            item.overallLimitationScore < 65 -> AmberAccent
                                            else -> GoldPrimary
                                        },
                                        trackColor = cardBorderColor.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Retake Button
                Button(
                    onClick = {
                        onDismiss()
                        onRetakeQuiz()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberAccent,
                        contentColor = RichBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("retake_blueprint_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RECALIBRATE MONEY BLUEPRINT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
