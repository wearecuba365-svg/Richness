package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * GivingGoalEntity stores the optional giving/tithing goal configuration.
 * Users can choose:
 * - NONE: Skip goal-setting and freely log acts of benevolence without a target
 * - FIXED_MONTHLY: A fixed monthly dollar target (e.g., $250/month)
 * - PERCENTAGE_INCOME: A tithing / percentage of income target (e.g., 10%)
 * - ACTS_COUNT_MONTHLY: A frequency target (e.g., 4 acts of giving per month)
 */
@Entity(tableName = "giving_goals")
data class GivingGoalEntity(
    @PrimaryKey val id: Int = 1,
    val goalType: String = GOAL_TYPE_NONE,
    val targetAmount: Double = 0.0,
    val targetPercentage: Double = 10.0,
    val targetActsCount: Int = 4,
    val currencySymbol: String = "$",
    val serviceMotto: String = "True wealth begins with the spirit of generosity. Abundance expands when circulated.",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        const val GOAL_TYPE_NONE = "NONE"
        const val GOAL_TYPE_FIXED_MONTHLY = "FIXED_MONTHLY"
        const val GOAL_TYPE_PERCENTAGE_INCOME = "PERCENTAGE_INCOME"
        const val GOAL_TYPE_ACTS_COUNT_MONTHLY = "ACTS_COUNT_MONTHLY"
    }

    val isGoalActive: Boolean
        get() = goalType != GOAL_TYPE_NONE
}

/**
 * GivingLogEntity stores individual acts of generosity and gratitude.
 * Monetary amounts are strictly optional to respect user privacy and allow non-financial giving.
 */
@Entity(tableName = "giving_logs")
data class GivingLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String, // Description of giving act (e.g. "Donated winter jackets to shelter")
    val amount: Double? = null, // Optional dollar amount (null for private / non-monetary acts)
    val currencySymbol: String = "$",
    val category: String = CATEGORY_CHARITY,
    val recipientName: String = "", // Optional recipient/cause (e.g. "Local Food Bank")
    val note: String = "", // Optional gratitude reflection
    val timestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String = "",
    val isMonetary: Boolean = false
) {
    companion object {
        const val CATEGORY_CHARITY = "Charity & Causes"
        const val CATEGORY_FAMILY = "Family & Friends"
        const val CATEGORY_COMMUNITY = "Community & Strangers"
        const val CATEGORY_TIPPING = "Tipping & Hospitality"
        const val CATEGORY_TIME_MENTORSHIP = "Time & Mentorship"
        const val CATEGORY_KINDNESS = "Random Acts of Kindness"
        const val CATEGORY_OTHER = "Other Generosity"

        val ALL_CATEGORIES = listOf(
            CATEGORY_CHARITY,
            CATEGORY_FAMILY,
            CATEGORY_COMMUNITY,
            CATEGORY_TIPPING,
            CATEGORY_TIME_MENTORSHIP,
            CATEGORY_KINDNESS,
            CATEGORY_OTHER
        )
    }
}
