package com.example.weatherkotlin.viewmodels

import androidx.lifecycle.*
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.domain.Task
import com.example.weatherkotlin.repository.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(app: BaseApplication) : ViewModel() {

    private val toDoRepository = ToDoRepository(app.databaseApplication)

    private val _selectedTask = MutableLiveData<Task>()

    val toDoList: LiveData<List<Task>> = toDoRepository.toDoList.asLiveData()
    val selectedTask: LiveData<Task> = _selectedTask

    init {
        getAllToDosAsc()
    }

    private fun getAllToDosAsc() {
        viewModelScope.launch {
            toDoRepository.refreshToDoListAsc()
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
        completed: Boolean
    ) {
        val newTask = Task(
            name = name,
            description = description,
            priority = priority,
            deadlineDate = deadlineDate,
            deadlineHour = deadlineHour,
            completed = completed
        )
        insertToDo(newTask)
    }

    fun updateToDoItem(
        id: Int,
        name: String,
        description: String,
        priority: Int,
        deadlineDate: String,
        deadlineHour: String,
        completed: Boolean
    ) {
        val taskToUpdate = Task(id, name, description, priority, deadlineDate, deadlineHour, completed)
        updateToDo(taskToUpdate)
    }

    fun onTaskClicked(selectedTask: Task) {
        _selectedTask.value = selectedTask
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