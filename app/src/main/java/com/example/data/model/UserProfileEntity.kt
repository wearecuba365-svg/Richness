package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Sovereign Initiate",
    val email: String = "member@riches.club",
    val role: String = "user", // "user" or "admin"
    val xpTotal: Int = 0,
    val currentStreak: Int = 1,
    val bestStreak: Int = 1,
    val mindsetScore: Int = 50,
    val tierName: String = "Novice", // Novice, Builder, Architect, Sovereign, Legacy
    val beliefScore: Int = 50,
    val disciplineScore: Int = 50,
    val desireScore: Int = 50,
    val persistenceScore: Int = 50,
    val identityScore: Int = 50,
    val isPaidUnlocked: Boolean = false,
    val hasCompletedOnboarding: Boolean = false,
    val onboardingStep: Int = 1,
    val onboardingMaxStepReached: Int = 1,
    val lastLoginDate: Long = System.currentTimeMillis(),
    val journalStreak: Int = 1,
    val learningStreak: Int = 1,
    val perfectWeeksCount: Int = 0,
    val birthYear: Int = 1996,
    val birthMonth: Int = 1,
    val birthDay: Int = 1,
    val lifeExpectancyYears: Int = 90,
    val definiteChiefAim: String = "",
    val affirmationStreak: Int = 0,
    val bestAffirmationStreak: Int = 0,
    val lastAffirmationEpochDay: Long = 0L,
    val affirmationAudioPath: String? = null,
    val visionBoardStreak: Int = 0,
    val bestVisionBoardStreak: Int = 0,
    val lastVisionBoardViewEpochDay: Long = 0L,
    val lastFearReframeEpochDay: Long = 0L,
    val fearReframeCount: Int = 0,
    val comebacksCount: Int = 0,
    val hasPendingPersistenceCheck: Boolean = false,
    val pendingPersistenceStreakType: String = "Daily Sovereign Ritual",
    val lastPersistenceCheckEpochDay: Long = 0L,
    val lastBlueprintEpochDay: Long = 0L,
    val primaryBlueprintPattern: String = "",
    val isLeaderboardOptedIn: Boolean = true
)
