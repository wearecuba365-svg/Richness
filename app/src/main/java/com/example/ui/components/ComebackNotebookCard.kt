package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotebookEntryEntity
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dedicated Card for Persistence Comeback Entries in the Sovereign Notebook and Insights.
 * Emphasizes Hill's persistence principle: a streak restart is a moment of deliberate strength.
 */
@Composable
fun ComebackNotebookCard(
    entry: NotebookEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val cardBorderColor = if (isDark) DarkBorder else LightBorder
    val surfaceColor = if (isDark) DarkCharcoal else LightSurface

    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault()) }
    val formattedDate = remember(entry.timestamp) { dateFormat.format(Date(entry.timestamp)) }

    val streakName = entry.comebackStreakType.ifBlank { "Daily Sovereign Ritual" }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("comeback_notebook_card_${entry.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Top Header: Tag, Streak Type Badge & Actions
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
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        AmberAccent.copy(alpha = 0.3f),
                                        GoldDark.copy(alpha = 0.15f)
                                    )
                                )
                            )
                            .border(1.dp, AmberBright.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Loop,
                            contentDescription = null,
                            tint = if (isDark) AmberBright else GoldDark,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "COMEBACK LOG",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) AmberBright else GoldDark,
                                letterSpacing = 1.2.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Surface(
                                color = AmberAccent.copy(alpha = if (isDark) 0.18f else 0.12f),
                                shape = RoundedCornerShape(4.dp),
                                border = BorderStroke(0.5.dp, AmberBright.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = streakName,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) AmberBright else GoldDark,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = formattedDate,
                            fontSize = 11.sp,
                            color = textMutedColor
                        )
                    }
                }

                // Action icons: Favorite, Edit, Delete
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = if (entry.isFavorite) "Unmark Favorite" else "Mark Favorite",
                            tint = if (entry.isFavorite) Color(0xFFFF5252) else textMutedColor.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Entry",
                            tint = textMutedColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Entry",
                            tint = textMutedColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Obstacle section (if logged)
            if (entry.comebackObstacle.isNotBlank()) {
                Surface(
                    color = if (isDark) SurfaceElevated.copy(alpha = 0.6f) else LightElevated,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(0.5.dp, cardBorderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "WHAT BROKE THE STREAK",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = textMutedColor,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = entry.comebackObstacle,
                            fontSize = 12.sp,
                            color = textColor,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            // Tomorrow's plan section
            Surface(
                color = if (isDark) DarkCharcoal else LightSurface,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, AmberBright.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = if (isDark) AmberBright else GoldDark,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "TOMORROW'S RECOVERY COMMITMENT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = if (isDark) AmberBright else GoldDark
                        )
                    }
                    Text(
                        text = if (entry.comebackPlan.isNotBlank()) entry.comebackPlan else entry.content,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = textColor,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
