package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconKey: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val tierRequired: String = "Novice",
    val progress: Int = 0,
    val maxProgress: Int = 1,
    val category: String = "General",
    val xpReward: Int = 100
)

