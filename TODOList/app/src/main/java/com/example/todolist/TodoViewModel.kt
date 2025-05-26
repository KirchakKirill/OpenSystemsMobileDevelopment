
package com.example.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TodoViewModel : ViewModel() {
    private val _todoItems =  MutableLiveData<List<TodoItem>>( mutableListOf(
        TodoItem(
            id = com.example.todolist.generateId(),
            title = "Купить хлеб",
            description = "Белый хлеб и батон",
            createdDate = Date(),
            isChecked = false
        ),
        TodoItem(
            id = com.example.todolist.generateId(),
            title = "Позвонить маме",
            description = "Обсудить выходные",
            createdDate = Date(),
            isChecked = false
        ),
        TodoItem(
            id = com.example.todolist.generateId(),
            title = "Пойти в зал",
            description = "Тренировка 1.5 часа",
            createdDate = Date(),
            isChecked = false
        ),
        TodoItem(
            id = com.example.todolist.generateId(),
            title = "Доделать лабу",
            description = "По мобильной разработке",
            createdDate = Date(),
            isChecked = false
        ),
        TodoItem(
            id = com.example.todolist.generateId(),
            title = "Прочитать книгу",
            description = "Глава 5-6",
            createdDate = Date(),
            isChecked = false
        )
    )
    )

    val todoItems: LiveData<List<TodoItem>> = _todoItems

    fun addItem(item: TodoItem) {
        val current = _todoItems.value?.toMutableList() ?: mutableListOf()
        current.add(item)
        _todoItems.value = current
    }

    fun updateItem(updatedItem: TodoItem) {
        val current = _todoItems.value?.toMutableList() ?: mutableListOf()
        val index = current.indexOfFirst { it.id == updatedItem.id }
        if (index != -1) {
            current[index] = updatedItem
            _todoItems.value = current
        }
    }

    fun getItemById(id: Int): TodoItem? {
        val found = _todoItems.value?.firstOrNull { it.id == id }
        println("DEBUG: Looking for item $id, found: $found") // Логирование
        return found
    }

    private var idCounter = 0

    fun generateId(): Int {
        return idCounter++
    }
}