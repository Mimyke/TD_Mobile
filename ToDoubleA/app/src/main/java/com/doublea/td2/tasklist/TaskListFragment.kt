package com.doublea.td2.tasklist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
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
import coil.load
import coil.transform.CircleCropTransformation
import com.doublea.td2.network.SHARED_PREF_TOKEN_KEY
import com.doublea.td2.userinfo.UserInfoActivity
import com.doublea.td2.userinfo.UserInfoViewModel
import androidx.core.content.edit
import com.doublea.td2.authentification.AuthentificationActivity

class TaskListFragment : Fragment() {
    private var my_text_view: TextView?= null
    private var my_image_view: ImageView?= null
    private val adapter = TaskListAdapter()
    private val viewModel: TaskListViewModel by viewModels()
    private val userModel: UserInfoViewModel by viewModels()

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
        my_image_view = view.findViewById<ImageView>(R.id.super_image)
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

        // Photo on click
        my_image_view?.setOnClickListener {
                    val intent = Intent(activity, UserInfoActivity::class.java)
                    startActivity(intent)
        }

        viewModel.taskList.observe(viewLifecycleOwner, Observer {
            adapter.taskList = it.toMutableList()
            adapter.notifyDataSetChanged()
        })

        val buttonDisconnect = view.findViewById<Button>(R.id.logout_button)
        buttonDisconnect.setOnClickListener {
            // On efface le Token du user
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putString(SHARED_PREF_TOKEN_KEY, "auth_token_key") // Valeur par défaut
            }
            startActivity(Intent(activity, AuthentificationActivity::class.java))
        }

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
        super.onResume()
        lifecycleScope.launch {
            userModel.getInfo()!!
        }
        userModel.userInfo.observe(viewLifecycleOwner, {
            view?.findViewById<TextView>(R.id.super_text)?.text = "${userModel.userInfo.value?.firstName} ${userModel.userInfo.value?.lastName}"
            my_image_view?.load(userModel.userInfo.value?.avatar) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            viewModel.loadTasks()

        })
    }

}

