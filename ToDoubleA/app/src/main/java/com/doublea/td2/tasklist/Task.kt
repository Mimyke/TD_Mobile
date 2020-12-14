package com.doublea.td2.tasklist

import android.widget.TextView
import kotlinx.serialization.SerialName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Task(
        //@SerialName("id")
        val id: String,
        //@SerialName("title")
        val title: String,
        //@SerialName("description")
        val description: String = "description"
) : Serializable
