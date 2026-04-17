package com.marchcode.evaluateuitest.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data model representing a Task in the application.
 * Uses Parcelable for efficient data passing between components.
 */
@Parcelize
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val priority: TaskPriority,
    val isCompleted: Boolean = false,
    val isFavorite: Boolean = false,
    val dueDate: String? = null
) : Parcelable

enum class TaskCategory(val displayName: String) {
    WORK("Work"),
    PERSONAL("Personal"),
    SHOPPING("Shopping"),
    HEALTH("Health"),
    OTHER("Other")
}

enum class TaskPriority(val displayName: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}