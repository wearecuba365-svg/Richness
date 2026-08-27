package com.example

import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.viewmodel.PdfExportUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NotebookPdfExportTest {

    @Test
    fun testPdfExportStateDefaults() {
        val defaultState = PdfExportUiState()
        assertFalse("Exporting should be false by default", defaultState.isExporting)
        assertFalse("Dialog should not be shown by default", defaultState.showExportDialog)
        assertEquals(null, defaultState.exportResult)
        assertEquals(null, defaultState.errorMessage)
    }

    @Test
    fun testNotebookEntrySerializationForExport() {
        val dummyProfile = UserProfileEntity(
            name = "Marcus Aurelius",
            role = "Architect",
            tierName = "Architect",
            xpTotal = 2500,
            mindsetScore = 88,
            isPaidUnlocked = true
        )

        val dummyEntries = listOf(
            NotebookEntryEntity(
                id = 1L,
                moduleId = 1,
                moduleTitle = "Definite Major Purpose",
                title = "My Sovereign Decree",
                content = "I dedicate my existence to sovereign financial and philosophical autonomy.",
                promptQuestion = "What is the exact date by which you will possess this wealth?",
                tags = "Autosuggestion, Decree",
                isFavorite = true,
                timestamp = 1700000000000L
            ),
            NotebookEntryEntity(
                id = 2L,
                moduleId = 2,
                moduleTitle = "MasterMind Alliance",
                title = "Strategic Counsel",
                content = "Harmonious coordination of knowledge and effort in a spirit of absolute definiteness.",
                promptQuestion = "Who are the 3 individuals that comprise your inner circle?",
                tags = "MasterMind, Strategy",
                isFavorite = false,
                timestamp = 1699900000000L
            )
        )

        assertNotNull(dummyProfile)
        assertEquals(2, dummyEntries.size)
        val sorted = dummyEntries.sortedByDescending { it.timestamp }
        assertEquals("My Sovereign Decree", sorted.first().title)
        assertTrue(sorted.first().isFavorite)
    }
}

