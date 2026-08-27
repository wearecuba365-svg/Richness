package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BadgeEntity
import com.example.data.model.CommitmentContractEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.DailyMoodEntryEntity
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

@Dao
interface RichesDao {

    // --- Modules ---
    @Query("SELECT * FROM modules ORDER BY `order` ASC")
    fun getAllModules(): Flow<List<ModuleEntity>>

    @Query("SELECT * FROM modules WHERE id = :id LIMIT 1")
    fun getModuleById(id: Int): Flow<ModuleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<ModuleEntity>)

    @Update
    suspend fun updateModule(module: ModuleEntity)

    @Query("UPDATE modules SET isUnlocked = 1 WHERE isUnlocked = 0")
    suspend fun unlockAllModules()

    @Query("UPDATE modules SET isUnlocked = 1 WHERE id = :id")
    suspend fun setModuleUnlocked(id: Int)

    @Query("UPDATE modules SET isCompleted = :completed WHERE id = :id")
    suspend fun setModuleCompleted(id: Int, completed: Boolean)

    @Query("UPDATE modules SET isQuestCompleted = :completed WHERE id = :id")
    suspend fun setQuestCompleted(id: Int, completed: Boolean)

    @Query("UPDATE modules SET savedField1 = :f1, savedField2 = :f2, savedField3 = :f3 WHERE id = :id")
    suspend fun saveModuleWorksheet(id: Int, f1: String, f2: String, f3: String)

    // --- User Profile ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateUserProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET xpTotal = xpTotal + :xp WHERE id = 1")
    suspend fun addXp(xp: Int)

    // --- Notebook ---
    @Query("SELECT * FROM notebook_entries ORDER BY timestamp DESC")
    fun getAllNotebookEntries(): Flow<List<NotebookEntryEntity>>

    @Query("SELECT * FROM notebook_entries WHERE moduleId = :moduleId ORDER BY timestamp DESC")
    fun getNotebookEntriesForModule(moduleId: Int): Flow<List<NotebookEntryEntity>>

    @Query("SELECT * FROM notebook_entries WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchNotebookEntries(query: String): Flow<List<NotebookEntryEntity>>

    @Query("SELECT * FROM notebook_entries WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteNotebookEntries(): Flow<List<NotebookEntryEntity>>

    @Query("SELECT COUNT(*) FROM notebook_entries")
    fun getNotebookEntriesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotebookEntry(entry: NotebookEntryEntity): Long

    @Update
    suspend fun updateNotebookEntry(entry: NotebookEntryEntity)

    @Query("UPDATE notebook_entries SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setNotebookFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM notebook_entries WHERE id = :id")
    suspend fun deleteNotebookEntry(id: Long)

    @Query("DELETE FROM notebook_entries")
    suspend fun deleteAllNotebookEntries()

    // --- Statistics & Offline Progress ---
    @Query("SELECT COUNT(*) FROM modules WHERE isCompleted = 1")
    fun getCompletedModulesCount(): Flow<Int>

    @Query("UPDATE modules SET isCompleted = 0, isQuestCompleted = 0, savedField1 = '', savedField2 = '', savedField3 = ''")
    suspend fun resetAllModulesProgress()

    // --- Badges ---
    @Query("SELECT * FROM badges ORDER BY isUnlocked DESC, id ASC")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    fun getUnlockedBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges WHERE id = :id LIMIT 1")
    fun getBadgeById(id: String): Flow<BadgeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadgeIfMissing(badge: BadgeEntity)

    @Update
    suspend fun updateBadge(badge: BadgeEntity)

    @Query("UPDATE badges SET isUnlocked = 1, unlockedAt = :timestamp, progress = maxProgress WHERE id = :id")
    suspend fun unlockBadge(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE badges SET progress = :progress WHERE id = :id")
    suspend fun updateBadgeProgress(id: String, progress: Int)

    // --- Daily Habits (Think & Grow Rich Rituals) ---
    @Query("SELECT * FROM daily_habits ORDER BY orderIndex ASC, createdAt ASC")
    fun getAllHabits(): Flow<List<DailyHabitEntity>>

    @Query("SELECT * FROM daily_habits WHERE id = :id LIMIT 1")
    fun getHabitById(id: String): Flow<DailyHabitEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<DailyHabitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: DailyHabitEntity)

    @Update
    suspend fun updateHabit(habit: DailyHabitEntity)

    @Query("DELETE FROM daily_habits WHERE id = :id")
    suspend fun deleteHabit(id: String)

    // --- Daily Habit Logs ---
    @Query("SELECT * FROM daily_habit_logs WHERE dateEpochDay = :dateEpochDay ORDER BY completedAt DESC")
    fun getHabitLogsForDay(dateEpochDay: Long): Flow<List<DailyHabitLogEntity>>

    @Query("SELECT * FROM daily_habit_logs ORDER BY completedAt DESC")
    fun getAllHabitLogs(): Flow<List<DailyHabitLogEntity>>

    @Query("SELECT * FROM daily_habit_logs WHERE dateEpochDay BETWEEN :startEpochDay AND :endEpochDay ORDER BY completedAt DESC")
    fun getHabitLogsForRange(startEpochDay: Long, endEpochDay: Long): Flow<List<DailyHabitLogEntity>>

    @Query("SELECT * FROM daily_habit_logs WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun getLogsForHabit(habitId: String): Flow<List<DailyHabitLogEntity>>

    @Query("SELECT * FROM daily_habit_logs WHERE habitId = :habitId AND dateEpochDay = :dateEpochDay LIMIT 1")
    suspend fun getLogForHabitOnDay(habitId: String, dateEpochDay: Long): DailyHabitLogEntity?

    @Query("SELECT COUNT(*) FROM daily_habit_logs")
    fun getHabitLogsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM daily_habit_logs WHERE dateEpochDay = :dateEpochDay")
    fun getTodayCompletedHabitsCount(dateEpochDay: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitLog(log: DailyHabitLogEntity): Long

    @Query("DELETE FROM daily_habit_logs WHERE habitId = :habitId AND dateEpochDay = :dateEpochDay")
    suspend fun deleteHabitLog(habitId: String, dateEpochDay: Long)

    @Query("DELETE FROM daily_habit_logs WHERE id = :id")
    suspend fun deleteHabitLogById(id: Long)

    // --- Mindset & Mood Entries (30-Day Emotional Growth Journey) ---
    @Query("SELECT * FROM daily_mood_entries ORDER BY dateEpochDay ASC")
    fun getAllMoodEntries(): Flow<List<DailyMoodEntryEntity>>

    @Query("SELECT * FROM daily_mood_entries WHERE dateEpochDay = :dateEpochDay LIMIT 1")
    fun getMoodEntryForDay(dateEpochDay: Long): Flow<DailyMoodEntryEntity?>

    @Query("SELECT * FROM daily_mood_entries WHERE dateEpochDay BETWEEN :startEpochDay AND :endEpochDay ORDER BY dateEpochDay ASC")
    fun getMoodEntriesForRange(startEpochDay: Long, endEpochDay: Long): Flow<List<DailyMoodEntryEntity>>

    @Query("SELECT * FROM daily_mood_entries ORDER BY dateEpochDay DESC LIMIT 30")
    fun getLast30DaysMoodEntries(): Flow<List<DailyMoodEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodEntry(entry: DailyMoodEntryEntity): Long

    @Query("DELETE FROM daily_mood_entries WHERE dateEpochDay = :dateEpochDay")
    suspend fun deleteMoodEntryForDay(dateEpochDay: Long)

    // --- Mastermind Circles (Accountability Groups) ---
    @Query("SELECT * FROM mastermind_groups ORDER BY groupStreakWeeks DESC, combinedXpThisWeek DESC")
    fun getAllMastermindGroups(): Flow<List<MastermindGroupEntity>>

    @Query("SELECT * FROM mastermind_groups WHERE isUserMember = 1 LIMIT 1")
    fun getUserMastermindGroup(): Flow<MastermindGroupEntity?>

    @Query("SELECT * FROM mastermind_groups WHERE id = :groupId LIMIT 1")
    fun getMastermindGroupById(groupId: String): Flow<MastermindGroupEntity?>

    @Query("SELECT * FROM mastermind_groups WHERE inviteCode = :inviteCode LIMIT 1")
    suspend fun getMastermindGroupByInviteCode(inviteCode: String): MastermindGroupEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastermindGroup(group: MastermindGroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastermindGroups(groups: List<MastermindGroupEntity>)

    @Update
    suspend fun updateMastermindGroup(group: MastermindGroupEntity)

    @Query("UPDATE mastermind_groups SET isUserMember = 0")
    suspend fun clearUserGroupMembership()

    @Query("UPDATE mastermind_groups SET isUserMember = 1 WHERE id = :groupId")
    suspend fun setUserGroupMembership(groupId: String)

    @Query("DELETE FROM mastermind_groups WHERE id = :groupId")
    suspend fun deleteMastermindGroup(groupId: String)

    // --- Mastermind Members ---
    @Query("SELECT * FROM mastermind_members WHERE groupId = :groupId ORDER BY isCurrentUser DESC, weeklyXp DESC")
    fun getMastermindMembers(groupId: String): Flow<List<MastermindMemberEntity>>

    @Query("SELECT * FROM mastermind_members")
    fun getAllMastermindMembers(): Flow<List<MastermindMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastermindMember(member: MastermindMemberEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastermindMembers(members: List<MastermindMemberEntity>)

    @Update
    suspend fun updateMastermindMember(member: MastermindMemberEntity)

    @Query("DELETE FROM mastermind_members WHERE groupId = :groupId AND isCurrentUser = 1")
    suspend fun removeCurrentUserFromGroup(groupId: String)

    @Query("DELETE FROM mastermind_members WHERE id = :memberId")
    suspend fun deleteMastermindMember(memberId: String)

    // --- Mastermind Weekly Check-ins ---
    @Query("SELECT * FROM mastermind_checkins WHERE groupId = :groupId AND weekNumber = :weekNumber AND year = :year ORDER BY isCurrentUser DESC, timestamp DESC")
    fun getCheckinsForWeek(groupId: String, weekNumber: Int, year: Int): Flow<List<MastermindCheckinEntity>>

    @Query("SELECT * FROM mastermind_checkins WHERE groupId = :groupId ORDER BY year DESC, weekNumber DESC, timestamp DESC")
    fun getAllCheckinsForGroup(groupId: String): Flow<List<MastermindCheckinEntity>>

    @Query("SELECT * FROM mastermind_checkins ORDER BY timestamp DESC")
    fun getAllCheckins(): Flow<List<MastermindCheckinEntity>>

    @Query("SELECT * FROM mastermind_checkins WHERE groupId = :groupId AND memberId = :memberId AND weekNumber = :weekNumber AND year = :year LIMIT 1")
    suspend fun getUserCheckinForWeek(groupId: String, memberId: String, weekNumber: Int, year: Int): MastermindCheckinEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastermindCheckin(checkin: MastermindCheckinEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMastermindCheckins(checkins: List<MastermindCheckinEntity>)

    @Update
    suspend fun updateMastermindCheckin(checkin: MastermindCheckinEntity)

    @Query("DELETE FROM mastermind_checkins WHERE id = :checkinId")
    suspend fun deleteMastermindCheckin(checkinId: String)

    // --- Vision Board ---
    @Query("SELECT * FROM vision_board_items ORDER BY isPinned DESC, orderIndex ASC, createdAt DESC")
    fun getAllVisionBoardItems(): Flow<List<VisionBoardItemEntity>>

    @Query("SELECT * FROM vision_board_items WHERE id = :id LIMIT 1")
    fun getVisionBoardItemById(id: Long): Flow<VisionBoardItemEntity?>

    @Query("SELECT * FROM vision_board_items WHERE category = :category ORDER BY isPinned DESC, orderIndex ASC, createdAt DESC")
    fun getVisionBoardItemsByCategory(category: String): Flow<List<VisionBoardItemEntity>>

    @Query("SELECT COUNT(*) FROM vision_board_items")
    fun getVisionBoardCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisionBoardItem(item: VisionBoardItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisionBoardItems(items: List<VisionBoardItemEntity>)

    @Update
    suspend fun updateVisionBoardItem(item: VisionBoardItemEntity)

    @Query("DELETE FROM vision_board_items WHERE id = :id")
    suspend fun deleteVisionBoardItem(id: Long)

    @Query("UPDATE vision_board_items SET orderIndex = :newOrder WHERE id = :id")
    suspend fun updateVisionBoardItemOrder(id: Long, newOrder: Int)

    @Query("UPDATE vision_board_items SET isPinned = :isPinned WHERE id = :id")
    suspend fun toggleVisionBoardItemPin(id: Long, isPinned: Boolean)

    // --- Wealth Goal Tracker ---
    @Query("SELECT * FROM wealth_goals WHERE id = :id LIMIT 1")
    fun getWealthGoalById(id: Int = 1): Flow<WealthGoalEntity?>

    @Query("SELECT * FROM wealth_goals ORDER BY id ASC")
    fun getAllWealthGoals(): Flow<List<WealthGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWealthGoal(goal: WealthGoalEntity)

    @Update
    suspend fun updateWealthGoal(goal: WealthGoalEntity)

    @Query("UPDATE wealth_goals SET currentAmount = :newAmount, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateWealthGoalProgress(id: Int, newAmount: Double, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM wealth_goal_logs WHERE goalId = :goalId ORDER BY timestamp DESC")
    fun getWealthGoalLogs(goalId: Int = 1): Flow<List<WealthGoalLogEntity>>

    @Query("SELECT * FROM wealth_goal_logs ORDER BY timestamp DESC")
    fun getAllWealthGoalLogs(): Flow<List<WealthGoalLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWealthGoalLog(log: WealthGoalLogEntity): Long

    @Query("DELETE FROM wealth_goal_logs WHERE id = :id")
    suspend fun deleteWealthGoalLog(id: Long)

    @Query("DELETE FROM wealth_goal_logs WHERE goalId = :goalId")
    suspend fun clearWealthGoalLogs(goalId: Int)

    // --- Money Blueprint Diagnosis & Results ---
    @Query("SELECT * FROM money_blueprint_results ORDER BY timestamp DESC")
    fun getAllMoneyBlueprintResults(): Flow<List<MoneyBlueprintResultEntity>>

    @Query("SELECT * FROM money_blueprint_results ORDER BY timestamp DESC LIMIT 1")
    fun getLatestMoneyBlueprintResult(): Flow<MoneyBlueprintResultEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneyBlueprintResult(result: MoneyBlueprintResultEntity): Long

    @Query("DELETE FROM money_blueprint_results WHERE id = :id")
    suspend fun deleteMoneyBlueprintResult(id: Long)

    // --- Saved Income Ideas ---
    @Query("SELECT * FROM saved_income_ideas ORDER BY savedAtEpoch DESC")
    fun getAllSavedIncomeIdeas(): Flow<List<SavedIncomeIdeaEntity>>

    @Query("SELECT ideaId FROM saved_income_ideas")
    fun getSavedIncomeIdeaIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_income_ideas WHERE ideaId = :ideaId)")
    fun isIncomeIdeaSaved(ideaId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedIncomeIdea(item: SavedIncomeIdeaEntity)

    @Query("DELETE FROM saved_income_ideas WHERE ideaId = :ideaId")
    suspend fun deleteSavedIncomeIdea(ideaId: String)

    // --- Gratitude & Giving Tracker ---
    @Query("SELECT * FROM giving_goals WHERE id = :id LIMIT 1")
    fun getGivingGoal(id: Int = 1): Flow<com.example.data.model.GivingGoalEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGivingGoal(goal: com.example.data.model.GivingGoalEntity)

    @Update
    suspend fun updateGivingGoal(goal: com.example.data.model.GivingGoalEntity)

    @Query("SELECT * FROM giving_logs ORDER BY timestamp DESC")
    fun getAllGivingLogs(): Flow<List<com.example.data.model.GivingLogEntity>>

    @Query("SELECT COUNT(*) FROM giving_logs")
    fun getGivingLogsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGivingLog(log: com.example.data.model.GivingLogEntity): Long

    @Update
    suspend fun updateGivingLog(log: com.example.data.model.GivingLogEntity)

    @Query("DELETE FROM giving_logs WHERE id = :id")
    suspend fun deleteGivingLog(id: Long)

    @Query("DELETE FROM giving_logs")
    suspend fun clearAllGivingLogs()

    // --- Short Lessons ---
    @Query("SELECT * FROM short_lessons ORDER BY moduleId ASC, `order` ASC")
    fun getAllShortLessons(): Flow<List<ShortLessonEntity>>

    @Query("SELECT * FROM short_lessons WHERE moduleId = :moduleId ORDER BY `order` ASC")
    fun getShortLessonsForModule(moduleId: Int): Flow<List<ShortLessonEntity>>

    @Query("SELECT * FROM short_lessons WHERE id = :id LIMIT 1")
    fun getShortLessonById(id: String): Flow<ShortLessonEntity?>

    @Query("SELECT COUNT(*) FROM short_lessons WHERE isCompleted = 1")
    fun getCompletedShortLessonsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM short_lessons WHERE moduleId = :moduleId AND isCompleted = 1")
    fun getCompletedShortLessonsCountForModule(moduleId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortLessons(lessons: List<ShortLessonEntity>)

    @Update
    suspend fun updateShortLesson(lesson: ShortLessonEntity)

    @Query("UPDATE short_lessons SET isCompleted = 1, completedAt = :completedAt WHERE id = :lessonId")
    suspend fun markLessonCompleted(lessonId: String, completedAt: Long = System.currentTimeMillis())

    @Query("UPDATE short_lessons SET isCompleted = 0, completedAt = NULL WHERE id = :lessonId")
    suspend fun resetLessonCompletion(lessonId: String)

    @Query("UPDATE short_lessons SET lastPlaybackPositionSeconds = :positionSeconds WHERE id = :lessonId")
    suspend fun updateLessonProgress(lessonId: String, positionSeconds: Int)

    // --- Onboarding Step Logs & Tracking ---
    @Query("SELECT * FROM onboarding_step_logs ORDER BY timestamp ASC")
    fun getAllOnboardingStepLogs(): Flow<List<com.example.data.model.OnboardingStepLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOnboardingStepLog(log: com.example.data.model.OnboardingStepLogEntity): Long

    @Query("DELETE FROM onboarding_step_logs")
    suspend fun clearOnboardingStepLogs()

    // --- Commitment Contracts ---
    @Query("SELECT * FROM commitment_contracts ORDER BY createdAtEpochMillis DESC")
    fun getAllCommitmentContracts(): Flow<List<CommitmentContractEntity>>

    @Query("SELECT * FROM commitment_contracts WHERE status = 'active' ORDER BY deadlineEpochMillis ASC")
    fun getActiveCommitmentContracts(): Flow<List<CommitmentContractEntity>>

    @Query("SELECT * FROM commitment_contracts WHERE status = 'active' ORDER BY deadlineEpochMillis ASC LIMIT 1")
    fun getActiveCommitmentContract(): Flow<CommitmentContractEntity?>

    @Query("SELECT * FROM commitment_contracts WHERE id = :id LIMIT 1")
    fun getCommitmentContractById(id: Long): Flow<CommitmentContractEntity?>

    @Query("SELECT COUNT(*) FROM commitment_contracts WHERE status = 'completed'")
    fun getCompletedCommitmentsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitmentContract(contract: CommitmentContractEntity): Long

    @Update
    suspend fun updateCommitmentContract(contract: CommitmentContractEntity)

    @Query("UPDATE commitment_contracts SET progressPercent = :progress WHERE id = :id")
    suspend fun updateCommitmentProgress(id: Long, progress: Int)

    @Query("UPDATE commitment_contracts SET status = 'completed', completedAtEpochMillis = :completedAt, completionNotes = :notes, progressPercent = 100 WHERE id = :id")
    suspend fun markCommitmentCompleted(id: Long, completedAt: Long = System.currentTimeMillis(), notes: String = "")

    @Query("UPDATE commitment_contracts SET status = 'renewed', renewedAtEpochMillis = :renewedAt, renewalNotes = :notes WHERE id = :id")
    suspend fun markCommitmentRenewed(id: Long, renewedAt: Long = System.currentTimeMillis(), notes: String = "")

    @Query("DELETE FROM commitment_contracts WHERE id = :id")
    suspend fun deleteCommitmentContract(id: Long)
}
