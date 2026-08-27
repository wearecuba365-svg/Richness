package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EffortLevel
import com.example.data.model.IncomeIdea
import com.example.data.model.IncomeIdeaCategory
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Effort level badge color resolver respecting theme
 */
@Composable
fun getEffortBadgeColors(effortLevel: EffortLevel): Pair<Color, Color> {
    val isDark = LocalIsDarkTheme.current
    return when (effortLevel) {
        EffortLevel.LOW -> {
            if (isDark) Pair(Color(0xFF1B5E20).copy(alpha = 0.35f), Color(0xFF81C784))
            else Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        }
        EffortLevel.MEDIUM -> {
            if (isDark) Pair(Color(0xFFE65100).copy(alpha = 0.35f), Color(0xFFFFB74D))
            else Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        }
        EffortLevel.HIGH -> {
            if (isDark) Pair(Color(0xFFB71C1C).copy(alpha = 0.35f), Color(0xFFE57373))
            else Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        }
    }
}

/**
 * Category Icon resolver
 */
fun getCategoryIcon(category: IncomeIdeaCategory): ImageVector {
    return when (category) {
        IncomeIdeaCategory.SKILLS_BASED -> Icons.Filled.Psychology
        IncomeIdeaCategory.PRODUCT_BASED -> Icons.Filled.RocketLaunch
        IncomeIdeaCategory.INVESTMENT_BASED -> Icons.Filled.Savings
    }
}

