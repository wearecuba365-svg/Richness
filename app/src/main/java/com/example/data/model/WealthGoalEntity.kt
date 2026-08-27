package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * WealthGoalEntity stores the user's primary Definite Wealth Target.
 * Represents a concrete, trackable financial commitment tied to a specific number and date,
 * embodying Napoleon Hill's First and Second Principles of Transmutation.
 * 
 * Note: Architected with an integer ID to support single active goal in v1
 * while being forward-compatible with multiple concurrent goals in v2.
 */
@Entity(tableName = "wealth_goals")
data class WealthGoalEntity(
    @PrimaryKey val id: Int = 1,
    val title: String = "Definite Wealth Target",
    val targetAmount: Double = 100000.0,
    val currentAmount: Double = 15000.0,
    val startingAmount: Double = 0.0,
    val currencySymbol: String = "$",
    val targetDateEpochMillis: Long = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000L), // 1 Year default
    val startDateEpochMillis: Long = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000L), // 30 Days ago default
    val category: String = "Financial Sovereignty",
    val servicePledge: String = "I will deliver unmatched value, intense focus, and relentless service to manifest this exact accumulation.",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
