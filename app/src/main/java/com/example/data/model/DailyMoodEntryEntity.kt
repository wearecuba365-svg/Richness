package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_mood_entries",
    indices = [
        Index(value = ["dateEpochDay"], unique = true)
    ]
)
data class DailyMoodEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val dateFormatted: String, // e.g. "Aug 22" or "2026-08-22"
    val mindsetScore: Int = 75, // 0 - 100
    val moodRating: Int = 80, // 0 - 100 (Emotional Equanimity & Calm)
    val energyLevel: Int = 85, // 0 - 100 (Discipline & Vitality)
    val transmutationScore: Int = 78, // 0 - 100 (Transmuting doubt into faith)
    val moodState: String = "Empowered", // Empowered, Resolute, Serene, Focused, Transmuting
    val reflectionNote: String = "",
    val ritualsCompletedCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
