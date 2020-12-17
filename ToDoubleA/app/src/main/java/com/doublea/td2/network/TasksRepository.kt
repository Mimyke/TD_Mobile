package com.doublea.td2.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doublea.td2.tasklist.Task

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()!!
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks
        }
    }

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateTask(task:Task): Task? {
        val response = tasksWebService.updateTask(task)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task:Task): Task? {
        val response = tasksWebService.createTask(task)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteTask(task:Task): String? {
        val response = tasksWebService.deleteTask(task.id)
        return if (response.isSuccessful) response.body() else null
    }
}