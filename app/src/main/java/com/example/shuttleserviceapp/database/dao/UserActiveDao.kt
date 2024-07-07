package com.example.shuttleserviceapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.shuttleserviceapp.database.entity.UserActive

@Dao
interface UserActiveDao {
    @Query("SELECT * FROM userActive")
    suspend fun getAll(): List<UserActive>

    @Insert
    suspend fun insert(userActive: UserActive)

    @Query("DELETE FROM userActive")
    suspend fun clearTable()
}