package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Data model representing an aggregated Weekly Progress Digest.
 * Compiled from existing app data (Journal, Goals, Habits, XP, Streaks, Badges).
 */
data class WeeklyProgressDigest(
    val weekNumber: Int,
    val year: Int,
    val weeksAgo: Int, // 0 for current week, 1 for last week, etc.
    val startEpochMillis: Long,
    val endEpochMillis: Long,
    val startEpochDay: Long,
    val endEpochDay: Long,
    val formattedDateRange: String, // e.g. "Aug 18 – Aug 24, 2026"
    val isCurrentWeek: Boolean,
    val isGenerationDay: Boolean,
    
    // Core headline & summary
    val headlineSummary: String,
    val subHeadline: String,
    val performanceTierTag: String, // "Extraordinary Pace", "Strong Momentum", "Steady Focus", "Initiate Pace"
    
    // Journal & Reflections
    val journalEntriesCount: Int,
    val decisionCount: Int,
    val fearReframeCount: Int,
    val moodCheckinsCount: Int,
    val journalHighlights: List<String>,
    
    // Daily Habits & Rituals
    val habitsCompletedCount: Int,
    val distinctHabitDays: Int,
    val totalHabitsConfigured: Int,
    val habitTargetTotal: Int,
    val habitCompletionRate: Float, // 0.0 to 1.0
    
    // Goals & Transmutation
    val wealthContributionsCount: Int,
    val wealthContributedAmount: Double,
    val wealthGoalTargetAmount: Double,
    val wealthGoalCurrentAmount: Double,
    val givingLogsCount: Int,
    val givingContributedAmount: Double,
    val mastermindCheckinSubmitted: Boolean,
    val modulesCompletedCount: Int,
    
    // XP & Streaks
    val xpEarnedThisWeek: Int,
    val currentStreak: Int,
    val bestStreak: Int,
    val currentTier: String,
    
    // Badges
    val badgesUnlockedThisWeek: List<BadgeEntity>
)

object WeeklyDigestAggregator {

    /**
     * Compiles a WeeklyProgressDigest for a specific offset (0 = current week, 1 = last week, etc.)
     */
    fun compileDigest(
        weeksAgo: Int = 0,
        userProfile: UserProfileEntity?,
        notebookEntries: List<NotebookEntryEntity>,
        allHabitLogs: List<DailyHabitLogEntity>,
        habits: List<DailyHabitEntity>,
        wealthGoal: WealthGoalEntity?,
        wealthGoalLogs: List<WealthGoalLogEntity>,
        givingGoal: GivingGoalEntity?,
        givingLogs: List<GivingLogEntity>,
        badges: List<BadgeEntity>,
        modules: List<ModuleEntity>,
        mastermindCheckins: List<MastermindCheckinEntity>
    ): WeeklyProgressDigest {
        val calendar = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // Roll back by weeksAgo
            add(Calendar.WEEK_OF_YEAR, -weeksAgo)
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        val startMillis = calendar.timeInMillis
        val startEpochDay = startMillis / (24 * 60 * 60 * 1000L)

        // End of week (Sunday 23:59:59.999)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endMillis = calendar.timeInMillis
        val endEpochDay = endMillis / (24 * 60 * 60 * 1000L)

        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)

        val dateFormat = SimpleDateFormat("MMM d", Locale.US)
        val fullDateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
        val formattedRange = "${dateFormat.format(Date(startMillis))} – ${fullDateFormat.format(Date(endMillis))}"

        val now = System.currentTimeMillis()
        val currentCal = Calendar.getInstance()
        val dayOfWeek = currentCal.get(Calendar.DAY_OF_WEEK)
        val isGenerationDay = weeksAgo == 0 && (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.MONDAY)

        // 1. Filter Notebook entries in this week
        val weeklyNotes = notebookEntries.filter { it.timestamp in startMillis..endMillis }
        val journalCount = weeklyNotes.size
        val decisionsCount = weeklyNotes.count {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG || it.tags.contains("Decision Log", ignoreCase = true)
        }
        val fearCount = weeklyNotes.count {
            it.entryType == NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME || it.tags.contains("Fear-to-Action", ignoreCase = true)
        }
        val highlights = weeklyNotes.take(3).map { it.title.ifBlank { "Journal Entry" } }

