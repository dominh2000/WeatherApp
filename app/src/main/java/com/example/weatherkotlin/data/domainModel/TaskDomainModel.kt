package com.example.weatherkotlin.data.domainModel

import com.example.weatherkotlin.data.dataSources.database.DatabaseTask

/*
    For ToDoList
 */
data class Task(
    val id: Int = 0,
    val name: String,
    val description: String,
    val priority: Int,
    val deadlineDate: String,
    val deadlineHour: String,
    val isNotified: Boolean,
    val completed: Boolean
)

fun Task.asDatabaseModel(): DatabaseTask {
    return this.let {
        DatabaseTask(
            it.id,
            it.name,
            it.description,
            it.priority,
            it.deadlineDate,
            it.deadlineHour,
            it.isNotified,
            it.completed
        )
    }
}