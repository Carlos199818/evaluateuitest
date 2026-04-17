package com.marchcode.evaluateuitest.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marchcode.evaluateuitest.data.model.Task
import com.marchcode.evaluateuitest.data.model.UiState
import com.marchcode.evaluateuitest.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Task List screen.
 * Manages UI state and handles business logic for task list operations.
 * Uses Hilt for dependency injection.
 */
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // UI State for task list
    private val _uiState = MutableStateFlow<UiState<List<Task>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Task>>> = _uiState.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Event for navigation
    private val _navigationEvent = MutableLiveData<TaskListEvent>()
    val navigationEvent: LiveData<TaskListEvent> = _navigationEvent

    // Snackbar messages
    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    init {
        loadTasks()
        observeSearchQuery()
    }

    /**
     * Loads tasks from repository
     */
    private fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.getTasks()
                    .catch { e ->
                        _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
                    }
                    .collect { tasks ->
                        if (tasks.isEmpty()) {
                            _uiState.value = UiState.Empty
                        } else {
                            _uiState.value = UiState.Success(tasks)
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load tasks")
            }
        }
    }

    /**
     * Observes search query and filters tasks
     */
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(300) // Debounce to avoid excessive filtering
                .collect { query ->
                    filterTasks(query)
                }
        }
    }

    /**
     * Filters tasks based on search query
     */
    private fun filterTasks(query: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.searchTasks(query)
                    .catch { e ->
                        _uiState.value = UiState.Error(e.message ?: "Search failed")
                    }
                    .collect { tasks ->
                        if (tasks.isEmpty()) {
                            _uiState.value = UiState.Empty
                        } else {
                            _uiState.value = UiState.Success(tasks)
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Search failed")
            }
        }
    }

    /**
     * Updates search query
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /**
     * Clears search query
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }

    /**
     * Handles task item click
     */
    fun onTaskClicked(task: Task) {
        _navigationEvent.value = TaskListEvent.NavigateToDetail(task.id)
    }

    /**
     * Handles toggle complete
     */
    fun onToggleComplete(taskId: String) {
        viewModelScope.launch {
            val result = repository.toggleComplete(taskId)
            if (result.isFailure) {
                _snackbarMessage.value = "Failed to update task"
            }
        }
    }

    /**
     * Handles FAB click to add new task
     */
    fun onAddTaskClicked() {
        _navigationEvent.value = TaskListEvent.NavigateToAddTask
    }

    /**
     * Simulates error state for testing
     */
    fun simulateError() {
        viewModelScope.launch {
            repository.simulateError(true)
            loadTasks()
        }
    }

    /**
     * Retries loading tasks
     */
    fun retry() {
        viewModelScope.launch {
            repository.simulateError(false)
            loadTasks()
        }
    }
}

/**
 * Sealed class representing navigation events from TaskList
 */
sealed class TaskListEvent {
    data class NavigateToDetail(val taskId: String) : TaskListEvent()
    object NavigateToAddTask : TaskListEvent()
}