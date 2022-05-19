package com.example.weatherkotlin.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherkotlin.domain.Task

@Entity(tableName = "task")
data class DatabaseTask(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val description: String,
    @ColumnInfo val priority: Int,
    @ColumnInfo(name = "deadline_date") val deadlineDate: String,
    @ColumnInfo(name = "deadline_hour") val deadlineHour: String,
    @ColumnInfo val completed: Boolean
    )

// Extension functions
fun List<DatabaseTask>.asDomainModel(): List<Task> {
    return this.map {
        Task(
            it.id,
            it.name,
            it.description,
            it.priority,
            it.deadlineDate,
            it.deadlineHour,
            it.completed
        )
    }
}