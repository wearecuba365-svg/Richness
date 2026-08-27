package com.example.ui.components

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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import com.example.data.model.ModuleEntity
import com.example.data.model.UserProfileEntity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.GoldChampagne
import com.example.ui.theme.LightAlabaster
import com.example.ui.theme.LightBorder
import com.example.ui.theme.LightCardGradient
import com.example.ui.theme.LightCardSurface
import com.example.ui.theme.LightElevated
import com.example.ui.theme.LightHighlight
import com.example.ui.theme.LightIvory
import com.example.ui.theme.LightSurface
import com.example.ui.theme.LightSurfaceGradient
import com.example.ui.theme.LightTextMuted
import com.example.ui.theme.LightTextPrimary
import com.example.ui.theme.LightTextSecondary
import com.example.ui.theme.LocalAppThemeMode
import com.example.ui.theme.LocalIsDarkTheme
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.BrushedCardGradient
import com.example.ui.theme.BrushedMetal
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.DarkSurfaceGradient
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldLinearGradient
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.LocalTierGoldTheme
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TierArchitect
import com.example.ui.theme.TierBuilder
import com.example.ui.theme.TierGoldTheme
import com.example.ui.theme.TierLegacy
import com.example.ui.theme.TierNovice
import com.example.ui.theme.TierSovereign
import com.example.ui.theme.getTierGoldIntensity
import com.example.ui.theme.rememberAnimatedTierGoldTheme
import com.example.ui.viewmodel.ScreenRoute
import kotlin.random.Random

