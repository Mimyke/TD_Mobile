package com.doublea.td2.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doublea.td2.R

class TaskListAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var titleView: TextView
        lateinit var descriptionView: TextView
        init{
            titleView = itemView.findViewById(R.id.task_title)
            descriptionView = itemView.findViewById(R.id.task_description)
        }
        fun bind(taskTitle: String) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                titleView = itemView.findViewById(R.id.task_title)
                descriptionView = itemView.findViewById(R.id.task_description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // Create a new view, which defines the UI of the list item
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val (id, title, description) = taskList[position]
        holder.titleView.text = title
        holder.descriptionView.text = description
    }

    override fun getItemCount() = taskList.size
}