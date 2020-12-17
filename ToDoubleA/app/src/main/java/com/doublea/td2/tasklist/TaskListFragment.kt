package com.doublea.td2.tasklist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private var my_text_view: TextView?= null
    private val tasksRepository = TasksRepository()
    private val adapter = TaskListAdapter()
    private val viewModel: TaskListViewModel by viewModels()

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
        var recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
        // DELETE BUTTON
        adapter.onDeleteClickListener = {
            task ->
            lifecycleScope.launch {
                viewModel.deleteTask(task)
            }
            adapter?.notifyDataSetChanged()
        }
        // EDIT BUTTON
        adapter.onEditClickListener = {
            task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            startActivityForResult(intent, TaskActivity.EDIT_TASK_REQUEST_CODE)
            adapter.notifyDataSetChanged()
        }

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            adapter.taskList = it.toMutableList()
            adapter.notifyDataSetChanged()
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // CREATE OR UPDATE A TASK
        val task = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TaskActivity.ADD_TASK_REQUEST_CODE) {
                lifecycleScope.launch {
                    viewModel.addTask(task)
                }
            } else if (requestCode == TaskActivity.EDIT_TASK_REQUEST_CODE) {
                lifecycleScope.launch {
                    viewModel.editTask(task)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onResume() {
        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            my_text_view?.text = "${userInfo.firstName} ${userInfo.lastName}"
            viewModel.loadTasks()
        }
        super.onResume()
    }

}

