package com.example.weatherkotlin.repository

import com.example.weatherkotlin.database.ApplicationRoomDatabase
import com.example.weatherkotlin.database.asDomainModel
import com.example.weatherkotlin.domain.Task
import com.example.weatherkotlin.domain.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext

class ToDoRepository(private val database: ApplicationRoomDatabase) {

    val toDoList: Flow<List<Task>> = database.taskDao().getAllTasksDesc().transform {
        emit(it.asDomainModel())
    }

    suspend fun refreshToDoListAsc(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksDesc().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun insertToDo(task: Task) {
        withContext(Dispatchers.IO) {
            database.taskDao().insertTask(task.asDatabaseModel())
        }
    }

    suspend fun updateToDo(task: Task) {
        withContext(Dispatchers.IO) {
            database.taskDao().updateTask(task.asDatabaseModel())
        }
    }

    suspend fun deleteToDo(task: Task) {
        withContext(Dispatchers.IO) {
            database.taskDao().deleteTask(task.asDatabaseModel())
        }
    }
}