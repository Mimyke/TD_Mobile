package com.doublea.td2.tasklist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doublea.td2.R
import com.doublea.td2.network.Api
import com.doublea.td2.network.TasksRepository
import com.doublea.td2.task.TaskActivity
import com.doublea.td2.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import java.util.*

class TaskListFragment : Fragment() {
    private val taskList = mutableListOf(
            Task(id = "id_1", title = "Task 1", description = "description 1"),
            Task(id = "id_2", title = "Task 2"),
            Task(id = "id_3", title = "Task 3")
    )
    private var my_text_view: TextView?= null
    private val tasksRepository = TasksRepository()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        my_text_view = view.findViewById<TextView>(R.id.super_text)
        // Pour une [RecyclerView] ayant l'id "recycler_view":
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        var adapter = recyclerView.adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = TaskListAdapter(taskList)
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
            // recyclerView?.adapter?.notifyDataSetChanged()
        }
        // DELETE BUTTON
        (recyclerView?.adapter as TaskListAdapter)?.onDeleteClickListener = {
            task -> taskList.remove(task)
            lifecycleScope.launch {
                tasksRepository.deleteTask(task)
            }
            recyclerView?.adapter?.notifyDataSetChanged()
        }
        // EDIT BUTTON
        (recyclerView?.adapter as TaskListAdapter)?.onEditClickListener = {
            task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, TaskActivity.EDIT_TASK_REQUEST_CODE)
            recyclerView?.adapter?.notifyDataSetChanged()
        }

        tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            taskList.clear()
            taskList.addAll(it)
            recyclerView?.adapter?.notifyDataSetChanged()
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // CREATE OR UPDATE A TASK
        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TaskActivity.ADD_TASK_REQUEST_CODE) {
                taskList.add(task)
                lifecycleScope.launch {
                    tasksRepository.createTask(task)
                }
            } else if (requestCode == TaskActivity.EDIT_TASK_REQUEST_CODE) {
                val position = taskList.indexOfFirst { task.id == it.id }
                taskList[position] = task
                lifecycleScope.launch {
                    tasksRepository.updateTask(task)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onResume() {
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            my_text_view?.text = "${userInfo.firstName} ${userInfo.lastName}"
            tasksRepository.refresh()
        }
        super.onResume()
    }

}

