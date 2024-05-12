package com.example.nntodo

import android.content.Context
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nntodo.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root){
    @RequiresApi(Build.VERSION_CODES.O)
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")!!
    @RequiresApi(Build.VERSION_CODES.O)
    fun bindTaskItem(taskItem: TaskItem) {
        binding.name.text = taskItem.name

        if(taskItem.isCompleted()){
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.completeButton.setImageResource(taskItem.imageResource())
        binding.completeButton.setColorFilter(taskItem.imageColor(context))

        binding.completeButton.setOnClickListener {
            clickListener.completeTaskItem(taskItem)
        }

        binding.deleteButton.setOnClickListener {
            // Call the deleteTaskItem method in the clickListener interface
            clickListener.deleteTaskItem(taskItem)
        }

        binding.taskCellContainer.setOnClickListener {
            clickListener.editTaskItem(taskItem)
        }

        if (taskItem.dueTime() != null)
            binding.dueTime.text = timeFormat.format(taskItem.dueTime())
        else
            binding.dueTime.text = ""
    }
}