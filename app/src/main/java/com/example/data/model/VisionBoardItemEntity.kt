package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vision_board_items")
data class VisionBoardItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Wealth & Abundance", // Wealth & Abundance, Empire & Career, Health & Vitality, Serene Travel, Relationships, Mind & Mastery
    val imageUrl: String, // Stock identifier (e.g., "stock_private_jet"), local file URI, or web URL
    val targetTimeline: String = "", // e.g. "By 2028", "Q4 2027", "Dec 2026"
    val affirmation: String = "", // Subconscious autosuggestion anchor text
    val orderIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)
