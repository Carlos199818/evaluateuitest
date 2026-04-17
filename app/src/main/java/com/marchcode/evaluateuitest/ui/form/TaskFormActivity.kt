package com.marchcode.evaluateuitest.ui.form

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.marchcode.evaluateuitest.R
import com.marchcode.evaluateuitest.data.model.TaskCategory
import com.marchcode.evaluateuitest.data.model.TaskPriority
import com.marchcode.evaluateuitest.databinding.ActivityTaskFormBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

/**
 * Form screen for adding new tasks or editing existing ones.
 * Includes comprehensive validation and user feedback.
 * Written in Kotlin with MVVM architecture.
 */
@AndroidEntryPoint
class TaskFormActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
    }

    private lateinit var binding: ActivityTaskFormBinding
    private val viewModel: TaskFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupToolbar()
        setupSpinners()
        setupClickListeners()
        setupTextWatchers()
        observeViewModel()

        // Check if editing existing task
        val taskId = intent.getStringExtra(EXTRA_TASK_ID)
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    /**
     * Sets up window insets for edge-to-edge display
     */
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply top inset to AppBarLayout
            binding.appBarLayoutForm.updatePadding(top = insets.top)

            // Apply bottom inset to ScrollView content
            binding.scrollViewForm.updatePadding(bottom = insets.bottom)

            windowInsets
        }
    }

    /**
     * Sets up toolbar
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add Task"
        }
    }

    /**
     * Sets up category and priority spinners
     */
    private fun setupSpinners() {
        // Category spinner
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            TaskCategory.values().map { it.displayName }
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        // Priority spinner
        val priorityAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            TaskPriority.values().map { it.displayName }
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = priorityAdapter
    }

    /**
     * Sets up click listeners
     */
    private fun setupClickListeners() {
        // Save button
        binding.buttonSave.setOnClickListener {
            // Update ViewModel with current spinner values
            val selectedCategory = TaskCategory.values()[binding.spinnerCategory.selectedItemPosition]
            val selectedPriority = TaskPriority.values()[binding.spinnerPriority.selectedItemPosition]

            viewModel.onCategoryChanged(selectedCategory)
            viewModel.onPriorityChanged(selectedPriority)
            viewModel.onSaveClicked()
        }

        // Due date picker
        binding.editTextDueDate.setOnClickListener {
            showDatePicker()
        }

        // Clear due date
        binding.buttonClearDate.setOnClickListener {
            binding.editTextDueDate.setText("")
            viewModel.onDueDateChanged("")
        }
    }

    /**
     * Sets up text change listeners for real-time validation
     */
    private fun setupTextWatchers() {
        binding.editTextTitle.addTextChangedListener { text ->
            viewModel.onTitleChanged(text?.toString() ?: "")
        }

        binding.editTextDescription.addTextChangedListener { text ->
            viewModel.onDescriptionChanged(text?.toString() ?: "")
        }

        binding.editTextDueDate.addTextChangedListener { text ->
            viewModel.onDueDateChanged(text?.toString() ?: "")
        }
    }

    /**
     * Shows date picker dialog
     */
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
                binding.editTextDueDate.setText(formattedDate)
                viewModel.onDueDateChanged(formattedDate)
            },
            year,
            month,
            day
        )

        // Set minimum date to today
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    /**
     * Observes ViewModel state
     */
    private fun observeViewModel() {
        // Observe title
        viewModel.title.observe(this) { title ->
            if (binding.editTextTitle.text.toString() != title) {
                binding.editTextTitle.setText(title)
            }
        }

        // Observe description
        viewModel.description.observe(this) { description ->
            if (binding.editTextDescription.text.toString() != description) {
                binding.editTextDescription.setText(description)
            }
        }

        // Observe category
        viewModel.category.observe(this) { category ->
            binding.spinnerCategory.setSelection(category.ordinal)
        }

        // Observe priority
        viewModel.priority.observe(this) { priority ->
            binding.spinnerPriority.setSelection(priority.ordinal)
        }

        // Observe due date
        viewModel.dueDate.observe(this) { date ->
            if (binding.editTextDueDate.text.toString() != date) {
                binding.editTextDueDate.setText(date)
            }
        }

        // Observe title error
        viewModel.titleError.observe(this) { error ->
            binding.textInputLayoutTitle.error = error
        }

        // Observe description error
        viewModel.descriptionError.observe(this) { error ->
            binding.textInputLayoutDescription.error = error
        }

        // Observe loading state
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.buttonSave.isEnabled = !isLoading
        }

        // Observe edit mode
        viewModel.isEditMode.observe(this) { isEditMode ->
            supportActionBar?.title = if (isEditMode) "Edit Task" else "Add Task"
            binding.buttonSave.text = if (isEditMode) "Update" else "Save"
        }

        // Observe save success
        viewModel.saveSuccessEvent.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            finish()
        }

        // Observe save error
        viewModel.saveErrorEvent.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}