        // 2. Filter Habit logs in this week
        val weeklyHabitLogs = allHabitLogs.filter { it.dateEpochDay in startEpochDay..endEpochDay }
        val habitsCompletedCount = weeklyHabitLogs.size
        val distinctHabitDays = weeklyHabitLogs.map { it.dateEpochDay }.distinct().size
        val activeHabitsCount = habits.size.coerceAtLeast(1)
        val habitTargetTotal = activeHabitsCount * 7
        val habitCompletionRate = (habitsCompletedCount.toFloat() / habitTargetTotal.toFloat()).coerceIn(0f, 1f)

        // 3. Goals & Transmutation logs in this week
        val weeklyWealthLogs = wealthGoalLogs.filter { it.timestamp in startMillis..endMillis }
        val wealthContributionsCount = weeklyWealthLogs.size
        val wealthContributedAmount = weeklyWealthLogs.sumOf { it.amount }
        val wealthTarget = wealthGoal?.targetAmount ?: 100000.0
        val wealthCurrent = wealthGoal?.currentAmount ?: 0.0

        val weeklyGivingLogs = givingLogs.filter { it.timestamp in startMillis..endMillis }
        val givingCount = weeklyGivingLogs.size
        val givingAmount = weeklyGivingLogs.sumOf { it.amount ?: 0.0 }

        val hasMastermindCheckin = mastermindCheckins.any {
            it.weekNumber == weekNumber && it.year == year
        }

        val weeklyModules = modules.filter { it.isCompleted } // completed modules count
        val modulesCount = weeklyModules.size

        // 4. Badges unlocked this week
        val weeklyBadges = badges.filter { badge ->
            badge.isUnlocked && badge.unlockedAt != null && badge.unlockedAt in startMillis..endMillis
        }

        // 5. XP Earned This Week calculation
        var estimatedXp = 0
        estimatedXp += weeklyHabitLogs.sumOf { it.xpEarned.coerceAtLeast(30) }
        estimatedXp += decisionsCount * 50
        estimatedXp += fearCount * 75
        estimatedXp += (journalCount - decisionsCount - fearCount).coerceAtLeast(0) * 40
        estimatedXp += weeklyWealthLogs.size * 50
        estimatedXp += weeklyGivingLogs.size * 50
        if (hasMastermindCheckin) estimatedXp += 75
        estimatedXp += weeklyBadges.sumOf { it.xpReward }
        if (estimatedXp == 0 && (userProfile?.xpTotal ?: 0) > 0 && weeksAgo == 0) {
            estimatedXp = 150 // baseline activity fallback
        }

        // 6. Formulate plain-language summary & headlines
        val currentStreak = userProfile?.currentStreak ?: 1
        val bestStreak = userProfile?.bestStreak ?: currentStreak

        val (headline, subhead, performanceTag) = generateSummarySentences(
            journalCount = journalCount,
            habitsCount = habitsCompletedCount,
            distinctDays = distinctHabitDays,
            streak = currentStreak,
            xp = estimatedXp,
            wealthContributed = wealthContributedAmount,
            badgesCount = weeklyBadges.size,
            isCurrentWeek = weeksAgo == 0
        )

