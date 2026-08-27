package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ModuleEntity
import com.example.data.model.SECTION_ACHIEVEMENTS
import com.example.data.model.SectionAchievementInfo
import com.example.ui.components.LocalWindowSizeInfo
import com.example.ui.components.ModuleVaultCircularProgressRing
import com.example.ui.components.SectionHeaderCard
import com.example.ui.components.ThirteenVaultsMasteryCard
import com.example.ui.components.luxurySharedBounds
import com.example.ui.components.luxurySharedElement
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.RichBlack
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ModulesPathScreen(
    modules: List<ModuleEntity>,
    isPaidUnlocked: Boolean,
    onSelectModule: (Int) -> Unit,
    onOpenPaywall: () -> Unit,
    onOpenSectionAchievement: (SectionAchievementInfo) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val windowInfo = LocalWindowSizeInfo.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("modules_path_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(6.dp)) }

        // --- 13 VAULTS CIRCULAR MASTERY CARD ---
        item {
            Box(
                modifier = Modifier.luxurySharedBounds(
                    key = "thirteen_vaults_card",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            ) {
                ThirteenVaultsMasteryCard(
                    modules = modules,
                    isPaidUnlocked = isPaidUnlocked,
                    onSelectModule = onSelectModule,
                    onOpenPaywall = onOpenPaywall
                )
            }
        }

        // --- SECTIONS & MODULES LIST ---
        SECTION_ACHIEVEMENTS.forEach { section ->
            val sectionModules = section.moduleIds.mapNotNull { id ->
                modules.firstOrNull { it.id == id }
            }

            // Section Header Card
            item(key = "section_header_${section.sectionId}") {
                SectionHeaderCard(
                    section = section,
                    modules = modules,
                    onBadgeClick = onOpenSectionAchievement
                )
            }

            // Section Modules Grid/List
            if (windowInfo.isTabletOrFoldable) {
                items(sectionModules.chunked(2), key = { pair -> "sec_${section.sectionId}_" + pair.map { it.id }.joinToString("_") }) { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowModules.forEach { module ->
                            Box(modifier = Modifier.weight(1f)) {
                                ModulePathCard(
                                    module = module,
                                    onClick = {
                                        if (module.isUnlocked) {
                                            onSelectModule(module.id)
                                        } else {
                                            onOpenPaywall()
                                        }
                                    },
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            }
                        }
                        if (rowModules.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                items(sectionModules, key = { it.id }) { module ->
                    ModulePathCard(
                        module = module,
                        onClick = {
                            if (module.isUnlocked) {
                                onSelectModule(module.id)
                            } else {
                                onOpenPaywall()
                            }
                        },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }

            item(key = "section_spacer_${section.sectionId}") {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ModulePathCard(
    module: ModuleEntity,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null
) {
    val isUnlocked = module.isUnlocked
    val isCompleted = module.isCompleted

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .luxurySharedBounds(
                key = "module_card_${module.id}",
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
            .border(
                width = if (isCompleted) 1.5.dp else if (isUnlocked) 1.dp else 0.5.dp,
                color = if (isCompleted) SuccessGreen.copy(alpha = 0.8f) else if (isUnlocked) GoldPrimary else DarkBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .testTag("module_item_${module.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) DarkCharcoal else RichBlack.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vault Badge / Animated Circular Progress Ring
            Box(
                modifier = Modifier.luxurySharedElement(
                    key = "module_ring_${module.id}",
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            ) {
                ModuleVaultCircularProgressRing(
                    isUnlocked = isUnlocked,
                    isCompleted = isCompleted,
                    order = module.order,
                    size = 46.dp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Module Details Column
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "VAULT ${module.order}",
                        color = if (isCompleted) SuccessGreen else if (isUnlocked) GoldLight else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    if (isCompleted) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• MASTERED",
                            color = SuccessGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = module.title,
                    color = if (isUnlocked) TextPrimary else TextMuted,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = module.originalPrinciple,
                    color = if (isUnlocked) AmberAccent else TextMuted.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            // Right Action Icon
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = if (isCompleted) SuccessGreen.copy(alpha = 0.15f)
                        else if (isUnlocked) GoldPrimary.copy(alpha = 0.15f)
                        else DarkBorder.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (!isUnlocked) Icons.Filled.Lock else Icons.Filled.PlayArrow,
                    contentDescription = if (!isUnlocked) "Locked Vault" else "Open Vault",
                    tint = if (isCompleted) SuccessGreen
                    else if (isUnlocked) GoldPrimary
                    else TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
