package com.doublea.td2.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.doublea.td2.network.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            val fetchedTasks = repository.loadTasks()
            _taskList.value = fetchedTasks!!
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            val createdTask = repository.createTask(task)

            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.add(createdTask!!)
            _taskList.value = editableList
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = repository.updateTask(task)

            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { updatedTask?.id == it.id }
            editableList[position] = updatedTask!!
            _taskList.value = editableList
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)

            val editableList = _taskList.value.orEmpty().toMutableList()
            editableList.remove(task)
            _taskList.value = editableList
        }
    }
}