        return WeeklyProgressDigest(
            weekNumber = weekNumber,
            year = year,
            weeksAgo = weeksAgo,
            startEpochMillis = startMillis,
            endEpochMillis = endMillis,
            startEpochDay = startEpochDay,
            endEpochDay = endEpochDay,
            formattedDateRange = formattedRange,
            isCurrentWeek = weeksAgo == 0,
            isGenerationDay = isGenerationDay,
            headlineSummary = headline,
            subHeadline = subhead,
            performanceTierTag = performanceTag,
            journalEntriesCount = journalCount,
            decisionCount = decisionsCount,
            fearReframeCount = fearCount,
            moodCheckinsCount = distinctHabitDays,
            journalHighlights = highlights,
            habitsCompletedCount = habitsCompletedCount,
            distinctHabitDays = distinctHabitDays,
            totalHabitsConfigured = activeHabitsCount,
            habitTargetTotal = habitTargetTotal,
            habitCompletionRate = habitCompletionRate,
            wealthContributionsCount = wealthContributionsCount,
            wealthContributedAmount = wealthContributedAmount,
            wealthGoalTargetAmount = wealthTarget,
            wealthGoalCurrentAmount = wealthCurrent,
            givingLogsCount = givingCount,
            givingContributedAmount = givingAmount,
            mastermindCheckinSubmitted = hasMastermindCheckin,
            modulesCompletedCount = modulesCount,
            xpEarnedThisWeek = estimatedXp,
            currentStreak = currentStreak,
            bestStreak = bestStreak,
            currentTier = userProfile?.tierName ?: "Novice",
            badgesUnlockedThisWeek = weeklyBadges
        )
    }

    /**
     * Generates past digests for history review (e.g. past 8 weeks).
     */
    fun compilePastDigests(
        count: Int = 6,
        userProfile: UserProfileEntity?,
        notebookEntries: List<NotebookEntryEntity>,
        allHabitLogs: List<DailyHabitLogEntity>,
        habits: List<DailyHabitEntity>,
        wealthGoal: WealthGoalEntity?,
        wealthGoalLogs: List<WealthGoalLogEntity>,
        givingGoal: GivingGoalEntity?,
        givingLogs: List<GivingLogEntity>,
        badges: List<BadgeEntity>,
        modules: List<ModuleEntity>,
        mastermindCheckins: List<MastermindCheckinEntity>
    ): List<WeeklyProgressDigest> {
        return (0 until count).map { weeksAgo ->
            compileDigest(
                weeksAgo = weeksAgo,
                userProfile = userProfile,
                notebookEntries = notebookEntries,
                allHabitLogs = allHabitLogs,
                habits = habits,
                wealthGoal = wealthGoal,
                wealthGoalLogs = wealthGoalLogs,
                givingGoal = givingGoal,
                givingLogs = givingLogs,
                badges = badges,
                modules = modules,
                mastermindCheckins = mastermindCheckins
            )
        }
    }

    private fun generateSummarySentences(
        journalCount: Int,
        habitsCount: Int,
        distinctDays: Int,
        streak: Int,
        xp: Int,
        wealthContributed: Double,
        badgesCount: Int,
        isCurrentWeek: Boolean
    ): Triple<String, String, String> {
        return when {
            distinctDays >= 5 && journalCount >= 3 -> {
                Triple(
                    "You had a strong week — $journalCount journal entries, $habitsCount habits completed, and a $streak-day streak kept alive.",
                    "Exceptional discipline. Your Definite Chief Aim is taking tangible shape through compounding daily rituals.",
                    "Sovereign Execution"
                )
            }
            habitsCount >= 7 || distinctDays >= 4 -> {
                Triple(
                    "High consistency week — $habitsCount ritual completions across $distinctDays active days with +$xp XP generated.",
                    "Persistence is turning daily action into automatic habit. Keep guarding your mental fortress.",
                    "Strong Momentum"
                )
            }
            journalCount >= 2 || wealthContributed > 0 -> {
                val financialClause = if (wealthContributed > 0) " and $${String.format(Locale.US, "%,.0f", wealthContributed)} deposited toward wealth" else ""
                Triple(
                    "Focused progress — $journalCount reflections inscribed$financialClause, maintaining your $streak-day streak.",
                    "Every reflection clarifies your vision and dissolves subconscious friction.",
                    "Steady Focus"
                )
            }
            badgesCount > 0 -> {
                Triple(
                    "Breakthrough week — $badgesCount new accolades achieved and +$xp XP accumulated on your path to sovereignty.",
                    "Your milestone achievements reflect inner transformation and applied knowledge.",
                    "Breakthrough Pace"
                )
            }
            else -> {
                if (isCurrentWeek) {
                    Triple(
                        "Week underway — $habitsCount rituals completed and $streak-day streak active. Ready to build further momentum.",
                        "Napoleon Hill's principle: 'Definiteness of purpose is the starting point of all achievement.'",
                        "Active Progression"
                    )
                } else {
                    Triple(
                        "Steady baseline — $habitsCount ritual actions logged and daily discipline sustained.",
                        "Quiet compounding creates the foundation for explosive transmutations.",
                        "Consistent Cadence"
                    )
                }
            }
        }
    }
}
