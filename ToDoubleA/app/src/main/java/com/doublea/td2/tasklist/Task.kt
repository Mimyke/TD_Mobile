package com.doublea.td2.tasklist

import android.widget.TextView
import java.io.Serializable

data class Task (val id: String, val title: String, val description: String = "") : Serializable {

}