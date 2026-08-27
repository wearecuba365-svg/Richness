package com.example

import com.example.data.model.ModuleEntity
import com.example.data.model.SECTION_ACHIEVEMENTS
import com.example.data.model.getSectionForBadgeId
import com.example.data.model.getSectionForModule
import com.example.data.model.getSectionProgress
import com.example.data.model.isSectionCompleted
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SectionAchievementTest {

    private fun createTestModule(id: Int, isCompleted: Boolean): ModuleEntity {
        return ModuleEntity(
            id = id,
            order = id,
            title = "Vault $id",
            originalPrinciple = "Principle $id",
            subtitle = "Subtitle $id",
            isUnlocked = true,
            isCompleted = isCompleted,
            xpReward = 150,
            videoTitle = "Video $id",
            videoDuration = "10:00",
            excerptTitle = "Excerpt $id",
            excerptText = "Text $id",
            keyTakeaways = "Takeaway",
            templateTitle = "Template $id",
            templatePrompt = "Prompt",
            templateFieldLabel1 = "F1",
            templateFieldLabel2 = "F2",
            templateFieldLabel3 = "F3",
            questTitle = "Quest $id",
            questDescription = "Quest Desc",
            questActionPrompt = "Quest Prompt",
            notebookPrompt = "Reflection"
        )
    }

    @Test
    fun testSectionMapping() {
        // Vault 0 to 3 -> Section 1 (The Mental Foundation)
        assertEquals(1, getSectionForModule(0)?.sectionId)
        assertEquals(1, getSectionForModule(1)?.sectionId)
        assertEquals(1, getSectionForModule(2)?.sectionId)
        assertEquals(1, getSectionForModule(3)?.sectionId)

        // Vault 4 to 8 -> Section 2 (Strategic Architecture)
        assertEquals(2, getSectionForModule(4)?.sectionId)
        assertEquals(2, getSectionForModule(8)?.sectionId)

        // Vault 9 to 11 -> Section 3 (Higher Synergies)
        assertEquals(3, getSectionForModule(9)?.sectionId)
        assertEquals(3, getSectionForModule(11)?.sectionId)

        // Vault 12 to 13 -> Section 4 (The Sovereign Apex)
        assertEquals(4, getSectionForModule(12)?.sectionId)
        assertEquals(4, getSectionForModule(13)?.sectionId)

        // Non-existent module
        assertNull(getSectionForModule(99))
    }

    @Test
    fun testBadgeMapping() {
        assertEquals(1, getSectionForBadgeId("achievement_section_1")?.sectionId)
        assertEquals(2, getSectionForBadgeId("achievement_section_2")?.sectionId)
        assertEquals(3, getSectionForBadgeId("achievement_section_3")?.sectionId)
        assertEquals(4, getSectionForBadgeId("achievement_section_4")?.sectionId)
        assertNull(getSectionForBadgeId("unknown_badge"))
    }

    @Test
    fun testSectionCompletionCalculation() {
        val section1 = SECTION_ACHIEVEMENTS.first { it.sectionId == 1 }

        // All incomplete
        val modulesIncomplete = (0..13).map { createTestModule(it, isCompleted = false) }
        val (completed0, total0) = getSectionProgress(section1, modulesIncomplete)
        assertEquals(0, completed0)
        assertEquals(4, total0)
        assertFalse(isSectionCompleted(section1, modulesIncomplete))

        // Partially complete (2 out of 4)
        val modulesPartial = (0..13).map {
            createTestModule(it, isCompleted = (it == 0 || it == 1))
        }
        val (completed1, total1) = getSectionProgress(section1, modulesPartial)
        assertEquals(2, completed1)
        assertEquals(4, total1)
        assertFalse(isSectionCompleted(section1, modulesPartial))

        // Section 1 fully complete (0, 1, 2, 3)
        val modulesFullSec1 = (0..13).map {
            createTestModule(it, isCompleted = (it in 0..3))
        }
        val (completed2, total2) = getSectionProgress(section1, modulesFullSec1)
        assertEquals(4, completed2)
        assertEquals(4, total2)
        assertTrue(isSectionCompleted(section1, modulesFullSec1))
    }

    @Test
    fun testAllSectionsDefinitions() {
        assertEquals(4, SECTION_ACHIEVEMENTS.size)
        SECTION_ACHIEVEMENTS.forEach { section ->
            assertNotNull(section.title)
            assertNotNull(section.romanNumeral)
            assertNotNull(section.quote)
            assertTrue(section.moduleIds.isNotEmpty())
            assertTrue(section.xpReward > 0)
        }
    }
}

