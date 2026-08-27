package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * WealthGoalLogEntity records contributions (monetary inflows) and non-monetary milestones
 * toward the wealth goal.
 */
@Entity(tableName = "wealth_goal_logs")
data class WealthGoalLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalId: Int = 1,
    val amount: Double = 0.0, // > 0 for monetary contribution, 0.0 for milestone note
    val isMilestoneOnly: Boolean = false,
    val title: String = "Capital Inflow",
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val dateFormatted: String = "",
    val resultingTotal: Double = 0.0
)
