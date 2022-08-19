package com.example.weatherkotlin.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        DatabaseLocationInfo::class,
        DatabaseWeatherOneDay::class,
        DatabaseTask::class,
        CityForForecastFiveDays::class,
        CurrentWeather::class,
        ForecastOneDay::class,
    ],
    version = 8,
    /*
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7)
    ],
     */
    exportSchema = true
)
abstract class ApplicationRoomDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun taskDao(): TaskDao
    abstract fun openWeatherDao(): OpenWeatherDao

    companion object {
        @Volatile
        private var INSTANCE: ApplicationRoomDatabase? = null

        fun getDatabase(context: Context, password: String): ApplicationRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val supportFactory = SupportFactory(SQLiteDatabase.getBytes(password.toCharArray()))
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApplicationRoomDatabase::class.java,
                    "encrypted_database"
                )
                    .fallbackToDestructiveMigration()
                    .openHelperFactory(supportFactory)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}