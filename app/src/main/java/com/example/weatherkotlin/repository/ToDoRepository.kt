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

    suspend fun refreshToDoListDsc(): Flow<List<Task>> {
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

    suspend fun insertToDoWithAlarm(task: Task): Long {
        return withContext(Dispatchers.IO) {
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

    suspend fun getToDoListByName(name: String): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getTasksByName(name).transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByAdvancedSearch(
        startDate: String,
        endDate: String,
        priority: Int,
        completed: Int,
        notified: Int
    ): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getTasksByAdvancedSearch(
                startDate,
                endDate,
                priority,
                completed,
                notified
            ).transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByClosestDeadline(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByClosestDeadline().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByFurthestDeadline(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByFurthestDeadline().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByPriority(priority: Int): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByPriority(priority).transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByCompleteState(completeState: Int): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByCompleteState(completeState).transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByNotificationState(notificationState: Int): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByNotificationState(notificationState).transform {
                emit(it.asDomainModel())
            }
        }
    }
}