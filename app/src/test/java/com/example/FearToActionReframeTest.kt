package com.example

import com.example.data.model.NotebookEntryEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FearToActionReframeTest {

    @Test
    fun testFearReframeEntryModel() {
        val entry = NotebookEntryEntity(
            moduleId = null,
            moduleTitle = "Fear-to-Action Transmutation",
            title = "Transmuted: Fear of Failure",
            content = "Fear: Fear of launching product\n\nWorst-Case Scenario:\nNobody buys and I lose time\n\nMicro-Action Today:\nSend 3 beta testing invitations to trusted mentors",
            promptQuestion = "What are you avoiding because you're afraid of it?",
            tags = "Fear Reframe,Mindset,Action",
            entryType = NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME,
            fearCategory = "Failure",
            fearText = "Fear of launching product",
            worstCaseText = "Nobody buys and I lose time",
            actionTodayText = "Send 3 beta testing invitations to trusted mentors",
            isActionCompleted = false
        )

        assertEquals(NotebookEntryEntity.ENTRY_TYPE_FEAR_REFRAME, entry.entryType)
        assertEquals("Failure", entry.fearCategory)
        assertEquals("Fear of launching product", entry.fearText)
        assertEquals("Nobody buys and I lose time", entry.worstCaseText)
        assertEquals("Send 3 beta testing invitations to trusted mentors", entry.actionTodayText)
        assertTrue(entry.tags.contains("Fear Reframe"))
    }
}
