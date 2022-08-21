package com.example.weatherkotlin.data.dataSources.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ORDER BY id DESC")
    fun getAllTasksDesc(): Flow<List<DatabaseTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: DatabaseTask): Long

    @Update
    suspend fun updateTask(task: DatabaseTask)

    @Delete
    suspend fun deleteTask(task: DatabaseTask)

    @Query("SELECT * FROM task WHERE name LIKE '%' || :searchName || '%' ")
    suspend fun getTasksByName(searchName: String): List<DatabaseTask>

    @Query(
        "SELECT * FROM task " +
                "WHERE deadline_date BETWEEN :startDate AND :endDate " +
                "AND priority = :priority " +
                "AND completed = :completed " +
                "AND is_notified = :notified " +
                "ORDER BY deadline_date ASC"
    )
    suspend fun getTasksByAdvancedSearch(
        startDate: String,
        endDate: String,
        priority: Int,
        completed: Int,
        notified: Int
    ): List<DatabaseTask>

    @Query("SELECT * FROM task ORDER BY deadline_date ASC")
    suspend fun getAllTasksByClosestDeadline(): List<DatabaseTask>

    @Query("SELECT * FROM task ORDER BY deadline_date DESC")
    suspend fun getAllTasksByFurthestDeadline(): List<DatabaseTask>

    @Query("SELECT * FROM task WHERE priority = :priorityLevel")
    suspend fun getAllTasksByPriority(priorityLevel: Int): List<DatabaseTask>

    @Query("SELECT * FROM task WHERE completed = :completeState")
    suspend fun getAllTasksByCompleteState(completeState: Int): List<DatabaseTask>

    @Query("SELECT * FROM task WHERE is_notified = :notificationState")
    suspend fun getAllTasksByNotificationState(notificationState: Int): List<DatabaseTask>
}