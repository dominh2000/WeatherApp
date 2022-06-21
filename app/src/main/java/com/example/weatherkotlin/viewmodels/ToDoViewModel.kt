package com.example.weatherkotlin.viewmodels

import androidx.lifecycle.*
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.domain.Task
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ToDoViewModel(app: BaseApplication) : ViewModel() {

    private val toDoRepository = app.toDoRepository

    private val _toDoList = MutableLiveData<List<Task>>()
    private val _selectedTask = MutableLiveData<Task>()
    private val _insertedId = MutableLiveData<Int>()

    val toDoList: LiveData<List<Task>> = _toDoList
    val selectedTask: LiveData<Task> = _selectedTask
    val insertedId: LiveData<Int> = _insertedId

    fun getAllToDosDsc() {
        viewModelScope.launch {
            toDoRepository.refreshToDoListDsc().collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun isEntryValid(
        name: String,
        description: String,
        priority: Int,
        deadlineDate: String,
        deadlineHour: String
    ): Boolean {
        if (name.isBlank() || description.isBlank() || priority <= 0 || deadlineDate.isBlank() || deadlineHour.isBlank()) {
            return false
        }
        return true
    }

    private fun insertToDo(task: Task) {
        viewModelScope.launch {
            toDoRepository.insertToDo(task)
        }
    }

    private suspend fun insertToDoWithAlarm(task: Task): Long {
        val insertedId = viewModelScope.async {
            toDoRepository.insertToDoWithAlarm(task)
        }
        return insertedId.await()
    }

    private fun updateToDo(task: Task) {
        viewModelScope.launch {
            toDoRepository.updateToDo(task)
        }
    }

    fun deleteToDoItem(task: Task) {
        viewModelScope.launch {
            toDoRepository.deleteToDo(task)
        }
    }

    fun addNewToDoItem(
        name: String,
        description: String,
        priority: Int,
        deadlineDate: String,
        deadlineHour: String,
        isNotified: Boolean,
        completed: Boolean
    ) {
        val newTask = Task(
            name = name,
            description = description,
            priority = priority,
            deadlineDate = deadlineDate,
            deadlineHour = deadlineHour,
            isNotified = isNotified,
            completed = completed
        )
        insertToDo(newTask)
    }

    fun addNewToDoItemWithAlarm(
        name: String,
        description: String,
        priority: Int,
        deadlineDate: String,
        deadlineHour: String,
        isNotified: Boolean,
        completed: Boolean
    ) {
        val newTask = Task(
            name = name,
            description = description,
            priority = priority,
            deadlineDate = deadlineDate,
            deadlineHour = deadlineHour,
            isNotified = isNotified,
            completed = completed
        )
        viewModelScope.launch {
            val insertId = insertToDoWithAlarm(newTask).toInt()
            _insertedId.postValue(insertId)
        }
    }

    fun updateToDoItem(
        id: Int,
        name: String,
        description: String,
        priority: Int,
        deadlineDate: String,
        deadlineHour: String,
        isNotified: Boolean,
        completed: Boolean
    ) {
        val taskToUpdate =
            Task(id, name, description, priority, deadlineDate, deadlineHour, isNotified, completed)
        updateToDo(taskToUpdate)
    }

    fun onTaskClicked(selectedTask: Task) {
        _selectedTask.value = selectedTask
    }

    fun filterByName(name: String) {
        viewModelScope.launch {
            toDoRepository.getToDoListByName(name).collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun filterByAdvancedSearch(
        startDate: String,
        endDate: String,
        priority: Int,
        completed: Int,
        notified: Int
    ) {
        viewModelScope.launch {
            toDoRepository.getToDoListByAdvancedSearch(
                startDate,
                endDate,
                priority,
                completed,
                notified
            ).collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun filterByClosestDeadline() {
        viewModelScope.launch {
            toDoRepository.getToDoListByClosestDeadline().collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun filterByFurthestDeadline() {
        viewModelScope.launch {
            toDoRepository.getToDoListByFurthestDeadline().collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun filterByPriority(priority: Int) {
        viewModelScope.launch {
            toDoRepository.getToDoListByPriority(priority).collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun filterByCompleteState(completeState: Int) {
        viewModelScope.launch {
            toDoRepository.getToDoListByCompleteState(completeState).collect {
                _toDoList.postValue(it)
            }
        }
    }

    fun filterByNotificationState(notificationState: Int) {
        viewModelScope.launch {
            toDoRepository.getToDoListByNotificationState(notificationState).collect {
                _toDoList.postValue(it)
            }
        }
    }
}

class ToDoViewModelFactory(val app: BaseApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}