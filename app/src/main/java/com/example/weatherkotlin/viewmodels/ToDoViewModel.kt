package com.example.weatherkotlin.viewmodels

import androidx.lifecycle.*
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.domain.Task
import com.example.weatherkotlin.repository.ToDoRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ToDoViewModel(app: BaseApplication) : ViewModel() {

    private val toDoRepository = ToDoRepository(app.databaseApplication)

    private val _toDoList = MutableLiveData<List<Task>>()
    private val _selectedTask = MutableLiveData<Task>()
    private val _insertedId = MutableLiveData<Int>()

    val toDoList: LiveData<List<Task>> = _toDoList
    val selectedTask: LiveData<Task> = _selectedTask
    val insertedId: LiveData<Int> = _insertedId

    init {
        getAllToDosAsc()
    }

    private fun getAllToDosAsc() {
        viewModelScope.launch {
            toDoRepository.refreshToDoListAsc().collect {
                _toDoList.value = it
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
            _insertedId.value = insertId
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

    fun filterByClosestDeadline() {
        viewModelScope.launch {
            toDoRepository.getToDoListByClosestDeadline().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByFurthestDeadline() {
        viewModelScope.launch {
            toDoRepository.getToDoListByFurthestDeadline().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByPriority1() {
        viewModelScope.launch {
            toDoRepository.getToDoListByPriority1().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByPriority2() {
        viewModelScope.launch {
            toDoRepository.getToDoListByPriority2().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByPriority3() {
        viewModelScope.launch {
            toDoRepository.getToDoListByPriority3().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByPriority4() {
        viewModelScope.launch {
            toDoRepository.getToDoListByPriority4().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByNotCompleted() {
        viewModelScope.launch {
            toDoRepository.getToDoListNotCompleted().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByCompleted() {
        viewModelScope.launch {
            toDoRepository.getToDoListCompleted().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByNotified() {
        viewModelScope.launch {
            toDoRepository.getToDoListNotified().collect {
                _toDoList.value = it
            }
        }
    }

    fun filterByNotNotified() {
        viewModelScope.launch {
            toDoRepository.getToDoListNotNotified().collect {
                _toDoList.value = it
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