package com.marchcode.evaluateuitest.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marchcode.evaluateuitest.R
import com.marchcode.evaluateuitest.data.model.Task
import com.marchcode.evaluateuitest.databinding.ItemTaskBinding

/**
 * RecyclerView adapter for displaying tasks.
 * Uses ListAdapter with DiffUtil for efficient updates.
 */
class TaskListAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onToggleComplete: (String) -> Unit
) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding, onTaskClick, onToggleComplete)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(
        private val binding: ItemTaskBinding,
        private val onTaskClick: (Task) -> Unit,
        private val onToggleComplete: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                // Set task title and description
                textTaskTitle.text = task.title
                textTaskDescription.text = task.description
                textTaskCategory.text = task.category.displayName
                textTaskPriority.text = task.priority.displayName

                // Set priority color
                val priorityColor = when (task.priority) {
                    com.marchcode.evaluateuitest.data.model.TaskPriority.HIGH ->
                        ContextCompat.getColor(root.context, R.color.priority_high)
                    com.marchcode.evaluateuitest.data.model.TaskPriority.MEDIUM ->
                        ContextCompat.getColor(root.context, R.color.priority_medium)
                    com.marchcode.evaluateuitest.data.model.TaskPriority.LOW ->
                        ContextCompat.getColor(root.context, R.color.priority_low)
                }
                textTaskPriority.setTextColor(priorityColor)

                // Set due date
                task.dueDate?.let {
                    textTaskDueDate.text = "Due: $it"
                } ?: run {
                    textTaskDueDate.text = ""
                }

                // Set completion state
                checkboxTaskComplete.isChecked = task.isCompleted

                // Apply strikethrough for completed tasks
                textTaskTitle.paint.isStrikeThruText = task.isCompleted

                // Set favorite icon
                imageTaskFavorite.setImageResource(
                    if (task.isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_outline
                )

                // Click listeners
                root.setOnClickListener { onTaskClick(task) }
                checkboxTaskComplete.setOnClickListener { onToggleComplete(task.id) }
                buttonTaskDetails.setOnClickListener { onTaskClick(task) }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}