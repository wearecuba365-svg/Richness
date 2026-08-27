package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notebook_entries")
data class NotebookEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val moduleId: Int? = null,
    val moduleTitle: String = "Freeform Reflection",
    val title: String,
    val content: String,
    val promptQuestion: String = "",
    val tags: String = "Mindset, Ritual",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val entryType: String = ENTRY_TYPE_REFLECTION,
    val fearCategory: String = "",
    val fearText: String = "",
    val worstCaseText: String = "",
    val actionTodayText: String = "",
    val isActionCompleted: Boolean = false,
    val decisionText: String = "",
    val confidenceLevel: Int = 3, // 1 to 5
    val decisionRationale: String = "",
    val outcomeText: String = "",
    val outcomeTag: String = "", // "Good", "Mixed", "Bad", "Too Early to Tell"
    val isRevisited: Boolean = false,
    val revisitedTimestamp: Long = 0L,
    val comebackStreakType: String = "",
    val comebackObstacle: String = "",
    val comebackPlan: String = ""
) {
    companion object {
        const val ENTRY_TYPE_REFLECTION = "reflection"
        const val ENTRY_TYPE_MODULE_REFLECTION = "module_reflection"
        const val ENTRY_TYPE_FEAR_REFRAME = "fear_reframe"
        const val ENTRY_TYPE_DECISION_LOG = "decision_log"
        const val ENTRY_TYPE_COMEBACK = "comeback"
        const val ENTRY_TYPE_MONEY_MINDSET = "money_mindset"
        const val ENTRY_TYPE_COMMITMENT_CONTRACT = "commitment_contract"

        const val OUTCOME_GOOD = "Good"
        const val OUTCOME_MIXED = "Mixed"
        const val OUTCOME_BAD = "Bad"
        const val OUTCOME_TOO_EARLY = "Too Early to Tell"
    }
}

