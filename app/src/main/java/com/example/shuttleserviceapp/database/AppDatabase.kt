package com.example.shuttleserviceapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shuttleserviceapp.database.dao.UserActiveDao
import com.example.shuttleserviceapp.database.dao.UserDao
import com.example.shuttleserviceapp.database.entity.User
import com.example.shuttleserviceapp.database.entity.UserActive

@Database(entities = [User::class, UserActive::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userActiveDao(): UserActiveDao
}