@Composable
fun LuxuryScaffold(
    currentRoute: ScreenRoute,
    userProfile: UserProfileEntity?,
    celebrationMessage: String?,
    isAmbientSoundPlaying: Boolean,
    onNavigate: (ScreenRoute) -> Unit,
    onToggleAmbientSound: () -> Unit,
    onOpenPaywall: () -> Unit,
    onClearCelebration: () -> Unit,
    onCycleTheme: () -> Unit = {},
    onSelectTheme: (AppThemeMode) -> Unit = {},
    isFloatingMoneyBubblesEnabled: Boolean = true,
    onToggleFloatingMoneyBubbles: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val dynamicTierTheme = rememberAnimatedTierGoldTheme(userProfile)
    val isDark = LocalIsDarkTheme.current
    val windowInfo = rememberWindowSizeInfo()
    val showSideNav = windowInfo.isDesktop && currentRoute !is ScreenRoute.Landing && currentRoute !is ScreenRoute.Assessment

    CompositionLocalProvider(
        LocalTierGoldTheme provides dynamicTierTheme,
        LocalWindowSizeInfo provides windowInfo
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = if (isDark) RichBlack else LightAlabaster,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                LuxuryTopAppBar(
                    userProfile = userProfile,
                    currentRoute = currentRoute,
                    isAmbientSoundPlaying = isAmbientSoundPlaying,
                    onToggleAmbientSound = onToggleAmbientSound,
                    onOpenPaywall = onOpenPaywall,
                    onCycleTheme = onCycleTheme,
                    onSelectTheme = onSelectTheme,
                    isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
                    onToggleFloatingMoneyBubbles = onToggleFloatingMoneyBubbles,
                    onNavigateToAdmin = { onNavigate(ScreenRoute.AdminPanel) },
                    onNavigateToLanding = { onNavigate(ScreenRoute.Landing) },
                    onNavigateToLeaderboard = { onNavigate(ScreenRoute.Leaderboard) },
                    onNavigateToSuccessLibrary = { onNavigate(ScreenRoute.SuccessStoryLibrary()) },
                    onNavigateToIncomeIdeaExplorer = { onNavigate(ScreenRoute.IncomeIdeaExplorer()) },
                    onNavigateToGivingTracker = { onNavigate(ScreenRoute.GivingTracker) }
                )
            },
            bottomBar = {
                // Show bottom navigation on mobile & tablet (collapsed on mobile/tablet, hidden on desktop rail)
                if (!showSideNav && currentRoute !is ScreenRoute.Landing && currentRoute !is ScreenRoute.Assessment) {
                    LuxuryBottomNavigation(
                        currentRoute = currentRoute,
                        onNavigate = onNavigate
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Desktop Navigation Rail / Sidebar on large displays (>= 1024dp)
                if (showSideNav) {
                    LuxuryNavigationRail(
                        currentRoute = currentRoute,
                        onNavigate = onNavigate
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(if (isDark) DarkSurfaceGradient else LightSurfaceGradient)
                ) {
                    // Dynamic Top Ambient Gold Bloom Aura (Intensifies with Tier)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        dynamicTierTheme.goldGlow.copy(alpha = dynamicTierTheme.glowAlpha * (if (isDark) 0.35f else 0.20f)),
                                        dynamicTierTheme.goldAura.copy(alpha = dynamicTierTheme.glowAlpha * (if (isDark) 0.15f else 0.08f)),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Ambient subtle background floating money bubbles (drifting gold coins & currency symbols)
                    FloatingMoneyBubblesBackground(
                        enabled = isFloatingMoneyBubblesEnabled,
                        tierTheme = dynamicTierTheme
                    )

                    // Ambient subtle background embers modulated by user tier
                    EmberParticlesCanvas(tierTheme = dynamicTierTheme)

                    // Center-constrained responsive content wrapper for tablet/desktop readability
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .widthIn(max = 1200.dp)
                        ) {
                            content()
                        }
                    }

                    // XP Celebration Toast
                    androidx.compose.animation.AnimatedVisibility(
                        visible = celebrationMessage != null,
                        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                    ) {
                        if (celebrationMessage != null) {
                            XpCelebrationBanner(
                                message = celebrationMessage,
                                onDismiss = onClearCelebration
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LuxuryTopAppBar(
    userProfile: UserProfileEntity?,
    currentRoute: ScreenRoute,
    isAmbientSoundPlaying: Boolean,
    onToggleAmbientSound: () -> Unit,
    onOpenPaywall: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToLanding: () -> Unit,
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToSuccessLibrary: () -> Unit = {},
    onNavigateToIncomeIdeaExplorer: () -> Unit = {},
    onNavigateToGivingTracker: () -> Unit = {},
    onCycleTheme: () -> Unit = {},
    onSelectTheme: (AppThemeMode) -> Unit = {},
    isFloatingMoneyBubblesEnabled: Boolean = true,
    onToggleFloatingMoneyBubbles: () -> Unit = {}
) {
    val tierTheme = LocalTierGoldTheme.current
    val isHighTier = tierTheme.rankLevel >= 4
    val isDark = LocalIsDarkTheme.current
    val currentThemeMode = LocalAppThemeMode.current
    val windowInfo = LocalWindowSizeInfo.current
    var showThemeDialog by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }

    // Check if we need condensed top navigation (compact phones < 480dp width)
    val isCompactScreen = windowInfo.screenWidthDp < 480.dp || windowInfo.isSmallPhone

    Surface(
        color = if (isDark) DarkCharcoal else LightIvory,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = (tierTheme.glowAlpha * 1.5f).coerceAtLeast(0.8f).dp,
                color = if (isDark) tierTheme.darkBorder else LightBorder,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
            ),
        shadowElevation = if (isDark) (8.dp + (tierTheme.rankLevel * 2).dp) else 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Top + WindowInsetsSides.Horizontal
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (windowInfo.isSmallPhone) 10.dp else 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            // Brand Logo & Title with Tier Aura Glow
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .clickable { onNavigateToLanding() }
                    .testTag("app_header_brand")
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    // Pulsing Halo for Sovereign / Legacy
                    if (isHighTier) {
                        Box(
                            modifier = Modifier
                                .size(if (windowInfo.isSmallPhone) 38.dp else 46.dp)
                                .clip(CircleShape)
                                .background(tierTheme.goldGlow.copy(alpha = tierTheme.haloIntensity * (if (isDark) 0.5f else 0.25f)))
                        )
                    }

                    // Crest Medallion with Dynamic Border Luster
                    Box(
                        modifier = Modifier
                            .size(if (windowInfo.isSmallPhone) 34.dp else 38.dp)
                            .clip(CircleShape)
                            .background(if (isDark) tierTheme.goldDark else tierTheme.goldPrimary)
                            .border(tierTheme.crestBorderWidth, if (isDark) tierTheme.goldLight else GoldChampagne, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Diamond,
                            contentDescription = "Richlogy Crest",
                            tint = if (isDark) tierTheme.goldLight else Color.White,
                            modifier = Modifier.size(if (windowInfo.isSmallPhone) 18.dp else 22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "RICHLOGY",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (windowInfo.isSmallPhone) 13.5.sp else 15.sp,
                            letterSpacing = 1.2.sp,
                            color = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        // Mini Tier Chip
                        Surface(
                            color = tierTheme.goldPrimary.copy(alpha = if (isDark) 0.18f else 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(0.8.dp, if (isDark) tierTheme.darkBorder else LightBorder)
                        ) {
                            Text(
                                text = tierTheme.tier.title.uppercase(),
                                fontSize = if (windowInfo.isSmallPhone) 8.sp else 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) tierTheme.goldPrimary else tierTheme.goldDark,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "THE WEALTH CODE",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = if (windowInfo.isSmallPhone) 8.5.sp else 10.sp,
                        letterSpacing = 0.8.sp,
                        color = if (isDark) TextSecondary else LightTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Right Action Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // UI Theme Mode Switcher Toggle Button (Dark / Light / System) - Always accessible
                IconButton(
                    onClick = { showThemeDialog = true },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isDark) SurfaceElevated else LightElevated)
                        .border(0.8.dp, if (isDark) DarkBorder else LightBorder, CircleShape)
                        .testTag("theme_mode_toggle_button")
                ) {
                    val themeIcon = when (currentThemeMode) {
                        AppThemeMode.DARK -> Icons.Filled.DarkMode
                        AppThemeMode.LIGHT -> Icons.Filled.LightMode
                        AppThemeMode.SYSTEM -> Icons.Filled.SettingsBrightness
                    }
                    val iconTint = if (isDark) GoldLight else tierTheme.goldDark
                    Icon(
                        imageVector = themeIcon,
                        contentDescription = "Theme: ${currentThemeMode.title}",
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (isCompactScreen) {
                    // Mobile & Compact Phones: Condense Secondary Actions into a Gold Overflow Kebab Menu
                    Box {
                        IconButton(
                            onClick = { showOverflowMenu = true },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isDark) SurfaceElevated else LightElevated)
                                .border(0.8.dp, if (isDark) DarkBorder else LightBorder, CircleShape)
                                .testTag("top_app_bar_overflow_menu")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "More Actions",
                                tint = if (isDark) GoldLight else tierTheme.goldDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showOverflowMenu,
                            onDismissRequest = { showOverflowMenu = false },
                            modifier = Modifier
                                .background(if (isDark) DarkCharcoal else LightIvory)
                                .border(1.dp, if (isDark) DarkBorder else LightBorder, RoundedCornerShape(12.dp))
                        ) {
                            // Ambient Sound Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (isAmbientSoundPlaying) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                                            contentDescription = null,
                                            tint = if (isAmbientSoundPlaying) GoldLight else (if (isDark) TextSecondary else LightTextSecondary),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = if (isAmbientSoundPlaying) "Ritual Sound: ON" else "Ritual Sound: OFF",
                                            fontSize = 13.sp,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onToggleAmbientSound()
                                }
                            )

                            // Unlock Sovereign Access Item
                            if (userProfile?.isPaidUnlocked != true) {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Filled.WorkspacePremium,
                                                contentDescription = null,
                                                tint = AmberBright,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Unlock Sovereign Pass",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AmberBright
                                            )
                                        }
                                    },
                                    onClick = {
                                        showOverflowMenu = false
                                        onOpenPaywall()
                                    }
                                )
                            }

                            // Success Story Library Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                            contentDescription = null,
                                            tint = if (isDark) AmberBright else tierTheme.goldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Success Story Library",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onNavigateToSuccessLibrary()
                                }
                            )

                            // Income Idea Explorer Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Lightbulb,
                                            contentDescription = null,
                                            tint = if (isDark) AmberBright else tierTheme.goldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Income Idea Explorer",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onNavigateToIncomeIdeaExplorer()
                                }
                            )

                            // Gratitude & Giving Tracker Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.VolunteerActivism,
                                            contentDescription = null,
                                            tint = if (isDark) GoldLight else tierTheme.goldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Gratitude & Giving Tracker",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onNavigateToGivingTracker()
                                }
                            )

                            // Sovereign Leaderboard Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.EmojiEvents,
                                            contentDescription = null,
                                            tint = if (isDark) GoldLight else tierTheme.goldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Sovereign Leaderboard",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onNavigateToLeaderboard()
                                }
                            )

                            // Admin Console Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.AdminPanelSettings,
                                            contentDescription = null,
                                            tint = if (isDark) GoldLight else tierTheme.goldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Architect Admin Panel",
                                            fontSize = 13.sp,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onNavigateToAdmin()
                                }
                            )

                            // Landing Screen Item
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Home,
                                            contentDescription = null,
                                            tint = if (isDark) TextSecondary else LightTextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Landing Portal",
                                            fontSize = 13.sp,
                                            color = if (isDark) TextPrimary else LightTextPrimary
                                        )
                                    }
                                },
                                onClick = {
                                    showOverflowMenu = false
                                    onNavigateToLanding()
                                }
                            )
                        }
                    }
                } else {
                    // Wider Screens: Full Inline Action Controls
                    IconButton(
                        onClick = onToggleAmbientSound,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isAmbientSoundPlaying) (if (isDark) GoldDark else AmberAccent) else (if (isDark) SurfaceElevated else LightElevated))
                            .testTag("ambient_sound_toggle")
                    ) {
                        Icon(
                            imageVector = if (isAmbientSoundPlaying) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                            contentDescription = "Ambient Ritual Sound",
                            tint = if (isAmbientSoundPlaying) (if (isDark) GoldLight else Color.White) else (if (isDark) TextSecondary else LightTextSecondary),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (userProfile?.isPaidUnlocked != true) {
                        Button(
                            onClick = onOpenPaywall,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmberAccent,
                                contentColor = RichBlack
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .testTag("unlock_pill_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.WorkspacePremium,
                                contentDescription = "Unlock",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "UNLOCK",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onNavigateToSuccessLibrary,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isDark) SurfaceElevated else LightElevated)
                            .testTag("success_library_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Success Story Library",
                            tint = if (isDark) AmberBright else GoldDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onNavigateToIncomeIdeaExplorer,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isDark) SurfaceElevated else LightElevated)
                            .testTag("income_idea_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = "Income Idea Explorer",
                            tint = if (isDark) AmberBright else GoldDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onNavigateToAdmin,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (userProfile?.role == "admin") (if (isDark) GoldDark else tierTheme.goldPrimary) else (if (isDark) SurfaceElevated else LightElevated))
                            .testTag("admin_panel_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AdminPanelSettings,
                            contentDescription = "Admin Console",
                            tint = if (userProfile?.role == "admin") (if (isDark) GoldLight else Color.White) else (if (isDark) TextMuted else LightTextMuted),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentMode = currentThemeMode,
            isFloatingMoneyBubblesEnabled = isFloatingMoneyBubblesEnabled,
            onSelectMode = { mode ->
                onSelectTheme(mode)
                showThemeDialog = false
            },
            onToggleFloatingMoneyBubbles = onToggleFloatingMoneyBubbles,
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
fun LuxuryNavigationRail(
    currentRoute: ScreenRoute,
    onNavigate: (ScreenRoute) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    NavigationRail(
        containerColor = if (isDark) DarkCharcoal else LightIvory,
        contentColor = if (isDark) GoldLight else tierTheme.goldDark,
        modifier = Modifier
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = if (isDark) DarkBorder else LightBorder
            )
            .testTag("desktop_navigation_rail"),
        header = {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isDark) tierTheme.goldDark else tierTheme.goldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Diamond,
                    contentDescription = "Richlogy",
                    tint = if (isDark) tierTheme.goldLight else Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    ) {
        val navItems = listOf(
            NavigationItemData(
                route = ScreenRoute.Dashboard,
                label = stringResource(R.string.nav_dashboard),
                icon = Icons.Filled.Dashboard,
                testTag = "nav_item_dashboard"
            ),
            NavigationItemData(
                route = ScreenRoute.ModulesPath,
                label = stringResource(R.string.nav_modules),
                icon = Icons.AutoMirrored.Filled.MenuBook,
                testTag = "nav_item_modules"
            ),
            NavigationItemData(
                route = ScreenRoute.Leaderboard,
                label = stringResource(R.string.nav_leaderboard),
                icon = Icons.Filled.EmojiEvents,
                testTag = "nav_item_leaderboard"
            ),
            NavigationItemData(
                route = ScreenRoute.VisionBoard,
                label = stringResource(R.string.nav_vision_board),
                icon = Icons.Filled.Visibility,
                testTag = "nav_item_vision_board"
            ),
            NavigationItemData(
                route = ScreenRoute.MasterMindChat,
                label = stringResource(R.string.nav_ai_council),
                icon = Icons.Filled.AutoAwesome,
                testTag = "nav_item_ai_council"
            ),
            NavigationItemData(
                route = ScreenRoute.Notebook,
                label = stringResource(R.string.nav_notebook),
                icon = Icons.Filled.EditNote,
                testTag = "nav_item_notebook"
            ),
            NavigationItemData(
                route = ScreenRoute.ProfileBadges,
                label = stringResource(R.string.nav_profile),
                icon = Icons.Filled.Person,
                testTag = "nav_item_profile"
            )
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            navItems.forEach { item ->
                val isSelected = when (item.route) {
                    is ScreenRoute.Dashboard -> currentRoute is ScreenRoute.Dashboard
                    is ScreenRoute.ModulesPath -> currentRoute is ScreenRoute.ModulesPath || currentRoute is ScreenRoute.ModuleDetail
                    is ScreenRoute.Leaderboard -> currentRoute is ScreenRoute.Leaderboard
                    is ScreenRoute.VisionBoard -> currentRoute is ScreenRoute.VisionBoard
                    is ScreenRoute.MasterMindChat -> currentRoute is ScreenRoute.MasterMindChat
                    is ScreenRoute.Notebook -> currentRoute is ScreenRoute.Notebook
                    is ScreenRoute.ProfileBadges -> currentRoute is ScreenRoute.ProfileBadges
                    else -> false
                }

                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = if (isDark) GoldLight else tierTheme.goldDark,
                        selectedTextColor = if (isDark) GoldLight else tierTheme.goldDark,
                        indicatorColor = if (isDark) SurfaceElevated else LightElevated,
                        unselectedIconColor = if (isDark) TextSecondary else LightTextSecondary,
                        unselectedTextColor = if (isDark) TextSecondary else LightTextSecondary
                    ),
                    modifier = Modifier.testTag(item.testTag)
                )
            }
        }
    }
}

