package com.marchcode.evaluateuitest.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.marchcode.evaluateuitest.R;
import com.marchcode.evaluateuitest.data.model.Task;
import com.marchcode.evaluateuitest.data.model.TaskPriority;
import com.marchcode.evaluateuitest.databinding.ActivityTaskDetailBinding;
import com.marchcode.evaluateuitest.ui.form.TaskFormActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Detail screen for displaying individual task information.
 * Written in Java to demonstrate mixed Java/Kotlin codebase.
 * Allows viewing task details, toggling favorite status, editing, and deleting.
 */
@AndroidEntryPoint
public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extra_task_id";

    private ActivityTaskDetailBinding binding;
    private TaskDetailViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        androidx.activity.EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TaskDetailViewModel.class);

        setupWindowInsets();
        setupToolbar();
        setupClickListeners();
        observeViewModel();

        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        if (taskId != null) {
            viewModel.loadTask(taskId);
        } else {
            Toast.makeText(this, "Error: Task ID not provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Sets up window insets for edge-to-edge display
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            binding.appBarLayoutDetail.setPadding(
                binding.appBarLayoutDetail.getPaddingLeft(),
                insets.top,
                binding.appBarLayoutDetail.getPaddingRight(),
                binding.appBarLayoutDetail.getPaddingBottom()
            );

            binding.scrollViewDetail.setPadding(
                binding.scrollViewDetail.getPaddingLeft(),
                binding.scrollViewDetail.getPaddingTop(),
                binding.scrollViewDetail.getPaddingRight(),
                insets.bottom
            );

            return windowInsets;
        });
    }

    /**
     * Sets up toolbar with back navigation
     */
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Task Details");
        }
    }

    /**
     * Sets up click listeners for buttons
     */
    private void setupClickListeners() {
        binding.buttonToggleFavorite.setOnClickListener(v -> viewModel.onToggleFavorite());
        binding.buttonEditTask.setOnClickListener(v -> viewModel.onEditClicked());
        binding.buttonDeleteTask.setOnClickListener(v -> showDeleteConfirmation());
    }

    /**
     * Observes ViewModel LiveData
     */
    private void observeViewModel() {
        viewModel.getTask().observe(this, this::displayTask);

        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.layoutContent.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                binding.layoutError.setVisibility(View.VISIBLE);
                binding.layoutContent.setVisibility(View.GONE);
                binding.textErrorMessage.setText(error);
            }
        });

        viewModel.getSnackbarMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });

        viewModel.getNavigationEvent().observe(this, event -> {
            if (event != null) {
                if (event instanceof TaskDetailViewModel.TaskDetailEvent.NavigateToEdit) {
                    String taskId = ((TaskDetailViewModel.TaskDetailEvent.NavigateToEdit) event).getTaskId();
                    navigateToEditTask(taskId);
                } else if (event instanceof TaskDetailViewModel.TaskDetailEvent.NavigateBack) {
                    String message = ((TaskDetailViewModel.TaskDetailEvent.NavigateBack) event).getMessage();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * Displays task information in the UI
     */
    private void displayTask(Task task) {
        if (task == null) return;

        binding.textDetailTitle.setText(task.getTitle());
        binding.textDetailDescription.setText(task.getDescription());

        binding.textDetailCategory.setText("Category: " + task.getCategory().getDisplayName());
        binding.textDetailPriority.setText("Priority: " + task.getPriority().getDisplayName());

        int priorityColor;
        if (task.getPriority() == TaskPriority.HIGH) {
            priorityColor = ContextCompat.getColor(this, R.color.priority_high);
        } else if (task.getPriority() == TaskPriority.MEDIUM) {
            priorityColor = ContextCompat.getColor(this, R.color.priority_medium);
        } else {
            priorityColor = ContextCompat.getColor(this, R.color.priority_low);
        }
        binding.textDetailPriority.setTextColor(priorityColor);

        if (task.getDueDate() != null && !task.getDueDate().isEmpty()) {
            binding.textDetailDueDate.setText("Due: " + task.getDueDate());
            binding.textDetailDueDate.setVisibility(View.VISIBLE);
        } else {
            binding.textDetailDueDate.setVisibility(View.GONE);
        }

        binding.textDetailStatus.setText(
            task.isCompleted() ? "Status: Completed" : "Status: Pending"
        );
        binding.textDetailStatus.setTextColor(
            ContextCompat.getColor(
                this,
                task.isCompleted() ? R.color.status_complete : R.color.status_pending
            )
        );

        int favoriteIcon = task.isFavorite()
            ? R.drawable.ic_favorite_filled
            : R.drawable.ic_favorite_outline;
        binding.buttonToggleFavorite.setImageResource(favoriteIcon);
        binding.buttonToggleFavorite.setContentDescription(
            task.isFavorite() ? "Remove from favorites" : "Add to favorites"
        );
    }

    /**
     * Shows delete confirmation dialog
     */
    private void showDeleteConfirmation() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete", (dialog, which) -> viewModel.onDeleteClicked())
            .setNegativeButton("Cancel", null)
            .show();
    }

    /**
     * Navigates to edit task screen
     */
    private void navigateToEditTask(String taskId) {
        Intent intent = new Intent(this, TaskFormActivity.class);
        intent.putExtra(TaskFormActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        if (taskId != null) {
            viewModel.loadTask(taskId);
        }
    }
}
