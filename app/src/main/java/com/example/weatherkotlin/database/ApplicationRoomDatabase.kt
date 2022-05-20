package com.example.weatherkotlin.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DatabaseLocationInfo::class, DatabaseWeatherOneDay::class, DatabaseTask::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4)
    ],
    exportSchema = true
)
abstract class ApplicationRoomDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationRoomDatabase? = null

        fun getDatabase(context: Context): ApplicationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationRoomDatabase::class.java,
                    "database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}