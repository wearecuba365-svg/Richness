package com.example.data.repository

import com.example.data.dao.RichesDao
import com.example.data.model.BadgeEntity
import com.example.data.model.CommitmentContractEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.MastermindGroupEntity
import com.example.data.model.MastermindMemberEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.MoneyBlueprintResultEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.SavedIncomeIdeaEntity
import com.example.data.model.ShortLessonEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.VisionBoardItemEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit

data class OfflineCacheStatus(
    val totalModulesCached: Int = 14,
    val completedModulesCount: Int = 0,
    val totalNotebookEntriesCached: Int = 0,
    val activeStreakDays: Int = 1,
    val bestStreakDays: Int = 1,
    val isLocalDatabaseReady: Boolean = true,
    val lastOfflineSyncTime: Long = System.currentTimeMillis()
)

class RichesRepository(private val dao: RichesDao) {

    val allModules: Flow<List<ModuleEntity>> = dao.getAllModules()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()
    val allNotebookEntries: Flow<List<NotebookEntryEntity>> = dao.getAllNotebookEntries()
    val allBadges: Flow<List<BadgeEntity>> = dao.getAllBadges()
    val completedModulesCount: Flow<Int> = dao.getCompletedModulesCount()
    val notebookEntriesCount: Flow<Int> = dao.getNotebookEntriesCount()
    val allHabits: Flow<List<DailyHabitEntity>> = dao.getAllHabits()
    val allHabitLogs: Flow<List<DailyHabitLogEntity>> = dao.getAllHabitLogs()
    
    // Mastermind Circles Flows
    val allMastermindGroups: Flow<List<MastermindGroupEntity>> = dao.getAllMastermindGroups()
    val userMastermindGroup: Flow<MastermindGroupEntity?> = dao.getUserMastermindGroup()
    val allMastermindMembers: Flow<List<MastermindMemberEntity>> = dao.getAllMastermindMembers()
    val allMastermindCheckins: Flow<List<MastermindCheckinEntity>> = dao.getAllCheckins()

    // Vision Board Flows
    val allVisionBoardItems: Flow<List<VisionBoardItemEntity>> = dao.getAllVisionBoardItems()
    val visionBoardCount: Flow<Int> = dao.getVisionBoardCount()

    // Wealth Goal Flows
    val primaryWealthGoal: Flow<WealthGoalEntity?> = dao.getWealthGoalById(1)
    val wealthGoalLogs: Flow<List<WealthGoalLogEntity>> = dao.getWealthGoalLogs(1)

    // Money Blueprint Flows
    val allMoneyBlueprintResults: Flow<List<MoneyBlueprintResultEntity>> = dao.getAllMoneyBlueprintResults()
    val latestMoneyBlueprintResult: Flow<MoneyBlueprintResultEntity?> = dao.getLatestMoneyBlueprintResult()

    // Saved Income Ideas Flows
    val allSavedIncomeIdeas: Flow<List<SavedIncomeIdeaEntity>> = dao.getAllSavedIncomeIdeas()
    val savedIncomeIdeaIds: Flow<List<String>> = dao.getSavedIncomeIdeaIds()

    // Gratitude & Giving Flows
    val allGivingLogs: Flow<List<GivingLogEntity>> = dao.getAllGivingLogs()
    val givingGoal: Flow<GivingGoalEntity?> = dao.getGivingGoal(1)
    val givingLogsCount: Flow<Int> = dao.getGivingLogsCount()

    // Short Lessons Flows
    val allShortLessons: Flow<List<ShortLessonEntity>> = dao.getAllShortLessons()
    val completedShortLessonsCount: Flow<Int> = dao.getCompletedShortLessonsCount()

    // Onboarding Telemetry & Logs Flow
    val allOnboardingStepLogs: Flow<List<com.example.data.model.OnboardingStepLogEntity>> = dao.getAllOnboardingStepLogs()

    // Commitment Contracts Flows
    val allCommitmentContracts: Flow<List<CommitmentContractEntity>> = dao.getAllCommitmentContracts()
    val activeCommitmentContract: Flow<CommitmentContractEntity?> = dao.getActiveCommitmentContract()
    val activeCommitmentContracts: Flow<List<CommitmentContractEntity>> = dao.getActiveCommitmentContracts()
    val completedCommitmentsCount: Flow<Int> = dao.getCompletedCommitmentsCount()

    fun getShortLessonsForModule(moduleId: Int): Flow<List<ShortLessonEntity>> =
        dao.getShortLessonsForModule(moduleId)

    fun getCompletedShortLessonsCountForModule(moduleId: Int): Flow<Int> =
        dao.getCompletedShortLessonsCountForModule(moduleId)

    fun getHabitLogsForDay(dateEpochDay: Long): Flow<List<DailyHabitLogEntity>> =
        dao.getHabitLogsForDay(dateEpochDay)

    fun getTodayCompletedHabitsCount(dateEpochDay: Long = getTodayEpochDay()): Flow<Int> =
        dao.getTodayCompletedHabitsCount(dateEpochDay)

    companion object {
        fun getTodayEpochDay(): Long {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis / (24 * 60 * 60 * 1000L)
        }

        fun formatDateEpochDay(epochDay: Long): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = epochDay * (24 * 60 * 60 * 1000L)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(cal.time)
        }

        fun formatDisplayDate(epochDay: Long): String {
            val cal = Calendar.getInstance()
            cal.timeInMillis = epochDay * (24 * 60 * 60 * 1000L)
            val sdf = SimpleDateFormat("EEE, MMM d", Locale.US)
            return sdf.format(cal.time)
        }

        fun calculateHabitStreak(allLogs: List<DailyHabitLogEntity>, todayEpochDay: Long = getTodayEpochDay()): Pair<Int, Int> {
            val loggedDays = allLogs.map { it.dateEpochDay }.distinct().sortedDescending()
            if (loggedDays.isEmpty()) return Pair(0, 0)

            val loggedSet = loggedDays.toSet()
            
            // Current streak (consecutive calendar days ending today or yesterday)
            var currentStreak = 0
            var checkDay = if (loggedSet.contains(todayEpochDay)) todayEpochDay else (todayEpochDay - 1)
            
            while (loggedSet.contains(checkDay)) {
                currentStreak++
                checkDay--
            }

            // Best streak (maximum unbroken consecutive days in all logs)
            var bestStreak = 0
            var tempStreak = 0
            val sortedAsc = loggedDays.sorted()
            var prevDay: Long? = null

            for (day in sortedAsc) {
                if (prevDay == null || day == prevDay + 1) {
                    tempStreak++
                } else if (day != prevDay) {
                    tempStreak = 1
                }
                bestStreak = maxOf(bestStreak, tempStreak)
                prevDay = day
            }

            bestStreak = maxOf(bestStreak, currentStreak)
            return Pair(maxOf(1, currentStreak), maxOf(1, bestStreak))
        }

        fun calculateGivingStreakWeeks(allLogs: List<GivingLogEntity>, nowMillis: Long = System.currentTimeMillis()): Pair<Int, Int> {
            if (allLogs.isEmpty()) return Pair(0, 0)
            val cal = Calendar.getInstance()
            
            // Map each log to year * 100 + week_of_year
            val loggedWeeks = allLogs.map { log ->
                cal.timeInMillis = log.timestamp
                val y = cal.get(Calendar.YEAR)
                val w = cal.get(Calendar.WEEK_OF_YEAR)
                y * 100 + w
            }.distinct().sortedDescending()

            if (loggedWeeks.isEmpty()) return Pair(0, 0)

            cal.timeInMillis = nowMillis
            val curYear = cal.get(Calendar.YEAR)
            val curWeek = cal.get(Calendar.WEEK_OF_YEAR)
            val thisWeekKey = curYear * 100 + curWeek

            cal.add(Calendar.WEEK_OF_YEAR, -1)
            val lastWeekKey = cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.WEEK_OF_YEAR)

            val loggedSet = loggedWeeks.toSet()
            var currentStreak = 0
            val checkCal = Calendar.getInstance()

            if (loggedSet.contains(thisWeekKey)) {
                checkCal.timeInMillis = nowMillis
            } else if (loggedSet.contains(lastWeekKey)) {
                checkCal.add(Calendar.WEEK_OF_YEAR, -1)
            } else {
                checkCal.timeInMillis = 0L
            }

            if (checkCal.timeInMillis > 0L) {
                while (true) {
                    val y = checkCal.get(Calendar.YEAR)
                    val w = checkCal.get(Calendar.WEEK_OF_YEAR)
                    val key = y * 100 + w
                    if (loggedSet.contains(key)) {
                        currentStreak++
                        checkCal.add(Calendar.WEEK_OF_YEAR, -1)
                    } else {
                        break
                    }
                }
            }

