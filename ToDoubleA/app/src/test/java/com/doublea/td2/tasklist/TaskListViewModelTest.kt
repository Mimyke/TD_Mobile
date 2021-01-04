package com.doublea.td2.tasklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.doublea.td2.network.Api
import com.doublea.td2.network.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@ExperimentalCoroutinesApi
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TaskListViewModelTest{
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private var task1 : Task = Task("1","Task 1","First Task")
    private var task2 : Task = Task("2","Task 2","Second Task")
    private var task3 : Task = Task("3","Task 3","Third Task")
    private var task4 : Task = Task("4","Task 4","4th Task")

    private var tasksRepository : TasksRepository = TasksRepository()
    private var taskListViewModel : TaskListViewModel = TaskListViewModel()

    @Test
    fun testAddTask(){
        val listTask = taskListViewModel.addTask(task1)

        assertEquals(listOf(task1),listTask)
    }
}