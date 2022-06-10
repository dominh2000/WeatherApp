package com.example.weatherkotlin.database

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
    fun getTasksByName(searchName: String): Flow<List<DatabaseTask>>

    @Query(
        "SELECT * FROM task " +
                "WHERE deadline_date BETWEEN :startDate AND :endDate " +
                "AND priority = :priority " +
                "AND completed = :completed " +
                "AND is_notified = :notified " +
                "ORDER BY deadline_date ASC"
    )
    fun getTasksByAdvancedSearch(
        startDate: String,
        endDate: String,
        priority: Int,
        completed: Int,
        notified: Int
    ): Flow<List<DatabaseTask>>

    @Query("SELECT * FROM task ORDER BY deadline_date ASC")
    fun getAllTasksByClosestDeadline(): Flow<List<DatabaseTask>>

    @Query("SELECT * FROM task ORDER BY deadline_date DESC")
    fun getAllTasksByFurthestDeadline(): Flow<List<DatabaseTask>>

    @Query("SELECT * FROM task WHERE priority = :priorityLevel")
    fun getAllTasksByPriority(priorityLevel: Int): Flow<List<DatabaseTask>>

    @Query("SELECT * FROM task WHERE completed = :completeState")
    fun getAllTasksByCompleteState(completeState: Int): Flow<List<DatabaseTask>>

    @Query("SELECT * FROM task WHERE is_notified = :notificationState")
    fun getAllTasksByNotificationState(notificationState: Int): Flow<List<DatabaseTask>>
}