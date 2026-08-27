package com.example.data.model

enum class LeaderboardMetric(val title: String, val shortLabel: String, val unitLabel: String) {
    XP("XP Total", "XP", "XP"),
    STREAK("Streak Length", "Streak", "Days"),
    MODULES("Vaults Completed", "Vaults", "Vaults")
}

enum class LeaderboardTimeframe(val title: String, val subtitle: String) {
    THIS_WEEK("This Week", "Weekly Reset Every Sunday Midnight"),
    ALL_TIME("All-Time", "Cumulative Sovereign Lifetime Standing")
}

data class LeaderboardMember(
    val id: String,
    val displayName: String,
    val avatarInitial: String,
    val avatarColorHex: String = "#D4AF37",
    val tierTitle: String = "Builder",
    val isCurrentUser: Boolean = false,
    val xpAllTime: Int,
    val xpThisWeek: Int,
    val streakDaysAllTime: Int,
    val streakDaysThisWeek: Int,
    val modulesCompletedAllTime: Int,
    val modulesCompletedThisWeek: Int,
    val motto: String = "Transmuting desire into reality",
    val isOptedIn: Boolean = true
)

data class LeaderboardEntry(
    val rank: Int,
    val member: LeaderboardMember,
    val metricValue: Int,
    val formattedMetricValue: String,
    val isCurrentUser: Boolean = false
)

object SovereignCommunityPeers {
    val peers = listOf(
        LeaderboardMember(
            id = "peer_elena",
            displayName = "Elena Rostova",
            avatarInitial = "ER",
            avatarColorHex = "#AB47BC",
            tierTitle = "Sovereign",
            xpAllTime = 14850,
            xpThisWeek = 1820,
            streakDaysAllTime = 84,
            streakDaysThisWeek = 7,
            modulesCompletedAllTime = 13,
            modulesCompletedThisWeek = 2,
            motto = "Definiteness of Purpose eliminates all resistance."
        ),
        LeaderboardMember(
            id = "peer_julian",
            displayName = "Julian Hayes",
            avatarInitial = "JH",
            avatarColorHex = "#E5A93C",
            tierTitle = "Architect",
            xpAllTime = 11420,
            xpThisWeek = 1450,
            streakDaysAllTime = 63,
            streakDaysThisWeek = 7,
            modulesCompletedAllTime = 11,
            modulesCompletedThisWeek = 1,
            motto = "Organized planning turns intangible thought into empire."
        ),
        LeaderboardMember(
            id = "peer_victoria",
            displayName = "Lady Victoria Sterling",
            avatarInitial = "VS",
            avatarColorHex = "#D4AF37",
            tierTitle = "Legacy",
            xpAllTime = 19200,
            xpThisWeek = 2100,
            streakDaysAllTime = 142,
            streakDaysThisWeek = 7,
            modulesCompletedAllTime = 13,
            modulesCompletedThisWeek = 3,
            motto = "Infinite Intelligence responds to unshakeable conviction."
        ),
        LeaderboardMember(
            id = "peer_marcus",
            displayName = "Marcus Vance",
            avatarInitial = "MV",
            avatarColorHex = "#2E7D32",
            tierTitle = "Architect",
            xpAllTime = 9850,
            xpThisWeek = 1180,
            streakDaysAllTime = 49,
            streakDaysThisWeek = 6,
            modulesCompletedAllTime = 9,
            modulesCompletedThisWeek = 1,
            motto = "Autosuggestion commands the subconscious powerhouse."
        ),
        LeaderboardMember(
            id = "peer_alistair",
            displayName = "Dr. Alistair Chen",
            avatarInitial = "AC",
            avatarColorHex = "#1565C0",
            tierTitle = "Builder",
            xpAllTime = 7400,
            xpThisWeek = 960,
            streakDaysAllTime = 35,
            streakDaysThisWeek = 5,
            modulesCompletedAllTime = 7,
            modulesCompletedThisWeek = 1,
            motto = "Specialized knowledge applied with relentless precision."
        ),
        LeaderboardMember(
            id = "peer_amara",
            displayName = "Amara Okafor",
            avatarInitial = "AO",
            avatarColorHex = "#F57C00",
            tierTitle = "Architect",
            xpAllTime = 8900,
            xpThisWeek = 1290,
            streakDaysAllTime = 42,
            streakDaysThisWeek = 6,
            modulesCompletedAllTime = 8,
            modulesCompletedThisWeek = 2,
            motto = "Burning desire burns down every bridge of retreat."
        ),
        LeaderboardMember(
            id = "peer_kenji",
            displayName = "Kenji Sato",
            avatarInitial = "KS",
            avatarColorHex = "#00897B",
            tierTitle = "Builder",
            xpAllTime = 6150,
            xpThisWeek = 840,
            streakDaysAllTime = 28,
            streakDaysThisWeek = 5,
            modulesCompletedAllTime = 6,
            modulesCompletedThisWeek = 1,
            motto = "Daily non-negotiable decree rituals build iron discipline."
        ),
        LeaderboardMember(
            id = "peer_genevieve",
            displayName = "Genevieve Dubois",
            avatarInitial = "GD",
            avatarColorHex = "#8E24AA",
            tierTitle = "Sovereign",
            xpAllTime = 13200,
            xpThisWeek = 1620,
            streakDaysAllTime = 77,
            streakDaysThisWeek = 7,
            modulesCompletedAllTime = 12,
            modulesCompletedThisWeek = 2,
            motto = "Master Mind alliance amplifies individual mental power 100x."
        ),
        LeaderboardMember(
            id = "peer_tariq",
            displayName = "Tariq Al-Mansoor",
            avatarInitial = "TM",
            avatarColorHex = "#00ACC1",
            tierTitle = "Builder",
            xpAllTime = 5300,
            xpThisWeek = 720,
            streakDaysAllTime = 21,
            streakDaysThisWeek = 4,
            modulesCompletedAllTime = 5,
            modulesCompletedThisWeek = 1,
            motto = "Decision over procrastination; boldness creates capital."
        ),
        LeaderboardMember(
            id = "peer_soren",
            displayName = "Soren Lindqvist",
            avatarInitial = "SL",
            avatarColorHex = "#3949AB",
            tierTitle = "Novice",
            xpAllTime = 3200,
            xpThisWeek = 510,
            streakDaysAllTime = 14,
            streakDaysThisWeek = 4,
            modulesCompletedAllTime = 3,
            modulesCompletedThisWeek = 1,
            motto = "First step into the 13 Vaults of Sovereign Wealth."
        ),
        LeaderboardMember(
            id = "peer_liam",
            displayName = "Liam O'Connor",
            avatarInitial = "LO",
            avatarColorHex = "#5E35B1",
            tierTitle = "Novice",
            xpAllTime = 2100,
            xpThisWeek = 390,
            streakDaysAllTime = 9,
            streakDaysThisWeek = 3,
            modulesCompletedAllTime = 2,
            modulesCompletedThisWeek = 1,
            motto = "Transmuting every temporary obstacle into wisdom."
        )
    )
}
