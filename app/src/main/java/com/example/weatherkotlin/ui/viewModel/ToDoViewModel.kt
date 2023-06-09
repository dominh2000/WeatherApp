package com.example.weatherkotlin.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.data.domainModel.Task
import com.example.weatherkotlin.data.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AdvancedSearchStatus { LOADING, DONE }

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _toDoList = MutableLiveData<List<Task>>()
    private val _advancedSearchResultList = MutableLiveData<List<Task>>(listOf())
    private val _selectedTask = MutableLiveData<Task>()
    private val _insertedId = MutableLiveData<Int>()
    private val _advancedSearchStatus = MutableLiveData<AdvancedSearchStatus>()

    val toDoList: LiveData<List<Task>> = _toDoList
    val selectedTask: LiveData<Task> = _selectedTask
    val insertedId: LiveData<Int> = _insertedId
    val advancedSearchResultList: LiveData<List<Task>> = _advancedSearchResultList
    val advancedSearchStatus: LiveData<AdvancedSearchStatus> = _advancedSearchStatus
    var startAdvancedSearch: Boolean = false

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
            _toDoList.value = toDoRepository.getToDoListByName(name)
        }
    }

    fun filterByAdvancedSearch(
        startDate: String,
        endDate: String,
        priority: Int,
        completed: Int,
        notified: Int
    ) {
        _advancedSearchResultList.value = listOf()
        viewModelScope.launch {
            _advancedSearchStatus.value = AdvancedSearchStatus.LOADING
            _advancedSearchResultList.value = toDoRepository.getToDoListByAdvancedSearch(
                startDate,
                endDate,
                priority,
                completed,
                notified
            )
            _advancedSearchStatus.value = AdvancedSearchStatus.DONE
        }
    }

    fun filterByClosestDeadline() {
        viewModelScope.launch {
            _toDoList.value = toDoRepository.getToDoListByClosestDeadline()
        }
    }

    fun filterByFurthestDeadline() {
        viewModelScope.launch {
            _toDoList.value = toDoRepository.getToDoListByFurthestDeadline()
        }
    }

    fun filterByPriority(priority: Int) {
        viewModelScope.launch {
            _toDoList.value = toDoRepository.getToDoListByPriority(priority)
        }
    }

    fun filterByCompleteState(completeState: Int) {
        viewModelScope.launch {
            _toDoList.value = toDoRepository.getToDoListByCompleteState(completeState)
        }
    }

    fun filterByNotificationState(notificationState: Int) {
        viewModelScope.launch {
            _toDoList.value = toDoRepository.getToDoListByNotificationState(notificationState)
        }
    }
}