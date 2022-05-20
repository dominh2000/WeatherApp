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

    suspend fun getToDoListByPriority1(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByPriority1().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByPriority2(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByPriority2().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByPriority3(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByPriority3().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListByPriority4(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksByPriority4().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListNotCompleted(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksNotCompleted().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListCompleted(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksCompleted().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListNotNotified(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksNotNotified().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun getToDoListNotified(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            database.taskDao().getAllTasksNotified().transform {
                emit(it.asDomainModel())
            }
        }
    }
}