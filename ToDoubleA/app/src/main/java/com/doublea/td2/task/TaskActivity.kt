package com.doublea.td2.task

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.doublea.td2.R
import com.doublea.td2.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {
    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val EDIT_TASK_REQUEST_CODE = 667
        const val TASK_KEY = "666"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_updater)
        val task = intent.getSerializableExtra(TASK_KEY) as? Task
        this.findViewById<EditText>(R.id.title_updater).setText(task?.title)
        this.findViewById<EditText>(R.id.description_updater).setText(task?.description)
        val UUID = task?.id ?: UUID.randomUUID().toString()

        findViewById<ImageButton>(R.id.saveButton).setOnClickListener {
            // Instanciation d'un nouvel objet [Task]
            var titleTask = this.findViewById<EditText>(R.id.title_updater).text.toString()
            var descriptionTask = this.findViewById<EditText>(R.id.description_updater).text.toString()
            val newTask = Task(id = UUID, title = titleTask, description = descriptionTask)
            intent?.putExtra(TASK_KEY, newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }



}
