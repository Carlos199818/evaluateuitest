package com.marchcode.evaluateuitest.tests.ui.form

import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marchcode.evaluateuitest.base.BaseHiltActivityTest
import com.marchcode.evaluateuitest.pages.form.TaskFormPage
import com.marchcode.evaluateuitest.pages.list.TaskListPage
import com.marchcode.evaluateuitest.ui.form.TaskFormActivity
import com.marchcode.evaluateuitest.ui.list.TaskListActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

private const val ADD_TASK_TOOLBAR_TITLE = "Add Task"
private const val EDIT_TASK_TOOLBAR_TITLE = "Edit Task"
private const val UPDATE_BUTTON_TEXT = "Update"
private const val TITLE_REQUIRED_ERROR = "Title is required"
private const val DESCRIPTION_REQUIRED_ERROR = "Description is required"
private const val TITLE_MIN_LENGTH_ERROR = "Title must be at least 3 characters"
private const val DESCRIPTION_MIN_LENGTH_ERROR = "Description must be at least 10 characters"
private const val VALID_TITLE = "Valid title"
private const val VALID_DESCRIPTION = "Valid description text"
private const val EXISTING_TASK_ID = "1"
private const val EXISTING_TASK_DESCRIPTION =
    "Finish the Q2 project proposal document and submit to management for review"
private const val EXISTING_TASK_CATEGORY = "Work"
private const val EXISTING_TASK_PRIORITY = "High"
private const val EXISTING_TASK_DUE_DATE = "2026-04-15"

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskFormActivityTest :
    BaseHiltActivityTest<TaskListActivity>(TaskListActivity::class.java) {

    private val taskListPage = TaskListPage()
    private val taskFormPage = TaskFormPage()

    @Test
    fun form_opens_from_fab_with_fields_visible() {
        taskListPage
            .verifyTaskListVisible()
            .verifyAddTaskButtonDisplayed()
            .tapAddTask()

        intended(hasComponent(TaskFormActivity::class.java.name))

        taskFormPage
            .verifyScreenDisplayed()
            .verifyTitleFieldDisplayed()
            .verifyDescriptionFieldDisplayed()
            .verifyCategorySpinnerDisplayed()
            .verifyPrioritySpinnerDisplayed()
            .verifyDueDateFieldDisplayed()
            .verifySaveButtonDisplayed()
            .verifyToolbarTitleDisplayed(ADD_TASK_TOOLBAR_TITLE)
    }

    @Test
    fun form_save_empty_shows_required_errors() {
        openAddTaskForm()

        taskFormPage
            .tapSave()
            .verifyTitleError(TITLE_REQUIRED_ERROR)
            .verifyDescriptionError(DESCRIPTION_REQUIRED_ERROR)
            .verifyScreenDisplayed()
            .verifySaveButtonDisplayed()
    }

    @Test
    fun form_errors_clear_when_input_is_fixed() {
        openAddTaskForm()

        taskFormPage
            .enterTitle("ab")
            .enterDescription("short")
            .tapSave()
            .verifyTitleError(TITLE_MIN_LENGTH_ERROR)
            .verifyDescriptionError(DESCRIPTION_MIN_LENGTH_ERROR)
            .enterTitle(VALID_TITLE)
            .verifyTitleErrorCleared()
            .enterDescription(VALID_DESCRIPTION)
            .verifyDescriptionErrorCleared()
    }

    @Test
    fun form_save_valid_task_with_category_priority() {
        val uniqueTitle = "New Task ${System.currentTimeMillis()}"

        openAddTaskForm()

        taskFormPage
            .enterTitle(uniqueTitle)
            .enterDescription("Valid description with enough length")
            .selectCategory(EXISTING_TASK_CATEGORY)
            .selectPriority(EXISTING_TASK_PRIORITY)
            .selectDueDate(2030, 12, 31)
            .tapSave()

        taskListPage
            .verifyTaskListVisible()
            .verifyTaskVisible(uniqueTitle)
    }

    @Test
    fun form_edit_mode_preloads_existing_task() {
        openEditTaskForm(EXISTING_TASK_ID)

        taskFormPage
            .verifyScreenDisplayed()
            .verifyToolbarTitleDisplayed(EDIT_TASK_TOOLBAR_TITLE)
            .verifySaveButtonText(UPDATE_BUTTON_TEXT)
            .verifyTitleValue("Complete Project Proposal")
            .verifyDescriptionValue(EXISTING_TASK_DESCRIPTION)
            .verifyCategorySelected(EXISTING_TASK_CATEGORY)
            .verifyPrioritySelected(EXISTING_TASK_PRIORITY)
            .verifyDueDateValue(EXISTING_TASK_DUE_DATE)
    }

    private fun openAddTaskForm() {
        taskListPage
            .verifyTaskListVisible()
            .verifyAddTaskButtonDisplayed()
            .tapAddTask()

        intended(hasComponent(TaskFormActivity::class.java.name))
        taskFormPage.verifyScreenDisplayed()
    }

    private fun openEditTaskForm(taskId: String) {
        activityRule.scenario.onActivity { activity ->
            val intent = Intent(activity, TaskFormActivity::class.java).apply {
                putExtra(TaskFormActivity.EXTRA_TASK_ID, taskId)
            }
            activity.startActivity(intent)
        }

        intended(hasComponent(TaskFormActivity::class.java.name))
        taskFormPage.verifyScreenDisplayed()
    }
}
