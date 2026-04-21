package com.marchcode.evaluateuitest.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marchcode.evaluateuitest.data.model.Task
import com.marchcode.evaluateuitest.data.model.TaskCategory
import com.marchcode.evaluateuitest.data.model.TaskPriority
import com.marchcode.evaluateuitest.data.repository.TaskRepository
import com.marchcode.evaluateuitest.utils.EspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel for Task Form screen.
 * Handles task creation and editing with comprehensive validation.
 */
@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    // Form fields
    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _category = MutableLiveData(TaskCategory.PERSONAL)
    val category: LiveData<TaskCategory> = _category

    private val _priority = MutableLiveData(TaskPriority.MEDIUM)
    val priority: LiveData<TaskPriority> = _priority

    private val _dueDate = MutableLiveData("")
    val dueDate: LiveData<String> = _dueDate

    // Validation errors
    private val _titleError = MutableLiveData<String?>()
    val titleError: LiveData<String?> = _titleError

    private val _descriptionError = MutableLiveData<String?>()
    val descriptionError: LiveData<String?> = _descriptionError

    // UI State
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEditMode = MutableLiveData(false)
    val isEditMode: LiveData<Boolean> = _isEditMode

    // Events
    private val _saveSuccessEvent = MutableLiveData<String>()
    val saveSuccessEvent: LiveData<String> = _saveSuccessEvent

    private val _saveErrorEvent = MutableLiveData<String>()
    val saveErrorEvent: LiveData<String> = _saveErrorEvent

    private var currentTaskId: String? = null
    private var currentTaskIsCompleted: Boolean = false
    private var currentTaskIsFavorite: Boolean = false

    /**
     * Loads task for editing
     */
    fun loadTask(taskId: String) {
        EspressoIdlingResource.increment()
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val task = repository.getTask(taskId)
                if (task != null) {
                    currentTaskId = task.id
                    currentTaskIsCompleted = task.isCompleted
                    currentTaskIsFavorite = task.isFavorite
                    _title.value = task.title
                    _description.value = task.description
                    _category.value = task.category
                    _priority.value = task.priority
                    _dueDate.value = task.dueDate ?: ""
                    _isEditMode.value = true
                }
            } catch (e: Exception) {
                _saveErrorEvent.value = "Failed to load task"
            } finally {
                _isLoading.value = false
                EspressoIdlingResource.decrement()
            }
        }
    }

    /**
     * Updates title field
     */
    fun onTitleChanged(newTitle: String) {
        _title.value = newTitle
        validateTitle(newTitle)
    }

    /**
     * Updates description field
     */
    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
        validateDescription(newDescription)
    }

    /**
     * Updates category field
     */
    fun onCategoryChanged(newCategory: TaskCategory) {
        _category.value = newCategory
    }

    /**
     * Updates priority field
     */
    fun onPriorityChanged(newPriority: TaskPriority) {
        _priority.value = newPriority
    }

    /**
     * Updates due date field
     */
    fun onDueDateChanged(newDate: String) {
        _dueDate.value = newDate
    }

    /**
     * Validates title field
     */
    private fun validateTitle(titleValue: String): Boolean {
        return when {
            titleValue.isBlank() -> {
                _titleError.value = "Title is required"
                false
            }
            titleValue.length < 3 -> {
                _titleError.value = "Title must be at least 3 characters"
                false
            }
            titleValue.length > 100 -> {
                _titleError.value = "Title must be less than 100 characters"
                false
            }
            else -> {
                _titleError.value = null
                true
            }
        }
    }

    /**
     * Validates description field
     */
    private fun validateDescription(descValue: String): Boolean {
        return when {
            descValue.isBlank() -> {
                _descriptionError.value = "Description is required"
                false
            }
            descValue.length < 10 -> {
                _descriptionError.value = "Description must be at least 10 characters"
                false
            }
            descValue.length > 500 -> {
                _descriptionError.value = "Description must be less than 500 characters"
                false
            }
            else -> {
                _descriptionError.value = null
                true
            }
        }
    }

    /**
     * Validates all fields
     */
    private fun validateAllFields(): Boolean {
        val isTitleValid = validateTitle(_title.value ?: "")
        val isDescValid = validateDescription(_description.value ?: "")
        return isTitleValid && isDescValid
    }

    /**
     * Saves or updates task
     */
    fun onSaveClicked() {
        if (!validateAllFields()) {
            _saveErrorEvent.value = "Please fix validation errors"
            return
        }

        EspressoIdlingResource.increment()
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val task = Task(
                    id = currentTaskId ?: UUID.randomUUID().toString(),
                    title = _title.value!!.trim(),
                    description = _description.value!!.trim(),
                    category = _category.value!!,
                    priority = _priority.value!!,
                    dueDate = _dueDate.value?.takeIf { it.isNotBlank() },
                    isCompleted = if (_isEditMode.value == true) currentTaskIsCompleted else false,
                    isFavorite = if (_isEditMode.value == true) currentTaskIsFavorite else false
                )

                val result = if (_isEditMode.value == true) {
                    repository.updateTask(task)
                } else {
                    repository.addTask(task)
                }

                if (result.isSuccess) {
                    val message = if (_isEditMode.value == true) {
                        "Task updated successfully"
                    } else {
                        "Task added successfully"
                    }
                    _saveSuccessEvent.value = message
                } else {
                    _saveErrorEvent.value = result.exceptionOrNull()?.message
                        ?: "Failed to save task"
                }
            } catch (e: Exception) {
                _saveErrorEvent.value = e.message ?: "Failed to save task"
            } finally {
                _isLoading.value = false
                EspressoIdlingResource.decrement()
            }
        }
    }
}