@Composable
fun LuxuryBottomNavigation(
    currentRoute: ScreenRoute,
    onNavigate: (ScreenRoute) -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    NavigationBar(
        containerColor = if (isDark) DarkCharcoal else LightIvory,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .border(
                1.dp,
                if (isDark) DarkBorder else LightBorder,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .testTag("bottom_navigation"),
        tonalElevation = if (isDark) 12.dp else 4.dp
    ) {
        val navItems = listOf(
            NavigationItemData(
                route = ScreenRoute.Dashboard,
                label = stringResource(R.string.nav_dashboard),
                icon = Icons.Filled.Dashboard,
                testTag = "nav_item_dashboard"
            ),
            NavigationItemData(
                route = ScreenRoute.ModulesPath,
                label = stringResource(R.string.nav_modules),
                icon = Icons.AutoMirrored.Filled.MenuBook,
                testTag = "nav_item_modules"
            ),
            NavigationItemData(
                route = ScreenRoute.Leaderboard,
                label = stringResource(R.string.nav_leaderboard),
                icon = Icons.Filled.EmojiEvents,
                testTag = "nav_item_leaderboard"
            ),
            NavigationItemData(
                route = ScreenRoute.VisionBoard,
                label = stringResource(R.string.nav_vision_board),
                icon = Icons.Filled.Visibility,
                testTag = "nav_item_vision_board"
            ),
            NavigationItemData(
                route = ScreenRoute.MasterMindChat,
                label = stringResource(R.string.nav_ai_council),
                icon = Icons.Filled.AutoAwesome,
                testTag = "nav_item_ai_council"
            ),
            NavigationItemData(
                route = ScreenRoute.Notebook,
                label = stringResource(R.string.nav_notebook),
                icon = Icons.Filled.EditNote,
                testTag = "nav_item_notebook"
            ),
            NavigationItemData(
                route = ScreenRoute.ProfileBadges,
                label = stringResource(R.string.nav_profile),
                icon = Icons.Filled.Person,
                testTag = "nav_item_profile"
            )
        )

        navItems.forEach { item ->
            val isSelected = when (item.route) {
                is ScreenRoute.Dashboard -> currentRoute is ScreenRoute.Dashboard
                is ScreenRoute.ModulesPath -> currentRoute is ScreenRoute.ModulesPath || currentRoute is ScreenRoute.ModuleDetail
                is ScreenRoute.Leaderboard -> currentRoute is ScreenRoute.Leaderboard
                is ScreenRoute.VisionBoard -> currentRoute is ScreenRoute.VisionBoard
                is ScreenRoute.MasterMindChat -> currentRoute is ScreenRoute.MasterMindChat
                is ScreenRoute.Notebook -> currentRoute is ScreenRoute.Notebook
                is ScreenRoute.ProfileBadges -> currentRoute is ScreenRoute.ProfileBadges
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.3.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (isDark) GoldLight else tierTheme.goldDark,
                    selectedTextColor = if (isDark) GoldLight else tierTheme.goldDark,
                    indicatorColor = if (isDark) SurfaceElevated else LightElevated,
                    unselectedIconColor = if (isDark) TextSecondary else LightTextSecondary,
                    unselectedTextColor = if (isDark) TextSecondary else LightTextSecondary
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

@Composable
fun BottomNavigationComponent(
    currentRoute: ScreenRoute,
    onNavigate: (ScreenRoute) -> Unit
) {
    LuxuryBottomNavigation(
        currentRoute = currentRoute,
        onNavigate = onNavigate
    )
}

private data class NavigationItemData(
    val route: ScreenRoute,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)

@Composable
fun CircularXpProgressRing(
    currentXp: Int,
    nextTierThreshold: Int,
    tierName: String,
    modifier: Modifier = Modifier,
    size: Dp = 110.dp
) {
    val tierTheme = LocalTierGoldTheme.current
    val progress = (currentXp.toFloat() / nextTierThreshold.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "xp_progress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val diameter = size.toPx() - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            // Background Track
            drawArc(
                color = SurfaceElevated,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active Glowing Dynamic Gold Progress Arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        tierTheme.goldDark,
                        tierTheme.goldPrimary,
                        tierTheme.goldLight,
                        AmberAccent,
                        tierTheme.goldPrimary
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$currentXp",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = tierTheme.goldLight
            )
            Text(
                text = "XP / $nextTierThreshold",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = TextMuted
            )
        }
    }
}

@Composable
fun TierBadgeChip(
    tier: String,
    modifier: Modifier = Modifier
) {
    val tierTheme = getTierGoldIntensity(tier)
    val (baseColor, icon) = when (tier.lowercase()) {
        "legacy" -> Pair(tierTheme.goldPrimary, Icons.Filled.WorkspacePremium)
        "sovereign" -> Pair(tierTheme.goldPrimary, Icons.Filled.Diamond)
        "architect" -> Pair(TierArchitect, Icons.Filled.AutoAwesome)
        "builder" -> Pair(TierBuilder, Icons.Filled.Star)
        else -> Pair(TierNovice, Icons.Filled.Person)
    }

    val glowAlpha = (tierTheme.glowAlpha * 0.9f).coerceIn(0.4f, 1.0f)
    val bgAlpha = (tierTheme.glowAlpha * 0.22f).coerceIn(0.12f, 0.35f)

    Surface(
        color = baseColor.copy(alpha = bgAlpha),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(tierTheme.badgeBorderWidth, baseColor.copy(alpha = glowAlpha)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tier,
                tint = baseColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = tier.uppercase(),
                color = baseColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
fun BrushedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    borderColor: Color? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current
    val effectiveBorderColor = borderColor ?: (if (isDark) tierTheme.darkBorder else LightBorder)
    val cardBackground = if (isDark) BrushedCardGradient else LightCardGradient
    val cardContainerColor = if (isDark) BrushedMetal else LightCardSurface

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, effectiveBorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardBackground)
                .padding(18.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentMode: AppThemeMode,
    isFloatingMoneyBubblesEnabled: Boolean = true,
    onSelectMode: (AppThemeMode) -> Unit,
    onToggleFloatingMoneyBubbles: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable { onDismiss() }
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 440.dp)
                    .clickable(enabled = false) {}
                    .border(1.2.dp, if (isDark) tierTheme.darkBorder else LightBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) DarkCharcoal else LightIvory)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(tierTheme.goldPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SettingsBrightness,
                                    contentDescription = null,
                                    tint = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "SELECT UI THEME",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    color = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Choose your preferred appearance",
                                    fontSize = 11.sp,
                                    color = if (isDark) TextMuted else LightTextMuted
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = if (isDark) TextMuted else LightTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppThemeMode.entries.forEach { mode ->
                        val isSelected = mode == currentMode
                        val icon = when (mode) {
                            AppThemeMode.DARK -> Icons.Filled.DarkMode
                            AppThemeMode.LIGHT -> Icons.Filled.LightMode
                            AppThemeMode.SYSTEM -> Icons.Filled.SettingsBrightness
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) {
                                tierTheme.goldPrimary.copy(alpha = if (isDark) 0.20f else 0.15f)
                            } else {
                                if (isDark) SurfaceElevated else LightSurface
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isSelected) 1.5.dp else 0.8.dp,
                                color = if (isSelected) (if (isDark) tierTheme.goldLight else tierTheme.goldDark) else (if (isDark) DarkBorder else LightBorder)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onSelectMode(mode) }
                                .testTag("theme_dialog_option_${mode.name.lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) tierTheme.goldPrimary else (if (isDark) DarkCharcoal else LightElevated)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = if (isSelected) (if (isDark) RichBlack else Color.White) else (if (isDark) TextSecondary else LightTextSecondary),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = mode.title,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 13.sp,
                                            color = if (isSelected) (if (isDark) tierTheme.goldLight else tierTheme.goldDark) else (if (isDark) TextPrimary else LightTextPrimary)
                                        )
                                        Text(
                                            text = mode.subtitle,
                                            fontSize = 10.sp,
                                            color = if (isDark) TextMuted else LightTextMuted
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Selected",
                                        tint = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(
                        color = if (isDark) DarkBorder else LightBorder,
                        thickness = 0.8.dp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Floating Money Bubbles Toggle Row in Dialog
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDark) SurfaceElevated else LightSurface)
                            .border(0.8.dp, if (isDark) DarkBorder else LightBorder, RoundedCornerShape(12.dp))
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
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(tierTheme.goldPrimary.copy(alpha = if (isDark) 0.25f else 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AttachMoney,
                                    contentDescription = null,
                                    tint = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Floating Money Bubbles",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) TextPrimary else LightTextPrimary
                                )
                                Text(
                                    text = "Ambient rising gold coin particles",
                                    fontSize = 10.sp,
                                    color = if (isDark) TextMuted else LightTextMuted
                                )
                            }
                        }

                        Switch(
                            checked = isFloatingMoneyBubblesEnabled,
                            onCheckedChange = { onToggleFloatingMoneyBubbles() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = LightIvory,
                                checkedTrackColor = tierTheme.goldPrimary,
                                checkedBorderColor = tierTheme.goldDark,
                                uncheckedThumbColor = if (isDark) TextMuted else LightTextMuted,
                                uncheckedTrackColor = if (isDark) DarkCharcoal else LightElevated,
                                uncheckedBorderColor = if (isDark) DarkBorder else LightBorder
                            ),
                            modifier = Modifier.testTag("dialog_money_bubbles_switch")
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeSelectorCard(
    currentThemeMode: AppThemeMode,
    onThemeChange: (AppThemeMode) -> Unit,
    isFloatingMoneyBubblesEnabled: Boolean = true,
    onToggleFloatingMoneyBubbles: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDarkTheme.current
    val tierTheme = LocalTierGoldTheme.current

    BrushedCard(modifier = modifier) {
        // --- HEADER WITH TOGGLE SWITCH ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isDark) tierTheme.goldDark.copy(alpha = 0.35f) else GoldChampagne.copy(alpha = 0.5f))
                        .border(1.dp, if (isDark) tierTheme.goldLight else tierTheme.goldDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (currentThemeMode) {
                            AppThemeMode.DARK -> Icons.Filled.DarkMode
                            AppThemeMode.LIGHT -> Icons.Filled.LightMode
                            AppThemeMode.SYSTEM -> Icons.Filled.SettingsBrightness
                        },
                        contentDescription = "Theme Icon",
                        tint = if (isDark) GoldLight else tierTheme.goldDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "THEME & PALETTE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) GoldLight else tierTheme.goldDark,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = when (currentThemeMode) {
                            AppThemeMode.DARK -> "Sovereign Matte-Black & 24K Gold"
                            AppThemeMode.LIGHT -> "Imperial Ivory & Antique Gold"
                            AppThemeMode.SYSTEM -> "Syncs with Android System Default"
                        },
                        fontSize = 11.sp,
                        color = if (isDark) TextSecondary else LightTextSecondary
                    )
                }
            }

            // Quick Light / Dark Mode Toggle Switch
            Switch(
                checked = currentThemeMode == AppThemeMode.LIGHT,
                onCheckedChange = { isLight ->
                    onThemeChange(if (isLight) AppThemeMode.LIGHT else AppThemeMode.DARK)
                },
                thumbContent = {
                    Icon(
                        imageVector = if (isDark) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = if (isDark) tierTheme.goldLight else tierTheme.goldDark
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LightIvory,
                    checkedTrackColor = tierTheme.goldPrimary,
                    checkedBorderColor = tierTheme.goldDark,
                    uncheckedThumbColor = GoldLight,
                    uncheckedTrackColor = DarkCharcoal,
                    uncheckedBorderColor = tierTheme.darkBorder
                ),
                modifier = Modifier.testTag("theme_toggle_switch")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // --- 3 SEGMENTED BUTTONS (DARK / LIGHT / SYSTEM) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isDark) DarkCharcoal else LightElevated)
                .border(1.dp, if (isDark) DarkBorder else LightBorder, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val options = listOf(
                Triple(AppThemeMode.DARK, Icons.Filled.DarkMode, "theme_toggle_dark"),
                Triple(AppThemeMode.LIGHT, Icons.Filled.LightMode, "theme_toggle_light"),
                Triple(AppThemeMode.SYSTEM, Icons.Filled.SettingsBrightness, "theme_toggle_system")
            )

            options.forEach { (mode, icon, tag) ->
                val isSelected = currentThemeMode == mode
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) (if (isDark) tierTheme.goldDark else tierTheme.goldPrimary) else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable { onThemeChange(mode) }
                        .testTag(tag)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = mode.title,
                            tint = if (isSelected) (if (isDark) tierTheme.goldLight else Color.White) else (if (isDark) TextMuted else LightTextMuted),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = mode.title,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) (if (isDark) tierTheme.goldLight else Color.White) else (if (isDark) TextSecondary else LightTextSecondary)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- LIVE DYNAMIC PALETTE SWATCH PREVIEW ---
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isDark) DarkCharcoal.copy(alpha = 0.7f) else LightElevated.copy(alpha = 0.8f),
            border = androidx.compose.foundation.BorderStroke(0.6.dp, if (isDark) DarkBorder else LightBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE PALETTE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) TextMuted else LightTextMuted,
                    letterSpacing = 0.5.sp
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Swatch 1: Canvas
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (isDark) RichBlack else LightAlabaster)
                            .border(0.8.dp, if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.2f), CircleShape)
                    )
                    // Swatch 2: Surface
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (isDark) DarkCharcoal else LightIvory)
                            .border(0.8.dp, if (isDark) Color.White.copy(alpha = 0.2f) else Color.Black.copy(alpha = 0.2f), CircleShape)
                    )
                    // Swatch 3: Gold Accent
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (isDark) GoldLight else tierTheme.goldPrimary)
                    )
                    // Swatch 4: Obsidian/White Text
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(if (isDark) TextPrimary else LightTextPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- FLOATING MONEY BUBBLES BACKGROUND ANIMATION TOGGLE ---
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isDark) DarkCharcoal.copy(alpha = 0.7f) else LightElevated.copy(alpha = 0.8f),
            border = androidx.compose.foundation.BorderStroke(0.6.dp, if (isDark) DarkBorder else LightBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(tierTheme.goldPrimary.copy(alpha = if (isDark) 0.25f else 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AttachMoney,
                            contentDescription = null,
                            tint = if (isDark) tierTheme.goldLight else tierTheme.goldDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Floating Money Bubbles",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) TextPrimary else LightTextPrimary
                        )
                        Text(
                            text = if (isFloatingMoneyBubblesEnabled) "Enabled: subtle gold coin atmosphere" else "Disabled: static luxury background",
                            fontSize = 9.5.sp,
                            color = if (isDark) TextMuted else LightTextMuted
                        )
                    }
                }

                Switch(
                    checked = isFloatingMoneyBubblesEnabled,
                    onCheckedChange = { onToggleFloatingMoneyBubbles() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = LightIvory,
                        checkedTrackColor = tierTheme.goldPrimary,
                        checkedBorderColor = tierTheme.goldDark,
                        uncheckedThumbColor = if (isDark) TextMuted else LightTextMuted,
                        uncheckedTrackColor = if (isDark) DarkCharcoal else LightElevated,
                        uncheckedBorderColor = if (isDark) DarkBorder else LightBorder
                    ),
                    modifier = Modifier.testTag("floating_money_bubbles_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (currentThemeMode) {
                AppThemeMode.DARK -> "Active: Sovereign Matte-Black canvas with 24K radiant gold accents."
                AppThemeMode.LIGHT -> "Active: Imperial Alabaster & Ivory canvas with antique gold luster."
                AppThemeMode.SYSTEM -> "Active: Automatically synchronizes between Dark and Light based on your Android system settings."
            },
            fontSize = 10.sp,
            color = if (isDark) TextMuted else LightTextMuted
        )
    }
}

