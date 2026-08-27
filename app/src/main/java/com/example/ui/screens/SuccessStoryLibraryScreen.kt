package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SuccessFigure
import com.example.data.repository.SuccessStoryLibraryData
import com.example.ui.components.BrushedCard
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.SuccessFigureDetailDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightElevated
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

@Composable
fun SuccessStoryLibraryScreen(
    initialFigureId: String? = null,
    initialPrinciple: String? = null,
    onBack: () -> Unit,
    onNavigateToVault: (Int) -> Unit,
    onSaveToNotebook: (title: String, content: String, tags: String) -> Unit
) {
    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background.red < 0.5f
    val windowInfo = LocalWindowSizeInfo.current
    val focusManager = LocalFocusManager.current

    val primaryTextColor = if (isDark) TextPrimary else LightTextPrimary
    val secondaryTextColor = if (isDark) TextSecondary else LightTextSecondary
    val mutedTextColor = if (isDark) TextMuted else LightTextMuted
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val goldColor = if (isDark) GoldLight else GoldDark

    var searchQuery by remember { mutableStateOf("") }
    var selectedPrincipleFilter by remember {
        mutableStateOf(
            if (!initialPrinciple.isNullOrBlank()) {
                SuccessStoryLibraryData.allPrincipleNames.firstOrNull {
                    it.contains(initialPrinciple, ignoreCase = true)
                } ?: "All Principles"
            } else {
                "All Principles"
            }
        )
    }

    var selectedFigureForModal by remember {
        mutableStateOf<SuccessFigure?>(
            if (!initialFigureId.isNullOrBlank()) {
                SuccessStoryLibraryData.getFigureById(initialFigureId)
            } else null
        )
    }

    LaunchedEffect(initialFigureId) {
        if (!initialFigureId.isNullOrBlank()) {
            val matched = SuccessStoryLibraryData.getFigureById(initialFigureId)
            if (matched != null) {
                selectedFigureForModal = matched
            }
        }
    }

    val filteredFigures = remember(searchQuery, selectedPrincipleFilter) {
        SuccessStoryLibraryData.figures.filter { figure ->
            val matchesSearch = searchQuery.isBlank() ||
                    figure.name.contains(searchQuery, ignoreCase = true) ||
                    figure.category.contains(searchQuery, ignoreCase = true) ||
                    figure.shortBio.contains(searchQuery, ignoreCase = true) ||
                    figure.exemplaryMoment.contains(searchQuery, ignoreCase = true) ||
                    figure.principleName.contains(searchQuery, ignoreCase = true) ||
                    (figure.quote?.contains(searchQuery, ignoreCase = true) == true) ||
                    figure.keyTakeaway.contains(searchQuery, ignoreCase = true) ||
                    figure.tags.any { it.contains(searchQuery, ignoreCase = true) }

            val matchesPrinciple = if (selectedPrincipleFilter == "All Principles") {
                true
            } else {
                val cleanFilterName = selectedPrincipleFilter.substringAfter(". ").trim()
                figure.principleName.contains(cleanFilterName, ignoreCase = true) ||
                        (figure.secondaryPrinciple?.contains(cleanFilterName, ignoreCase = true) == true)
            }

            matchesSearch && matchesPrinciple
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("success_story_library_screen")
    ) {
        // --- TOP BAR ---
        Surface(
            color = if (isDark) DarkCharcoal else LightIvory,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, cardBorder, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = goldColor
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        (if (isDark) GoldLight else GoldPrimary).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .border(1.dp, goldColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = goldColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SUCCESS STORY LIBRARY",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = primaryTextColor
                        )
                        Text(
                            text = "16 Historical Case Studies on the 13 Principles",
                            fontSize = 11.sp,
                            color = secondaryTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search by titan, quote, or keyword...",
                            fontSize = 12.5.sp,
                            color = mutedTextColor
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = goldColor,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear",
                                    tint = mutedTextColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = cardBorder,
                        focusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.6f) else LightElevated,
                        unfocusedContainerColor = if (isDark) RichBlack.copy(alpha = 0.6f) else LightElevated,
                        focusedTextColor = primaryTextColor,
                        unfocusedTextColor = primaryTextColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("success_library_search_field")
                )
            }
        }

        // --- FILTER PILLS ROW ---
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SuccessStoryLibraryData.allPrincipleNames) { principleName ->
                val isSelected = selectedPrincipleFilter == principleName
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedPrincipleFilter = principleName },
                    label = {
                        Text(
                            text = principleName,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (isDark) GoldDark else AmberAccent,
                        selectedLabelColor = if (isDark) GoldLight else RichBlack,
                        containerColor = if (isDark) DarkCharcoal else LightElevated,
                        labelColor = if (isDark) TextSecondary else LightTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) goldColor else cardBorder
                    ),
                    modifier = Modifier.testTag("filter_chip_$principleName")
                )
            }
        }

        // --- TITANS LIST / GRID ---
        if (filteredFigures.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null,
                        tint = mutedTextColor,
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = "No Titans Found",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = primaryTextColor
                    )
                    Text(
                        text = "Try adjusting your search terms or selecting a different principle filter.",
                        fontSize = 12.sp,
                        color = mutedTextColor,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            val isMultiColumn = windowInfo.isTabletOrFoldable || windowInfo.isDesktop
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("success_story_library_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (isMultiColumn) {
                    // Chunk into pairs for 2-column layout
                    val chunked = filteredFigures.chunked(2)
                    items(chunked) { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                FigureCardItem(
                                    figure = pair[0],
                                    onClick = { selectedFigureForModal = pair[0] }
                                )
                            }
                            if (pair.size > 1) {
                                Box(modifier = Modifier.weight(1f)) {
                                    FigureCardItem(
                                        figure = pair[1],
                                        onClick = { selectedFigureForModal = pair[1] }
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                } else {
                    items(filteredFigures) { figure ->
                        FigureCardItem(
                            figure = figure,
                            onClick = { selectedFigureForModal = figure }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialog
    selectedFigureForModal?.let { figure ->
        SuccessFigureDetailDialog(
            figure = figure,
            onDismiss = { selectedFigureForModal = null },
            onNavigateToVault = { vaultId ->
                selectedFigureForModal = null
                onNavigateToVault(vaultId)
            },
            onSaveToNotebook = onSaveToNotebook
        )
    }
}

@Composable
private fun FigureCardItem(
    figure: SuccessFigure,
    onClick: () -> Unit
) {
    val isDark = androidx.compose.material3.MaterialTheme.colorScheme.background.red < 0.5f
    val cardBorder = if (isDark) DarkBorder else LightBorder
    val primaryText = if (isDark) TextPrimary else LightTextPrimary
    val secondaryText = if (isDark) TextSecondary else LightTextSecondary
    val mutedText = if (isDark) TextMuted else LightTextMuted
    val goldColor = if (isDark) GoldLight else GoldDark

    BrushedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("figure_card_${figure.id}")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Avatar Initials, Name, Era, Category
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Crest Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    (if (isDark) GoldLight else GoldPrimary).copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(1.dp, goldColor.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = figure.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = goldColor
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = figure.name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = primaryText
                    )
                    Text(
                        text = "${figure.era} • ${figure.category}",
                        fontSize = 10.5.sp,
                        color = mutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Principle Badge
            Surface(
                color = if (isDark) SurfaceElevated else LightElevated,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(0.6.dp, cardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = goldColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Exemplifies Vault ${figure.principleId}: ${figure.principleName}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = goldColor
                    )
                }
            }

            // Short Bio / Exemplary snippet
            Text(
                text = figure.exemplaryMoment,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = secondaryText,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            // Quote snippet (if present)
            if (!figure.quote.isNullOrBlank()) {
                Surface(
                    color = (if (isDark) RichBlack else LightIvory).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.5.dp, cardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FormatQuote,
                            contentDescription = null,
                            tint = goldColor.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "“${figure.quote}”",
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            fontSize = 11.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = primaryText
                        )
                    }
                }
            }

            // Bottom trigger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Read Case Study",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = goldColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = goldColor,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}