/**
 * Income Idea Card used in grid / lists
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeIdeaCard(
    idea: IncomeIdea,
    isSaved: Boolean,
    onSelectIdea: (IncomeIdea) -> Unit,
    onToggleSave: (IncomeIdea) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldPrimary else tierTheme.goldDark
    val bgCard = if (isDark) DarkCharcoal else Color.White
    val borderColor = if (isDark) {
        if (isSaved) GoldPrimary.copy(alpha = 0.7f) else DarkBorder
    } else {
        if (isSaved) tierTheme.goldDark else Color(0xFFE0D8C8)
    }

    val (effortBg, effortText) = getEffortBadgeColors(idea.effortLevel)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSaved) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelectIdea(idea) }
            .testTag("income_idea_card_${idea.id}"),
        colors = CardDefaults.cardColors(containerColor = bgCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 2.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Category badge, Effort indicator, and Shortlist Star
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category Badge
                    Surface(
                        color = goldAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.35f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = getCategoryIcon(idea.category),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = goldAccent
                            )
                            Text(
                                text = idea.category.shortBadge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    // Effort Level Badge
                    Surface(
                        color = effortBg,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = idea.effortLevel.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = effortText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Bookmark / Shortlist Star with Tooltip
                val tooltipState = rememberTooltipState()
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text(
                                text = if (isSaved) "Saved in your idea shortlist" else "Save — add this to your idea shortlist",
                                fontSize = 12.sp
                            )
                        }
                    },
                    state = tooltipState
                ) {
                    IconButton(
                        onClick = { onToggleSave(idea) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("income_idea_save_button_${idea.id}")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = if (isSaved) "Saved to shortlist" else "Save to shortlist",
                            tint = if (isSaved) (if (isDark) AmberBright else tierTheme.goldDark) else if (isDark) TextMuted else Color.Gray,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // Idea Title & Tagline
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = idea.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = if (isDark) TextPrimary else Color(0xFF1A1A1A),
                    lineHeight = 22.sp
                )
                Text(
                    text = idea.tagLine,
                    fontSize = 12.sp,
                    color = goldAccent,
                    fontWeight = FontWeight.Medium
                )
            }

            // Brief Explainer Teaser
            Text(
                text = idea.briefExplainer,
                fontSize = 12.sp,
                color = if (isDark) TextSecondary else Color(0xFF555555),
                lineHeight = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                color = if (isDark) DarkBorder.copy(alpha = 0.5f) else Color(0xFFE8E0D0),
                thickness = 1.dp
            )

            // Bottom Info: Linked Principle & Expand affordance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Linked Principle Tag
                Row(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = goldAccent.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${idea.linkedPrinciple} • Mod ${idea.linkedModuleId}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) TextSecondary else Color(0xFF444444),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Read more / expand indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Explore",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = goldAccent
                    )
                }
            }
        }
    }
}

/**
 * Comprehensive Income Idea Detail Dialog
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun IncomeIdeaDetailDialog(
    idea: IncomeIdea,
    isSaved: Boolean,
    onDismiss: () -> Unit,
    onToggleSave: (IncomeIdea) -> Unit,
    onInscribeInNotebook: (IncomeIdea) -> Unit,
    onNavigateToLinkedModule: (Int) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldPrimary else tierTheme.goldDark
    val bgModal = if (isDark) RichBlack else Color.White
    val surfaceColor = if (isDark) DarkCharcoal else Color(0xFFF9F7F2)
    val (effortBg, effortText) = getEffortBadgeColors(idea.effortLevel)

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, if (isDark) GoldPrimary.copy(alpha = 0.5f) else tierTheme.goldDark, RoundedCornerShape(20.dp))
            .testTag("income_idea_detail_dialog")
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = bgModal,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with Category, Effort, and Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = goldAccent.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = getCategoryIcon(idea.category),
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = goldAccent
                                )
                                Text(
                                    text = idea.category.title.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldAccent
                                )
                            }
                        }

                        Surface(
                            color = effortBg,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = idea.effortLevel.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = effortText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_income_idea_detail_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = if (isDark) TextSecondary else Color.Gray
                        )
                    }
                }

                // Title & Subtitle
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = idea.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDark) TextPrimary else Color(0xFF111111)
                    )
                    Text(
                        text = idea.tagLine,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = goldAccent
                    )
                }

                // Metric Snapshot Strip (Capital, Time to Revenue, Scalability)
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isDark) DarkBorder else Color(0xFFE8E0D0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Estimated Capital:",
                                fontSize = 11.sp,
                                color = if (isDark) TextSecondary else Color(0xFF666666)
                            )
                            Text(
                                text = idea.capitalRequired,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else Color(0xFF222222)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Time to First Revenue:",
                                fontSize = 11.sp,
                                color = if (isDark) TextSecondary else Color(0xFF666666)
                            )
                            Text(
                                text = idea.timeToFirstRevenue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else Color(0xFF222222)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Scalability Ceiling:",
                                fontSize = 11.sp,
                                color = if (isDark) TextSecondary else Color(0xFF666666)
                            )
                            Text(
                                text = idea.scalabilityRating,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent
                            )
                        }
                    }
                }

                // Comprehensive Explainer
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "THE CONCEPT & MECHANICS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = goldAccent
                    )
                    Text(
                        text = idea.briefExplainer,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = if (isDark) TextSecondary else Color(0xFF333333)
                    )
                }

                // Tactical Steps
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "TACTICAL EXECUTION BLUEPRINT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = goldAccent
                    )
                    idea.keySteps.forEachIndexed { index, step ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(goldAccent.copy(alpha = 0.2f), CircleShape)
                                    .border(1.dp, goldAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = goldAccent
                                )
                            }
                            Text(
                                text = step,
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                                color = if (isDark) TextPrimary else Color(0xFF222222),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Prerequisites & Skills
                Surface(
                    color = surfaceColor,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isDark) DarkBorder else Color(0xFFE8E0D0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "PREREQUISITES & KEY ASSETS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextPrimary else Color(0xFF222222)
                        )
                        idea.prerequisites.forEach { prereq ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = goldAccent
                                )
                                Text(
                                    text = prereq,
                                    fontSize = 11.sp,
                                    color = if (isDark) TextSecondary else Color(0xFF555555)
                                )
                            }
                        }
                    }
                }

                // Pros & Strategic Considerations
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pros Card
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = surfaceColor,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF1B5E20).copy(alpha = 0.5f) else Color(0xFFC8E6C9))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "ADVANTAGES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                            )
                            idea.pros.forEach { pro ->
                                Text(
                                    text = "+ $pro",
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp,
                                    color = if (isDark) TextSecondary else Color(0xFF444444)
                                )
                            }
                        }
                    }

                    // Considerations Card
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = surfaceColor,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFFB71C1C).copy(alpha = 0.5f) else Color(0xFFFFCDD2))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "CONSIDERATIONS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color(0xFFE57373) else Color(0xFFC62828)
                            )
                            idea.considerations.forEach { con ->
                                Text(
                                    text = "• $con",
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp,
                                    color = if (isDark) TextSecondary else Color(0xFF444444)
                                )
                            }
                        }
                    }
                }

                // Linked Think & Grow Rich Principle Module Connection
                Surface(
                    color = goldAccent.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, goldAccent.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = goldAccent
                            )
                            Text(
                                text = "PHILOSOPHIC ALIGNMENT: ${idea.linkedPrinciple.uppercase()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent
                            )
                        }

                        Text(
                            text = idea.linkedPrincipleRationale,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = if (isDark) TextPrimary else Color(0xFF222222)
                        )

                        OutlinedButton(
                            onClick = {
                                onDismiss()
                                onNavigateToLinkedModule(idea.linkedModuleId)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                            border = BorderStroke(1.dp, goldAccent),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("study_linked_module_button_${idea.linkedModuleId}")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "STUDY ${idea.linkedModuleTitle.uppercase()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Educational Disclaimer Pill
                Surface(
                    color = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF0EAE0),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (isDark) TextMuted else Color.Gray
                        )
                        Text(
                            text = "Educational & conceptual idea generation. Not financial advice. Real-world execution demands persistent effort and individual risk assessment.",
                            fontSize = 10.sp,
                            color = if (isDark) TextMuted else Color.Gray,
                            lineHeight = 14.sp
                        )
                    }
                }

                // Action Buttons: Inscribe in Notebook, Toggle Save, Done
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onInscribeInNotebook(idea)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldAccent,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inscribe_income_idea_notebook_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "INSCRIBE IN SOVEREIGN NOTEBOOK",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onToggleSave(idea) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("toggle_save_income_idea_dialog_button"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isSaved) goldAccent else if (isDark) TextSecondary else Color(0xFF444444)
                            ),
                            border = BorderStroke(1.dp, if (isSaved) goldAccent else if (isDark) DarkBorder else Color(0xFFCCCCCC)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.Star else Icons.Filled.StarBorder,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSaved) (if (isDark) AmberBright else tierTheme.goldDark) else if (isDark) TextMuted else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isSaved) "SHORTLISTED" else "SHORTLIST",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "CLOSE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) TextSecondary else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dashboard Promotion Widget for Income Idea Explorer
 */
@Composable
fun IncomeIdeaDashboardWidget(
    onExploreIdeas: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldPrimary else tierTheme.goldDark
    val bgCard = if (isDark) DarkCharcoal else Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, if (isDark) DarkBorder else Color(0xFFE0D8C8), RoundedCornerShape(16.dp))
            .clickable { onExploreIdeas() }
            .testTag("dashboard_income_idea_widget"),
        colors = CardDefaults.cardColors(containerColor = bgCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = if (isDark) listOf(
                            DarkCharcoal,
                            GoldPrimary.copy(alpha = 0.08f)
                        ) else listOf(
                            Color.White,
                            tierTheme.goldLight.copy(alpha = 0.2f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(goldAccent.copy(alpha = 0.15f), CircleShape)
                        .border(1.dp, goldAccent.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = goldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "INCOME IDEA EXPLORER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = goldAccent,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Skills, Products & Cash-Flow Vehicles",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDark) TextPrimary else Color(0xFF111111)
                    )
                    Text(
                        text = "Curated frameworks mapped to Napoleon Hill's 13 Principles",
                        fontSize = 11.sp,
                        color = if (isDark) TextSecondary else Color(0xFF666666)
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Explore",
                    tint = goldAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