@Composable
fun EmberParticlesCanvas(
    tierTheme: TierGoldTheme = LocalTierGoldTheme.current
) {
    val infiniteTransition = rememberInfiniteTransition(label = "embers")
    val animVal by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(tierTheme.shimmerDurationMs.coerceAtLeast(1000) * 3, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ember_phase"
    )

    // Dynamic deterministic ember particles determined by tier rank
    val totalCount = tierTheme.emberParticleCount.coerceIn(4, 36)
    val particles = remember(totalCount) {
        List(totalCount) { index ->
            val xRatio = ((index * 0.137f + 0.08f) % 0.92f) + 0.04f
            val yStartRatio = ((index * 0.231f + 0.15f) % 0.90f) + 0.05f
            val radius = 1.5f + (index % 4) * 0.9f
            Triple(xRatio, yStartRatio, radius)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEachIndexed { index, (xRatio, yStartRatio, radius) ->
            val phaseOffset = (index * (1f / totalCount))
            val currentProgress = (animVal + phaseOffset) % 1f
            val yPos = height * ((yStartRatio - currentProgress + 1f) % 1f)
            val xPos = width * (xRatio + (kotlin.math.sin(currentProgress * 6.28 + index) * 0.04).toFloat())
            val alpha = ((1f - (yPos / height)) * tierTheme.glowAlpha).coerceIn(0.08f, 0.75f)

            val particleColor = when (index % 3) {
                0 -> tierTheme.goldPrimary
                1 -> tierTheme.goldLight
                else -> AmberBright
            }

            drawCircle(
                color = particleColor.copy(alpha = alpha),
                radius = radius.dp.toPx() * (1f + (tierTheme.rankLevel - 1) * 0.12f),
                center = Offset(xPos, yPos)
            )
        }
    }
}

@Composable
fun XpCelebrationBanner(
    message: String,
    onDismiss: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DarkCharcoal,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldLight),
        shadowElevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDismiss() }
            .testTag("celebration_banner")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GoldLinearGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.WorkspacePremium,
                    contentDescription = "Reward",
                    tint = RichBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = GoldLight,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Dismiss",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun PaywallBottomSheet(
    onDismiss: () -> Unit,
    onUnlock: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, GoldLight, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCharcoal)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top close icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }

                    // Diamond Crest
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(GoldLinearGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Diamond,
                            contentDescription = "Sovereign Tier",
                            tint = RichBlack,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "THE SOVEREIGN UNLOCK",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp,
                        color = GoldLight,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Unlock All 13 Vaults • Lifetime Sovereign Access",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Value Checklist
                    val perks = listOf(
                        "All 12 Locked Vaults & Masterclasses Unlocked",
                        "13 Interactive Actionable Worksheets & Templates",
                        "Sovereign Quests & High-Leverage Challenges",
                        "Unlimited Notebook Journaling & Mindset History",
                        "Sovereign Tier Profile Flair & Legacy Badge Unlocks",
                        "One-Time Payment • No Recurring Subscriptions"
                    )

                    perks.forEach { perk ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = perk,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Price Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "ONE-TIME PASS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Lifetime Sovereign Access",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                            }
                            Text(
                                text = "$97",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = GoldLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onUnlock,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("paywall_unlock_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberAccent,
                            contentColor = RichBlack
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COMMAND YOUR WEALTH • $97",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Encrypted 256-Bit Security • 30-Day Sovereign Guarantee",
                        fontSize = 10.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun WealthMindsetProgressRing(
    mindsetScore: Int,
    tierName: String,
    modifier: Modifier = Modifier,
    size: Dp = 125.dp,
    showTierBadge: Boolean = true
) {
    val clampedScore = mindsetScore.coerceIn(0, 100)
    val progress = clampedScore.toFloat() / 100f

    // Smooth animate progress arc
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "mindset_progress"
    )

    // Subtle ambient breathing gold shimmer
    val infiniteTransition = rememberInfiniteTransition(label = "gold_ring_shimmer")
    val haloPulse by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_pulse"
    )

    val tierColor = when (tierName.lowercase()) {
        "legacy" -> TierLegacy
        "sovereign" -> TierSovereign
        "architect" -> TierArchitect
        "builder" -> TierBuilder
        else -> TierNovice
    }

    Box(
        modifier = modifier
            .size(size)
            .testTag("wealth_mindset_circular_progress"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 9.dp.toPx()
            val diameter = size.toPx() - (strokeWidth * 2.2f)
            val center = Offset(size.toPx() / 2, size.toPx() / 2)
            val radius = diameter / 2f
            val topLeft = Offset(center.x - radius, center.y - radius)
            val arcSize = Size(diameter, diameter)

            // 1. Ambient Outer Halo Glow Ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GoldLight.copy(alpha = 0.22f * haloPulse),
                        GoldDark.copy(alpha = 0.06f * haloPulse),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.toPx() / 2f
                ),
                radius = size.toPx() / 2f,
                center = center
            )

            // 2. Background Track Ring
            drawArc(
                color = SurfaceElevated,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Subtle gold tick notches (12 radial clock notches)
            val tickCount = 12
            for (i in 0 until tickCount) {
                val angleDeg = (i * 360f / tickCount) - 90f
                val rad = Math.toRadians(angleDeg.toDouble())
                val innerR = radius - (strokeWidth / 2f) - 3.dp.toPx()
                val outerR = radius - (strokeWidth / 2f) + 1.dp.toPx()
                val startX = (center.x + innerR * Math.cos(rad)).toFloat()
                val startY = (center.y + innerR * Math.sin(rad)).toFloat()
                val endX = (center.x + outerR * Math.cos(rad)).toFloat()
                val endY = (center.y + outerR * Math.sin(rad)).toFloat()

                drawLine(
                    color = if (i % 3 == 0) GoldPrimary.copy(alpha = 0.45f) else DarkBorder.copy(alpha = 0.6f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (i % 3 == 0) 2.dp.toPx() else 1.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 4. Active Progress Arc with Sweeping Dynamic Gold Gradient
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            GoldDark,
                            GoldPrimary,
                            GoldLight,
                            AmberAccent,
                            tierColor,
                            GoldLight
                        ),
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // 5. Glowing Head Particle at the Tip of the Arc
                val tipAngleDeg = -90f + (360f * animatedProgress)
                val tipRad = Math.toRadians(tipAngleDeg.toDouble())
                val tipX = (center.x + radius * Math.cos(tipRad)).toFloat()
                val tipY = (center.y + radius * Math.sin(tipRad)).toFloat()

                // Particle outer aura
                drawCircle(
                    color = AmberBright.copy(alpha = 0.5f * haloPulse),
                    radius = strokeWidth * 1.1f,
                    center = Offset(tipX, tipY)
                )
                // Particle bright core
                drawCircle(
                    color = Color(0xFFFFF8E7),
                    radius = strokeWidth * 0.45f,
                    center = Offset(tipX, tipY)
                )
            }
        }

        // Center Content: Score, Max, and Tier
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${(animatedProgress * 100).toInt()}",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (size >= 130.dp) 30.sp else 24.sp,
                    color = GoldLight,
                    lineHeight = if (size >= 130.dp) 30.sp else 24.sp
                )
                Text(
                    text = "/100",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 2.dp, start = 1.dp)
                )
            }

            Text(
                text = "MINDSET SCORE",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 1.sp
            )

            if (showTierBadge) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = tierName.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = tierColor,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

