package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Nightlife
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Villa
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class VisionStockCategory(
    val id: String,
    val name: String,
    val icon: ImageVector
)

val VISION_CATEGORIES = listOf(
    VisionStockCategory("all", "All Visions", Icons.Filled.AutoAwesome),
    VisionStockCategory("wealth", "Wealth & Abundance", Icons.Filled.MonetizationOn),
    VisionStockCategory("empire", "Empire & Career", Icons.Filled.TrendingUp),
    VisionStockCategory("travel", "Serene Travel & Retreats", Icons.Filled.Public),
    VisionStockCategory("health", "Health & Longevity", Icons.Filled.FitnessCenter),
    VisionStockCategory("relationships", "Relationships & Family", Icons.Filled.Favorite),
    VisionStockCategory("mastery", "Mind & Mastery", Icons.Filled.SelfImprovement)
)

data class VisionStockItem(
    val id: String,
    val category: String,
    val title: String,
    val defaultTimeline: String,
    val defaultAffirmation: String,
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val fallbackEmoji: String,
    val webImageUrl: String
)

val VISION_STOCK_PRESETS = listOf(
    // WEALTH & ABUNDANCE
    VisionStockItem(
        id = "stock_vault_gold",
        category = "Wealth & Abundance",
        title = "$10,000,000 Liquid Sovereign Reserve",
        defaultTimeline = "By Dec 2027",
        defaultAffirmation = "Money flows to me in avalanches of abundance as I deliver massive value to the world.",
        icon = Icons.Filled.AccountBalance,
        gradientColors = listOf(Color(0xFF2A1C0A), Color(0xFF5C4010), Color(0xFFD4AF37)),
        fallbackEmoji = "🏛️",
        webImageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=800&q=80"
    ),
    VisionStockItem(
        id = "stock_private_aviation",
        category = "Wealth & Abundance",
        title = "Private Aviation & Sovereign Freedom",
        defaultTimeline = "By 2028",
        defaultAffirmation = "I command my time, my destination, and my state of being with complete autonomy.",
        icon = Icons.Filled.Flight,
        gradientColors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFFD4AF37)),
        fallbackEmoji = "✈️",
        webImageUrl = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?auto=format&fit=crop&w=800&q=80"
    ),
    VisionStockItem(
        id = "stock_luxury_supercar",
        category = "Wealth & Abundance",
        title = "Artisanal Automotive Precision",
        defaultTimeline = "By Q3 2027",
        defaultAffirmation = "I appreciate flawless craftsmanship, engineering mastery, and uncompromising performance.",
        icon = Icons.Filled.DirectionsCar,
        gradientColors = listOf(Color(0xFF18181B), Color(0xFF3F3F46), Color(0xFFE2C974)),
        fallbackEmoji = "🏎️",
        webImageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80"
    ),

    // EMPIRE & CAREER
    VisionStockItem(
        id = "stock_penthouse_hq",
        category = "Empire & Career",
        title = "Global Enterprise Headquarters",
        defaultTimeline = "By 2028",
        defaultAffirmation = "My enterprise creates generational wealth and transforms the lives of millions worldwide.",
        icon = Icons.Filled.Apartment,
        gradientColors = listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFFD4AF37)),
        fallbackEmoji = "🏙️",
        webImageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80"
    ),
    VisionStockItem(
        id = "stock_keynote_summit",
        category = "Empire & Career",
        title = "Global Keynote on World Stage",
        defaultTimeline = "By 2027",
        defaultAffirmation = "My voice carries conviction, clarity, and the power to awaken human greatness.",
        icon = Icons.Filled.Lightbulb,
        gradientColors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFFF6E05E)),
        fallbackEmoji = "🎙️",
        webImageUrl = "https://images.unsplash.com/photo-1475721027785-f74eccf877e2?auto=format&fit=crop&w=800&q=80"
    ),

    // SERENE TRAVEL & RETREATS
    VisionStockItem(
        id = "stock_swiss_alps_chalet",
        category = "Serene Travel & Retreats",
        title = "Alpine Sanctuary & Mastermind Villa",
        defaultTimeline = "By Q4 2027",
        defaultAffirmation = "In serenity and pristine nature, my highest strategic insights crystallize into reality.",
        icon = Icons.Filled.Landscape,
        gradientColors = listOf(Color(0xFF064E3B), Color(0xFF065F46), Color(0xFFE2C974)),
        fallbackEmoji = "🏔️",
        webImageUrl = "https://images.unsplash.com/photo-1502784444187-359ac186c5bb?auto=format&fit=crop&w=800&q=80"
    ),
    VisionStockItem(
        id = "stock_superyacht_mediterranean",
        category = "Serene Travel & Retreats",
        title = "Mediterranean Yacht Voyage",
        defaultTimeline = "Summer 2027",
        defaultAffirmation = "I savor the boundless horizons of the earth surrounded by the people I cherish most.",
        icon = Icons.Filled.Sailing,
        gradientColors = listOf(Color(0xFF0C4A6E), Color(0xFF0369A1), Color(0xFFF3E5AB)),
        fallbackEmoji = "⛵",
        webImageUrl = "https://images.unsplash.com/photo-1569263979104-865ab7cd8d17?auto=format&fit=crop&w=800&q=80"
    ),

    // HEALTH & LONGEVITY
    VisionStockItem(
        id = "stock_elite_vitality",
        category = "Health & Vitality",
        title = "Peak Bio-Vitality & 100-Year Longevity",
        defaultTimeline = "Daily Practice",
        defaultAffirmation = "My body is a temple of boundless energy, cellular youthfulness, and athletic power.",
        icon = Icons.Filled.FitnessCenter,
        gradientColors = listOf(Color(0xFF14532D), Color(0xFF166534), Color(0xFF86EFAC)),
        fallbackEmoji = "⚡",
        webImageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?auto=format&fit=crop&w=800&q=80"
    ),
    VisionStockItem(
        id = "stock_zen_sanctuary",
        category = "Health & Vitality",
        title = "Daily Meditation & Inner Calm",
        defaultTimeline = "Unwavering Ritual",
        defaultAffirmation = "Stillness is my supreme superpower; through unshakeable focus, all creation bends to my will.",
        icon = Icons.Filled.Spa,
        gradientColors = listOf(Color(0xFF3B185F), Color(0xFF2A0944), Color(0xFFFEC260)),
        fallbackEmoji = "🧘",
        webImageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?auto=format&fit=crop&w=800&q=80"
    ),

    // RELATIONSHIPS & FAMILY
    VisionStockItem(
        id = "stock_family_estate",
        category = "Relationships & Family",
        title = "Generational Family Estate & Legacy",
        defaultTimeline = "By 2028",
        defaultAffirmation = "I build a fortress of love, security, wisdom, and celebration for my family for generations.",
        icon = Icons.Filled.Villa,
        gradientColors = listOf(Color(0xFF451A03), Color(0xFF78350F), Color(0xFFFDE68A)),
        fallbackEmoji = "🏡",
        webImageUrl = "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&w=800&q=80"
    ),

    // MIND & MASTERY
    VisionStockItem(
        id = "stock_philosopher_library",
        category = "Mind & Mastery",
        title = "Grand Master Library & Polymath Mastery",
        defaultTimeline = "Lifetime Pursuit",
        defaultAffirmation = "I feed my mind with the greatest ideas in human history and synthesize them into timeless wisdom.",
        icon = Icons.Filled.Psychology,
        gradientColors = listOf(Color(0xFF31101E), Color(0xFF581C87), Color(0xFFE2C974)),
        fallbackEmoji = "📚",
        webImageUrl = "https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=800&q=80"
    ),
    VisionStockItem(
        id = "stock_sovereign_crown",
        category = "Mind & Mastery",
        title = "Total Mental Sovereignty & Self-Mastery",
        defaultTimeline = "Always Present",
        defaultAffirmation = "I am the undisputed master of my fate, the sovereign captain of my soul.",
        icon = Icons.Filled.WorkspacePremium,
        gradientColors = listOf(Color(0xFF271500), Color(0xFF6B4200), Color(0xFFFFD700)),
        fallbackEmoji = "👑",
        webImageUrl = "https://images.unsplash.com/photo-1579783902614-a3fb3927b675?auto=format&fit=crop&w=800&q=80"
    )
)

fun getStockPresetById(id: String): VisionStockItem? {
    return VISION_STOCK_PRESETS.firstOrNull { it.id == id }
}
