package com.example.shuttleserviceapp.repository

import android.app.Application
import com.example.shuttleserviceapp.MyApplication
import com.example.shuttleserviceapp.database.dao.UserDao
import com.example.shuttleserviceapp.database.entity.User

class UserRepository(application: Application) {
    private val userDao: UserDao

    init {
        val db = (application as MyApplication).database
        userDao = db.userDao()
    }

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAll()
    }

    suspend fun checkLogin(username: String, password: String): User? {
        return userDao.getUserByUsernameAndPassword(username, password)
    }
}