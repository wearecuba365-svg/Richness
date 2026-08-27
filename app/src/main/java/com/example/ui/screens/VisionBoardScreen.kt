package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.UserProfileEntity
import com.example.data.model.VISION_CATEGORIES
import com.example.data.model.VISION_STOCK_PRESETS
import com.example.data.model.VisionBoardItemEntity
import com.example.data.model.VisionStockCategory
import com.example.data.model.VisionStockItem
import com.example.data.model.getStockPresetById
import com.example.data.repository.RichesRepository
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldBorder
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightCard
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightIvory
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RichesViewModel
import kotlinx.coroutines.delay

@Composable
fun VisionBoardScreen(
    viewModel: RichesViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    val userProfile by viewModel.userProfile.collectAsState()
    val allItems by viewModel.allVisionBoardItems.collectAsState()
    val selectedCategory by viewModel.selectedVisionCategory.collectAsState()
    val showAddDialog by viewModel.showAddVisionItemDialog.collectAsState()
    val editingItem by viewModel.editingVisionItem.collectAsState()
    val isContemplating by viewModel.isVisionContemplationActive.collectAsState()
    val remainingSeconds by viewModel.visionContemplationSecondsRemaining.collectAsState()

    var deletingItem by remember { mutableStateOf<VisionBoardItemEntity?>(null) }
    var focusedItemForContemplation by remember { mutableStateOf<VisionBoardItemEntity?>(null) }

    val todayEpochDay = remember { RichesRepository.getTodayEpochDay() }
    val hasContemplatedToday = (userProfile?.lastVisionBoardViewEpochDay ?: 0L) == todayEpochDay

    val filteredItems = remember(allItems, selectedCategory) {
        if (selectedCategory == "all") {
            allItems
        } else {
            val catObj = VISION_CATEGORIES.firstOrNull { it.id == selectedCategory }
            val catName = catObj?.name ?: selectedCategory
            allItems.filter { it.category.equals(catName, ignoreCase = true) || it.category.contains(catObj?.id ?: "", ignoreCase = true) }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isDark) RichBlack else LightIvory)
            .testTag("vision_board_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Screen Header
            VisionBoardTopHeader(
                onBack = onBack,
                onInscribeNew = { viewModel.setShowAddVisionItemDialog(true) },
                onStartContemplation = { viewModel.startVisionContemplationRitual() },
                hasContemplatedToday = hasContemplatedToday,
                visionStreak = userProfile?.visionBoardStreak ?: 0
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Chips
            VisionBoardCategoryChips(
                categories = VISION_CATEGORIES,
                selectedCategory = selectedCategory,
                onSelectCategory = { viewModel.setSelectedVisionCategory(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Grid / Collage
            if (filteredItems.isEmpty()) {
                VisionBoardEmptyState(
                    onInscribeFirst = { viewModel.setShowAddVisionItemDialog(true) }
                )
            } else {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val columns = when {
                        maxWidth >= 900.dp -> 4
                        maxWidth >= 600.dp -> 3
                        else -> 2
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columns),
                        state = rememberLazyGridState(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 96.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = filteredItems,
                            key = { _, item -> item.id }
                        ) { index, item ->
                            VisionBoardGridCard(
                                item = item,
                                isFirst = index == 0,
                                isLast = index == filteredItems.lastIndex,
                                onEdit = { viewModel.setEditingVisionItem(item) },
                                onDelete = { deletingItem = item },
                                onTogglePin = { viewModel.toggleVisionItemPin(item.id, !item.isPinned) },
                                onMoveUp = {
                                    if (index > 0) {
                                        val mutable = filteredItems.toMutableList()
                                        val prev = mutable[index - 1]
                                        mutable[index - 1] = item
                                        mutable[index] = prev
                                        viewModel.reorderVisionBoardItems(mutable)
                                    }
                                },
                                onMoveDown = {
                                    if (index < filteredItems.lastIndex) {
                                        val mutable = filteredItems.toMutableList()
                                        val next = mutable[index + 1]
                                        mutable[index + 1] = item
                                        mutable[index] = next
                                        viewModel.reorderVisionBoardItems(mutable)
                                    }
                                },
                                onFocusContemplate = {
                                    focusedItemForContemplation = item
                                }
                            )
                        }
                    }
                }
            }
        }

        // Add / Edit Dialog
        if (showAddDialog) {
            AddEditVisionItemDialog(
                initialItem = editingItem,
                onDismiss = { viewModel.setShowAddVisionItemDialog(false) },
                onSave = { title, category, imageUrl, timeline, affirmation ->
                    if (editingItem != null) {
                        viewModel.updateVisionBoardItem(
                            id = editingItem!!.id,
                            title = title,
                            category = category,
                            imageUrl = imageUrl,
                            targetTimeline = timeline,
                            affirmation = affirmation
                        )
                    } else {
                        viewModel.addVisionBoardItem(
                            title = title,
                            category = category,
                            imageUrl = imageUrl,
                            targetTimeline = timeline,
                            affirmation = affirmation
                        )
                    }
                }
            )
        }

        // Delete Confirmation Dialog
        deletingItem?.let { itemToDelete ->
            AlertDialog(
                onDismissRequest = { deletingItem = null },
                containerColor = if (isDark) DarkCard else LightCard,
                title = {
                    Text(
                        text = "Remove Vision Goal?",
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) TextPrimary else LightTextPrimary
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to remove \"${itemToDelete.title}\" from your sacred Vision Board?",
                        color = if (isDark) TextSecondary else LightTextSecondary,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteVisionBoardItem(itemToDelete.id)
                            deletingItem = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deletingItem = null }) {
                        Text(
                            "Cancel",
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }
            )
        }

        // 60-Second Meditative Contemplation Overlay
        if (isContemplating) {
            VisionBoardContemplationOverlay(
                items = allItems,
                remainingSeconds = remainingSeconds,
                onTick = { viewModel.tickVisionContemplation() },
                onComplete = { viewModel.completeDailyVisionRitual() },
                onCancel = { viewModel.cancelVisionContemplation() }
            )
        }

        // Single Item Focused Contemplation Modal
        focusedItemForContemplation?.let { item ->
            SingleVisionItemModal(
                item = item,
                onDismiss = { focusedItemForContemplation = null },
                onStartFullRitual = {
                    focusedItemForContemplation = null
                    viewModel.startVisionContemplationRitual()
                }
            )
        }
    }
}

@Composable
fun VisionBoardTopHeader(
    onBack: () -> Unit,
    onInscribeNew: () -> Unit,
    onStartContemplation: () -> Unit,
    hasContemplatedToday: Boolean,
    visionStreak: Int,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isDark) SurfaceElevated else LightElevated)
                        .testTag("vision_board_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SACRED VISION BOARD",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp,
                            color = if (isDark) tierTheme.goldLight else tierTheme.goldDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Visual Blueprint of Transmutation & Chief Aims",
                        fontSize = 11.sp,
                        color = if (isDark) TextSecondary else LightTextSecondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onInscribeNew,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                        contentColor = if (isDark) RichBlack else Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("add_vision_item_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Inscribe",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Daily Ritual Action Banner
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) DarkCard else LightCard
            ),
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        if (isDark) tierTheme.goldDark else tierTheme.goldPrimary,
                        if (isDark) tierTheme.goldLight else tierTheme.goldDark
                    )
                )
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (hasContemplatedToday) {
                                    Color(0xFF10B981).copy(alpha = 0.2f)
                                } else {
                                    tierTheme.goldPrimary.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (hasContemplatedToday) Icons.Filled.Check else Icons.Filled.SelfImprovement,
                            contentDescription = null,
                            tint = if (hasContemplatedToday) Color(0xFF10B981) else tierTheme.goldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (hasContemplatedToday) "Vision Contemplated Today" else "Daily 60s Visualization Ritual",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) TextPrimary else LightTextPrimary
                            )
                            if (visionStreak > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🔥 $visionStreak Day${if (visionStreak > 1) "s" else ""}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = tierTheme.goldLight
                                )
                            }
                        }
                        Text(
                            text = if (hasContemplatedToday) "Subconscious impressed. +50 XP recorded." else "Hold images in mind for 60 seconds to earn +50 XP.",
                            fontSize = 11.sp,
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onStartContemplation,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasContemplatedToday) {
                            if (isDark) SurfaceElevated else LightElevated
                        } else {
                            if (isDark) tierTheme.goldPrimary else tierTheme.goldDark
                        },
                        contentColor = if (hasContemplatedToday) {
                            if (isDark) tierTheme.goldLight else tierTheme.goldDark
                        } else {
                            if (isDark) RichBlack else Color.White
                        }
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("start_contemplation_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (hasContemplatedToday) "Contemplate" else "60s Ritual",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun VisionBoardCategoryChips(
    categories: List<VisionStockCategory>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { cat ->
            val isSelected = selectedCategory == cat.id
            FilterChip(
                selected = isSelected,
                onClick = { onSelectCategory(cat.id) },
                label = {
                    Text(
                        text = cat.name,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = cat.icon,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = if (isDark) DarkCard else LightCard,
                    labelColor = if (isDark) TextSecondary else LightTextSecondary,
                    selectedContainerColor = if (isDark) tierTheme.goldDark else tierTheme.goldPrimary,
                    selectedLabelColor = if (isDark) tierTheme.goldLight else Color.White,
                    selectedLeadingIconColor = if (isDark) tierTheme.goldLight else Color.White,
                    iconColor = if (isDark) TextMuted else LightTextMuted
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) tierTheme.goldPrimary else (if (isDark) DarkBorder else LightBorder)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("filter_chip_${cat.id}")
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VisionBoardGridCard(
    item: VisionBoardItemEntity,
    isFirst: Boolean,
    isLast: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTogglePin: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onFocusContemplate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    var showMenu by remember { mutableStateOf(false) }

    val preset = remember(item.imageUrl) { getStockPresetById(item.imageUrl) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) DarkCard else LightCard
        ),
        border = BorderStroke(
            width = if (item.isPinned) 1.5.dp else 1.dp,
            color = if (item.isPinned) tierTheme.goldPrimary else (if (isDark) DarkBorder else LightBorder)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onFocusContemplate() }
            .testTag("vision_item_card_${item.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Visual Frame Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.25f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                if (item.imageUrl.startsWith("http") || item.imageUrl.startsWith("content://") || item.imageUrl.startsWith("file://")) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(item.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (preset != null) {
                    // Try to load preset web image with luxury gradient fallback
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(preset.webImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient Overlay for Luxury Contrast
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        RichBlack.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                } else {
                    // Fallback luxury gradient card
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        tierTheme.goldDark.copy(alpha = 0.4f),
                                        DarkCharcoal
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            tint = tierTheme.goldPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Top Badge (Category or Pinned)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (item.isPinned) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(tierTheme.goldPrimary)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.PushPin,
                                    contentDescription = "Pinned",
                                    tint = RichBlack,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "PINNED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = RichBlack
                                )
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(RichBlack.copy(alpha = 0.65f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.category.uppercase(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = tierTheme.goldLight
                            )
                        }
                    }

                    // More Options Dropdown
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(RichBlack.copy(alpha = 0.65f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(if (isDark) DarkCard else LightCard)
                        ) {
                            DropdownMenuItem(
                                text = { Text(if (item.isPinned) "Unpin from Top" else "Pin to Top", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (item.isPinned) Icons.Outlined.PushPin else Icons.Filled.PushPin,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = tierTheme.goldPrimary
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onTogglePin()
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Edit Inscription", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = tierTheme.goldPrimary
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                }
                            )

                            if (!isFirst) {
                                DropdownMenuItem(
                                    text = { Text("Move Priority Up", fontSize = 13.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowUpward,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        onMoveUp()
                                    }
                                )
                            }

                            if (!isLast) {
                                DropdownMenuItem(
                                    text = { Text("Move Priority Down", fontSize = 13.sp) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.ArrowDownward,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    },
                                    onClick = {
                                        showMenu = false
                                        onMoveDown()
                                    }
                                )
                            }

                            DropdownMenuItem(
                                text = { Text("Contemplate (Full)", fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Fullscreen,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = tierTheme.goldPrimary
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onFocusContemplate()
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Delete", color = Color(0xFFEF4444), fontSize = 13.sp) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }

                // Bottom Overlay for Target Date
                if (item.targetTimeline.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(tierTheme.goldDark.copy(alpha = 0.85f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.targetTimeline,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = tierTheme.goldLight
                        )
                    }
                }
            }

            // Caption & Intention
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) TextPrimary else LightTextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.affirmation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "“${item.affirmation}”",
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = if (isDark) tierTheme.goldLight.copy(alpha = 0.8f) else tierTheme.goldDark,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun VisionBoardEmptyState(
    onInscribeFirst: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(tierTheme.goldPrimary.copy(alpha = 0.15f))
                .border(1.dp, tierTheme.goldPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = tierTheme.goldPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your Vision Board is Clear",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = if (isDark) TextPrimary else LightTextPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Whatever the mind can conceive and believe, it can achieve. Inscribe your first chief aim to transmute mental desire into physical reality.",
            fontSize = 13.sp,
            color = if (isDark) TextSecondary else LightTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 320.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onInscribeFirst,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                contentColor = if (isDark) RichBlack else Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.height(44.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Inscribe Your First Goal",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * 60-Second Subconscious Contemplation Overlay
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VisionBoardContemplationOverlay(
    items: List<VisionBoardItemEntity>,
    remainingSeconds: Int,
    onTick: () -> Unit,
    onComplete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tierTheme = LocalTierGoldTheme.current
    val isDark = LocalIsDarkTheme.current

    val pagerState = rememberPagerState(pageCount = { items.size.coerceAtLeast(1) })

    // Auto-tick every 1 second
    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000L)
            onTick()
        } else {
            onComplete()
        }
    }

    // Auto-advance pages during the 60s ritual if multiple items exist
    LaunchedEffect(remainingSeconds) {
        if (items.isNotEmpty() && remainingSeconds > 0 && remainingSeconds % 8 == 0) {
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    // Infinite breathing transition for gold aura
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val progress = (60 - remainingSeconds) / 60f

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(RichBlack.copy(alpha = 0.95f))
                .padding(16.dp)
                .testTag("vision_contemplation_overlay"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Bar with Close and Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SUBCONSCIOUS IMPRESSION RITUAL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = tierTheme.goldLight
                        )
                        Text(
                            text = "Hold each vision with unshakeable conviction",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Middle: Carousel of Visualized Goals
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (items.isEmpty()) {
                        Text(
                            text = "No vision items inscribed yet.",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    } else {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val currentItem = items[page]
                            val preset = remember(currentItem.imageUrl) { getStockPresetById(currentItem.imageUrl) }

                            Card(
                                colors = CardDefaults.cardColors(containerColor = DarkCard),
                                border = BorderStroke(
                                    width = 1.5.dp,
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            tierTheme.goldLight,
                                            tierTheme.goldDark
                                        )
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    // Visual Image
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                    ) {
                                        if (currentItem.imageUrl.startsWith("http") || currentItem.imageUrl.startsWith("content://") || currentItem.imageUrl.startsWith("file://")) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(currentItem.imageUrl)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = currentItem.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else if (preset != null) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(preset.webImageUrl)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = currentItem.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Transparent,
                                                                RichBlack.copy(alpha = 0.6f)
                                                            )
                                                        )
                                                    )
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        Brush.radialGradient(
                                                            colors = listOf(
                                                                tierTheme.goldDark.copy(alpha = 0.5f),
                                                                DarkCharcoal
                                                            )
                                                        )
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.WorkspacePremium,
                                                    contentDescription = null,
                                                    tint = tierTheme.goldPrimary,
                                                    modifier = Modifier.size(54.dp)
                                                )
                                            }
                                        }

                                        // Category Pill
                                        Box(
                                            modifier = Modifier
                                                .padding(12.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(tierTheme.goldDark.copy(alpha = 0.9f))
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = currentItem.category.uppercase(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = tierTheme.goldLight
                                            )
                                        }
                                    }

                                    // Affirmation & Title Deck
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(DarkCharcoal)
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = currentItem.title,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Serif,
                                            color = TextPrimary,
                                            textAlign = TextAlign.Center
                                        )

                                        if (currentItem.targetTimeline.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "TARGET: ${currentItem.targetTimeline}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.5.sp,
                                                color = tierTheme.goldLight
                                            )
                                        }

                                        if (currentItem.affirmation.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "“${currentItem.affirmation}”",
                                                fontSize = 13.sp,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                                color = tierTheme.goldLight.copy(alpha = 0.9f),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Bottom Countdown Ring & Completion
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .drawBehind {
                                drawCircle(
                                    color = Color(0xFF222222),
                                    radius = size.minDimension / 2,
                                    style = Stroke(width = 6.dp.toPx())
                                )
                                drawArc(
                                    brush = Brush.sweepGradient(
                                        colors = listOf(
                                            tierTheme.goldPrimary,
                                            tierTheme.goldLight,
                                            tierTheme.goldDark,
                                            tierTheme.goldPrimary
                                        )
                                    ),
                                    startAngle = -90f,
                                    sweepAngle = progress * 360f,
                                    useCenter = false,
                                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$remainingSeconds",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            color = tierTheme.goldLight
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Feel the emotion of achievement as if it is already done.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tierTheme.goldPrimary,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("complete_vision_ritual_early_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Seal Inscription (+50 XP)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Add / Edit Vision Item Dialog with Presets, Upload, and URL support
 */
@Composable
fun AddEditVisionItemDialog(
    initialItem: VisionBoardItemEntity?,
    onDismiss: () -> Unit,
    onSave: (title: String, category: String, imageUrl: String, timeline: String, affirmation: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    var title by remember { mutableStateOf(initialItem?.title ?: "") }
    var category by remember { mutableStateOf(initialItem?.category ?: "Wealth & Abundance") }
    var imageUrl by remember { mutableStateOf(initialItem?.imageUrl ?: "stock_vault_gold") }
    var timeline by remember { mutableStateOf(initialItem?.targetTimeline ?: "") }
    var affirmation by remember { mutableStateOf(initialItem?.affirmation ?: "") }

    var selectedImageTab by remember { mutableIntStateOf(0) } // 0: Curated Presets, 1: Upload from Gallery, 2: Image URL
    var customUrlInput by remember { mutableStateOf(if (initialItem?.imageUrl?.startsWith("http") == true) initialItem.imageUrl else "") }

    // Image Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUrl = uri.toString()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) DarkCard else LightCard
            ),
            border = BorderStroke(
                1.dp,
                if (isDark) DarkBorder else LightBorder
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .testTag("add_edit_vision_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (initialItem != null) "EDIT VISION GOAL" else "INSCRIBE VISION GOAL",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 0.5.sp,
                            color = if (isDark) tierTheme.goldLight else tierTheme.goldDark
                        )
                        Text(
                            text = "Anchor your desire into clear visual form",
                            fontSize = 11.sp,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Form Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Title Input
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Goal Title / Caption *") },
                        placeholder = { Text("e.g. $10M Liquid Sovereign Reserve") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = tierTheme.goldPrimary,
                            unfocusedBorderColor = if (isDark) DarkBorder else LightBorder,
                            focusedLabelColor = tierTheme.goldPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("vision_title_input")
                    )

                    // Category Selector
                    Column {
                        Text(
                            text = "Category",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            VISION_CATEGORIES.filter { it.id != "all" }.forEach { cat ->
                                val isSelected = category.equals(cat.name, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { category = cat.name },
                                    label = { Text(cat.name, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = if (isDark) tierTheme.goldDark else tierTheme.goldPrimary,
                                        selectedLabelColor = if (isDark) tierTheme.goldLight else Color.White
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                            }
                        }
                    }

                    // Image Selector Deck
                    Column {
                        Text(
                            text = "Vision Image & Artwork",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Image Source Tabs
                        ScrollableTabRow(
                            selectedTabIndex = selectedImageTab,
                            containerColor = if (isDark) SurfaceElevated else LightElevated,
                            contentColor = tierTheme.goldLight,
                            edgePadding = 0.dp,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedImageTab]),
                                    color = tierTheme.goldPrimary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Tab(
                                selected = selectedImageTab == 0,
                                onClick = { selectedImageTab = 0 },
                                text = { Text("Curated Gallery", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                icon = { Icon(Icons.Filled.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            )
                            Tab(
                                selected = selectedImageTab == 1,
                                onClick = { selectedImageTab = 1 },
                                text = { Text("Upload Photo", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                icon = { Icon(Icons.Filled.Upload, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            )
                            Tab(
                                selected = selectedImageTab == 2,
                                onClick = { selectedImageTab = 2 },
                                text = { Text("Custom URL", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                icon = { Icon(Icons.Filled.Link, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tab 0: Curated Presets
                        if (selectedImageTab == 0) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            ) {
                                items(VISION_STOCK_PRESETS.size) { index ->
                                    val preset = VISION_STOCK_PRESETS[index]
                                    val isChosen = imageUrl == preset.id

                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = DarkCharcoal),
                                        border = BorderStroke(
                                            width = if (isChosen) 2.dp else 1.dp,
                                            color = if (isChosen) tierTheme.goldPrimary else (if (isDark) DarkBorder else LightBorder)
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clickable {
                                                imageUrl = preset.id
                                                if (title.isBlank()) title = preset.title
                                                if (category.isBlank()) category = preset.category
                                                if (timeline.isBlank()) timeline = preset.defaultTimeline
                                                if (affirmation.isBlank()) affirmation = preset.defaultAffirmation
                                            }
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(preset.webImageUrl)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = preset.title,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Transparent,
                                                                RichBlack.copy(alpha = 0.75f)
                                                            )
                                                        )
                                                    )
                                            )

                                            if (isChosen) {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(4.dp)
                                                        .size(20.dp)
                                                        .clip(CircleShape)
                                                        .background(tierTheme.goldPrimary)
                                                        .align(Alignment.TopEnd),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Filled.Check,
                                                        contentDescription = null,
                                                        tint = RichBlack,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = preset.title,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Tab 1: Upload from Gallery
                        if (selectedImageTab == 1) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (isDark) SurfaceElevated else LightElevated),
                                border = BorderStroke(1.dp, if (isDark) DarkBorder else LightBorder),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (imageUrl.startsWith("content://") || imageUrl.startsWith("file://")) {
                                        Box(
                                            modifier = Modifier
                                                .size(90.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .border(1.dp, tierTheme.goldPrimary, RoundedCornerShape(10.dp))
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(LocalContext.current)
                                                    .data(imageUrl)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = "Uploaded preview",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Image Selected from Device",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tierTheme.goldLight
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Filled.Upload,
                                            contentDescription = null,
                                            tint = tierTheme.goldPrimary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Select an image or photo from your device",
                                            fontSize = 12.sp,
                                            color = if (isDark) TextSecondary else LightTextSecondary
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = { photoPickerLauncher.launch("image/*") },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                                            contentColor = if (isDark) RichBlack else Color.White
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Browse Gallery / Photos")
                                    }
                                }
                            }
                        }

                        // Tab 2: Custom URL
                        if (selectedImageTab == 2) {
                            Column {
                                OutlinedTextField(
                                    value = customUrlInput,
                                    onValueChange = {
                                        customUrlInput = it
                                        imageUrl = it
                                    },
                                    label = { Text("Direct Image URL") },
                                    placeholder = { Text("https://example.com/vision.jpg") },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = tierTheme.goldPrimary,
                                        unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (customUrlInput.startsWith("http")) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .border(1.dp, tierTheme.goldPrimary, RoundedCornerShape(10.dp))
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(customUrlInput)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "URL Preview",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Target Timeline Input
                    OutlinedTextField(
                        value = timeline,
                        onValueChange = { timeline = it },
                        label = { Text("Target Timeline / Realization Date") },
                        placeholder = { Text("e.g. By Dec 2027, Q4 2028") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = tierTheme.goldPrimary,
                            unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Subconscious Autosuggestion Affirmation
                    OutlinedTextField(
                        value = affirmation,
                        onValueChange = { affirmation = it },
                        label = { Text("Subconscious Autosuggestion Anchor") },
                        placeholder = { Text("e.g. I hold complete mastery over my financial destiny.") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = tierTheme.goldPrimary,
                            unfocusedBorderColor = if (isDark) DarkBorder else LightBorder
                        ),
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "Cancel",
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(title, category, imageUrl, timeline, affirmation)
                            }
                        },
                        enabled = title.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("save_vision_item_button")
                    ) {
                        Text(
                            text = if (initialItem != null) "Update Inscription" else "Inscribe Goal (+15 XP)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Single Item Focused Contemplation Modal
 */
@Composable
fun SingleVisionItemModal(
    item: VisionBoardItemEntity,
    onDismiss: () -> Unit,
    onStartFullRitual: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val preset = remember(item.imageUrl) { getStockPresetById(item.imageUrl) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDark) DarkCard else LightCard),
            border = BorderStroke(1.5.dp, tierTheme.goldPrimary),
            shape = RoundedCornerShape(24.dp),
            modifier = modifier
                .fillMaxWidth()
                .widthIn(max = 440.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.3f)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                ) {
                    if (item.imageUrl.startsWith("http") || item.imageUrl.startsWith("content://") || item.imageUrl.startsWith("file://")) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else if (preset != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(preset.webImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(RichBlack.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = tierTheme.goldPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = if (isDark) TextPrimary else LightTextPrimary
                    )

                    if (item.targetTimeline.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Target Timeline: ${item.targetTimeline}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = tierTheme.goldLight
                        )
                    }

                    if (item.affirmation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "“${item.affirmation}”",
                            fontSize = 13.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = if (isDark) TextSecondary else LightTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onStartFullRitual,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                            contentColor = if (isDark) RichBlack else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.SelfImprovement, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start 60-Second Visualization Ritual (+50 XP)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
