package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.dataSources.database.TaskDao
import com.example.weatherkotlin.data.dataSources.database.asDomainModel
import com.example.weatherkotlin.data.domainModel.Task
import com.example.weatherkotlin.data.domainModel.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToDoRepository @Inject constructor(
    val taskDao: TaskDao
) {

    val toDoList: Flow<List<Task>> = taskDao.getAllTasksDesc().transform {
        emit(it.asDomainModel())
    }

    suspend fun refreshToDoListDsc(): Flow<List<Task>> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasksDesc().transform {
                emit(it.asDomainModel())
            }
        }
    }

    suspend fun insertToDo(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task.asDatabaseModel())
        }
    }

    suspend fun insertToDoWithAlarm(task: Task): Long {
        return withContext(Dispatchers.IO) {
            taskDao.insertTask(task.asDatabaseModel())
        }
    }

    suspend fun updateToDo(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task.asDatabaseModel())
        }
    }

    suspend fun deleteToDo(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task.asDatabaseModel())
        }
    }

    suspend fun getToDoListByName(name: String): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getTasksByName(name).asDomainModel()
        }
    }

    suspend fun getToDoListByAdvancedSearch(
        startDate: String,
        endDate: String,
        priority: Int,
        completed: Int,
        notified: Int
    ): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getTasksByAdvancedSearch(
                startDate, endDate, priority, completed, notified
            ).asDomainModel()
        }
    }

    suspend fun getToDoListByClosestDeadline(): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasksByClosestDeadline().asDomainModel()
        }
    }

    suspend fun getToDoListByFurthestDeadline(): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasksByFurthestDeadline().asDomainModel()
        }
    }

    suspend fun getToDoListByPriority(priority: Int): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasksByPriority(priority).asDomainModel()
        }
    }

    suspend fun getToDoListByCompleteState(completeState: Int): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasksByCompleteState(completeState).asDomainModel()
        }
    }

    suspend fun getToDoListByNotificationState(notificationState: Int): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasksByNotificationState(notificationState).asDomainModel()
        }
    }
}