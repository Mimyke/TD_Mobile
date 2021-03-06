package com.doublea.td2.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublea.td2.R

object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.title == newItem.title
    }
}

class TaskListAdapter() : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback) {
    // Déclaration de la variable lambda dans l'adapter:
    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onEditClickListener: ((Task) -> Unit)? = null
    var taskList: List<Task> = emptyList()

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var titleView: TextView
        lateinit var descriptionView: TextView

        init{
            titleView = itemView.findViewById(R.id.task_title)
            descriptionView = itemView.findViewById(R.id.task_description)
        }
        fun bind(task: Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                titleView = itemView.findViewById(R.id.task_title)
                titleView.setText(task.title)
                descriptionView = itemView.findViewById(R.id.task_description)
                descriptionView.setText(task.description)

                val buttonDelete = itemView.findViewById<ImageButton>(R.id.deleteButton)
                buttonDelete.setOnClickListener {onDeleteClickListener?.invoke(task) }
                val buttonEdit = itemView.findViewById<ImageButton>(R.id.editButton)
                buttonEdit.setOnClickListener {onEditClickListener?.invoke(task) }
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
        holder.bind(this.taskList[position])

    }

    override fun getItemCount() = taskList.size
}

