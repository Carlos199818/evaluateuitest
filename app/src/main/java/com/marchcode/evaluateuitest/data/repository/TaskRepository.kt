package com.marchcode.evaluateuitest.data.repository

import com.marchcode.evaluateuitest.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Task operations.
 * This abstraction allows for easy testing and dependency injection.
 */
interface TaskRepository {
    /**
     * Gets all tasks as a Flow for reactive updates
     */
    fun getTasks(): Flow<List<Task>>

    /**
     * Gets a single task by ID
     */
    suspend fun getTask(taskId: String): Task?

    /**
     * Searches tasks by query string
     */
    fun searchTasks(query: String): Flow<List<Task>>

    /**
     * Adds a new task
     */
    suspend fun addTask(task: Task): Result<Unit>

    /**
     * Updates an existing task
     */
    suspend fun updateTask(task: Task): Result<Unit>

    /**
     * Deletes a task by ID
     */
    suspend fun deleteTask(taskId: String): Result<Unit>

    /**
     * Toggles the favorite status of a task
     */
    suspend fun toggleFavorite(taskId: String): Result<Unit>

    /**
     * Toggles the completion status of a task
     */
    suspend fun toggleComplete(taskId: String): Result<Unit>

    /**
     * Simulates a network error for testing error states
     */
    suspend fun simulateError(shouldError: Boolean)
}