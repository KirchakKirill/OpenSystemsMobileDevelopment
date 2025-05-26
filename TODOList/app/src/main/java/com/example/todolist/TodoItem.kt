package com.example.todolist

import java.util.Date
import java.util.UUID

data class TodoItem(
    val id: Int,
    val title: String,
    val description: String = "",
    val createdDate: Date = Date(),
    var isChecked: Boolean = false


)

fun generateId(): Int = UUID.randomUUID().hashCode()