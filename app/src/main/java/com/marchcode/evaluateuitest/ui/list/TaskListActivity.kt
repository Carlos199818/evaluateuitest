package com.marchcode.evaluateuitest.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.marchcode.evaluateuitest.data.model.UiState
import com.marchcode.evaluateuitest.databinding.ActivityTaskListBinding
import com.marchcode.evaluateuitest.ui.detail.TaskDetailActivity
import com.marchcode.evaluateuitest.ui.form.TaskFormActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Main screen displaying the list of tasks.
 * Implements search, filtering, and navigation to detail/form screens.
 * Written in Kotlin with MVVM architecture and Hilt DI.
 */
@AndroidEntryPoint
class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupFab()
        observeViewModel()
    }

    /**
     * Sets up window insets for edge-to-edge display
     */
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply top inset to AppBarLayout
            binding.appBarLayout.updatePadding(top = insets.top)

            // Apply bottom inset to FAB (16dp is the standard FAB margin)
            val fabParams = binding.fabAddTask.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            fabParams.bottomMargin = insets.bottom + 16.dpToPx()
            binding.fabAddTask.layoutParams = fabParams

            // Apply bottom inset to RecyclerView (already has 80dp padding, add inset on top)
            binding.recyclerViewTasks.updatePadding(bottom = 80.dpToPx() + insets.bottom)

            windowInsets
        }
    }

    /**
     * Convert dp to pixels
     */
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    /**
     * Sets up the toolbar with title
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Task Manager"
    }

    /**
     * Sets up RecyclerView with adapter
     */
    private fun setupRecyclerView() {
        adapter = TaskListAdapter(
            onTaskClick = { task -> viewModel.onTaskClicked(task) },
            onToggleComplete = { taskId -> viewModel.onToggleComplete(taskId) }
        )

        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(this@TaskListActivity)
            adapter = this@TaskListActivity.adapter
            setHasFixedSize(true)
        }
    }

    /**
     * Sets up search view for filtering tasks
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText ?: "")
                return true
            }
        })

        binding.buttonClearSearch.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
            viewModel.clearSearch()
        }
    }

    /**
     * Sets up FAB for adding new tasks
     */
    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            viewModel.onAddTaskClicked()
        }
    }

    /**
     * Observes ViewModel state and updates UI
     */
    private fun observeViewModel() {
        // Observe UI state
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleUiState(state)
                }
            }
        }

        // Observe search query
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchQuery.collect { query ->
                    binding.buttonClearSearch.isVisible = query.isNotEmpty()
                }
            }
        }

        // Observe navigation events
        viewModel.navigationEvent.observe(this) { event ->
            handleNavigationEvent(event)
        }

        // Observe snackbar messages
        viewModel.snackbarMessage.observe(this) { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Handles different UI states
     */
    private fun handleUiState(state: UiState<List<com.marchcode.evaluateuitest.data.model.Task>>) {
        when (state) {
            is UiState.Loading -> {
                binding.progressBar.isVisible = true
                binding.recyclerViewTasks.isVisible = false
                binding.layoutEmpty.isVisible = false
                binding.layoutError.isVisible = false
            }
            is UiState.Success -> {
                binding.progressBar.isVisible = false
                binding.recyclerViewTasks.isVisible = true
                binding.layoutEmpty.isVisible = false
                binding.layoutError.isVisible = false
                adapter.submitList(state.data)
            }
            is UiState.Empty -> {
                binding.progressBar.isVisible = false
                binding.recyclerViewTasks.isVisible = false
                binding.layoutEmpty.isVisible = true
                binding.layoutError.isVisible = false
            }
            is UiState.Error -> {
                binding.progressBar.isVisible = false
                binding.recyclerViewTasks.isVisible = false
                binding.layoutEmpty.isVisible = false
                binding.layoutError.isVisible = true
                binding.textErrorMessage.text = state.message
                binding.buttonRetry.setOnClickListener {
                    viewModel.retry()
                }
            }
        }
    }

    /**
     * Handles navigation events
     */
    private fun handleNavigationEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.NavigateToDetail -> {
                val intent = Intent(this, TaskDetailActivity::class.java).apply {
                    putExtra(TaskDetailActivity.EXTRA_TASK_ID, event.taskId)
                }
                startActivity(intent)
            }
            is TaskListEvent.NavigateToAddTask -> {
                val intent = Intent(this, TaskFormActivity::class.java)
                startActivity(intent)
            }
        }
    }
}