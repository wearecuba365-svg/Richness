package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class LessonMediaType(val label: String, val badge: String) {
    AUDIO("Audio Masterclass", "AUDIO"),
    VIDEO("Video Lecture", "VIDEO")
}

@Entity(tableName = "short_lessons")
data class ShortLessonEntity(
    @PrimaryKey val id: String,
    val moduleId: Int,
    val order: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val durationText: String, // e.g. "06:45"
    val durationSeconds: Int, // e.g. 405
    val mediaType: String, // "AUDIO" or "VIDEO"
    val mediaUrl: String, // Audio resource/stream path or video canvas descriptor
    val instructorName: String = "Napoleon Hill Philosophy Vault",
    val keyTakeaway: String,
    val keyBulletPoints: String, // newline-separated bullet points/chapter stamps
    val transcript: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val lastPlaybackPositionSeconds: Int = 0,
    val xpReward: Int = 35
) {
    val isVideo: Boolean get() = mediaType.equals("VIDEO", ignoreCase = true)
    
    val chapterList: List<String> get() = keyBulletPoints
        .split("\n")
        .map { it.trim() }
        .filter { it.isNotBlank() }
}