@Composable
fun WealthMindsetScoreCard(
    userProfile: UserProfileEntity?,
    onRetakeAssessment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mindsetScore = userProfile?.mindsetScore ?: 50
    val tierName = userProfile?.tierName ?: "Novice"
    val belief = userProfile?.beliefScore ?: 50
    val discipline = userProfile?.disciplineScore ?: 50
    val desire = userProfile?.desireScore ?: 50
    val persistence = userProfile?.persistenceScore ?: 50
    val identity = userProfile?.identityScore ?: 50

    val tierColor = when (tierName.lowercase()) {
        "legacy" -> TierLegacy
        "sovereign" -> TierSovereign
        "architect" -> TierArchitect
        "builder" -> TierBuilder
        else -> TierNovice
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        GoldDark.copy(alpha = 0.5f),
                        GoldLight.copy(alpha = 0.8f),
                        GoldDark.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .testTag("wealth_mindset_dashboard_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCharcoal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            DarkCharcoal,
                            SurfaceElevated.copy(alpha = 0.65f),
                            DarkCharcoal
                        )
                    )
                )
                .padding(18.dp)
        ) {
            // Header Row: Title & Tier Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(GoldDark, DarkCharcoal)))
                            .border(1.dp, GoldLight.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "WEALTH MINDSET SCORE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Current Tier: $tierName Status",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AmberAccent
                        )
                    }
                }

                TierBadgeChip(tier = tierName)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Content: Circular Progress Ring + Dimensions Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Progress Dial
                WealthMindsetProgressRing(
                    mindsetScore = mindsetScore,
                    tierName = tierName,
                    size = 125.dp,
                    showTierBadge = false
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Mindset 5 Dimensions Breakdown
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MindsetDimensionBar(label = "Belief & Faith", score = belief, color = GoldLight)
                    MindsetDimensionBar(label = "Discipline & Will", score = discipline, color = AmberBright)
                    MindsetDimensionBar(label = "Definite Desire", score = desire, color = GoldPrimary)
                    MindsetDimensionBar(label = "Persistence", score = persistence, color = tierColor)
                    MindsetDimensionBar(label = "Sovereign Identity", score = identity, color = Color(0xFFFFD700))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer Calibrate Assessment Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceElevated)
                    .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                    .clickable { onRetakeAssessment() }
                    .padding(horizontal = 12.dp, vertical = 9.dp)
                    .testTag("calibrate_mindset_button"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Calibrate Diagnostic Assessment",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Retake",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldLight
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = GoldLight,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MindsetDimensionBar(
    label: String,
    score: Int,
    color: Color
) {
    val progress = (score.toFloat() / 100f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "dimension_progress"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$score%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(SurfaceElevated)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(color.copy(alpha = 0.4f), color)
                        )
                    )
            )
        }
    }
}

