package com.example.weatherkotlin

import android.app.Application
import com.example.weatherkotlin.database.ApplicationRoomDatabase

class ApplicationClass: Application() {
    val databaseApplication: ApplicationRoomDatabase by lazy {
        ApplicationRoomDatabase.getDatabase(this)
    }
}