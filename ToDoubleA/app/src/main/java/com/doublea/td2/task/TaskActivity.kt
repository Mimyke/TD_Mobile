package com.doublea.td2.task

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.doublea.td2.R
import com.doublea.td2.tasklist.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_updater)

        findViewById<ImageButton>(R.id.saveButton).setOnClickListener {
            // Instanciation d'un nouvel objet [Task]
            var titleTask = this.findViewById<EditText>(R.id.title_updater).text.toString()
            var descriptionTask = this.findViewById<EditText>(R.id.description_updater).text.toString()
            val newTask = Task(id = UUID.randomUUID().toString(), title = titleTask, description = descriptionTask)
            intent?.putExtra(TASK_KEY.toString(), newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val TASK_KEY = 666
    }

}
