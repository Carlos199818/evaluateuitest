package com.marchcode.evaluateuitest.data.source

import com.marchcode.evaluateuitest.data.model.Task
import com.marchcode.evaluateuitest.data.model.TaskCategory
import com.marchcode.evaluateuitest.data.model.TaskPriority
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fake data source that provides sample tasks for testing.
 * Uses in-memory storage with Flow for reactive updates.
 * Includes simulated network delays for realistic behavior.
 */
@Singleton
class FakeTaskDataSource @Inject constructor() {

    private val _tasks = MutableStateFlow(getSampleTasks())
    val tasks: Flow<List<Task>> = _tasks.asStateFlow()

    private var shouldSimulateError = false

    /**
     * Simulates network delay to make the app behavior more realistic
     */
    private suspend fun simulateNetworkDelay() {
        delay(800) // Simulated network latency
    }

    /**
     * Gets all tasks
     */
    suspend fun getTasks(): List<Task> {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to fetch tasks")
        }
        return _tasks.value
    }

    /**
     * Gets a single task by ID
     */
    suspend fun getTask(taskId: String): Task? {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to fetch task")
        }
        return _tasks.value.find { it.id == taskId }
    }

    /**
     * Searches tasks by query
     */
    fun searchTasks(query: String): Flow<List<Task>> {
        return tasks.map { taskList ->
            if (query.isBlank()) {
                taskList
            } else {
                taskList.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
            }
        }
    }

    /**
     * Adds a new task
     */
    suspend fun addTask(task: Task) {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to add task")
        }
        _tasks.value = _tasks.value + task
    }

    /**
     * Updates an existing task
     */
    suspend fun updateTask(task: Task) {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to update task")
        }
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) task else it
        }
    }

    /**
     * Deletes a task
     */
    suspend fun deleteTask(taskId: String) {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to delete task")
        }
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    /**
     * Toggles favorite status
     */
    suspend fun toggleFavorite(taskId: String) {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to update favorite status")
        }
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    /**
     * Toggles completion status
     */
    suspend fun toggleComplete(taskId: String) {
        simulateNetworkDelay()
        if (shouldSimulateError) {
            throw Exception("Network error: Unable to update completion status")
        }
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    /**
     * Enable/disable error simulation for testing error states
     */
    fun simulateError(shouldError: Boolean) {
        shouldSimulateError = shouldError
    }

    /**
     * Provides sample tasks for initial data
     */
    private fun getSampleTasks(): List<Task> {
        return listOf(
            Task(
                id = "1",
                title = "Complete Project Proposal",
                description = "Finish the Q2 project proposal document and submit to management for review",
                category = TaskCategory.WORK,
                priority = TaskPriority.HIGH,
                isCompleted = false,
                isFavorite = true,
                dueDate = "2026-04-15"
            ),
            Task(
                id = "2",
                title = "Buy Groceries",
                description = "Get milk, eggs, bread, and vegetables from the supermarket",
                category = TaskCategory.SHOPPING,
                priority = TaskPriority.MEDIUM,
                isCompleted = false,
                isFavorite = false,
                dueDate = "2026-04-01"
            ),
            Task(
                id = "3",
                title = "Schedule Dentist Appointment",
                description = "Call Dr. Smith's office to schedule annual dental checkup",
                category = TaskCategory.HEALTH,
                priority = TaskPriority.MEDIUM,
                isCompleted = true,
                isFavorite = false,
                dueDate = "2026-04-10"
            ),
            Task(
                id = "4",
                title = "Review Code Pull Requests",
                description = "Review and approve pending pull requests from team members",
                category = TaskCategory.WORK,
                priority = TaskPriority.HIGH,
                isCompleted = false,
                isFavorite = true,
                dueDate = "2026-04-02"
            ),
            Task(
                id = "5",
                title = "Plan Weekend Trip",
                description = "Research and book hotel for weekend getaway to the mountains",
                category = TaskCategory.PERSONAL,
                priority = TaskPriority.LOW,
                isCompleted = false,
                isFavorite = true,
                dueDate = "2026-04-20"
            ),
            Task(
                id = "6",
                title = "Update Resume",
                description = "Add recent projects and certifications to resume",
                category = TaskCategory.PERSONAL,
                priority = TaskPriority.LOW,
                isCompleted = false,
                isFavorite = false,
                dueDate = null
            ),
            Task(
                id = "7",
                title = "Attend Team Meeting",
                description = "Weekly sprint planning and retrospective meeting",
                category = TaskCategory.WORK,
                priority = TaskPriority.MEDIUM,
                isCompleted = true,
                isFavorite = false,
                dueDate = "2026-03-31"
            ),
            Task(
                id = "8",
                title = "Fix Production Bug",
                description = "Investigate and fix the login timeout issue reported by users",
                category = TaskCategory.WORK,
                priority = TaskPriority.HIGH,
                isCompleted = false,
                isFavorite = true,
                dueDate = "2026-04-03"
            )
        )
    }
}