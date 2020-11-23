package com.doublea.td2.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.doublea.td2.R

class TaskListFragment : Fragment() {
    private val taskList = listOf("Task 1", "Task 2", "Task 3")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LayoutInflater inflater = (LayoutInflater())
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
        return rootView
    }
}