package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.RichesDao
import com.example.data.model.BadgeEntity
import com.example.data.model.CommitmentContractEntity
import com.example.data.model.DailyHabitEntity
import com.example.data.model.DailyHabitLogEntity
import com.example.data.model.DailyMoodEntryEntity
import com.example.data.model.GivingGoalEntity
import com.example.data.model.GivingLogEntity
import com.example.data.model.MastermindCheckinEntity
import com.example.data.model.MastermindGroupEntity
import com.example.data.model.MastermindMemberEntity
import com.example.data.model.ModuleEntity
import com.example.data.model.MoneyBlueprintResultEntity
import com.example.data.model.NotebookEntryEntity
import com.example.data.model.OnboardingStepLogEntity
import com.example.data.model.SavedIncomeIdeaEntity
import com.example.data.model.ShortLessonEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.VisionBoardItemEntity
import com.example.data.model.WealthGoalEntity
import com.example.data.model.WealthGoalLogEntity

@Database(
    entities = [
        ModuleEntity::class,
        UserProfileEntity::class,
        NotebookEntryEntity::class,
        BadgeEntity::class,
        DailyHabitEntity::class,
        DailyHabitLogEntity::class,
        DailyMoodEntryEntity::class,
        MastermindGroupEntity::class,
        MastermindMemberEntity::class,
        MastermindCheckinEntity::class,
        VisionBoardItemEntity::class,
        WealthGoalEntity::class,
        WealthGoalLogEntity::class,
        MoneyBlueprintResultEntity::class,
        SavedIncomeIdeaEntity::class,
        GivingGoalEntity::class,
        GivingLogEntity::class,
        ShortLessonEntity::class,
        OnboardingStepLogEntity::class,
        CommitmentContractEntity::class
    ],
    version = 17,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun richesDao(): RichesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "riches_protocol_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
