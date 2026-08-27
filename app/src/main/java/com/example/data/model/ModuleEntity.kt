package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey val id: Int,
    val order: Int,
    val title: String,
    val originalPrinciple: String,
    val subtitle: String,
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val xpReward: Int,
    val videoTitle: String,
    val videoDuration: String,
    val excerptTitle: String,
    val excerptText: String,
    val keyTakeaways: String, // newline-separated
    val templateTitle: String,
    val templatePrompt: String,
    val templateFieldLabel1: String,
    val templateFieldLabel2: String,
    val templateFieldLabel3: String,
    val savedField1: String = "",
    val savedField2: String = "",
    val savedField3: String = "",
    val questTitle: String,
    val questDescription: String,
    val questActionPrompt: String,
    val isQuestCompleted: Boolean = false,
    val notebookPrompt: String
)