/**
 * Animated, gold-accented 13-Vault circular progress ring visualizing completion status for all 13 modules.
 */
@Composable
fun ThirteenVaultsCircularProgressRing(
    modules: List<ModuleEntity>,
    modifier: Modifier = Modifier,
    size: Dp = 190.dp,
    selectedVaultIndex: Int? = null,
    onSelectVaultIndex: ((Int) -> Unit)? = null
) {
    val totalVaults = 13
    // Standardize to 13 modules if possible, otherwise use passed modules
    val paddedModules = remember(modules) {
        if (modules.size >= totalVaults) modules.take(totalVaults)
        else {
            val list = modules.toMutableList()
            while (list.size < totalVaults) {
                val idx = list.size
                list.add(
                    ModuleEntity(
                        id = idx,
                        order = idx + 1,
                        title = "Vault ${idx + 1}",
                        originalPrinciple = "Wealth Principle ${idx + 1}",
                        subtitle = "Mastery of Vault ${idx + 1}",
                        isUnlocked = false,
                        isCompleted = false,
                        xpReward = 150,
                        videoTitle = "",
                        videoDuration = "",
                        excerptTitle = "",
                        excerptText = "",
                        keyTakeaways = "",
                        templateTitle = "",
                        templatePrompt = "",
                        templateFieldLabel1 = "",
                        templateFieldLabel2 = "",
                        templateFieldLabel3 = "",
                        questTitle = "",
                        questDescription = "",
                        questActionPrompt = "",
                        notebookPrompt = ""
                    )
                )
            }
            list
        }
    }

    val completedCount = paddedModules.count { it.isCompleted }
    val progressFraction = completedCount.toFloat() / totalVaults.toFloat()

    // Smooth progress animation
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
        label = "thirteen_vaults_progress"
    )

    // Ambient breathing gold shimmer and rotation
    val infiniteTransition = rememberInfiniteTransition(label = "vaults_ring_transition")
    val haloPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring_halo_pulse"
    )

    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ambient_rotation"
    )

    Box(
        modifier = modifier
            .size(size)
            .pointerInput(onSelectVaultIndex) {
                if (onSelectVaultIndex != null) {
                    detectTapGestures { offset ->
                        val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        var angleDeg = Math.toDegrees(Math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
                        // Normalize angle relative to -90 (top)
                        angleDeg = (angleDeg + 90f + 360f) % 360f
                        val slice = 360f / totalVaults
                        val tappedIndex = ((angleDeg / slice).toInt()).coerceIn(0, totalVaults - 1)
                        onSelectVaultIndex(tappedIndex)
                    }
                }
            }
            .testTag("thirteen_vaults_circular_progress_ring"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 9.dp.toPx()
            val diameter = size.toPx() - (strokeWidth * 2.6f)
            val center = Offset(size.toPx() / 2, size.toPx() / 2)
            val radius = diameter / 2f
            val topLeft = Offset(center.x - radius, center.y - radius)
            val arcSize = Size(diameter, diameter)

            // 1. Ambient Radial Gold Glow Bezel
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GoldLight.copy(alpha = 0.20f * haloPulse),
                        GoldDark.copy(alpha = 0.08f * haloPulse),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.toPx() / 2f
                ),
                radius = size.toPx() / 2f,
                center = center
            )

            // 2. Inner Medallion Gradient Background
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        DarkCharcoal,
                        SurfaceElevated,
                        RichBlack
                    ),
                    center = center,
                    radius = radius - strokeWidth
                ),
                radius = radius - strokeWidth,
                center = center
            )

            // Inner subtle border ring
            drawCircle(
                color = GoldDark.copy(alpha = 0.35f),
                radius = radius - strokeWidth,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            // 3. Draw 13 Distinct Module Segments
            val segmentSpan = 360f / totalVaults
            val gapAngle = 4.5f
            val activeArcSpan = segmentSpan - gapAngle

            for (i in 0 until totalVaults) {
                val module = paddedModules.getOrNull(i)
                val isCompleted = module?.isCompleted == true
                val isUnlocked = module?.isUnlocked == true
                val isSelected = selectedVaultIndex == i
                val startAngle = -90f + (i * segmentSpan) + (gapAngle / 2f)

                // Background segment track
                drawArc(
                    color = if (isSelected) GoldDark.copy(alpha = 0.35f) else SurfaceElevated.copy(alpha = 0.85f),
                    startAngle = startAngle,
                    sweepAngle = activeArcSpan,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(
                        width = if (isSelected) strokeWidth * 1.25f else strokeWidth,
                        cap = StrokeCap.Round
                    )
                )

                // Active status rendering for each of the 13 modules
                if (isCompleted) {
                    // Brilliant Emerald-Gold Sovereign Arc for completed modules
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                GoldPrimary,
                                GoldLight,
                                AmberBright,
                                GoldLight
                            ),
                            center = center
                        ),
                        startAngle = startAngle,
                        sweepAngle = activeArcSpan,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(
                            width = if (isSelected) strokeWidth * 1.3f else strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                } else if (isUnlocked) {
                    // Radiant Amber Active Arc for current/in-progress module
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                AmberAccent.copy(alpha = 0.7f),
                                AmberBright,
                                GoldLight
                            ),
                            center = center
                        ),
                        startAngle = startAngle,
                        sweepAngle = activeArcSpan * 0.5f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }

                // 4. Orbital Jewel Marker at the center of each vault segment
                val midAngleDeg = startAngle + (activeArcSpan / 2f)
                val midRad = Math.toRadians(midAngleDeg.toDouble())
                val jewelX = (center.x + radius * Math.cos(midRad)).toFloat()
                val jewelY = (center.y + radius * Math.sin(midRad)).toFloat()

                if (isCompleted) {
                    // Glowing completed jewel
                    drawCircle(
                        color = GoldLight.copy(alpha = 0.6f * haloPulse),
                        radius = (strokeWidth / 2f) + 2.5.dp.toPx(),
                        center = Offset(jewelX, jewelY)
                    )
                    drawCircle(
                        color = Color(0xFFFFFDF0),
                        radius = 2.5.dp.toPx(),
                        center = Offset(jewelX, jewelY)
                    )
                } else if (isUnlocked) {
                    // Pulsating active jewel
                    drawCircle(
                        color = AmberBright.copy(alpha = 0.5f * haloPulse),
                        radius = (strokeWidth / 2f) + 1.5.dp.toPx(),
                        center = Offset(jewelX, jewelY)
                    )
                    drawCircle(
                        color = AmberAccent,
                        radius = 2.dp.toPx(),
                        center = Offset(jewelX, jewelY)
                    )
                } else {
                    // Locked subtle metallic jewel pin
                    drawCircle(
                        color = DarkBorder,
                        radius = 1.5.dp.toPx(),
                        center = Offset(jewelX, jewelY)
                    )
                }

                // Selected highlight indicator
                if (isSelected) {
                    val outerPointerR = radius + strokeWidth + 4.dp.toPx()
                    val pX = (center.x + outerPointerR * Math.cos(midRad)).toFloat()
                    val pY = (center.y + outerPointerR * Math.sin(midRad)).toFloat()
                    drawCircle(
                        color = GoldLight,
                        radius = 3.dp.toPx(),
                        center = Offset(pX, pY)
                    )
                }
            }

            // 5. Subtle ambient spinning particle dot on outer perimeter
            val particleRad = Math.toRadians(ambientRotation.toDouble())
            val orbR = radius + (strokeWidth * 0.9f)
            val orbX = (center.x + orbR * Math.cos(particleRad)).toFloat()
            val orbY = (center.y + orbR * Math.sin(particleRad)).toFloat()
            drawCircle(
                color = GoldLight.copy(alpha = 0.45f * haloPulse),
                radius = 2.dp.toPx(),
                center = Offset(orbX, orbY)
            )
        }

        // Center Content: Medallion with Completed/Total Fraction and Sovereign Label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.WorkspacePremium,
                contentDescription = null,
                tint = GoldLight,
                modifier = Modifier.size(if (size >= 180.dp) 22.dp else 18.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$completedCount",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (size >= 180.dp) 32.sp else 26.sp,
                    color = GoldLight,
                    lineHeight = if (size >= 180.dp) 32.sp else 26.sp
                )
                Text(
                    text = "/13",
                    fontSize = if (size >= 180.dp) 15.sp else 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(bottom = 3.dp, start = 1.dp)
                )
            }

            Text(
                text = "VAULTS MASTERED",
                fontSize = if (size >= 180.dp) 10.sp else 9.sp,
                fontWeight = FontWeight.Bold,
                color = AmberAccent,
                letterSpacing = 1.2.sp
            )

            Text(
                text = "${(animatedProgress * 100).toInt()}% COMPLETE",
                fontSize = if (size >= 180.dp) 9.5.sp else 8.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondary,
                letterSpacing = 0.8.sp
            )
        }
    }
}

