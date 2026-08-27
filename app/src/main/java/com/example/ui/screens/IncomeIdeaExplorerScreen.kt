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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EffortLevel
import com.example.data.model.IncomeIdea
import com.example.data.model.IncomeIdeaCategory
import com.example.data.model.IncomeIdeaLibraryData
import com.example.ui.components.IncomeIdeaCard
import com.example.ui.components.IncomeIdeaDetailDialog
import com.example.ui.components.LocalWindowSizeInfo
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeIdeaExplorerScreen(
    savedIdeaIds: Set<String>,
    onToggleSave: (IncomeIdea) -> Unit,
    onInscribeInNotebook: (IncomeIdea) -> Unit,
    onNavigateToModule: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    initialCategoryId: String? = null,
    initialFilterSavedOnly: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val goldAccent = if (isDark) GoldPrimary else tierTheme.goldDark
    val bgScaffold = if (isDark) RichBlack else Color(0xFFFAF8F5)
    val windowInfo = LocalWindowSizeInfo.current

    // UI state
    var selectedCategoryFilter by remember {
        mutableStateOf(
            if (initialFilterSavedOnly) "SAVED"
            else initialCategoryId ?: "ALL"
        )
    }
    var selectedEffortFilter by remember { mutableStateOf<EffortLevel?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedIdeaForDetail by remember { mutableStateOf<IncomeIdea?>(null) }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Filter logic
    val filteredIdeas = remember(selectedCategoryFilter, selectedEffortFilter, searchQuery, savedIdeaIds) {
        IncomeIdeaLibraryData.ideas.filter { idea ->
            // Category / Saved Filter
            val matchesCategory = when (selectedCategoryFilter) {
                "ALL" -> true
                "SAVED" -> savedIdeaIds.contains(idea.id)
                "skills" -> idea.category == IncomeIdeaCategory.SKILLS_BASED
                "product" -> idea.category == IncomeIdeaCategory.PRODUCT_BASED
                "investment" -> idea.category == IncomeIdeaCategory.INVESTMENT_BASED
                else -> true
            }

            // Effort Filter
            val matchesEffort = selectedEffortFilter == null || idea.effortLevel == selectedEffortFilter

            // Search Query
            val matchesSearch = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                idea.title.lowercase().contains(q) ||
                        idea.tagLine.lowercase().contains(q) ||
                        idea.briefExplainer.lowercase().contains(q) ||
                        idea.linkedPrinciple.lowercase().contains(q) ||
                        idea.keySteps.any { it.lowercase().contains(q) } ||
                        idea.prerequisites.any { it.lowercase().contains(q) }
            }

            matchesCategory && matchesEffort && matchesSearch
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("income_idea_explorer_screen"),
        containerColor = bgScaffold,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Income Idea Explorer",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = if (isDark) TextPrimary else Color(0xFF1A1A1A)
                        )
                        Text(
                            text = "Wealth Vehicles Mapped to 13 Principles",
                            fontSize = 11.sp,
                            color = goldAccent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("income_idea_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = goldAccent
                        )
                    }
                },
                actions = {
                    // Quick Shortlist Filter toggle button
                    IconButton(
                        onClick = {
                            selectedCategoryFilter = if (selectedCategoryFilter == "SAVED") "ALL" else "SAVED"
                        },
                        modifier = Modifier.testTag("income_idea_quick_shortlist_toggle")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (selectedCategoryFilter == "SAVED") Icons.Filled.Star else Icons.Filled.Bookmark,
                                contentDescription = "Shortlisted Ideas",
                                tint = if (selectedCategoryFilter == "SAVED") goldAccent else if (isDark) TextSecondary else Color.Gray,
                                modifier = Modifier.size(22.dp)
                            )
                            if (savedIdeaIds.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(14.dp)
                                        .background(goldAccent, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${savedIdeaIds.size}",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) RichBlack else Color.White
                                    )
                                }
                            }
                        }
                    }

                    // Search icon
                    IconButton(
                        onClick = { isSearchExpanded = !isSearchExpanded },
                        modifier = Modifier.testTag("income_idea_search_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = if (isSearchExpanded) goldAccent else if (isDark) TextSecondary else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) RichBlack else Color(0xFFFAF8F5)
                )
            )
        }
    ) { innerPadding ->
        val columns = if (windowInfo.isTabletOrFoldable) 2 else 1

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Content spanning all columns
            item(span = { GridItemSpan(columns) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Search Bar if expanded
                    AnimatedVisibility(
                        visible = isSearchExpanded,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    text = "Search ideas, principles, skills...",
                                    fontSize = 13.sp,
                                    color = if (isDark) TextMuted else Color.Gray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = goldAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear",
                                            tint = if (isDark) TextMuted else Color.Gray,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = goldAccent,
                                unfocusedBorderColor = if (isDark) DarkBorder else Color(0xFFD0C8B8),
                                focusedContainerColor = if (isDark) DarkCharcoal else Color.White,
                                unfocusedContainerColor = if (isDark) DarkCharcoal else Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("income_idea_search_input")
                        )
                    }

                    // Educational Disclaimer Banner
                    Surface(
                        color = if (isDark) DarkCharcoal else Color(0xFFF3ECE0),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isDark) DarkBorder else Color(0xFFE2D6C2))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = goldAccent
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "EDUCATIONAL & STRATEGIC PLANNING FRAMEWORK",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = goldAccent
                                )
                                Text(
                                    text = "Curated conceptual models and business blueprints to spark entrepreneurial initiative and specialized knowledge application. Not financial or investment advice. True wealth requires disciplined execution and persistent risk management.",
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    color = if (isDark) TextSecondary else Color(0xFF444444)
                                )
                            }
                        }
                    }

                    // Category Tabs / Filter Chips Row
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == "ALL",
                                onClick = { selectedCategoryFilter = "ALL" },
                                label = { Text(text = "All Ideas (${IncomeIdeaLibraryData.ideas.size})", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = goldAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = goldAccent
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategoryFilter == "ALL",
                                    borderColor = if (isDark) DarkBorder else Color(0xFFCCCCCC),
                                    selectedBorderColor = goldAccent
                                ),
                                modifier = Modifier.testTag("filter_category_all")
                            )
                        }

                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == "skills",
                                onClick = { selectedCategoryFilter = "skills" },
                                label = { Text(text = "Skills-Based (5)", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Psychology,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (selectedCategoryFilter == "skills") goldAccent else if (isDark) TextMuted else Color.Gray
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = goldAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = goldAccent
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategoryFilter == "skills",
                                    borderColor = if (isDark) DarkBorder else Color(0xFFCCCCCC),
                                    selectedBorderColor = goldAccent
                                ),
                                modifier = Modifier.testTag("filter_category_skills")
                            )
                        }

                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == "product",
                                onClick = { selectedCategoryFilter = "product" },
                                label = { Text(text = "Product-Based (5)", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.RocketLaunch,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (selectedCategoryFilter == "product") goldAccent else if (isDark) TextMuted else Color.Gray
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = goldAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = goldAccent
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategoryFilter == "product",
                                    borderColor = if (isDark) DarkBorder else Color(0xFFCCCCCC),
                                    selectedBorderColor = goldAccent
                                ),
                                modifier = Modifier.testTag("filter_category_product")
                            )
                        }

                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == "investment",
                                onClick = { selectedCategoryFilter = "investment" },
                                label = { Text(text = "Investment-Based (4)", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Savings,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (selectedCategoryFilter == "investment") goldAccent else if (isDark) TextMuted else Color.Gray
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = goldAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = goldAccent
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategoryFilter == "investment",
                                    borderColor = if (isDark) DarkBorder else Color(0xFFCCCCCC),
                                    selectedBorderColor = goldAccent
                                ),
                                modifier = Modifier.testTag("filter_category_investment")
                            )
                        }

                        item {
                            FilterChip(
                                selected = selectedCategoryFilter == "SAVED",
                                onClick = { selectedCategoryFilter = "SAVED" },
                                label = { Text(text = "★ My Shortlist (${savedIdeaIds.size})", fontSize = 12.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = if (selectedCategoryFilter == "SAVED") goldAccent else if (isDark) TextMuted else Color.Gray
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = goldAccent.copy(alpha = 0.25f),
                                    selectedLabelColor = goldAccent
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedCategoryFilter == "SAVED",
                                    borderColor = if (isDark) DarkBorder else Color(0xFFCCCCCC),
                                    selectedBorderColor = goldAccent
                                ),
                                modifier = Modifier.testTag("filter_category_shortlist")
                            )
                        }
                    }

                    // Effort Filter Chips Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Effort:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextMuted else Color.Gray
                        )

                        listOf(
                            null to "All",
                            EffortLevel.LOW to "Low Effort",
                            EffortLevel.MEDIUM to "Medium Effort",
                            EffortLevel.HIGH to "High Effort"
                        ).forEach { (level, label) ->
                            val isSelected = selectedEffortFilter == level
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) goldAccent.copy(alpha = 0.2f) else if (isDark) DarkCharcoal else Color.White,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) goldAccent else if (isDark) DarkBorder else Color(0xFFDDDDDD)
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedEffortFilter = level }
                                    .testTag("effort_filter_${label.lowercase().replace(" ", "_")}")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) goldAccent else if (isDark) TextSecondary else Color(0xFF555555),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Results Count Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Showing ${filteredIdeas.size} income ${if (filteredIdeas.size == 1) "vehicle" else "vehicles"}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) TextSecondary else Color(0xFF666666)
                        )

                        if (selectedCategoryFilter != "ALL" || selectedEffortFilter != null || searchQuery.isNotBlank()) {
                            Text(
                                text = "Reset Filters",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = goldAccent,
                                modifier = Modifier
                                    .clickable {
                                        selectedCategoryFilter = "ALL"
                                        selectedEffortFilter = null
                                        searchQuery = ""
                                    }
                                    .testTag("reset_income_filters_button")
                            )
                        }
                    }
                }
            }

            // If empty state
            if (filteredIdeas.isEmpty()) {
                item(span = { GridItemSpan(columns) }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isDark) DarkCharcoal else Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, if (isDark) DarkBorder else Color(0xFFE0D8C8))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = if (selectedCategoryFilter == "SAVED") Icons.Filled.Star else Icons.Filled.Lightbulb,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = goldAccent.copy(alpha = 0.6f)
                            )
                            Text(
                                text = if (selectedCategoryFilter == "SAVED") "No Ideas Shortlisted Yet" else "No Matching Income Ideas Found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = if (isDark) TextPrimary else Color(0xFF222222),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (selectedCategoryFilter == "SAVED") {
                                    "Tap the star icon on any income vehicle to bookmark it into your personal execution shortlist."
                                } else {
                                    "Try clearing search queries or adjusting category and effort filters to explore the full library."
                                },
                                fontSize = 12.sp,
                                color = if (isDark) TextSecondary else Color(0xFF666666),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                            OutlinedButton(
                                onClick = {
                                    selectedCategoryFilter = "ALL"
                                    selectedEffortFilter = null
                                    searchQuery = ""
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = goldAccent),
                                border = BorderStroke(1.dp, goldAccent),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("BROWSE ALL INCOME IDEAS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // Render Idea Cards in Grid
                items(filteredIdeas, key = { it.id }) { idea ->
                    IncomeIdeaCard(
                        idea = idea,
                        isSaved = savedIdeaIds.contains(idea.id),
                        onSelectIdea = { selectedIdeaForDetail = it },
                        onToggleSave = { onToggleSave(it) }
                    )
                }
            }
        }

        // Full Detail Dialog
        selectedIdeaForDetail?.let { idea ->
            IncomeIdeaDetailDialog(
                idea = idea,
                isSaved = savedIdeaIds.contains(idea.id),
                onDismiss = { selectedIdeaForDetail = null },
                onToggleSave = { onToggleSave(it) },
                onInscribeInNotebook = {
                    onInscribeInNotebook(it)
                    selectedIdeaForDetail = null
                },
                onNavigateToLinkedModule = { moduleId ->
                    selectedIdeaForDetail = null
                    onNavigateToModule(moduleId)
                }
            )
        }
    }
}
