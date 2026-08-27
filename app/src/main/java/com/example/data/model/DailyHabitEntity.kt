package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_habits")
data class DailyHabitEntity(
    @PrimaryKey val id: String,
    val title: String,
    val principle: String,
    val description: String,
    val category: String, // "Mindset", "Knowledge", "Spiritual", "Action"
    val iconKey: String,   // "visualization", "reading", "meditation", "affirmation", "transmutation", "mastermind", "gratitude"
    val targetMinutes: Int = 15,
    val xpReward: Int = 30,
    val isPredefined: Boolean = true,
    val orderIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
