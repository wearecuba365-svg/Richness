package com.example

import com.example.data.model.NotebookEntryEntity
import com.example.util.AutoPatternDetector
import com.example.util.PatternCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoPatternDetectorTest {

    @Test
    fun testEmptyEntriesReturnsEmptyPatterns() {
        val patterns = AutoPatternDetector.detectPatterns(emptyList())
        assertTrue(patterns.isEmpty())
    }

    @Test
    fun testGuiltPatternDetection() {
        val now = System.currentTimeMillis()
        val entries = listOf(
            NotebookEntryEntity(
                id = 1L,
                title = "Reflection on career",
                content = "Felt deep guilt when taking time off for myself.",
                timestamp = now
            ),
            NotebookEntryEntity(
                id = 2L,
                title = "Self worth check",
                content = "Guilt was surfacing around charging what I am worth.",
                timestamp = now - 100000L
            )
        )

        val patterns = AutoPatternDetector.detectPatterns(entries, rollingDays = 30)
        val guiltPattern = patterns.find { it.id == "theme_guilt" }

        assertNotNull(guiltPattern)
        assertEquals(2, guiltPattern!!.occurrencesCount)
        assertEquals(PatternCategory.EMOTIONAL_TONE, guiltPattern.category)
        assertTrue(guiltPattern.plainLanguageObservation.contains("guilt", ignoreCase = true))
        assertTrue(guiltPattern.matchingEntryIds.contains(1L))
        assertTrue(guiltPattern.matchingEntryIds.contains(2L))
    }

    @Test
    fun testStartingOverThemeDetection() {
        val now = System.currentTimeMillis()
        val entries = listOf(
            NotebookEntryEntity(
                id = 10L,
                title = "New Chapter",
                content = "I am starting over with fresh vision and unwavering focus.",
                timestamp = now
            ),
            NotebookEntryEntity(
                id = 11L,
                title = "Morning Intentions",
                content = "It feels like starting over, but with much more wisdom.",
                timestamp = now - 50000L
            )
        )

        val patterns = AutoPatternDetector.detectPatterns(entries, rollingDays = 30)
        val startingOverPattern = patterns.find { it.id == "theme_starting_over" }

        assertNotNull(startingOverPattern)
        assertEquals(2, startingOverPattern!!.occurrencesCount)
        assertEquals(PatternCategory.RECURRING_THEME, startingOverPattern.category)
        assertTrue(startingOverPattern.plainLanguageObservation.contains("starting over", ignoreCase = true))
    }

    @Test
    fun testMoneyMindsetPatternDetection() {
        val now = System.currentTimeMillis()
        val entries = listOf(
            NotebookEntryEntity(
                id = 20L,
                title = "Financial choice",
                content = "Examining scarcity vs abundance in my pricing.",
                tags = "Money Mindset, Scarcity",
                entryType = NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET,
                timestamp = now
            ),
            NotebookEntryEntity(
                id = 21L,
                title = "Abundance Affirmation",
                content = "I shift from fear of scarcity into sovereign abundance.",
                tags = "Money Mindset, Abundance",
                entryType = NotebookEntryEntity.ENTRY_TYPE_MONEY_MINDSET,
                timestamp = now - 20000L
            )
        )

        val patterns = AutoPatternDetector.detectPatterns(entries, rollingDays = 30)
        val moneyPattern = patterns.find { it.id == "theme_scarcity_abundance" }

        assertNotNull(moneyPattern)
        assertEquals(2, moneyPattern!!.occurrencesCount)
        assertEquals(PatternCategory.MONEY_MINDSET, moneyPattern.category)
    }

    @Test
    fun testDynamicKeywordExtraction() {
        val now = System.currentTimeMillis()
        val entries = listOf(
            NotebookEntryEntity(
                id = 30L,
                title = "Podcast launch ideas",
                content = "Outlining episode structure for the mastermind podcast series.",
                timestamp = now
            ),
            NotebookEntryEntity(
                id = 31L,
                title = "Audio gear checklist",
                content = "Ready to record the first podcast guest interview next Tuesday.",
                timestamp = now - 30000L
            )
        )

        val patterns = AutoPatternDetector.detectPatterns(entries, rollingDays = 30)
        val podcastPattern = patterns.find { it.themeOrKeyword.equals("podcast", ignoreCase = true) }

        assertNotNull(podcastPattern)
        assertEquals(2, podcastPattern!!.occurrencesCount)
        assertTrue(podcastPattern.plainLanguageObservation.contains("podcast", ignoreCase = true))
    }
}
