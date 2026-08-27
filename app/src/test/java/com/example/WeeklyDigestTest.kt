package com.example

import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity
import com.example.data.model.WeeklyDigestAggregator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class WeeklyDigestTest {

    @Test
    fun testCompileDigestForEmptyData() {
        val digest = WeeklyDigestAggregator.compileDigest(
            weeksAgo = 0,
            userProfile = null,
            notebookEntries = emptyList(),
            allHabitLogs = emptyList(),
            habits = emptyList(),
            wealthGoal = null,
            wealthGoalLogs = emptyList(),
            givingGoal = null,
            givingLogs = emptyList(),
            badges = emptyList(),
            modules = emptyList(),
            mastermindCheckins = emptyList()
        )

        assertNotNull(digest)
        assertEquals(0, digest.journalEntriesCount)
        assertEquals(0, digest.habitsCompletedCount)
        assertEquals(0, digest.wealthContributionsCount)
        assertTrue(digest.headlineSummary.isNotBlank())
        assertTrue(digest.subHeadline.isNotBlank())
        assertTrue(digest.formattedDateRange.isNotBlank())
    }

    @Test
    fun testCompileDigestWithActiveEntries() {
        val now = System.currentTimeMillis()
        val currentDay = now / (24 * 60 * 60 * 1000L)

        val profile = UserProfileEntity(
            id = 1,
            name = "Napoleon",
            currentStreak = 7,
            bestStreak = 14,
            xpTotal = 1200,
            tierName = "Master"
        )

        val entries = listOf(
            NotebookEntryEntity(
                id = 1L,
                title = "Definite Chief Aim",
                content = "Committed to daily action and focus.",
                timestamp = now,
                entryType = NotebookEntryEntity.ENTRY_TYPE_REFLECTION
            ),
            NotebookEntryEntity(
                id = 2L,
                title = "Decision on Investment",
                content = "Decided on allocating resources to property.",
                timestamp = now - 3600000L,
                entryType = NotebookEntryEntity.ENTRY_TYPE_DECISION_LOG
            )
        )

        val habits = listOf(
            DailyHabitEntity(id = "1", title = "Morning Autosuggestion", principle = "Autosuggestion", description = "Daily practice", category = "Mindset", iconKey = "affirmation", orderIndex = 0),
            DailyHabitEntity(id = "2", title = "Evening Review", principle = "Self-Analysis", description = "Review day", category = "Action", iconKey = "reading", orderIndex = 1)
        )

        val habitLogs = listOf(
            DailyHabitLogEntity(habitId = "1", dateEpochDay = currentDay, dateFormatted = "2026-08-26", completedAt = now, xpEarned = 30),
            DailyHabitLogEntity(habitId = "2", dateEpochDay = currentDay, dateFormatted = "2026-08-26", completedAt = now, xpEarned = 30)
        )

        val digest = WeeklyDigestAggregator.compileDigest(
            weeksAgo = 0,
            userProfile = profile,
            notebookEntries = entries,
            allHabitLogs = habitLogs,
            habits = habits,
            wealthGoal = WealthGoalEntity(targetAmount = 50000.0, currentAmount = 5000.0),
            wealthGoalLogs = listOf(
                WealthGoalLogEntity(id = 1L, amount = 500.0, timestamp = now, note = "Weekly saving")
            ),
            givingGoal = null,
            givingLogs = emptyList(),
            badges = emptyList(),
            modules = emptyList(),
            mastermindCheckins = emptyList()
        )

        assertEquals(2, digest.journalEntriesCount)
        assertEquals(1, digest.decisionCount)
        assertEquals(2, digest.habitsCompletedCount)
        assertEquals(1, digest.wealthContributionsCount)
        assertEquals(500.0, digest.wealthContributedAmount, 0.01)
        assertEquals(7, digest.currentStreak)
        assertTrue(digest.xpEarnedThisWeek > 0)
    }

    @Test
    fun testPastDigestsGeneration() {
        val pastDigests = WeeklyDigestAggregator.compilePastDigests(
            count = 6,
            userProfile = null,
            notebookEntries = emptyList(),
            allHabitLogs = emptyList(),
            habits = emptyList(),
            wealthGoal = null,
            wealthGoalLogs = emptyList(),
            givingGoal = null,
            givingLogs = emptyList(),
            badges = emptyList(),
            modules = emptyList(),
            mastermindCheckins = emptyList()
        )

        assertEquals(6, pastDigests.size)
        assertEquals(0, pastDigests[0].weeksAgo)
        assertEquals(1, pastDigests[1].weeksAgo)
        assertEquals(5, pastDigests[5].weeksAgo)
    }
}
