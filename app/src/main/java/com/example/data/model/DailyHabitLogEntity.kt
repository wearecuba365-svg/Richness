package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_habit_logs",
    indices = [
        Index(value = ["habitId", "dateEpochDay"], unique = true),
        Index(value = ["dateEpochDay"])
    ]
)
data class DailyHabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: String,
    val dateEpochDay: Long, // e.g. Calendar / epoch day
    val dateFormatted: String, // e.g. "2026-08-22"
    val completedAt: Long = System.currentTimeMillis(),
    val durationMinutes: Int = 15,
    val notes: String = "",
    val xpEarned: Int = 30
)
