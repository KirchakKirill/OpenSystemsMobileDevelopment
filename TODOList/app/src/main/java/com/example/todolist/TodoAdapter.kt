package com.example.todolist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding

class TodoAdapter(
    private val items: List<TodoItem>,
    private val onItemCheckChanged: () -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    inner class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            checkbox.text = item.title
            checkbox.isChecked = item.isChecked

            checkbox.setOnCheckedChangeListener(null)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                updateStrikeThrough(checkbox, isChecked)
                onItemCheckChanged()
            }

            updateStrikeThrough(checkbox, item.isChecked)
        }
    }

    override fun getItemCount(): Int = items.size

    private fun updateStrikeThrough(checkbox: android.widget.CheckBox, isChecked: Boolean) {
        if (isChecked) {
            checkbox.paintFlags = checkbox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            checkbox.paintFlags = checkbox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
