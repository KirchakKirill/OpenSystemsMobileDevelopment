package com.example.todolist

import android.graphics.Paint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TodoAdapter
    private lateinit var headerTextView: TextView

    private val todoList = mutableListOf(
        TodoItem("Купить хлеб", false),
        TodoItem("Позвонить маме", true),
        TodoItem("Пойти в зал", false),
        TodoItem("Доделать лабу", false),
        TodoItem("Прочитать книгу", false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        headerTextView = findViewById(R.id.headerTextView)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TodoAdapter(todoList) {
            updateHeader()
        }
        recyclerView.adapter = adapter

        updateHeader()
    }

    private fun updateHeader() {
        val total = todoList.size
        val checked = todoList.count { it.isChecked }
        headerTextView.text = "TODOList          Total: $total - Checked: $checked"
    }
}

data class TodoItem(val title: String, var isChecked: Boolean)