/**
 * Animated, gold-accented progress ring for an individual module item / card.
 */
@Composable
fun ModuleVaultCircularProgressRing(
    isUnlocked: Boolean,
    isCompleted: Boolean,
    order: Int,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    progressFraction: Float = if (isCompleted) 1.0f else if (isUnlocked) 0.33f else 0f
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progressFraction,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "single_module_progress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "module_ring_glow")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "module_ring_pulse"
    )

    Box(
        modifier = modifier
            .size(size)
            .testTag("module_vault_progress_ring_$order"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 3.5.dp.toPx()
            val diameter = size.toPx() - (strokeWidth * 2f)
            val center = Offset(size.toPx() / 2, size.toPx() / 2)
            val radius = diameter / 2f
            val topLeft = Offset(center.x - radius, center.y - radius)
            val arcSize = Size(diameter, diameter)

            // Outer subtle halo when completed or unlocked
            if (isCompleted || isUnlocked) {
                drawCircle(
                    color = (if (isCompleted) SuccessGreen else GoldLight).copy(alpha = 0.15f * pulse),
                    radius = size.toPx() / 2f,
                    center = center
                )
            }

            // Track background ring
            drawArc(
                color = if (isUnlocked) SurfaceElevated else DarkBorder.copy(alpha = 0.6f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )

            // Active animated progress arc
            if (animatedProgress > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(
                        colors = if (isCompleted) {
                            listOf(SuccessGreen, GoldLight, SuccessGreen)
                        } else {
                            listOf(GoldDark, GoldLight, AmberBright, GoldLight)
                        },
                        center = center
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                // Tip glow particle
                val tipAngleDeg = -90f + (360f * animatedProgress)
                val tipRad = Math.toRadians(tipAngleDeg.toDouble())
                val tipX = (center.x + radius * Math.cos(tipRad)).toFloat()
                val tipY = (center.y + radius * Math.sin(tipRad)).toFloat()

                drawCircle(
                    color = (if (isCompleted) SuccessGreen else GoldLight).copy(alpha = 0.7f * pulse),
                    radius = strokeWidth * 1.1f,
                    center = Offset(tipX, tipY)
                )
                drawCircle(
                    color = Color.White,
                    radius = strokeWidth * 0.45f,
                    center = Offset(tipX, tipY)
                )
            }
        }

        // Center Content: Checkmark, Order number, or Lock icon
        if (isCompleted) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Completed",
                tint = SuccessGreen,
                modifier = Modifier.size(size * 0.52f)
            )
        } else if (isUnlocked) {
            Text(
                text = "$order",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = if (size >= 44.dp) 16.sp else 13.sp,
                color = GoldLight
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Locked",
                tint = TextMuted,
                modifier = Modifier.size(size * 0.45f)
            )
        }
    }
}

