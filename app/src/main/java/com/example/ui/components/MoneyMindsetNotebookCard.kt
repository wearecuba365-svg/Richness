package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MoneyMindsetNotebookCard(
    entry: NotebookEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val textColor = if (isDark) TextPrimary else LightTextPrimary
    val textSecColor = if (isDark) TextSecondary else LightTextSecondary
    val textMutedColor = if (isDark) TextMuted else LightTextMuted
    val goldAccent = if (isDark) GoldLight else tierTheme.goldDark
    val surfaceColor = if (isDark) DarkCharcoal else LightElevated
    val cardBorderColor = if (isDark) DarkBorder else LightBorder

    val dateFormatter = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(entry.timestamp))

    // Parse decision type & emotion from tags or content
    val isExpense = entry.tags.contains("Expense", ignoreCase = true) || entry.title.startsWith("Expense", ignoreCase = true)
    val isSaving = entry.tags.contains("Saving", ignoreCase = true) || entry.title.startsWith("Saving", ignoreCase = true)
    val isIncome = entry.tags.contains("Income", ignoreCase = true) || entry.title.startsWith("Income", ignoreCase = true)
    val isInvestment = entry.tags.contains("Investment", ignoreCase = true) || entry.title.startsWith("Investment", ignoreCase = true)

    val decisionTypeLabel = when {
        isSaving -> "Saving"
        isIncome -> "Income"
        isInvestment -> "Investment"
        else -> "Expense"
    }

    val typeIcon = when {
        isSaving -> Icons.Filled.Savings
        isIncome -> Icons.Filled.TrendingUp
        isInvestment -> Icons.Filled.AccountBalance
        else -> Icons.Filled.ShoppingCart
    }

    val typeColor = when {
        isSaving -> Color(0xFF4CAF50)
        isIncome -> Color(0xFFFFB300)
        isInvestment -> Color(0xFF00BCD4)
        else -> if (isDark) AmberAccent else tierTheme.goldDark
    }

    BrushedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("money_mindset_card_${entry.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TOP BAR: Type Badge, Title/Category, Favorite Star & Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(typeColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = typeIcon,
                            contentDescription = null,
                            tint = typeColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Decision Type Badge
                    Surface(
                        color = typeColor.copy(alpha = if (isDark) 0.18f else 0.12f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.8.dp, typeColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = decisionTypeLabel.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = typeColor,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        color = (if (isDark) GoldPrimary else tierTheme.goldPrimary).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.5.dp, goldAccent.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "MONEY MINDSET",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = goldAccent,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = if (entry.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (entry.isFavorite) AmberBright else textMutedColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = textMutedColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = textMutedColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // TITLE / HEADLINE
            Text(
                text = entry.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                lineHeight = 19.sp
            )

            // CONTENT BLOCK (Structured decision & underlying belief)
            Surface(
                color = surfaceColor,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, cardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = entry.content,
                        fontSize = 12.sp,
                        color = textColor,
                        lineHeight = 18.sp
                    )

                    if (entry.promptQuestion.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Psychology,
                                contentDescription = null,
                                tint = goldAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Prompt: ${entry.promptQuestion}",
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                color = textMutedColor
                            )
                        }
                    }
                }
            }

            // FOOTER: Tags & Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.tags,
                    fontSize = 10.sp,
                    color = goldAccent,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = formattedDate,
                    fontSize = 10.sp,
                    color = textMutedColor
                )
            }
        }
    }
}
