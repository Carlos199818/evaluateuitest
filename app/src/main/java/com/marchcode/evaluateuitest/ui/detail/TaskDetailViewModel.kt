package com.marchcode.evaluateuitest.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marchcode.evaluateuitest.data.model.Task
import com.marchcode.evaluateuitest.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Task Detail screen.
 * Manages task details and favorite toggle functionality.
 * Works seamlessly with the Java-based TaskDetailActivity.
 */
@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Task data
    private val _task = MutableLiveData<Task?>()
    @JvmName("getTask")
    fun getTask(): LiveData<Task?> = _task

    // Loading state
    private val _isLoading = MutableLiveData(false)
    @JvmName("getIsLoading")
    fun getIsLoading(): LiveData<Boolean> = _isLoading

    // Error message
    private val _errorMessage = MutableLiveData<String?>()
    @JvmName("getErrorMessage")
    fun getErrorMessage(): LiveData<String?> = _errorMessage

    // Snackbar message
    private val _snackbarMessage = MutableLiveData<String?>()
    @JvmName("getSnackbarMessage")
    fun getSnackbarMessage(): LiveData<String?> = _snackbarMessage

    // Navigation events
    private val _navigationEvent = MutableLiveData<TaskDetailEvent?>()
    @JvmName("getNavigationEvent")
    fun getNavigationEvent(): LiveData<TaskDetailEvent?> = _navigationEvent

    /**
     * Loads task details by ID
     */
    fun loadTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val taskData = repository.getTask(taskId)
                if (taskData != null) {
                    _task.value = taskData
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Task not found"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load task"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Toggles favorite status
     */
    fun onToggleFavorite() {
        val currentTask = _task.value ?: return

        viewModelScope.launch {
            try {
                val result = repository.toggleFavorite(currentTask.id)
                if (result.isSuccess) {
                    _snackbarMessage.value = if (currentTask.isFavorite) {
                        "Removed from favorites"
                    } else {
                        "Added to favorites"
                    }
                } else {
                    _snackbarMessage.value = "Failed to update favorite status"
                }
            } catch (e: Exception) {
                _snackbarMessage.value = "Failed to update favorite status"
            }
        }
    }

    /**
     * Handles edit button click
     */
    fun onEditClicked() {
        _task.value?.let { currentTask ->
            _navigationEvent.value = TaskDetailEvent.NavigateToEdit(currentTask.id)
        }
    }

    /**
     * Handles delete button click
     */
    fun onDeleteClicked() {
        val currentTask = _task.value ?: return

        viewModelScope.launch {
            try {
                val result = repository.deleteTask(currentTask.id)
                if (result.isSuccess) {
                    _navigationEvent.value = TaskDetailEvent.NavigateBack("Task deleted")
                } else {
                    _snackbarMessage.value = "Failed to delete task"
                }
            } catch (e: Exception) {
                _snackbarMessage.value = "Failed to delete task"
            }
        }
    }

    /**
     * Sealed class representing navigation events
     */
    sealed class TaskDetailEvent {
        data class NavigateToEdit(val taskId: String) : TaskDetailEvent()
        data class NavigateBack(val message: String) : TaskDetailEvent()
    }
}