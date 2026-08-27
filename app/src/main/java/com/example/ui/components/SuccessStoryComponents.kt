package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.SuccessFigure
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightHighlight
import com.example.ui.theme.LightIvory
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Full detail modal for viewing a historical figure's case study.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SuccessFigureDetailDialog(
    figure: SuccessFigure,
    onDismiss: () -> Unit,
    onNavigateToVault: ((Int) -> Unit)? = null,
    onSaveToNotebook: ((title: String, content: String, tags: String) -> Unit)? = null
) {
    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background.red < 0.5f
    val bgGradient = if (isDark) {
        Brush.verticalGradient(listOf(DarkCharcoal, RichBlack))
    } else {
        Brush.verticalGradient(listOf(LightIvory, LightSurface))
    }
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val primaryTextColor = if (isDark) TextPrimary else LightTextPrimary
    val secondaryTextColor = if (isDark) TextSecondary else LightTextSecondary
    val mutedTextColor = if (isDark) TextMuted else LightTextMuted
    val goldColor = if (isDark) GoldPrimary else GoldDark

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 560.dp)
                .padding(vertical = 20.dp)
                .border(
                    BorderStroke(1.2.dp, goldColor.copy(alpha = 0.6f)),
                    RoundedCornerShape(20.dp)
                )
                .testTag("figure_detail_dialog_${figure.id}"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(bgGradient)
                    .padding(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Top Bar: Category & Close
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AmberAccent.copy(alpha = if (isDark) 0.16f else 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(0.8.dp, goldColor.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = null,
                                    tint = if (isDark) AmberBright else GoldDark,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = figure.category.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    color = if (isDark) AmberBright else GoldDark
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isDark) SurfaceHighlight else LightHighlight)
                                .testTag("figure_dialog_close")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = mutedTextColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Avatar Seal & Identity Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Monogram Crest Avatar
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            GoldLight.copy(alpha = if (isDark) 0.35f else 0.25f),
                                            GoldDark.copy(alpha = 0.1f)
                                        )
                                    )
                                )
                                .border(1.5.dp, goldColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = figure.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = if (isDark) GoldLight else GoldDark
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = figure.name,
                                fontFamily = FontFamily.Serif,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryTextColor
                            )
                            Text(
                                text = figure.era,
                                fontSize = 12.sp,
                                color = mutedTextColor,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Principle badge
                            Surface(
                                color = if (isDark) SurfaceElevated else LightElevated,
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(0.6.dp, cardBorder)
                            ) {
                                Text(
                                    text = "Vault ${figure.principleId} • ${figure.principleName}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) GoldLight else GoldDark,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Biography Section
                    Surface(
                        color = if (isDark) DarkCharcoal.copy(alpha = 0.8f) else LightElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, cardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "HISTORICAL BACKGROUND",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = mutedTextColor
                            )
                            Text(
                                text = figure.shortBio,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = secondaryTextColor
                            )
                        }
                    }

                    // How They Exemplified Principle (Core Narrative Highlight)
                    Surface(
                        color = if (isDark) SurfaceElevated else LightElevated,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.2.dp, AmberAccent.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (isDark) AmberBright else GoldDark,
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "HOW THEY APPLIED ${figure.principleName.uppercase()}",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp,
                                    color = if (isDark) AmberBright else GoldDark
                                )
                            }
                            Text(
                                text = figure.exemplaryMoment,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = primaryTextColor
                            )
                        }
                    }

                    // Verified Historical Quote
                    if (!figure.quote.isNullOrBlank()) {
                        Surface(
                            color = if (isDark) RichBlack.copy(alpha = 0.8f) else LightIvory,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(0.8.dp, cardBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FormatQuote,
                                        contentDescription = null,
                                        tint = goldColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "“${figure.quote}”",
                                            fontFamily = FontFamily.Serif,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 13.sp,
                                            lineHeight = 19.sp,
                                            color = primaryTextColor
                                        )
                                        if (!figure.quoteSource.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "— ${figure.name}, ${figure.quoteSource}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = mutedTextColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Sovereign Rule / Key Takeaway
                    Surface(
                        color = (if (isDark) GoldDark else GoldPrimary).copy(alpha = if (isDark) 0.12f else 0.08f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(0.8.dp, goldColor.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                tint = goldColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "SOVEREIGN TAKEAWAY",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = goldColor
                                )
                                Text(
                                    text = figure.keyTakeaway,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = primaryTextColor
                                )
                            }
                        }
                    }

                    // Action Buttons: Explore Vault & Save to Notebook
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (onNavigateToVault != null && figure.principleId in 0..13) {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onNavigateToVault(figure.principleId)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberAccent,
                                    contentColor = RichBlack
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("figure_explore_vault_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "EXPLORE VAULT ${figure.principleId}: ${figure.principleName.uppercase()}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (onSaveToNotebook != null) {
                            OutlinedButton(
                                onClick = {
                                    val reflectionContent = buildString {
                                        append("Historical Case Study: ${figure.name} (${figure.era})\n")
                                        append("Principle: ${figure.principleName}\n\n")
                                        append("HOW THEY APPLIED IT:\n${figure.exemplaryMoment}\n\n")
                                        if (!figure.quote.isNullOrBlank()) {
                                            append("VERIFIED QUOTE:\n\"${figure.quote}\" — ${figure.name}\n\n")
                                        }
                                        append("MY SOVEREIGN APPLICATION:\n")
                                    }
                                    onSaveToNotebook(
                                        "Case Study: ${figure.name}",
                                        reflectionContent,
                                        "Case Study,${figure.principleName},${figure.name}"
                                    )
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, cardBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(42.dp)
                                    .testTag("figure_save_notebook_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.EditNote,
                                    contentDescription = null,
                                    tint = primaryTextColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Reflect in Sovereign Notebook",
                                    fontSize = 12.sp,
                                    color = primaryTextColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Lightweight, non-intrusive cross-link banner/card for placing in module screens or recovery moments.
 */
@Composable
fun PrincipleFigureCrossLinkCard(
    figure: SuccessFigure,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    customTitle: String? = null
) {
    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background.red < 0.5f
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val goldColor = if (isDark) GoldLight else GoldDark

    Surface(
        color = if (isDark) DarkCharcoal.copy(alpha = 0.9f) else LightElevated,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.8.dp, goldColor.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("figure_crosslink_card_${figure.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isDark) SurfaceElevated else LightIvory)
                        .border(1.dp, goldColor.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = goldColor,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = customTitle ?: "CASE STUDY: ${figure.name}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "See how ${figure.name.split(" ").firstOrNull() ?: figure.name} exemplified ${figure.principleName}",
                        fontSize = 11.sp,
                        color = secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open Case Study",
                tint = goldColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
