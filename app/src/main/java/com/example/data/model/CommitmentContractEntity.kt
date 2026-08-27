package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Entity(tableName = "commitment_contracts")
data class CommitmentContractEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String = "Definite Chief Aim Covenant",
    val goalStatement: String,
    val whyItMatters: String,
    val deadlineEpochMillis: Long,
    val createdAtEpochMillis: Long = System.currentTimeMillis(),
    val progressPercent: Int = 0, // 0..100
    val milestonesJson: String = "", // Comma-separated or milestone list
    val status: String = STATUS_ACTIVE, // "active", "completed", "renewed"
    val completedAtEpochMillis: Long? = null,
    val renewedAtEpochMillis: Long? = null,
    val originalContractId: Long? = null,
    val signatureName: String = "",
    val signedDateEpochMillis: Long = System.currentTimeMillis(),
    val xpAwardedForCreation: Int = 75,
    val xpAwardedForCompletion: Int = 150,
    val completionNotes: String = "",
    val renewalNotes: String = ""
) {
    val isActive: Boolean get() = status == STATUS_ACTIVE
    val isCompleted: Boolean get() = status == STATUS_COMPLETED
    val isRenewed: Boolean get() = status == STATUS_RENEWED

    /**
     * Calculates calendar days remaining until deadline.
     * Returns negative value if overdue.
     */
    fun getDaysRemaining(nowMillis: Long = System.currentTimeMillis()): Long {
        val diffMillis = deadlineEpochMillis - nowMillis
        return TimeUnit.MILLISECONDS.toDays(diffMillis)
    }

    fun isDeadlineReached(nowMillis: Long = System.currentTimeMillis()): Boolean {
        return isActive && nowMillis >= deadlineEpochMillis
    }

    fun getFormattedDeadline(): String {
        val sdf = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(deadlineEpochMillis))
    }

    fun getFormattedCreatedDate(): String {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(createdAtEpochMillis))
    }

    fun getFormattedSignedDate(): String {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        return sdf.format(Date(signedDateEpochMillis))
    }

    fun getFormattedCompletedDate(): String {
        return completedAtEpochMillis?.let {
            val sdf = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            sdf.format(Date(it))
        } ?: ""
    }

    fun getCountdownStatusText(nowMillis: Long = System.currentTimeMillis()): String {
        if (isCompleted) return "Completed"
        if (isRenewed) return "Renewed"

        val days = getDaysRemaining(nowMillis)
        return when {
            days > 14 -> {
                val weeks = (days + 6) / 7
                "$weeks Weeks Remaining ($days Days)"
            }
            days in 2..14 -> "$days Days Remaining"
            days == 1L -> "1 Day Remaining (Tomorrow)"
            days == 0L -> "Due Today"
            days == -1L -> "Deadline Passed (1 Day Ago) - Resolution Required"
            else -> "Deadline Passed (${Math.abs(days)} Days Ago) - Resolution Required"
        }
    }

    companion object {
        const val STATUS_ACTIVE = "active"
        const val STATUS_COMPLETED = "completed"
        const val STATUS_RENEWED = "renewed"
    }
}
