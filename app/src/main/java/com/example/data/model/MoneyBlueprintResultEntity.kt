package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_blueprint_results")
data class MoneyBlueprintResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val primaryPatternKey: String = PATTERN_SCARCITY,
    val primaryPatternTitle: String = "Scarcity & Zero-Sum Mindset",
    val secondaryPatternKey: String = PATTERN_FEAR_FAILURE,
    val secondaryPatternTitle: String = "Fear of Financial Loss & Failure",
    val scarcityScore: Int = 50,
    val guiltScore: Int = 50,
    val fearFailureScore: Int = 50,
    val fearJudgmentScore: Int = 50,
    val selfWorthScore: Int = 50,
    val overallLimitationScore: Int = 50,
    val recommendedModuleIds: String = "1,9",
    val recommendedFeatureKey: String = "money_mindset_journal",
    val summaryInsight: String = "",
    val actionPledge: String = ""
) {
    companion object {
        const val PATTERN_SCARCITY = "scarcity"
        const val PATTERN_GUILT = "guilt"
        const val PATTERN_FEAR_FAILURE = "fear_failure"
        const val PATTERN_FEAR_JUDGMENT = "fear_judgment"
        const val PATTERN_SELF_WORTH = "self_worth"
    }
}
