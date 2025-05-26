package com.example.todolist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding

class TodoAdapter(
    private val onItemClick: (TodoItem) -> Unit,
    private val onCheckedChange: (TodoItem, Boolean) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var items = emptyList<TodoItem>()

    inner class TodoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TodoItem) {
            binding.apply {
                checkbox.text = item.title
                checkbox.isChecked = item.isChecked


                checkbox.setOnCheckedChangeListener(null)
                checkbox.setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChange(item, isChecked)
                }

                itemView.setOnClickListener { onItemClick(item) }
                updateStrikeThrough(item.isChecked)
            }
        }

        private fun updateStrikeThrough(isChecked: Boolean) {
            binding.checkbox.paintFlags = if (isChecked) {
                binding.checkbox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.checkbox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<TodoItem>) {
        val diffResult = DiffUtil.calculateDiff(TodoDiffCallback(items, newItems))
        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    private class TodoDiffCallback(
        private val oldList: List<TodoItem>,
        private val newList: List<TodoItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}