package com.doublea.td2.task

import com.doublea.td2.tasklist.Task
import org.junit.Assert.*
import org.junit.Test

class TaskTest {

    private val task1 : Task = Task("1","Task 1","First Task")
    private val task2 : Task = Task("2","Task 2","Second Task")
    private val tasks = mutableListOf<Task>(task1, task2)
    /*@Test
    fun addTaskTest(){

    }*/

}