/**
 * Flagship luxury dashboard card featuring the 13-Module Circular Progress Ring with interactive preview.
 */
@Composable
fun ThirteenVaultsMasteryCard(
    modules: List<ModuleEntity>,
    isPaidUnlocked: Boolean,
    onSelectModule: (Int) -> Unit,
    onOpenPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val totalVaults = 13
    val completedCount = modules.count { it.isCompleted }
    val unlockedCount = modules.count { it.isUnlocked }
    val lockedCount = totalVaults - unlockedCount

    val activeModule = remember(selectedIndex, modules) {
        if (selectedIndex != null && selectedIndex!! < modules.size) {
            modules[selectedIndex!!]
        } else {
            modules.firstOrNull { it.isUnlocked && !it.isCompleted } ?: modules.firstOrNull()
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        GoldDark.copy(alpha = 0.5f),
                        GoldLight.copy(alpha = 0.85f),
                        GoldDark.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(22.dp)
            )
            .testTag("thirteen_vaults_mastery_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCharcoal)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            DarkCharcoal,
                            SurfaceElevated.copy(alpha = 0.65f),
                            DarkCharcoal
                        )
                    )
                )
                .padding(18.dp)
        ) {
            // Header: Title & Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(GoldDark, DarkCharcoal)))
                            .border(1.dp, GoldLight.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "THE 13 VAULTS OF WEALTH",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldLight,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Interactive Sovereign Mastery Ring",
                            fontSize = 9.sp,
                            color = TextSecondary
                        )
                    }
                }

                if (!isPaidUnlocked) {
                    Surface(
                        color = AmberAccent,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.clickable { onOpenPaywall() }
                    ) {
                        Text(
                            text = "UNLOCK ALL",
                            color = RichBlack,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                } else {
                    TierBadgeChip(tier = "Sovereign")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Visual: 13-Module Circular Progress Ring + Status Column
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular 13-Module Ring
                ThirteenVaultsCircularProgressRing(
                    modules = modules,
                    size = 150.dp,
                    selectedVaultIndex = selectedIndex ?: activeModule?.order?.minus(1),
                    onSelectVaultIndex = { idx ->
                        selectedIndex = idx
                    }
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Stats & Status Legend Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VaultLegendStatRow(
                        label = "Vaults Mastered",
                        count = completedCount,
                        total = totalVaults,
                        indicatorColor = GoldLight,
                        icon = Icons.Filled.CheckCircle
                    )
                    VaultLegendStatRow(
                        label = "Unlocked Active",
                        count = unlockedCount - completedCount,
                        total = totalVaults,
                        indicatorColor = AmberBright,
                        icon = Icons.Filled.PlayArrow
                    )
                    VaultLegendStatRow(
                        label = "Locked Sovereign",
                        count = lockedCount.coerceAtLeast(0),
                        total = totalVaults,
                        indicatorColor = TextMuted,
                        icon = Icons.Filled.Lock
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tip: Tap any dial segment to inspect vault",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Interactive Vault Preview Strip if activeModule exists
            if (activeModule != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceElevated)
                        .border(1.dp, if (activeModule.isCompleted) SuccessGreen.copy(alpha = 0.5f) else GoldDark.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            if (activeModule.isUnlocked) {
                                onSelectModule(activeModule.id)
                            } else {
                                onOpenPaywall()
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("vault_ring_preview_item_${activeModule.id}"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        ModuleVaultCircularProgressRing(
                            isUnlocked = activeModule.isUnlocked,
                            isCompleted = activeModule.isCompleted,
                            order = activeModule.order,
                            size = 34.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "VAULT ${activeModule.order}: ${activeModule.title}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeModule.isUnlocked) GoldLight else TextMuted
                            )
                            Text(
                                text = activeModule.originalPrinciple,
                                fontSize = 9.5.sp,
                                color = AmberAccent
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (activeModule.isCompleted) "Revisit" else if (activeModule.isUnlocked) "Enter" else "Unlock",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeModule.isUnlocked) GoldLight else AmberAccent
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = if (activeModule.isUnlocked) GoldLight else AmberAccent,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VaultLegendStatRow(
    label: String,
    count: Int,
    total: Int,
    indicatorColor: Color,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = indicatorColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = "$count/$total",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = indicatorColor
        )
    }
}


