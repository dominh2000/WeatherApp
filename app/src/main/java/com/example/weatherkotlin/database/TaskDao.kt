package com.example.weatherkotlin.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY id DESC")
    fun getAllTasksDesc(): Flow<List<DatabaseTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: DatabaseTask)

    @Update
    suspend fun updateTask(task: DatabaseTask)

    @Delete
    suspend fun deleteTask(task: DatabaseTask)
}