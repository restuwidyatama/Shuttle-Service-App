package com.example.shuttleserviceapp

import android.app.Application
import androidx.room.Room
import com.example.shuttleserviceapp.database.AppDatabase

class MyApplication : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my-database"
        ).build()
    }
}