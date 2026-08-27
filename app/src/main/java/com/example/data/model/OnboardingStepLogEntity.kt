package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "onboarding_step_logs")
data class OnboardingStepLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stepNumber: Int,
    val stepName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = true
)
