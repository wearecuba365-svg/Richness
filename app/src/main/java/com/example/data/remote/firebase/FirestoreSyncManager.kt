package com.example.data.remote.firebase

import android.util.Log
import com.example.data.model.BadgeEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.UserProfileEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

enum class CloudSyncStatus {
    IDLE,
    SYNCING,
    SYNCED,
    OFFLINE_ONLY,
    ERROR
}

data class CloudSyncState(
    val status: CloudSyncStatus = CloudSyncStatus.IDLE,
    val lastSyncTimestamp: Long = 0L,
    val errorMessage: String? = null
)

class FirestoreSyncManager {

    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        Log.w("FirestoreSyncManager", "Firestore not available: ${e.message}")
        null
    }

    private val _syncState = MutableStateFlow(CloudSyncState())
    val syncState: StateFlow<CloudSyncState> = _syncState.asStateFlow()

    suspend fun syncUserData(
        userId: String,
        profile: UserProfileEntity?,
        modules: List<ModuleEntity>,
        notebookEntries: List<NotebookEntryEntity>,
        badges: List<BadgeEntity>
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        if (firestore == null || userId.isBlank()) {
            _syncState.value = CloudSyncState(
                status = CloudSyncStatus.OFFLINE_ONLY,
                lastSyncTimestamp = System.currentTimeMillis(),
                errorMessage = "Offline storage active"
            )
            return@withContext Result.success(true)
        }

        try {
            _syncState.value = _syncState.value.copy(status = CloudSyncStatus.SYNCING)

            val userDocRef = firestore.collection("users").document(userId)

            // 1. Sync Profile Data
            if (profile != null) {
                val profileMap = hashMapOf(
                    "name" to profile.name,
                    "email" to profile.email,
                    "role" to profile.role,
                    "xpTotal" to profile.xpTotal,
                    "currentStreak" to profile.currentStreak,
                    "bestStreak" to profile.bestStreak,
                    "mindsetScore" to profile.mindsetScore,
                    "tierName" to profile.tierName,
                    "beliefScore" to profile.beliefScore,
                    "disciplineScore" to profile.disciplineScore,
                    "desireScore" to profile.desireScore,
                    "persistenceScore" to profile.persistenceScore,
                    "identityScore" to profile.identityScore,
                    "isPaidUnlocked" to profile.isPaidUnlocked,
                    "hasCompletedOnboarding" to profile.hasCompletedOnboarding,
                    "lastLoginDate" to profile.lastLoginDate,
                    "journalStreak" to profile.journalStreak,
                    "learningStreak" to profile.learningStreak,
                    "perfectWeeksCount" to profile.perfectWeeksCount,
                    "lastCloudSync" to System.currentTimeMillis()
                )
                userDocRef.set(profileMap, SetOptions.merge()).await()
            }

            // 2. Sync Completed Modules
            val completedModules = modules.filter { it.isCompleted || it.isQuestCompleted || it.savedField1.isNotBlank() }
            if (completedModules.isNotEmpty()) {
                val batch = firestore.batch()
                completedModules.forEach { module ->
                    val modRef = userDocRef.collection("modules").document(module.id.toString())
                    val modData = hashMapOf(
                        "id" to module.id,
                        "title" to module.title,
                        "isCompleted" to module.isCompleted,
                        "isQuestCompleted" to module.isQuestCompleted,
                        "savedField1" to module.savedField1,
                        "savedField2" to module.savedField2,
                        "savedField3" to module.savedField3,
                        "savedTimestamp" to System.currentTimeMillis()
                    )
                    batch.set(modRef, modData, SetOptions.merge())
                }
                batch.commit().await()
            }

            // 3. Sync Notebook Entries
            if (notebookEntries.isNotEmpty()) {
                val batch = firestore.batch()
                notebookEntries.take(50).forEach { entry ->
                    val noteRef = userDocRef.collection("notebook").document(entry.id.toString())
                    val noteData = hashMapOf(
                        "id" to entry.id,
                        "moduleId" to entry.moduleId,
                        "moduleTitle" to entry.moduleTitle,
                        "title" to entry.title,
                        "content" to entry.content,
                        "promptQuestion" to entry.promptQuestion,
                        "tags" to entry.tags,
                        "timestamp" to entry.timestamp,
                        "isFavorite" to entry.isFavorite
                    )
                    batch.set(noteRef, noteData, SetOptions.merge())
                }
                batch.commit().await()
            }

            _syncState.value = CloudSyncState(
                status = CloudSyncStatus.SYNCED,
                lastSyncTimestamp = System.currentTimeMillis()
            )
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreSyncManager", "Sync failed: ${e.message}", e)
            _syncState.value = CloudSyncState(
                status = CloudSyncStatus.ERROR,
                lastSyncTimestamp = System.currentTimeMillis(),
                errorMessage = e.localizedMessage ?: "Sync error"
            )
            Result.success(false)
        }
    }
}
