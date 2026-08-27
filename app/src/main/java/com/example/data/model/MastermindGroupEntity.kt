package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mastermind_groups")
data class MastermindGroupEntity(
    @PrimaryKey val id: String,
    val name: String,
    val motto: String,
    val inviteCode: String,
    val targetTier: String = "Builder", // Novice, Builder, Architect, Sovereign, Legacy
    val minLevelOrModule: Int = 1,
    val maxMembers: Int = 6,
    val groupStreakWeeks: Int = 1,
    val combinedXpThisWeek: Int = 1200,
    val isUserMember: Boolean = false,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mastermind_members")
data class MastermindMemberEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val displayName: String,
    val avatarInitial: String,
    val avatarColorHex: String = "#D4AF37",
    val tierTitle: String = "Builder",
    val currentModuleTitle: String = "Vault 3: Faith",
    val weeklyXp: Int = 350,
    val isCurrentUser: Boolean = false,
    val joinedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mastermind_checkins")
data class MastermindCheckinEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val memberId: String,
    val memberDisplayName: String,
    val memberAvatarInitial: String,
    val memberAvatarColorHex: String = "#D4AF37",
    val memberTier: String = "Builder",
    val isCurrentUser: Boolean = false,
    val weekNumber: Int,
    val year: Int,
    val goalTitle: String,
    val status: String, // "YES", "PARTIAL", "NO"
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val fireCount: Int = 0,
    val clapCount: Int = 0,
    val diamondCount: Int = 0,
    val userReactedFire: Boolean = false,
    val userReactedClap: Boolean = false,
    val userReactedDiamond: Boolean = false
)
