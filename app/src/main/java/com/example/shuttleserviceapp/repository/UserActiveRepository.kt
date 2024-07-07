package com.example.shuttleserviceapp.repository

import android.app.Application
import com.example.shuttleserviceapp.MyApplication
import com.example.shuttleserviceapp.database.dao.UserActiveDao
import com.example.shuttleserviceapp.database.dao.UserDao
import com.example.shuttleserviceapp.database.entity.User
import com.example.shuttleserviceapp.database.entity.UserActive

class UserActiveRepository(application: Application) {
    private val userActiveDao: UserActiveDao

    init {
        val db = (application as MyApplication).database
        userActiveDao = db.userActiveDao()
    }

    suspend fun insert(userActive: UserActive) {
        userActiveDao.insert(userActive)
    }

    suspend fun getUserActive(): List<UserActive> {
        return userActiveDao.getAll()
    }

    suspend fun clear() {
        userActiveDao.clearTable()
    }
}