            val bestStreak = maxOf(currentStreak, if (allLogs.isNotEmpty() && currentStreak == 0) 1 else currentStreak)
            return Pair(currentStreak, bestStreak)
        }
    }

    suspend fun initializeIfNeeded() {
        val existingModules = dao.getAllModules().firstOrNull()
        if (existingModules.isNullOrEmpty()) {
            dao.insertModules(InitialContentData.getInitialModules())
        }

        val existingProfile = dao.getUserProfile().firstOrNull()
        if (existingProfile == null) {
            dao.insertUserProfile(UserProfileEntity())
        }

        val existingHabits = dao.getAllHabits().firstOrNull()
        if (existingHabits.isNullOrEmpty()) {
            dao.insertHabits(InitialContentData.getInitialDailyHabits())
        }

        val initialBadges = InitialContentData.getInitialBadges()
        val existingBadges = dao.getAllBadges().firstOrNull() ?: emptyList()
        val existingMap = existingBadges.associateBy { it.id }

        val badgesToPersist = initialBadges.map { initial ->
            val existing = existingMap[initial.id]
            if (existing != null) {
                // Keep unlocked status and timestamps, update metadata
                initial.copy(
                    isUnlocked = existing.isUnlocked,
                    unlockedAt = existing.unlockedAt,
                    progress = existing.progress
                )
            } else {
                initial
            }
        }
        dao.insertBadges(badgesToPersist)

        val existingGroups = dao.getAllMastermindGroups().firstOrNull()
        if (existingGroups.isNullOrEmpty()) {
            dao.insertMastermindGroups(InitialContentData.getInitialMastermindGroups())
            dao.insertMastermindMembers(InitialContentData.getInitialMastermindMembers())
            dao.insertMastermindCheckins(InitialContentData.getInitialMastermindCheckins())
        }

        val existingVisionItems = dao.getAllVisionBoardItems().firstOrNull()
        if (existingVisionItems.isNullOrEmpty()) {
            dao.insertVisionBoardItems(InitialContentData.getInitialVisionBoardItems())
        }

        val existingWealthGoal = dao.getWealthGoalById(1).firstOrNull()
        if (existingWealthGoal == null) {
            val now = System.currentTimeMillis()
            val oneYearOut = now + (365L * 24 * 60 * 60 * 1000L)
            val initialGoal = WealthGoalEntity(
                id = 1,
                title = "Definite Wealth Target",
                targetAmount = 100000.0,
                currentAmount = 15000.0,
                startingAmount = 5000.0,
                currencySymbol = "$",
                targetDateEpochMillis = oneYearOut,
                startDateEpochMillis = now - (30L * 24 * 60 * 60 * 1000L),
                category = "Financial Sovereignty",
                servicePledge = "I will deliver unmatched value, intense focus, and persistent excellence in my specialized craft in return for this exact accumulation.",
                isCompleted = false,
                createdAt = now,
                updatedAt = now
            )
            dao.insertWealthGoal(initialGoal)

            // Seed initial logs for immediate rich visualization
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US)
            val log1 = WealthGoalLogEntity(
                goalId = 1,
                amount = 5000.0,
                isMilestoneOnly = false,
                title = "Initial Starting Capital",
                note = "Base liquid reserves allocated to the Definite Aim transmutation fund.",
                timestamp = now - (25L * 24 * 60 * 60 * 1000L),
                dateFormatted = sdf.format(now - (25L * 24 * 60 * 60 * 1000L)),
                resultingTotal = 5000.0
            )
            val log2 = WealthGoalLogEntity(
                goalId = 1,
                amount = 0.0,
                isMilestoneOnly = true,
                title = "Inscribed Definite Chief Aim",
                note = "Clarified exact date and major service to be given in return for riches.",
                timestamp = now - (18L * 24 * 60 * 60 * 1000L),
                dateFormatted = sdf.format(now - (18L * 24 * 60 * 60 * 1000L)),
                resultingTotal = 5000.0
            )
            val log3 = WealthGoalLogEntity(
                goalId = 1,
                amount = 10000.0,
                isMilestoneOnly = false,
                title = "High-Ticket Client Retainer",
                note = "Transmuted specialized knowledge and concentrated focus into cash inflow.",
                timestamp = now - (7L * 24 * 60 * 60 * 1000L),
                dateFormatted = sdf.format(now - (7L * 24 * 60 * 60 * 1000L)),
                resultingTotal = 15000.0
            )
            dao.insertWealthGoalLog(log1)
            dao.insertWealthGoalLog(log2)
            dao.insertWealthGoalLog(log3)
        }

        val existingGivingGoal = dao.getGivingGoal(1).firstOrNull()
        if (existingGivingGoal == null) {
            val now = System.currentTimeMillis()
            dao.insertGivingGoal(
                GivingGoalEntity(
                    id = 1,
                    goalType = GivingGoalEntity.GOAL_TYPE_NONE,
                    targetAmount = 250.0,
                    targetPercentage = 10.0,
                    targetActsCount = 4,
                    currencySymbol = "$",
                    serviceMotto = "True wealth begins with the spirit of generosity. Abundance expands when circulated.",
                    createdAt = now,
                    updatedAt = now
                )
            )

            // Seed initial inspiring giving acts
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US)
            val act1 = GivingLogEntity(
                title = "Sponsored Books for Community Library",
                amount = 75.0,
                currencySymbol = "$",
                category = GivingLogEntity.CATEGORY_COMMUNITY,
                recipientName = "Civic Youth Library",
                note = "Provided books on creative thinking and financial literacy to young scholars.",
                timestamp = now - (12L * 24 * 60 * 60 * 1000L),
                dateFormatted = sdf.format(now - (12L * 24 * 60 * 60 * 1000L)),
                isMonetary = true
            )
            val act2 = GivingLogEntity(
                title = "Volunteered Mentorship Hour",
                amount = null,
                currencySymbol = "$",
                category = GivingLogEntity.CATEGORY_TIME_MENTORSHIP,
                recipientName = "Aspiring Local Entrepreneur",
                note = "Helped structure an initial business pitch and provided encouragement on persistence.",
                timestamp = now - (5L * 24 * 60 * 60 * 1000L),
                dateFormatted = sdf.format(now - (5L * 24 * 60 * 60 * 1000L)),
                isMonetary = false
            )
            dao.insertGivingLog(act1)
            dao.insertGivingLog(act2)
        }

        val existingShortLessons = dao.getAllShortLessons().firstOrNull()
        if (existingShortLessons.isNullOrEmpty()) {
            dao.insertShortLessons(InitialShortLessonsData.getInitialShortLessons())
        } else {
            val initial = InitialShortLessonsData.getInitialShortLessons()
            val existingIds = existingShortLessons.map { it.id }.toSet()
            val missing = initial.filter { it.id !in existingIds }
            if (missing.isNotEmpty()) {
                dao.insertShortLessons(missing)
            }
        }

        evaluateBadgesAndProgress()
    }

    /**
     * Resilient offline streak validator that uses calendar date arithmetic
     */
    suspend fun recordDailyLogin() {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val now = System.currentTimeMillis()
        val lastDate = profile.lastLoginDate
        
        val calNow = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val calLast = Calendar.getInstance().apply {
            timeInMillis = lastDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val dayDiff = ((calNow.timeInMillis - calLast.timeInMillis) / TimeUnit.DAYS.toMillis(1)).toInt()

        if (dayDiff >= 1) {
            val isStreakBroken = dayDiff > 1 && profile.currentStreak >= 1
            val newStreak = if (dayDiff == 1) {
                profile.currentStreak + 1
            } else {
                1 // Missed one or more days, restart current streak
            }
            val newBest = maxOf(newStreak, profile.bestStreak)
            val updated = profile.copy(
                currentStreak = newStreak,
                bestStreak = newBest,
                lastLoginDate = now,
                xpTotal = profile.xpTotal + 25,
                hasPendingPersistenceCheck = if (isStreakBroken) true else profile.hasPendingPersistenceCheck,
                pendingPersistenceStreakType = if (isStreakBroken) "Daily Sovereign Ritual" else profile.pendingPersistenceStreakType
            )
            val recalced = checkTierProgression(updated)
            dao.updateUserProfile(recalced)
        }
        evaluateBadgesAndProgress()
    }

    suspend fun completeDailyRitual(): Boolean {
        val profile = dao.getUserProfile().firstOrNull() ?: return false
        val now = System.currentTimeMillis()
        val newStreak = profile.currentStreak + 1
        val newBest = maxOf(newStreak, profile.bestStreak)
        val newLearning = profile.learningStreak + 1
        val newJournal = profile.journalStreak + 1
        val updated = profile.copy(
            currentStreak = newStreak,
            bestStreak = newBest,
            learningStreak = newLearning,
            journalStreak = newJournal,
            lastLoginDate = now,
            xpTotal = profile.xpTotal + 50
        )
        val recalced = checkTierProgression(updated)
        dao.updateUserProfile(recalced)
        evaluateBadgesAndProgress()
        return true
    }

    suspend fun updateDefiniteChiefAim(aim: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(definiteChiefAim = aim)
        dao.updateUserProfile(updated)
    }

    suspend fun updateLeaderboardOptIn(isOptedIn: Boolean) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(isLeaderboardOptedIn = isOptedIn)
        dao.updateUserProfile(updated)
    }

    suspend fun updateAffirmationAudioMemo(path: String?) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(affirmationAudioPath = path)
        dao.updateUserProfile(updated)
    }

    suspend fun completeDailyAffirmation(): Pair<Boolean, Int> {
        val profile = dao.getUserProfile().firstOrNull() ?: return Pair(false, 0)
        val todayEpochDay = getTodayEpochDay()
        val lastDay = profile.lastAffirmationEpochDay

        if (lastDay == todayEpochDay) {
            return Pair(true, profile.affirmationStreak)
        }

        val newAffirmationStreak = if (lastDay == todayEpochDay - 1) {
            profile.affirmationStreak + 1
        } else {
            1
        }

        val newBest = maxOf(newAffirmationStreak, profile.bestAffirmationStreak)
        val now = System.currentTimeMillis()
        val updated = profile.copy(
            affirmationStreak = newAffirmationStreak,
            bestAffirmationStreak = newBest,
            lastAffirmationEpochDay = todayEpochDay,
            lastLoginDate = now,
            xpTotal = profile.xpTotal + 50
        )
        val recalced = checkTierProgression(updated)
        dao.updateUserProfile(recalced)
        evaluateBadgesAndProgress()
        return Pair(true, newAffirmationStreak)
    }

    // --- Daily Think and Grow Rich Habits / Rituals Persistence ---

    suspend fun toggleHabitCompletion(
        habitId: String,
        dateEpochDay: Long = getTodayEpochDay(),
        durationMinutes: Int? = null,
        notes: String = ""
    ): Boolean {
        val existing = dao.getLogForHabitOnDay(habitId, dateEpochDay)
        val habit = dao.getHabitById(habitId).firstOrNull()
        val xp = habit?.xpReward ?: 30
        val duration = durationMinutes ?: habit?.targetMinutes ?: 15
        val dateFormatted = formatDateEpochDay(dateEpochDay)

        return if (existing != null) {
            // Remove log
            dao.deleteHabitLog(habitId, dateEpochDay)
            val profile = dao.getUserProfile().firstOrNull()
            if (profile != null) {
                val updated = profile.copy(xpTotal = maxOf(0, profile.xpTotal - xp))
                dao.updateUserProfile(checkTierProgression(updated))
            }
            evaluateBadgesAndProgress()
            false
        } else {
            // Create log
            val log = DailyHabitLogEntity(
                habitId = habitId,
                dateEpochDay = dateEpochDay,
                dateFormatted = dateFormatted,
                completedAt = System.currentTimeMillis(),
                durationMinutes = duration,
                notes = notes,
                xpEarned = xp
            )
            dao.insertHabitLog(log)

            val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
            val now = System.currentTimeMillis()
            val isToday = dateEpochDay == getTodayEpochDay()
            val newStreak = if (isToday) profile.currentStreak + 1 else profile.currentStreak
            val newBest = maxOf(newStreak, profile.bestStreak)
            val updated = profile.copy(
                xpTotal = profile.xpTotal + xp,
                currentStreak = newStreak,
                bestStreak = newBest,
                lastLoginDate = now
            )
            dao.updateUserProfile(checkTierProgression(updated))
            evaluateBadgesAndProgress()
            true
        }
    }

    suspend fun logHabitWithReflection(
        habitId: String,
        dateEpochDay: Long = getTodayEpochDay(),
        durationMinutes: Int,
        notes: String,
        saveToNotebook: Boolean = false
    ): Boolean {
        val habit = dao.getHabitById(habitId).firstOrNull()
        val xp = habit?.xpReward ?: 30
        val dateFormatted = formatDateEpochDay(dateEpochDay)

        val log = DailyHabitLogEntity(
            habitId = habitId,
            dateEpochDay = dateEpochDay,
            dateFormatted = dateFormatted,
            completedAt = System.currentTimeMillis(),
            durationMinutes = durationMinutes,
            notes = notes,
            xpEarned = xp
        )
        dao.insertHabitLog(log)

        if (saveToNotebook && notes.isNotBlank() && habit != null) {
            dao.insertNotebookEntry(
                NotebookEntryEntity(
                    moduleId = null,
                    moduleTitle = "Daily Ritual: ${habit.principle}",
                    title = "${habit.title} - $dateFormatted",
                    content = notes,
                    promptQuestion = habit.description,
                    tags = "Ritual, ${habit.category}, ${habit.principle}",
                    timestamp = System.currentTimeMillis(),
                    isFavorite = false
                )
            )
        }

        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val now = System.currentTimeMillis()
        val isToday = dateEpochDay == getTodayEpochDay()
        val newStreak = if (isToday) profile.currentStreak + 1 else profile.currentStreak
        val newBest = maxOf(newStreak, profile.bestStreak)
        val updated = profile.copy(
            xpTotal = profile.xpTotal + xp + (if (saveToNotebook) 50 else 0),
            currentStreak = newStreak,
            bestStreak = newBest,
            lastLoginDate = now
        )
        dao.updateUserProfile(checkTierProgression(updated))
        evaluateBadgesAndProgress()
        return true
    }

    suspend fun addCustomHabit(
        title: String,
        principle: String,
        description: String,
        category: String,
        iconKey: String,
        targetMinutes: Int,
        xpReward: Int
    ) {
        val customHabit = DailyHabitEntity(
            id = "habit_custom_${System.currentTimeMillis()}",
            title = title.trim(),
            principle = principle.trim(),
            description = description.trim(),
            category = category,
            iconKey = iconKey,
            targetMinutes = targetMinutes,
            xpReward = xpReward,
            isPredefined = false,
            orderIndex = 100
        )
        dao.insertHabit(customHabit)
    }

    suspend fun deleteHabit(habitId: String) {
        dao.deleteHabit(habitId)
    }

    suspend fun logOnboardingStep(
        stepNumber: Int,
        stepName: String,
        isCompleted: Boolean = true
    ) {
        dao.insertOnboardingStepLog(
            com.example.data.model.OnboardingStepLogEntity(
                stepNumber = stepNumber,
                stepName = stepName,
                timestamp = System.currentTimeMillis(),
                isCompleted = isCompleted
            )
        )
    }

    suspend fun saveOnboardingStep(step: Int) {
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val maxReached = maxOf(profile.onboardingMaxStepReached, step)
        val updated = profile.copy(
            onboardingStep = step,
            onboardingMaxStepReached = maxReached
        )
        dao.updateUserProfile(updated)
        logOnboardingStep(step, "Reached Step $step", isCompleted = false)
    }

    suspend fun saveOnboardingName(name: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val updated = profile.copy(
            name = name.trim().ifBlank { profile.name },
            onboardingStep = maxOf(profile.onboardingStep, 2),
            onboardingMaxStepReached = maxOf(profile.onboardingMaxStepReached, 2)
        )
        dao.updateUserProfile(updated)
        logOnboardingStep(2, "Name Inscription: ${updated.name}", isCompleted = true)
    }

    suspend fun saveOnboardingChiefAim(aim: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val updated = profile.copy(
            definiteChiefAim = aim.trim(),
            onboardingStep = maxOf(profile.onboardingStep, 3),
            onboardingMaxStepReached = maxOf(profile.onboardingMaxStepReached, 3)
        )
        dao.updateUserProfile(updated)
        logOnboardingStep(3, "Definite Chief Aim Inscription", isCompleted = true)
    }

    suspend fun completeMindsetAssessment(
        belief: Int,
        discipline: Int,
        desire: Int,
        persistence: Int,
        identity: Int
    ) {
        val overallScore = ((belief + discipline + desire + persistence + identity) / 5).coerceIn(10, 100)
        val assignedTier = calculateTierFromScore(overallScore)
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        
        val updated = profile.copy(
            mindsetScore = overallScore,
            tierName = assignedTier,
            beliefScore = belief,
            disciplineScore = discipline,
            desireScore = desire,
            persistenceScore = persistence,
            identityScore = identity,
            onboardingStep = maxOf(profile.onboardingStep, 4),
            onboardingMaxStepReached = maxOf(profile.onboardingMaxStepReached, 4),
            xpTotal = profile.xpTotal + 250 // assessment completion XP
        )
        val finalProfile = checkTierProgression(updated)
        dao.updateUserProfile(finalProfile)
        dao.unlockBadge("badge_assessment")
        logOnboardingStep(4, "Mindset Assessment Completed: Score $overallScore Tier $assignedTier", isCompleted = true)
        evaluateBadgesAndProgress()
    }

    suspend fun completeUnifiedOnboarding() {
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val updated = profile.copy(
            hasCompletedOnboarding = true,
            onboardingStep = 5,
            onboardingMaxStepReached = 5,
            xpTotal = profile.xpTotal + 100 // Onboarding completion bonus XP
        )
        val finalProfile = checkTierProgression(updated)
        dao.updateUserProfile(finalProfile)
        dao.unlockBadge("badge_assessment")
        logOnboardingStep(5, "Unlocked Sovereign Arsenal & Dashboard Handoff", isCompleted = true)
        evaluateBadgesAndProgress()
    }

    suspend fun resetOnboardingForDev() {
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val updated = profile.copy(
            hasCompletedOnboarding = false,
            onboardingStep = 1,
            onboardingMaxStepReached = 1
        )
        dao.updateUserProfile(updated)
        logOnboardingStep(1, "Reset Onboarding Flow for Testing", isCompleted = false)
    }

    suspend fun completeMoneyBlueprintAssessment(
        scarcityScore: Int,
        guiltScore: Int,
        fearFailureScore: Int,
        fearJudgmentScore: Int,
        selfWorthScore: Int
    ): MoneyBlueprintResultEntity {
        // Find top 2 patterns
        val categoryScores = listOf(
            Triple(MoneyBlueprintResultEntity.PATTERN_SCARCITY, "Scarcity & Zero-Sum Mindset", scarcityScore),
            Triple(MoneyBlueprintResultEntity.PATTERN_GUILT, "Guilt Around Wealth & Deservedness", guiltScore),
            Triple(MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE, "Fear of Financial Loss & Failure", fearFailureScore),
            Triple(MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT, "Fear of Judgment & Social Rejection", fearJudgmentScore),
            Triple(MoneyBlueprintResultEntity.PATTERN_SELF_WORTH, "Self-Worth Tied to Money & Imposter Syndrome", selfWorthScore)
        ).sortedByDescending { it.third }

        val primary = categoryScores[0]
        val secondary = categoryScores[1]
        val overallAvg = ((scarcityScore + guiltScore + fearFailureScore + fearJudgmentScore + selfWorthScore) / 5)

        val recommendedModules = when (primary.first) {
            MoneyBlueprintResultEntity.PATTERN_SCARCITY -> "1,9" // Vault 1 (Desire/Definiteness of Purpose), Vault 9 (Subconscious Mind)
            MoneyBlueprintResultEntity.PATTERN_GUILT -> "2,10" // Vault 2 (Faith/Autosuggestion), Vault 10 (The Brain/Transmutation)
            MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE -> "7,8" // Vault 7 (Decision), Vault 8 (Persistence)
            MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT -> "9,11" // Vault 9 (Mastermind/Sixth Sense), Vault 11 (Mastermind Synergy)
            MoneyBlueprintResultEntity.PATTERN_SELF_WORTH -> "3,13" // Vault 3 (Specialized Knowledge), Vault 13 (The Sixth Sense)
            else -> "1,2"
        }

        val recommendedFeature = when (primary.first) {
            MoneyBlueprintResultEntity.PATTERN_SCARCITY -> "money_mindset_journal"
            MoneyBlueprintResultEntity.PATTERN_GUILT -> "mastermind_chat"
            MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE -> "fear_reframe"
            MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT -> "mastermind_circle"
            MoneyBlueprintResultEntity.PATTERN_SELF_WORTH -> "notebook"
            else -> "money_mindset_journal"
        }

        val summaryInsight = when (primary.first) {
            MoneyBlueprintResultEntity.PATTERN_SCARCITY ->
                "Your primary limiting blueprint is the Scarcity Pattern. You possess high vigilance and survival instincts, but treat wealth as a fixed pie. Shifting your subconscious awareness from scarcity to infinite value creation will unlock effortless financial flow."
            MoneyBlueprintResultEntity.PATTERN_GUILT ->
                "Your primary limiting blueprint is Guilt Around Abundance. You have strong empathy and moral integrity, but harbor hidden feelings that large wealth is selfish. When you realize ethical wealth magnifies your capacity to serve others, the psychological ceiling dissolves."
            MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE ->
                "Your primary limiting blueprint is Fear of Financial Loss. You value prudence and precision, but the anxiety of temporary defeat paralyzes high-conviction decisions. Transmuting defeat into data will ignite exponential momentum."
            MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT ->
                "Your primary limiting blueprint is Fear of Judgment & Envy. You value harmonious relationships, but play small to avoid alienation or criticism. Sovereign clarity attracts higher-caliber masterminds who celebrate your ascension."
            MoneyBlueprintResultEntity.PATTERN_SELF_WORTH ->
                "Your primary limiting blueprint is Self-Worth Tied to Balance. You have fierce ambition, but let financial fluctuations dictate your internal dignity. True sovereignty anchors your worth in character, rendering external market swings powerless."
            else -> "Your blueprint highlights key opportunities for subconscious rewiring and sovereign clarity."
        }

        val actionPledge = when (primary.first) {
            MoneyBlueprintResultEntity.PATTERN_SCARCITY -> "Daily Transmutation: Log 1 expense with abundance conviction in your Money Mindset Journal."
            MoneyBlueprintResultEntity.PATTERN_GUILT -> "Daily Transmutation: Recite your Definite Chief Aim morning and evening with unwavering self-worth."
            MoneyBlueprintResultEntity.PATTERN_FEAR_FAILURE -> "Daily Transmutation: Perform a Fear Reframe exercise whenever hesitation arises."
            MoneyBlueprintResultEntity.PATTERN_FEAR_JUDGMENT -> "Daily Transmutation: Engage in Mastermind discussions without diminishing your ambitions."
            MoneyBlueprintResultEntity.PATTERN_SELF_WORTH -> "Daily Transmutation: Inscribe 3 non-monetary sovereign victories in your Sovereign Notebook daily."
            else -> "Daily Transmutation: Complete 1 daily ritual and review your Money Blueprint regularly."
        }

        val resultEntity = MoneyBlueprintResultEntity(
            timestamp = System.currentTimeMillis(),
            primaryPatternKey = primary.first,
            primaryPatternTitle = primary.second,
            secondaryPatternKey = secondary.first,
            secondaryPatternTitle = secondary.second,
            scarcityScore = scarcityScore,
            guiltScore = guiltScore,
            fearFailureScore = fearFailureScore,
            fearJudgmentScore = fearJudgmentScore,
            selfWorthScore = selfWorthScore,
            overallLimitationScore = overallAvg,
            recommendedModuleIds = recommendedModules,
            recommendedFeatureKey = recommendedFeature,
            summaryInsight = summaryInsight,
            actionPledge = actionPledge
        )

        val insertedId = dao.insertMoneyBlueprintResult(resultEntity)

        // Update User Profile with Blueprint data and award 250 XP
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val updatedProfile = profile.copy(
            xpTotal = profile.xpTotal + 250,
            lastBlueprintEpochDay = getTodayEpochDay(),
            primaryBlueprintPattern = primary.second
        )
        dao.updateUserProfile(checkTierProgression(updatedProfile))
        dao.unlockBadge("badge_assessment")
        evaluateBadgesAndProgress()

        return resultEntity.copy(id = insertedId)
    }

    suspend fun deleteMoneyBlueprintResult(id: Long) {
        dao.deleteMoneyBlueprintResult(id)
    }

    suspend fun completeLesson(moduleId: Int) {
        val module = dao.getModuleById(moduleId).firstOrNull() ?: return
        addXpAndRecalculate(50)
        evaluateBadgesAndProgress()
    }

    suspend fun submitModuleCompletionReflection(
        moduleId: Int,
        answers: Map<String, String>,
        xpReward: Int = 100
    ): NotebookEntryEntity? {
        val module = dao.getModuleById(moduleId).firstOrNull() ?: return null
        val prompts = com.example.data.model.ModuleReflectionPromptsProvider.getPromptsForModule(moduleId)
        val formattedContent = com.example.data.model.ModuleReflectionPromptsProvider.formatCombinedReflection(prompts, answers)
        val promptQuestions = prompts.joinToString("\n") { it.promptText }

        val entry = NotebookEntryEntity(
            moduleId = moduleId,
            moduleTitle = module.title,
            title = "Vault ${module.order} Reflection: ${module.originalPrinciple}",
            content = formattedContent,
            promptQuestion = promptQuestions,
            tags = "Module Reflection, Vault ${module.order}, ${module.originalPrinciple}",
            timestamp = System.currentTimeMillis(),
            entryType = NotebookEntryEntity.ENTRY_TYPE_MODULE_REFLECTION
        )
        val entryId = dao.insertNotebookEntry(entry)

        val wasAlreadyCompleted = module.isCompleted
        dao.setModuleCompleted(moduleId, true)

        // Unlock next sequential module
        val allModules = dao.getAllModules().firstOrNull() ?: emptyList()
        val nextModule = allModules.firstOrNull { it.order == module.order + 1 }
            ?: allModules.firstOrNull { it.id == moduleId + 1 }
        if (nextModule != null && !nextModule.isUnlocked) {
            dao.setModuleUnlocked(nextModule.id)
        }

        val xpToAward = if (!wasAlreadyCompleted) (module.xpReward.takeIf { it > 0 } ?: xpReward) else 35
        addXpAndRecalculate(xpToAward)

        if (moduleId == 0) {
            dao.unlockBadge("badge_vault_0")
        } else if (moduleId == 1) {
            dao.unlockBadge("badge_ignition")
        } else if (moduleId == 9) {
            dao.unlockBadge("badge_mastermind")
        } else if (moduleId == 10) {
            dao.unlockBadge("badge_transmutation")
        } else if (moduleId == 13) {
            dao.unlockBadge("badge_apex_legacy")
        }

        evaluateBadgesAndProgress()
        return entry.copy(id = entryId)
    }

    suspend fun completeShortLesson(lessonId: String): Boolean {
        val lesson = dao.getShortLessonById(lessonId).firstOrNull() ?: return false
        if (!lesson.isCompleted) {
            dao.markLessonCompleted(lessonId)
            addXpAndRecalculate(lesson.xpReward)
            evaluateBadgesAndProgress()
            return true
        }
        return false
    }

    suspend fun updateLessonProgress(lessonId: String, positionSeconds: Int) {
        dao.updateLessonProgress(lessonId, positionSeconds)
    }

    suspend fun resetLessonCompletion(lessonId: String) {
        dao.resetLessonCompletion(lessonId)
        evaluateBadgesAndProgress()
    }

    suspend fun completeQuest(moduleId: Int) {
        val module = dao.getModuleById(moduleId).firstOrNull() ?: return
        if (!module.isQuestCompleted) {
            dao.setQuestCompleted(moduleId, true)
            addXpAndRecalculate(100)
        }
        evaluateBadgesAndProgress()
    }

    suspend fun saveWorksheet(moduleId: Int, f1: String, f2: String, f3: String) {
        dao.saveModuleWorksheet(moduleId, f1, f2, f3)
        addXpAndRecalculate(50)
        evaluateBadgesAndProgress()
    }

    // --- Offline-First Notebook Operations ---
    fun searchNotebookEntries(query: String): Flow<List<NotebookEntryEntity>> {
        return if (query.isBlank()) {
            dao.getAllNotebookEntries()
        } else {
            dao.searchNotebookEntries(query.trim())
        }
    }

    fun getFavoriteNotebookEntries(): Flow<List<NotebookEntryEntity>> =
        dao.getFavoriteNotebookEntries()

    suspend fun setNotebookFavorite(id: Long, isFavorite: Boolean) {
        dao.setNotebookFavorite(id, isFavorite)
    }

    suspend fun addNotebookEntry(
        moduleId: Int?,
        moduleTitle: String,
        title: String,
        content: String,
        promptQuestion: String,
        tags: String,
        isFavorite: Boolean
    ): Long {
        val entry = NotebookEntryEntity(
            moduleId = moduleId,
            moduleTitle = moduleTitle,
            title = title,
            content = content,
            promptQuestion = promptQuestion,
            tags = tags,
            timestamp = System.currentTimeMillis(),
            isFavorite = isFavorite
        )
        val id = dao.insertNotebookEntry(entry)
        
        // Update journal streak & XP offline
        val profile = dao.getUserProfile().firstOrNull()
        if (profile != null) {
            val updated = profile.copy(
                journalStreak = profile.journalStreak + 1,
                xpTotal = profile.xpTotal + 75
            )
            dao.updateUserProfile(checkTierProgression(updated))
        }
        evaluateBadgesAndProgress()
        return id
    }

    suspend fun deleteNotebookEntry(id: Long) {
        dao.deleteNotebookEntry(id)
        evaluateBadgesAndProgress()
    }

    suspend fun updateNotebookEntry(entry: NotebookEntryEntity) {
        dao.updateNotebookEntry(entry)
        evaluateBadgesAndProgress()
    }

    // --- Fear-to-Action Reframe Operations (Six Ghosts of Fear) ---
    suspend fun saveFearReframeEntry(
        fearText: String,
        worstCaseText: String,
        actionTodayText: String,
        fearCategory: String = "Mindset",
        addToDailyHabits: Boolean = true
    ): Pair<Long, Boolean> {
        val todayEpochDay = getTodayEpochDay()
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val isFirstToday = profile.lastFearReframeEpochDay != todayEpochDay
        val xpEarned = if (isFirstToday) 75 else 0

        val categoryTag = if (fearCategory.isNotBlank()) fearCategory else "Mindset"
        val formattedTitle = "Fear Reframe: ${fearText.take(40).trim()}${if (fearText.length > 40) "..." else ""}"
        val structuredContent = buildString {
            append("🛡️ GHOST OF FEAR:\n")
            append(fearText.trim())
            append("\n\n⚖️ WORST CASE DECONSTRUCTED:\n")
            append(worstCaseText.trim())
            append("\n\n⚡ IMMEDIATE ACTION TODAY:\n")
            append(actionTodayText.trim())
        }

        val entry = NotebookEntryEntity(
            moduleId = null,
            moduleTitle = "Fear-to-Action Reframe",
            title = formattedTitle,
            content = structuredContent,
            promptQuestion = "What are you avoiding because you're afraid of it? → What is the worst case? → Name 1 action today.",
            tags = "Fear Reframe, Six Ghosts, $categoryTag, Action",
            timestamp = System.currentTimeMillis(),
            isFavorite = false,
            entryType = NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME,
            fearCategory = categoryTag,
            fearText = fearText.trim(),
            worstCaseText = worstCaseText.trim(),
            actionTodayText = actionTodayText.trim(),
            isActionCompleted = false
        )
        val entryId = dao.insertNotebookEntry(entry)

        // Optionally create a linked habit / task item for follow-through tracking
        if (addToDailyHabits && actionTodayText.isNotBlank()) {
            val habitTitle = "Antidote Action: ${actionTodayText.take(35).trim()}${if (actionTodayText.length > 35) "..." else ""}"
            val customHabit = DailyHabitEntity(
                id = "habit_fear_reframe_${System.currentTimeMillis()}",
                title = habitTitle,
                principle = "Transmutation of Fear ($categoryTag)",
                description = "Concrete micro-action to dismantle fear: '$actionTodayText'",
                category = "Action",
                iconKey = "sparkles",
                targetMinutes = 15,
                xpReward = 30,
                isPredefined = false,
                orderIndex = 99
            )
            dao.insertHabit(customHabit)
            // TODO (v2 Feature): Add bidirectional sync / deep linking between notebook action status and daily habit entity.
        }

        // Update profile streak & XP (capped at once per day for XP to prevent throwaway gaming)
        val now = System.currentTimeMillis()
        val newStreak = if (isFirstToday) profile.journalStreak + 1 else profile.journalStreak
        val updatedProfile = profile.copy(
            journalStreak = newStreak,
            xpTotal = profile.xpTotal + xpEarned,
            lastFearReframeEpochDay = todayEpochDay,
            fearReframeCount = profile.fearReframeCount + 1,
            lastLoginDate = now
        )
        dao.updateUserProfile(checkTierProgression(updatedProfile))
        evaluateBadgesAndProgress()

        return Pair(entryId, isFirstToday)
    }

    suspend fun toggleFearActionCompleted(entryId: Long, isCompleted: Boolean) {
        val allEntries = dao.getAllNotebookEntries().firstOrNull() ?: return
        val entry = allEntries.firstOrNull { it.id == entryId } ?: return
        val updated = entry.copy(isActionCompleted = isCompleted)
        dao.updateNotebookEntry(updated)
    }

    // --- Decision Log Operations (Decisiveness Habit & 30-Day Revisit) ---
    suspend fun saveDecisionLogEntry(
        decisionText: String,
        confidenceLevel: Int = 3,
        customTimestamp: Long = System.currentTimeMillis(),
        rationale: String = ""
    ): Pair<Long, Boolean> {
        val todayEpochDay = getTodayEpochDay()
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val xpEarned = 50 // Standard +50 XP for making and logging a prompt decision

        val formattedTitle = "Decision: ${decisionText.take(40).trim()}${if (decisionText.length > 40) "..." else ""}"
        val structuredContent = buildString {
            append("👑 SOVEREIGN DECISION:\n")
            append(decisionText.trim())
            append("\n\n🎯 GUT-CHECK CONFIDENCE: ")
            append(when (confidenceLevel) {
                1 -> "1/5 (Tentative / Leaping forward)"
                2 -> "2/5 (Moderate doubt)"
                3 -> "3/5 (Balanced confidence)"
                4 -> "4/5 (High conviction)"
                else -> "5/5 (Absolute certainty)"
            })
            if (rationale.isNotBlank()) {
                append("\n\n📝 CONTEXT / RATIONALE:\n")
                append(rationale.trim())
            }
        }

        val entry = NotebookEntryEntity(
            moduleId = null,
            moduleTitle = "Decision Log",
            title = formattedTitle,
            content = structuredContent,
            promptQuestion = "What did you decide? (Decide quickly, change mind slowly)",
            tags = "Decision Log, Decisiveness, Mastery",
            timestamp = customTimestamp,
            isFavorite = false,
            entryType = NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG,
            decisionText = decisionText.trim(),
            confidenceLevel = confidenceLevel.coerceIn(1, 5),
            decisionRationale = rationale.trim(),
            outcomeText = "",
            outcomeTag = "",
            isRevisited = false,
            revisitedTimestamp = 0L
        )
        val entryId = dao.insertNotebookEntry(entry)

        // Award +50 XP immediately for deciding & logging
        val now = System.currentTimeMillis()
        val updatedProfile = profile.copy(
            xpTotal = profile.xpTotal + xpEarned,
            journalStreak = profile.journalStreak + (if (profile.lastFearReframeEpochDay != todayEpochDay) 1 else 0),
            lastLoginDate = now
        )
        dao.updateUserProfile(checkTierProgression(updatedProfile))
        evaluateBadgesAndProgress()

        return Pair(entryId, true)
    }

    suspend fun revisitDecisionLog(
        entryId: Long,
        outcomeText: String,
        outcomeTag: String
    ): Boolean {
        val allEntries = dao.getAllNotebookEntries().firstOrNull() ?: return false
        val entry = allEntries.firstOrNull { it.id == entryId } ?: return false

        val updatedContent = buildString {
            append(entry.content.trim())
            append("\n\n🔍 30-DAY REVISIT OUTCOME [Tag: $outcomeTag]:\n")
            append(outcomeText.trim())
        }

        val updated = entry.copy(
            content = updatedContent,
            outcomeText = outcomeText.trim(),
            outcomeTag = outcomeTag.trim(),
            isRevisited = true,
            revisitedTimestamp = System.currentTimeMillis()
        )
        dao.updateNotebookEntry(updated)

        // Award flat +25 XP for following through on 30-day revisit regardless of outcome tag
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val updatedProfile = profile.copy(
            xpTotal = profile.xpTotal + 25,
            lastLoginDate = System.currentTimeMillis()
        )
        dao.updateUserProfile(checkTierProgression(updatedProfile))
        evaluateBadgesAndProgress()

        return true
    }

    // --- Money Mindset Journal Operations (Beliefs, Emotional Patterns & Spending/Saving Awareness) ---
    suspend fun saveMoneyMindsetEntry(
        decisionType: String,
        actionText: String,
        emotion: String,
        beliefText: String,
        amount: String = "",
        promptQuestion: String = "What financial decision did you make today, and what belief or emotion was driving it?"
    ): Pair<Long, Boolean> {
        val todayEpochDay = getTodayEpochDay()
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val xpEarned = 50 // Standard +50 XP for daily financial self-awareness journal

        val formattedTitle = "$decisionType: ${actionText.take(35).trim()}${if (actionText.length > 35) "..." else ""}"
        val structuredContent = buildString {
            append("🪙 FINANCIAL DECISION [Type: $decisionType]:\n")
            append(actionText.trim())
            if (amount.isNotBlank()) {
                append("\n\n💵 AMOUNT / IMPACT: ")
                append(amount.trim())
            }
            append("\n\n💭 UNDERLYING EMOTION & BELIEF:\n")
            append("Emotion: $emotion\n")
            append("Internal Self-Talk: ")
            append(beliefText.trim())
        }

        val entry = NotebookEntryEntity(
            moduleId = null,
            moduleTitle = "Money Mindset Journal",
            title = formattedTitle,
            content = structuredContent,
            promptQuestion = promptQuestion,
            tags = "Money Mindset, $decisionType, $emotion, Reflection",
            timestamp = System.currentTimeMillis(),
            isFavorite = false,
            entryType = NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET
        )
        val entryId = dao.insertNotebookEntry(entry)

        // Award +50 XP and increment journal streak
        val now = System.currentTimeMillis()
        val updatedProfile = profile.copy(
            xpTotal = profile.xpTotal + xpEarned,
            journalStreak = profile.journalStreak + 1,
            lastLoginDate = now
        )
        dao.updateUserProfile(checkTierProgression(updatedProfile))
        evaluateBadgesAndProgress()

        return Pair(entryId, true)
    }

    // --- Persistence Streak Recovery Operations (Napoleon Hill Comeback Protocol) ---
    suspend fun saveComebackEntry(
        streakType: String,
        obstacleText: String,
        tomorrowPlanText: String
    ): Pair<Long, Int> {
        val todayEpochDay = getTodayEpochDay()
        val profile = dao.getUserProfile().firstOrNull() ?: UserProfileEntity()
        val xpEarned = 35 // Small flat XP to reinforce showing up after a miss

        val targetStreakName = streakType.ifBlank { "Daily Sovereign Ritual" }
        val formattedTitle = "Comeback: $targetStreakName Recovery"
        val structuredContent = buildString {
            append("🔥 COMEBACK REFLECTION [$targetStreakName]:\n")
            if (obstacleText.isNotBlank()) {
                append("• What broke the streak: ")
                append(obstacleText.trim())
                append("\n\n")
            } else {
                append("• What broke the streak: Acknowledged temporary disruption without justification.\n\n")
            }
            append("⚡ CONCRETE PLAN FOR TOMORROW:\n")
            if (tomorrowPlanText.isNotBlank()) {
                append(tomorrowPlanText.trim())
            } else {
                append("Resume disciplined daily execution promptly.")
            }
        }

        val entry = NotebookEntryEntity(
            moduleId = null,
            moduleTitle = "Persistence Streak Recovery",
            title = formattedTitle,
            content = structuredContent,
            promptQuestion = "What broke the streak? → What's the plan for tomorrow?",
            tags = "Comeback, Persistence, Recovery, $targetStreakName",
            timestamp = System.currentTimeMillis(),
            isFavorite = false,
            entryType = NotebookEntryEntity.ENTRY_TYPE_COMEBACK,
            comebackStreakType = targetStreakName,
            comebackObstacle = obstacleText.trim(),
            comebackPlan = tomorrowPlanText.trim()
        )
        val entryId = dao.insertNotebookEntry(entry)

        val now = System.currentTimeMillis()
        val updatedProfile = profile.copy(
            xpTotal = profile.xpTotal + xpEarned,
            comebacksCount = profile.comebacksCount + 1,
            hasPendingPersistenceCheck = false,
            lastPersistenceCheckEpochDay = todayEpochDay,
            lastLoginDate = now
        )
        dao.updateUserProfile(checkTierProgression(updatedProfile))
        evaluateBadgesAndProgress()

        return Pair(entryId, xpEarned)
    }

    suspend fun dismissPendingPersistenceCheck() {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(hasPendingPersistenceCheck = false)
        dao.updateUserProfile(updated)
    }

    suspend fun setPendingPersistenceCheck(streakType: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(
            hasPendingPersistenceCheck = true,
            pendingPersistenceStreakType = streakType
        )
        dao.updateUserProfile(updated)
    }

    suspend fun unlockAllPaidModules() {
        dao.unlockAllModules()
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(
            isPaidUnlocked = true,
            xpTotal = profile.xpTotal + 500 // Sovereign Unlock bonus XP
        )
        dao.updateUserProfile(checkTierProgression(updated))
        evaluateBadgesAndProgress()
    }

    suspend fun updateUserRole(role: String) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        dao.updateUserProfile(profile.copy(role = role))
    }

    suspend fun updateUserBirthDate(
        birthYear: Int,
        birthMonth: Int = 1,
        birthDay: Int = 1,
        lifeExpectancyYears: Int = 90
    ) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(
            birthYear = birthYear,
            birthMonth = birthMonth,
            birthDay = birthDay,
            lifeExpectancyYears = lifeExpectancyYears
        )
        dao.updateUserProfile(updated)
    }

    suspend fun manualAdminUpdate(
        xp: Int,
        tier: String,
        mindsetScore: Int,
        isPaidUnlocked: Boolean,
        role: String
    ) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(
            xpTotal = xp,
            tierName = tier,
            mindsetScore = mindsetScore,
            isPaidUnlocked = isPaidUnlocked,
            role = role
        )
        dao.updateUserProfile(updated)
        if (isPaidUnlocked) {
            dao.unlockAllModules()
        }
        evaluateBadgesAndProgress()
    }

    suspend fun resetAllProgress() {
        dao.resetAllModulesProgress()
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val resetProfile = profile.copy(
            xpTotal = 0,
            currentStreak = 1,
            journalStreak = 1,
            learningStreak = 1,
            tierName = "Novice",
            isPaidUnlocked = false
        )
        dao.updateUserProfile(resetProfile)

        // Reset badges progress
        val initialBadges = InitialContentData.getInitialBadges()
        dao.insertBadges(initialBadges)
        evaluateBadgesAndProgress()
    }

    /**
     * Achievement evaluation engine: checks all criteria and updates Room badges table
     */
    suspend fun evaluateBadgesAndProgress() {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val notebookEntries = dao.getAllNotebookEntries().firstOrNull() ?: emptyList()
        val modules = dao.getAllModules().firstOrNull() ?: emptyList()
        val badges = dao.getAllBadges().firstOrNull() ?: emptyList()

        val completedCount = modules.count { it.isCompleted }
        val notebookCount = notebookEntries.size
        
        // Distinct entry days calculation
        val distinctCalendarDays = notebookEntries.map { entry ->
            val cal = Calendar.getInstance().apply {
                timeInMillis = entry.timestamp
            }
            "${cal.get(Calendar.YEAR)}_${cal.get(Calendar.DAY_OF_YEAR)}"
        }.distinct().size

        // Streak days or entry days
        val ritualDays = maxOf(
            profile.currentStreak,
            profile.bestStreak,
            profile.journalStreak,
            distinctCalendarDays,
            notebookCount
        )

        val allHabitLogsList = dao.getAllHabitLogs().firstOrNull() ?: emptyList()
        val (habitCurrentStreak, habitBestStreak) = calculateHabitStreak(allHabitLogsList, getTodayEpochDay())
        val effectiveStreak = maxOf(profile.currentStreak, profile.bestStreak, habitCurrentStreak, habitBestStreak, ritualDays)

        val allGivingLogsList = dao.getAllGivingLogs().firstOrNull() ?: emptyList()
        val givingActsCount = allGivingLogsList.size
        val (givingStreakWeeks, _) = calculateGivingStreakWeeks(allGivingLogsList)

        var newlyEarnedXp = 0

        badges.forEach { badge ->
            var newProgress = badge.progress
            var shouldUnlock = badge.isUnlocked

            when (badge.id) {
                "badge_streak_3" -> {
                    // 3-Day Discipline Spark Milestone
                    newProgress = effectiveStreak.coerceAtMost(3)
                    if (effectiveStreak >= 3) shouldUnlock = true
                }
                "badge_consistent_ritualist" -> {
                    // Earned for completing 7 days of entries & daily rituals
                    val p = maxOf(ritualDays, effectiveStreak)
                    newProgress = p.coerceAtMost(7)
                    if (p >= 7) shouldUnlock = true
                }
                "badge_first_reflection" -> {
                    // Authored very first notebook entry
                    newProgress = if (notebookCount > 0) 1 else 0
                    if (notebookCount > 0) shouldUnlock = true
                }
                "badge_prolific_scribe" -> {
                    // Authored 10+ reflections
                    newProgress = notebookCount.coerceAtMost(10)
                    if (notebookCount >= 10) shouldUnlock = true
                }
                "badge_assessment" -> {
                    val isDone = profile.hasCompletedOnboarding || profile.mindsetScore > 0
                    newProgress = if (isDone) 1 else 0
                    if (isDone) shouldUnlock = true
                }
                "badge_vault_0" -> {
                    val isDone = modules.any { it.id == 0 && it.isCompleted }
                    newProgress = if (isDone) 1 else 0
                    if (isDone) shouldUnlock = true
                }
                "badge_flame_7" -> {
                    // 7-Day Sovereign Flame Milestone
                    newProgress = effectiveStreak.coerceAtMost(7)
                    if (effectiveStreak >= 7) shouldUnlock = true
                }
                "badge_ignition" -> {
                    val isDone = modules.any { it.id == 1 && it.isCompleted }
                    newProgress = if (isDone) 1 else 0
                    if (isDone) shouldUnlock = true
                }
                "badge_mastermind" -> {
                    val isDone = modules.any { it.id == 9 && it.isCompleted } || profile.xpTotal >= 1500
                    newProgress = if (isDone) 1 else 0
                    if (isDone) shouldUnlock = true
                }
                "badge_transmutation" -> {
                    val isDone = modules.any { it.id == 10 && it.isCompleted }
                    newProgress = if (isDone) 1 else 0
                    if (isDone) shouldUnlock = true
                }
                "badge_fortress_14" -> {
                    // 14-Day Fortress of Habit Milestone
                    newProgress = effectiveStreak.coerceAtMost(14)
                    if (effectiveStreak >= 14) shouldUnlock = true
                }
                "badge_streak_30" -> {
                    // 30-Day Transmutation Ironclad Milestone
                    newProgress = effectiveStreak.coerceAtMost(30)
                    if (effectiveStreak >= 30) shouldUnlock = true
                }
                "achievement_section_1" -> {
                    // Section I: The Mental Foundation (Vaults 0, 1, 2, 3)
                    val s1Modules = listOf(0, 1, 2, 3)
                    val s1Done = s1Modules.count { id -> modules.any { it.id == id && it.isCompleted } }
                    newProgress = s1Done.coerceAtMost(4)
                    if (s1Done >= 4) shouldUnlock = true
                }
                "achievement_section_2" -> {
                    // Section II: Strategic Architecture (Vaults 4, 5, 6, 7, 8)
                    val s2Modules = listOf(4, 5, 6, 7, 8)
                    val s2Done = s2Modules.count { id -> modules.any { it.id == id && it.isCompleted } }
                    newProgress = s2Done.coerceAtMost(5)
                    if (s2Done >= 5) shouldUnlock = true
                }
                "achievement_section_3" -> {
                    // Section III: Higher Synergies (Vaults 9, 10, 11)
                    val s3Modules = listOf(9, 10, 11)
                    val s3Done = s3Modules.count { id -> modules.any { it.id == id && it.isCompleted } }
                    newProgress = s3Done.coerceAtMost(3)
                    if (s3Done >= 3) shouldUnlock = true
                }
                "achievement_section_4" -> {
                    // Section IV: Sovereign Apex (Vaults 12, 13)
                    val s4Modules = listOf(12, 13)
                    val s4Done = s4Modules.count { id -> modules.any { it.id == id && it.isCompleted } }
                    newProgress = s4Done.coerceAtMost(2)
                    if (s4Done >= 2) shouldUnlock = true
                }
                "badge_apex_legacy" -> {
                    newProgress = completedCount.coerceAtMost(13)
                    if (completedCount >= 13) shouldUnlock = true
                }
                "badge_giving_first" -> {
                    newProgress = if (givingActsCount > 0) 1 else 0
                    if (givingActsCount > 0) shouldUnlock = true
                }
                "badge_giving_5" -> {
                    newProgress = givingActsCount.coerceAtMost(5)
                    if (givingActsCount >= 5) shouldUnlock = true
                }
                "badge_giving_15" -> {
                    newProgress = givingActsCount.coerceAtMost(15)
                    if (givingActsCount >= 15) shouldUnlock = true
                }
                "badge_giving_streak_3" -> {
                    newProgress = givingStreakWeeks.coerceAtMost(3)
                    if (givingStreakWeeks >= 3) shouldUnlock = true
                }
                "badge_giving_titan" -> {
                    newProgress = givingActsCount.coerceAtMost(25)
                    if (givingActsCount >= 25) shouldUnlock = true
                }
                else -> {
                    // Leave unchanged
                }
            }

            val newlyUnlocked = shouldUnlock && !badge.isUnlocked
            if (newlyUnlocked) {
                newlyEarnedXp += badge.xpReward
            }

            if (newProgress != badge.progress || shouldUnlock != badge.isUnlocked) {
                dao.updateBadge(
                    badge.copy(
                        progress = newProgress,
                        isUnlocked = shouldUnlock,
                        unlockedAt = if (newlyUnlocked) System.currentTimeMillis() else badge.unlockedAt
                    )
                )
            }
        }

        if (newlyEarnedXp > 0) {
            addXpAndRecalculate(newlyEarnedXp)
        }
    }

    suspend fun getOfflineCacheStatus(): OfflineCacheStatus {
        val profile = dao.getUserProfile().firstOrNull()
        val completedCount = dao.getCompletedModulesCount().firstOrNull() ?: 0
        val notesCount = dao.getNotebookEntriesCount().firstOrNull() ?: 0
        return OfflineCacheStatus(
            totalModulesCached = 14,
            completedModulesCount = completedCount,
            totalNotebookEntriesCached = notesCount,
            activeStreakDays = profile?.currentStreak ?: 1,
            bestStreakDays = profile?.bestStreak ?: 1,
            isLocalDatabaseReady = true,
            lastOfflineSyncTime = System.currentTimeMillis()
        )
    }

    // --- Mastermind Circles Operations ---

    fun getGroupMembers(groupId: String): Flow<List<MastermindMemberEntity>> =
        dao.getMastermindMembers(groupId)

    fun getGroupWeeklyCheckins(groupId: String, weekNumber: Int, year: Int): Flow<List<MastermindCheckinEntity>> =
        dao.getCheckinsForWeek(groupId, weekNumber, year)

    fun getAllCheckinsForGroup(groupId: String): Flow<List<MastermindCheckinEntity>> =
        dao.getAllCheckinsForGroup(groupId)

    fun getMastermindGroupById(groupId: String): Flow<MastermindGroupEntity?> =
        dao.getMastermindGroupById(groupId)

    suspend fun joinGroupByInviteCode(code: String): Pair<Boolean, String> {
        val trimmed = code.trim().uppercase(Locale.US)
        val group = dao.getMastermindGroupByInviteCode(trimmed)
            ?: return Pair(false, "No Mastermind Circle found for code '$trimmed'")

        val profile = dao.getUserProfile().firstOrNull()
        val userName = profile?.name ?: "You (Initiate)"
        val userTier = profile?.tierName ?: "Builder"

        dao.clearUserGroupMembership()
        dao.setUserGroupMembership(group.id)

        // Add or update current user in this group
        val userMember = MastermindMemberEntity(
            id = "mem_user_${group.id}",
            groupId = group.id,
            displayName = "$userName",
            avatarInitial = "YOU",
            avatarColorHex = "#D4AF37",
            tierTitle = userTier,
            currentModuleTitle = "Vault 4: Auto-Suggestion",
            weeklyXp = 400,
            isCurrentUser = true
        )
        dao.insertMastermindMember(userMember)

        // Award +50 XP for joining an accountability circle
        addXpAndRecalculate(50)
        return Pair(true, "Successfully entered '${group.name}'! (+50 XP)")
    }

    suspend fun autoMatchCircle(userTier: String): Pair<Boolean, String> {
        val allGroups = dao.getAllMastermindGroups().firstOrNull() ?: emptyList()
        // Find best match by targetTier or any group with vacancy
        val matchingGroup = allGroups.find { it.targetTier.equals(userTier, ignoreCase = true) }
            ?: allGroups.firstOrNull()
            ?: return Pair(false, "No available Mastermind Circles to match right now.")

        dao.clearUserGroupMembership()
        dao.setUserGroupMembership(matchingGroup.id)

        val profile = dao.getUserProfile().firstOrNull()
        val userName = profile?.name ?: "You (Initiate)"

        val userMember = MastermindMemberEntity(
            id = "mem_user_${matchingGroup.id}",
            groupId = matchingGroup.id,
            displayName = "$userName",
            avatarInitial = "YOU",
            avatarColorHex = "#D4AF37",
            tierTitle = userTier,
            currentModuleTitle = "Vault 4: Auto-Suggestion",
            weeklyXp = 420,
            isCurrentUser = true
        )
        dao.insertMastermindMember(userMember)

        addXpAndRecalculate(50)
        return Pair(true, "Matched with '${matchingGroup.name}'! (+50 XP)")
    }

    suspend fun createMastermindCircle(
        name: String,
        motto: String,
        targetTier: String
    ): Pair<Boolean, String> {
        if (name.isBlank()) return Pair(false, "Please provide a Circle name.")
        val randomSuffix = (1000..9999).random()
        val prefix = name.filter { it.isLetter() }.take(4).uppercase(Locale.US).ifEmpty { "SOV" }
        val inviteCode = "$prefix-$randomSuffix"
        val groupId = "grp_${UUID.randomUUID().toString().take(8)}"

        val newGroup = MastermindGroupEntity(
            id = groupId,
            name = name.trim(),
            motto = motto.trim().ifEmpty { "Harmonious Mind Power & Sovereign Action" },
            inviteCode = inviteCode,
            targetTier = targetTier,
            minLevelOrModule = 1,
            maxMembers = 6,
            groupStreakWeeks = 1,
            combinedXpThisWeek = 500,
            isUserMember = true,
            createdAtTimestamp = System.currentTimeMillis()
        )

        dao.clearUserGroupMembership()
        dao.insertMastermindGroup(newGroup)

        val profile = dao.getUserProfile().firstOrNull()
        val userName = profile?.name ?: "You (Leader)"

        val userMember = MastermindMemberEntity(
            id = "mem_user_$groupId",
            groupId = groupId,
            displayName = "$userName",
            avatarInitial = "YOU",
            avatarColorHex = "#D4AF37",
            tierTitle = targetTier,
            currentModuleTitle = "Vault 4: Auto-Suggestion",
            weeklyXp = 500,
            isCurrentUser = true
        )
        dao.insertMastermindMember(userMember)

        // Also seed 2 initial peer mentors into the newly founded circle
        val peer1 = MastermindMemberEntity(
            id = "mem_peer1_$groupId",
            groupId = groupId,
            displayName = "Harrison Blake",
            avatarInitial = "HB",
            avatarColorHex = "#2E7D32",
            tierTitle = targetTier,
            currentModuleTitle = "Vault 3: Faith",
            weeklyXp = 350,
            isCurrentUser = false
        )
        val peer2 = MastermindMemberEntity(
            id = "mem_peer2_$groupId",
            groupId = groupId,
            displayName = "Genevieve Shaw",
            avatarInitial = "GS",
            avatarColorHex = "#AB47BC",
            tierTitle = targetTier,
            currentModuleTitle = "Vault 5: Specialized Knowledge",
            weeklyXp = 410,
            isCurrentUser = false
        )
        dao.insertMastermindMembers(listOf(peer1, peer2))

        addXpAndRecalculate(100)
        return Pair(true, "Created '${newGroup.name}'! Invite Code: $inviteCode (+100 XP)")
    }

    suspend fun leaveCurrentCircle() {
        val userGroup = dao.getUserMastermindGroup().firstOrNull() ?: return
        dao.removeCurrentUserFromGroup(userGroup.id)
        dao.clearUserGroupMembership()
    }

    suspend fun submitWeeklyCheckin(
        groupId: String,
        goalTitle: String,
        status: String,
        note: String
    ): Int {
        if (goalTitle.isBlank()) return 0
        val cal = Calendar.getInstance()
        val currentWeek = cal.get(Calendar.WEEK_OF_YEAR)
        val currentYear = cal.get(Calendar.YEAR)

        val profile = dao.getUserProfile().firstOrNull()
        val userName = profile?.name ?: "You (Initiate)"
        val userTier = profile?.tierName ?: "Builder"

        val checkinId = "chk_user_${groupId}_${currentYear}_$currentWeek"

        val checkin = MastermindCheckinEntity(
            id = checkinId,
            groupId = groupId,
            memberId = "mem_user_$groupId",
            memberDisplayName = userName,
            memberAvatarInitial = "YOU",
            memberAvatarColorHex = "#D4AF37",
            memberTier = userTier,
            isCurrentUser = true,
            weekNumber = currentWeek,
            year = currentYear,
            goalTitle = goalTitle.trim(),
            status = status,
            note = note.trim(),
            timestamp = System.currentTimeMillis(),
            fireCount = 1,
            clapCount = 1,
            diamondCount = 0
        )
        dao.insertMastermindCheckin(checkin)

        // Award +75 XP for completing weekly accountability check-in
        val xpGain = if (status == "YES") 75 else if (status == "PARTIAL") 50 else 30
        addXpAndRecalculate(xpGain)

        // Update group combined XP and streak
        val group = dao.getMastermindGroupById(groupId).firstOrNull()
        if (group != null) {
            val updatedGroup = group.copy(
                combinedXpThisWeek = group.combinedXpThisWeek + xpGain,
                groupStreakWeeks = group.groupStreakWeeks + (if (status == "YES") 1 else 0)
            )
            dao.updateMastermindGroup(updatedGroup)
        }

        evaluateBadgesAndProgress()
        return xpGain
    }

    suspend fun toggleCheckinReaction(checkinId: String, reactionType: String) {
        val checkins = dao.getAllCheckins().firstOrNull() ?: return
        val item = checkins.find { it.id == checkinId } ?: return

        val updated = when (reactionType.lowercase(Locale.US)) {
            "fire" -> {
                val newReact = !item.userReactedFire
                val newCount = if (newReact) item.fireCount + 1 else (item.fireCount - 1).coerceAtLeast(0)
                item.copy(userReactedFire = newReact, fireCount = newCount)
            }
            "clap" -> {
                val newReact = !item.userReactedClap
                val newCount = if (newReact) item.clapCount + 1 else (item.clapCount - 1).coerceAtLeast(0)
                item.copy(userReactedClap = newReact, clapCount = newCount)
            }
            "diamond" -> {
                val newReact = !item.userReactedDiamond
                val newCount = if (newReact) item.diamondCount + 1 else (item.diamondCount - 1).coerceAtLeast(0)
                item.copy(userReactedDiamond = newReact, diamondCount = newCount)
            }
            else -> item
        }
        dao.updateMastermindCheckin(updated)
    }

    // ==========================================
    // VISION BOARD OPERATIONS
    // ==========================================

    suspend fun addVisionBoardItem(
        title: String,
        category: String,
        imageUrl: String,
        targetTimeline: String = "",
        affirmation: String = ""
    ): Long {
        val currentItems = dao.getAllVisionBoardItems().firstOrNull() ?: emptyList()
        val nextOrder = (currentItems.maxOfOrNull { it.orderIndex } ?: -1) + 1
        val item = VisionBoardItemEntity(
            title = title.trim(),
            category = category.trim(),
            imageUrl = imageUrl.trim(),
            targetTimeline = targetTimeline.trim(),
            affirmation = affirmation.trim(),
            orderIndex = nextOrder,
            createdAt = System.currentTimeMillis()
        )
        val id = dao.insertVisionBoardItem(item)
        addXpAndRecalculate(15) // +15 XP for inscribing a new vision goal
        evaluateBadgesAndProgress()
        return id
    }

    suspend fun updateVisionBoardItem(item: VisionBoardItemEntity) {
        dao.updateVisionBoardItem(item)
        evaluateBadgesAndProgress()
    }

    suspend fun deleteVisionBoardItem(id: Long) {
        dao.deleteVisionBoardItem(id)
        evaluateBadgesAndProgress()
    }

    suspend fun updateVisionBoardItemOrder(id: Long, newOrder: Int) {
        dao.updateVisionBoardItemOrder(id, newOrder)
    }

    suspend fun toggleVisionBoardItemPin(id: Long, isPinned: Boolean) {
        dao.toggleVisionBoardItemPin(id, isPinned)
    }

    suspend fun reorderVisionBoardItems(itemsInOrder: List<VisionBoardItemEntity>) {
        itemsInOrder.forEachIndexed { index, item ->
            dao.updateVisionBoardItemOrder(item.id, index)
        }
    }

    /**
     * Daily Vision Contemplation Ritual (60-second mindfulness / subconscious impression)
     * Awards +50 XP and updates vision streak if not yet completed today.
     * Returns the XP gained (0 if already completed today, 50 if new).
     */
    suspend fun completeDailyVisionContemplation(): Int {
        val profile = dao.getUserProfile().firstOrNull() ?: return 0
        val todayEpochDay = getTodayEpochDay()
        
        if (profile.lastVisionBoardViewEpochDay == todayEpochDay) {
            // Already contemplated today
            return 0
        }

        val lastDay = profile.lastVisionBoardViewEpochDay
        val newStreak = if (lastDay == todayEpochDay - 1) {
            profile.visionBoardStreak + 1
        } else {
            1
        }
        val newBest = maxOf(newStreak, profile.bestVisionBoardStreak)
        val xpGain = 50

        val updated = profile.copy(
            visionBoardStreak = newStreak,
            bestVisionBoardStreak = newBest,
            lastVisionBoardViewEpochDay = todayEpochDay,
            xpTotal = profile.xpTotal + xpGain
        )
        dao.updateUserProfile(checkTierProgression(updated))
        evaluateBadgesAndProgress()
        return xpGain
    }

    // ==========================================
    // WEALTH GOAL TRACKER OPERATIONS
    // ==========================================

    suspend fun saveWealthGoal(goal: WealthGoalEntity) {
        dao.insertWealthGoal(goal.copy(updatedAt = System.currentTimeMillis()))
        evaluateBadgesAndProgress()
    }

    suspend fun updateWealthGoalSettings(
        goalId: Int = 1,
        title: String,
        targetAmount: Double,
        startingAmount: Double,
        targetDateEpochMillis: Long,
        currencySymbol: String = "$",
        category: String = "Financial Sovereignty",
        servicePledge: String = ""
    ) {
        val currentGoal = dao.getWealthGoalById(goalId).firstOrNull()
        val existingLogs = dao.getWealthGoalLogs(goalId).firstOrNull() ?: emptyList()
        val totalContributionsFromLogs = existingLogs.filter { !it.isMilestoneOnly }.sumOf { it.amount }
        val newCurrentAmount = (startingAmount + totalContributionsFromLogs).coerceAtLeast(0.0)

        val updatedGoal = (currentGoal ?: WealthGoalEntity(id = goalId)).copy(
            title = title.trim(),
            targetAmount = targetAmount.coerceAtLeast(1.0),
            startingAmount = startingAmount.coerceAtLeast(0.0),
            currentAmount = newCurrentAmount,
            targetDateEpochMillis = targetDateEpochMillis,
            currencySymbol = currencySymbol.trim().ifBlank { "$" },
            category = category.trim().ifBlank { "Financial Sovereignty" },
            servicePledge = servicePledge.trim(),
            isCompleted = newCurrentAmount >= targetAmount,
            updatedAt = System.currentTimeMillis()
        )
        dao.insertWealthGoal(updatedGoal)
        evaluateBadgesAndProgress()
    }

    suspend fun logWealthContribution(
        goalId: Int = 1,
        amount: Double,
        isMilestoneOnly: Boolean,
        title: String,
        note: String,
        saveToNotebook: Boolean = false
    ): Long {
        val goal = dao.getWealthGoalById(goalId).firstOrNull() ?: WealthGoalEntity(id = goalId)
        val newTotal = if (!isMilestoneOnly) (goal.currentAmount + amount).coerceAtLeast(0.0) else goal.currentAmount
        val now = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US)
        val dateFormatted = sdf.format(now)

        val log = WealthGoalLogEntity(
            goalId = goalId,
            amount = if (isMilestoneOnly) 0.0 else amount,
            isMilestoneOnly = isMilestoneOnly,
            title = title.trim().ifBlank { if (isMilestoneOnly) "Milestone Inscribed" else "Capital Inflow" },
            note = note.trim(),
            timestamp = now,
            dateFormatted = dateFormatted,
            resultingTotal = newTotal
        )
        val logId = dao.insertWealthGoalLog(log)

        // Update goal current amount and completion
        val isCompleted = newTotal >= goal.targetAmount
        val updatedGoal = goal.copy(
            currentAmount = newTotal,
            isCompleted = isCompleted,
            updatedAt = now
        )
        dao.updateWealthGoal(updatedGoal)

        // Award XP (+35 XP for logging financial progress or milestone)
        val xpGain = if (isCompleted) 100 else 35
        addXpAndRecalculate(xpGain)

        // Optionally record in Sovereign Notebook
        if (saveToNotebook) {
            val formattedAmount = "${goal.currencySymbol}${String.format(Locale.US, "%,.2f", amount)}"
            val entryTitle = if (isMilestoneOnly) {
                "★ Wealth Milestone: ${log.title}"
            } else {
                "💰 Wealth Inflow: +$formattedAmount ($title)"
            }
            val content = buildString {
                appendLine("WEALTH GOAL PROGRESS UPDATE")
                appendLine("Target: ${goal.title} (${goal.currencySymbol}${String.format(Locale.US, "%,.0f", goal.targetAmount)})")
                if (!isMilestoneOnly) {
                    appendLine("Contribution: +$formattedAmount")
                    appendLine("New Accumulated Balance: ${goal.currencySymbol}${String.format(Locale.US, "%,.2f", newTotal)}")
                }
                if (note.isNotBlank()) {
                    appendLine()
                    appendLine("Reflection / Note:")
                    appendLine(note.trim())
                }
            }
            dao.insertNotebookEntry(
                NotebookEntryEntity(
                    moduleId = null,
                    moduleTitle = "Wealth Goal Tracker",
                    title = entryTitle,
                    content = content,
                    timestamp = now,
                    tags = "Wealth, Inflow, Transmutation"
                )
            )
        }

        evaluateBadgesAndProgress()
        return logId
    }

    suspend fun deleteWealthGoalLog(logId: Long, goalId: Int = 1) {
        dao.deleteWealthGoalLog(logId)
        
        // Recalculate goal balance from startingAmount + remaining logs
        val goal = dao.getWealthGoalById(goalId).firstOrNull() ?: return
        val remainingLogs = dao.getWealthGoalLogs(goalId).firstOrNull() ?: emptyList()
        val totalContributions = remainingLogs.filter { !it.isMilestoneOnly }.sumOf { it.amount }
        val recalculatedAmount = (goal.startingAmount + totalContributions).coerceAtLeast(0.0)

        val updatedGoal = goal.copy(
            currentAmount = recalculatedAmount,
            isCompleted = recalculatedAmount >= goal.targetAmount,
            updatedAt = System.currentTimeMillis()
        )
        dao.updateWealthGoal(updatedGoal)
        evaluateBadgesAndProgress()
    }

    private suspend fun addXpAndRecalculate(xpGain: Int) {
        val profile = dao.getUserProfile().firstOrNull() ?: return
        val updated = profile.copy(xpTotal = profile.xpTotal + xpGain)
        dao.updateUserProfile(checkTierProgression(updated))
    }

    private fun checkTierProgression(profile: UserProfileEntity): UserProfileEntity {
        val calculatedTier = calculateTierFromXpAndScore(profile.xpTotal, profile.mindsetScore)
        return profile.copy(tierName = calculatedTier)
    }

    private fun calculateTierFromScore(score: Int): String {
        return when {
            score >= 90 -> "Legacy"
            score >= 75 -> "Sovereign"
            score >= 60 -> "Architect"
            score >= 40 -> "Builder"
            else -> "Novice"
        }
    }

    private fun calculateTierFromXpAndScore(xp: Int, score: Int): String {
        return when {
            xp >= 7000 || score >= 90 -> "Legacy"
            xp >= 3500 || score >= 75 -> "Sovereign"
            xp >= 1500 || score >= 60 -> "Architect"
            xp >= 500 || score >= 40 -> "Builder"
            else -> "Novice"
        }
    }

    // ==========================================
    // INCOME IDEA EXPLORER ACTIONS
    // ==========================================

    suspend fun toggleSaveIncomeIdea(ideaId: String, notes: String = ""): Boolean {
        val isSaved = dao.isIncomeIdeaSaved(ideaId).firstOrNull() ?: false
        if (isSaved) {
            dao.deleteSavedIncomeIdea(ideaId)
            return false
        } else {
            dao.insertSavedIncomeIdea(
                SavedIncomeIdeaEntity(
                    ideaId = ideaId,
                    savedAtEpoch = System.currentTimeMillis(),
                    notes = notes
                )
            )
            return true
        }
    }

    fun isIncomeIdeaSaved(ideaId: String): Flow<Boolean> = dao.isIncomeIdeaSaved(ideaId)

    // ==========================================
    // GRATITUDE & GIVING OPERATIONS
    // ==========================================

    suspend fun saveGivingGoal(goal: GivingGoalEntity) {
        dao.insertGivingGoal(goal.copy(updatedAt = System.currentTimeMillis()))
        evaluateBadgesAndProgress()
    }

    suspend fun updateGivingGoalSettings(
        goalType: String,
        targetAmount: Double,
        targetPercentage: Double,
        targetActsCount: Int,
        currencySymbol: String = "$",
        serviceMotto: String = ""
    ) {
        val currentGoal = dao.getGivingGoal(1).firstOrNull() ?: GivingGoalEntity(id = 1)
        val updatedGoal = currentGoal.copy(
            goalType = goalType,
            targetAmount = targetAmount.coerceAtLeast(0.0),
            targetPercentage = targetPercentage.coerceIn(0.0, 100.0),
            targetActsCount = targetActsCount.coerceAtLeast(1),
            currencySymbol = currencySymbol.trim().ifBlank { "$" },
            serviceMotto = serviceMotto.trim().ifBlank { "True wealth begins with the spirit of generosity. Abundance expands when circulated." },
            updatedAt = System.currentTimeMillis()
        )
        dao.insertGivingGoal(updatedGoal)
        evaluateBadgesAndProgress()
    }

    suspend fun logGivingAct(
        title: String,
        amount: Double?,
        currencySymbol: String = "$",
        category: String = GivingLogEntity.CATEGORY_CHARITY,
        recipientName: String = "",
        note: String = "",
        saveToNotebook: Boolean = false,
        customTimestamp: Long = System.currentTimeMillis()
    ): Long {
        val now = customTimestamp
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US)
        val dateFormatted = sdf.format(now)

        val isMonetary = amount != null && amount > 0.0
        val log = GivingLogEntity(
            title = title.trim(),
            amount = if (isMonetary) amount else null,
            currencySymbol = currencySymbol.trim().ifBlank { "$" },
            category = category.trim().ifBlank { GivingLogEntity.CATEGORY_CHARITY },
            recipientName = recipientName.trim(),
            note = note.trim(),
            timestamp = now,
            dateFormatted = dateFormatted,
            isMonetary = isMonetary
        )
        val logId = dao.insertGivingLog(log)

        // Award XP (+35 XP for circulating abundance & benevolence)
        val xpGain = 35
        addXpAndRecalculate(xpGain)

        // Optionally record in Sovereign Notebook
        if (saveToNotebook) {
            val amountFormatted = if (isMonetary && amount != null) " (${currencySymbol}${String.format(Locale.US, "%,.2f", amount)})" else ""
            val entryTitle = "✨ Act of Giving: ${log.title}$amountFormatted"
            val content = buildString {
                appendLine("GRATITUDE & GIVING RECORD")
                appendLine("Description: ${log.title}")
                if (isMonetary && amount != null) {
                    appendLine("Contribution Amount: ${currencySymbol}${String.format(Locale.US, "%,.2f", amount)}")
                }
                appendLine("Category: ${log.category}")
                if (log.recipientName.isNotBlank()) {
                    appendLine("Recipient / Cause: ${log.recipientName}")
                }
                if (log.note.isNotBlank()) {
                    appendLine()
                    appendLine("Gratitude & Abundance Reflection:")
                    appendLine(log.note)
                }
                appendLine()
                appendLine("Mindset Anchor: Giving freely affirms inner wealth and opens the channel for infinite return.")
            }
            dao.insertNotebookEntry(
                NotebookEntryEntity(
                    moduleId = null,
                    moduleTitle = "Gratitude & Giving Tracker",
                    title = entryTitle,
                    content = content,
                    timestamp = now,
                    tags = "Giving, Gratitude, Abundance, ${log.category}"
                )
            )
        }

        evaluateBadgesAndProgress()
        return logId
    }

    suspend fun deleteGivingLog(id: Long) {
        dao.deleteGivingLog(id)
        evaluateBadgesAndProgress()
    }

    suspend fun updateGivingLog(log: GivingLogEntity) {
        dao.updateGivingLog(log)
        evaluateBadgesAndProgress()
    }

    // ==========================================
    // COMMITMENT CONTRACT OPERATIONS
    // ==========================================

    suspend fun createCommitmentContract(
        goalStatement: String,
        whyItMatters: String,
        deadlineMillis: Long,
        signatureName: String,
        title: String = "Definite Chief Aim Covenant",
        saveToNotebook: Boolean = true
    ): Long {
        val now = System.currentTimeMillis()
        val contract = CommitmentContractEntity(
            title = title,
            goalStatement = goalStatement.trim(),
            whyItMatters = whyItMatters.trim(),
            deadlineEpochMillis = deadlineMillis,
            createdAtEpochMillis = now,
            progressPercent = 0,
            status = CommitmentContractEntity.STATUS_ACTIVE,
            signatureName = signatureName.trim(),
            signedDateEpochMillis = now,
            xpAwardedForCreation = 75,
            xpAwardedForCompletion = 150
        )
        val contractId = dao.insertCommitmentContract(contract)

        // Award Creation XP (+75 XP)
        addXpAndRecalculate(75)

        if (saveToNotebook) {
            val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val deadlineStr = sdf.format(Date(deadlineMillis))
            val content = buildString {
                appendLine("📜 SOVEREIGN COMMITMENT CONTRACT")
                appendLine("Target Deadline: $deadlineStr")
                appendLine("Signed by: ${if (signatureName.isNotBlank()) signatureName else "Sovereign Initiate"}")
                appendLine()
                appendLine("Definite Goal Statement:")
                appendLine(goalStatement.trim())
                appendLine()
                appendLine("Sacred Purpose & Stakes (Why It Matters):")
                appendLine(whyItMatters.trim())
                appendLine()
                appendLine("Solemn Affirmation: I hold myself unconditionally accountable to the manifestation of this aim.")
            }
            dao.insertNotebookEntry(
                NotebookEntryEntity(
                    moduleId = null,
                    moduleTitle = "Commitment Contract",
                    title = "Covenant: ${goalStatement.take(45)}${if (goalStatement.length > 45) "..." else ""}",
                    content = content,
                    timestamp = now,
                    tags = "Commitment Contract, Covenant, Definite Chief Aim",
                    entryType = NotebookEntryEntity.ENTRY_TYPE_COMMITMENT_CONTRACT
                )
            )
        }

        evaluateBadgesAndProgress()
        return contractId
    }

    suspend fun updateCommitmentProgress(contractId: Long, progressPercent: Int) {
        val clampedProgress = progressPercent.coerceIn(0, 100)
        dao.updateCommitmentProgress(contractId, clampedProgress)
    }

    suspend fun completeCommitmentContract(
        contractId: Long,
        completionNotes: String = "",
        saveToNotebook: Boolean = true
    ) {
        val now = System.currentTimeMillis()
        val contract = dao.getCommitmentContractById(contractId).firstOrNull() ?: return

        dao.markCommitmentCompleted(
            id = contractId,
            completedAt = now,
            notes = completionNotes.trim()
        )

        // Award Completion XP (+150 XP)
        addXpAndRecalculate(contract.xpAwardedForCompletion)

        if (saveToNotebook) {
            val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val completedDateStr = sdf.format(Date(now))
            val deadlineStr = sdf.format(Date(contract.deadlineEpochMillis))
            val content = buildString {
                appendLine("👑 COMMITMENT FULFILLED & TRANSCENDED")
                appendLine("Completed on: $completedDateStr (Original Target: $deadlineStr)")
                appendLine("Covenant: ${contract.goalStatement}")
                appendLine()
                if (completionNotes.isNotBlank()) {
                    appendLine("Victory Reflection & Learnings:")
                    appendLine(completionNotes.trim())
                    appendLine()
                }
                appendLine("Transmutation Anchor: Definiteness of purpose coupled with unwavering persistence is the immutable master key to all riches.")
            }
            dao.insertNotebookEntry(
                NotebookEntryEntity(
                    moduleId = null,
                    moduleTitle = "Commitment Contract (Completed)",
                    title = "Fulfilled: ${contract.goalStatement.take(45)}${if (contract.goalStatement.length > 45) "..." else ""}",
                    content = content,
                    timestamp = now,
                    tags = "Commitment Completed, Victory, Covenant, Riches",
                    entryType = NotebookEntryEntity.ENTRY_TYPE_COMMITMENT_CONTRACT
                )
            )
        }

        evaluateBadgesAndProgress()
    }

    suspend fun renewCommitmentContract(
        contractId: Long,
        newDeadlineMillis: Long,
        renewalNotes: String = "",
        saveToNotebook: Boolean = true
    ): Long {
        val now = System.currentTimeMillis()
        val oldContract = dao.getCommitmentContractById(contractId).firstOrNull() ?: return 0L

        // Mark old as renewed
        dao.markCommitmentRenewed(
            id = contractId,
            renewedAt = now,
            notes = renewalNotes.trim()
        )

        // Create renewed contract with new deadline
        val newContract = CommitmentContractEntity(
            title = oldContract.title,
            goalStatement = oldContract.goalStatement,
            whyItMatters = oldContract.whyItMatters,
            deadlineEpochMillis = newDeadlineMillis,
            createdAtEpochMillis = now,
            progressPercent = oldContract.progressPercent.coerceAtLeast(0),
            status = CommitmentContractEntity.STATUS_ACTIVE,
            originalContractId = oldContract.originalContractId ?: oldContract.id,
            signatureName = oldContract.signatureName,
            signedDateEpochMillis = now,
            xpAwardedForCreation = 50,
            xpAwardedForCompletion = 150,
            renewalNotes = renewalNotes.trim()
        )
        val newId = dao.insertCommitmentContract(newContract)

        // Award Renewal Recommitment XP (+50 XP)
        addXpAndRecalculate(50)

        if (saveToNotebook) {
            val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            val newDeadlineStr = sdf.format(Date(newDeadlineMillis))
            val content = buildString {
                appendLine("🔄 COVENANT EXTENSION & RECOMMISSION")
                appendLine("New Target Deadline: $newDeadlineStr")
                appendLine("Original Commitment: ${oldContract.goalStatement}")
                appendLine()
                if (renewalNotes.isNotBlank()) {
                    appendLine("Strategic Calibration & Why Extended:")
                    appendLine(renewalNotes.trim())
                    appendLine()
                }
                appendLine("Persistence Decree: Temporary defeat is not permanent failure. We calibrate the timeline and double our resolve.")
            }
            dao.insertNotebookEntry(
                NotebookEntryEntity(
                    moduleId = null,
                    moduleTitle = "Commitment Contract (Renewed)",
                    title = "Recommitted: ${oldContract.goalStatement.take(40)}${if (oldContract.goalStatement.length > 40) "..." else ""}",
                    content = content,
                    timestamp = now,
                    tags = "Commitment Renewed, Recommitment, Persistence",
                    entryType = NotebookEntryEntity.ENTRY_TYPE_COMMITMENT_CONTRACT
                )
            )
        }

        evaluateBadgesAndProgress()
        return newId
    }

    suspend fun deleteCommitmentContract(contractId: Long) {
        dao.deleteCommitmentContract(contractId)
        evaluateBadgesAndProgress()
    }
}
