package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TierArchitect
import com.example.ui.theme.TierBuilder
import com.example.ui.theme.TierLegacy
import com.example.ui.theme.TierSovereign

/**
 * High-craft Section Achievement definitions for Napoleon Hill's 13 Vaults.
 * Categorized into 4 core philosophical pillars of Think and Grow Rich.
 */
data class SectionAchievementInfo(
    val sectionId: Int,
    val romanNumeral: String,
    val badgeId: String,
    val title: String,
    val pillarName: String,
    val subtitle: String,
    val moduleIds: List<Int>,
    val xpReward: Int,
    val quote: String,
    val quoteAuthor: String = "Napoleon Hill",
    val description: String,
    val perks: List<String>,
    val iconVector: ImageVector,
    val primaryGold: Color,
    val accentGold: Color
)

val SECTION_ACHIEVEMENTS = listOf(
    SectionAchievementInfo(
        sectionId = 1,
        romanNumeral = "I",
        badgeId = "achievement_section_1",
        title = "The Mental Foundation",
        pillarName = "Pillar of Mind",
        subtitle = "Vaults 0 – 3: Transmutation, Desire, Faith & Autosuggestion",
        moduleIds = listOf(0, 1, 2, 3),
        xpReward = 350,
        quote = "Whatever the mind can conceive and believe, the mind can achieve.",
        description = "You have mastered the foundational mental laws of wealth. By anchoring a Definite Major Purpose, unshakeable faith, and daily autosuggestion, the subconscious seed of riches is permanently planted.",
        perks = listOf(
            "+350 Sovereign XP Credited",
            "Mental Blueprint Transmuted",
            "Autosuggestion Loop Permanent Lock"
        ),
        iconVector = Icons.Filled.Psychology,
        primaryGold = GoldLight,
        accentGold = AmberBright
    ),
    SectionAchievementInfo(
        sectionId = 2,
        romanNumeral = "II",
        badgeId = "achievement_section_2",
        title = "Strategic Architecture",
        pillarName = "Pillar of Execution",
        subtitle = "Vaults 4 – 8: Specialized Knowledge, Imagination, Planning, Decision & Persistence",
        moduleIds = listOf(4, 5, 6, 7, 8),
        xpReward = 500,
        quote = "Knowledge is only potential power. It becomes power only when organized into definite plans and directed to a definite end.",
        description = "You have constructed the execution fortress. Knowledge has been transmuted into structured planning, prompt decisions, and indestructible persistence against all defeat.",
        perks = listOf(
            "+500 Sovereign XP Credited",
            "Execution Fortress Seal",
            "Indomitable Persistence Token"
        ),
        iconVector = Icons.Filled.Shield,
        primaryGold = GoldPrimary,
        accentGold = TierArchitect
    ),
    SectionAchievementInfo(
        sectionId = 3,
        romanNumeral = "III",
        badgeId = "achievement_section_3",
        title = "Higher Synergies",
        pillarName = "Pillar of Energy",
        subtitle = "Vaults 9 – 11: Master Mind Alliance, Vital Transmutation & Subconscious Engine",
        moduleIds = listOf(9, 10, 11),
        xpReward = 600,
        quote = "No two minds ever come together without thereby creating a third, invisible, intangible force which may be likened to a third mind.",
        description = "You have unlocked the intangible catalysts of immense power. Through the Master Mind alliance, vital energy redirection, and direct subconscious access, your thought vibrations operate at sovereign frequency.",
        perks = listOf(
            "+600 Sovereign XP Credited",
            "Master Mind Harmonic Alignment",
            "Subconscious Channel Unlocked"
        ),
        iconVector = Icons.Filled.AutoAwesome,
        primaryGold = AmberBright,
        accentGold = TierSovereign
    ),
    SectionAchievementInfo(
        sectionId = 4,
        romanNumeral = "IV",
        badgeId = "achievement_section_4",
        title = "The Sovereign Apex",
        pillarName = "Pillar of Intuition",
        subtitle = "Vaults 12 – 13: Brain Vibration Broadcast & The Sixth Sense",
        moduleIds = listOf(12, 13),
        xpReward = 750,
        quote = "The Sixth Sense is the apex of the philosophy, where Infinite Intelligence voluntarily communicates with man without effort.",
        description = "You have attained the pinnacle of Napoleon Hill's magnum opus. Your brain vibrates as both sending and receiving station, and the Sixth Sense guides your destiny with effortless clarity.",
        perks = listOf(
            "+750 Sovereign XP Credited",
            "Sixth Sense Crown of Legacy",
            "Infinite Intelligence Direct Link"
        ),
        iconVector = Icons.Filled.WorkspacePremium,
        primaryGold = GoldLight,
        accentGold = TierLegacy
    )
)

fun getSectionForModule(moduleId: Int): SectionAchievementInfo? {
    return SECTION_ACHIEVEMENTS.firstOrNull { it.moduleIds.contains(moduleId) }
}

fun getSectionById(sectionId: Int): SectionAchievementInfo? {
    return SECTION_ACHIEVEMENTS.firstOrNull { it.sectionId == sectionId }
}

fun getSectionForBadgeId(badgeId: String): SectionAchievementInfo? {
    return SECTION_ACHIEVEMENTS.firstOrNull { it.badgeId == badgeId }
}

fun isSectionCompleted(section: SectionAchievementInfo, modules: List<ModuleEntity>): Boolean {
    if (modules.isEmpty()) return false
    val sectionModuleEntities = modules.filter { section.moduleIds.contains(it.id) }
    if (sectionModuleEntities.size < section.moduleIds.size) return false
    return sectionModuleEntities.all { it.isCompleted }
}

fun getSectionProgress(section: SectionAchievementInfo, modules: List<ModuleEntity>): Pair<Int, Int> {
    val completed = section.moduleIds.count { id ->
        modules.firstOrNull { it.id == id }?.isCompleted == true
    }
    return Pair(completed, section.moduleIds.size)
}
