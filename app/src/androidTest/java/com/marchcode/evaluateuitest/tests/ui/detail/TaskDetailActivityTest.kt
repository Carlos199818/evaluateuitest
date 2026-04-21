package com.marchcode.evaluateuitest.tests.ui.detail

import android.content.Intent
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marchcode.evaluateuitest.base.BaseHiltActivityTest
import com.marchcode.evaluateuitest.pages.detail.TaskDetailPage
import com.marchcode.evaluateuitest.pages.form.TaskFormPage
import com.marchcode.evaluateuitest.pages.list.TaskListPage
import com.marchcode.evaluateuitest.ui.detail.TaskDetailActivity
import com.marchcode.evaluateuitest.ui.form.TaskFormActivity
import com.marchcode.evaluateuitest.ui.list.TaskListActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

private const val TASK_DETAILS_TOOLBAR_TITLE = "Task Details"
private const val EDIT_TASK_TOOLBAR_TITLE = "Edit Task"
private const val UPDATE_BUTTON_TEXT = "Update"
private const val COMPLETE_PROJECT_PROPOSAL_ID = "1"
private const val COMPLETE_PROJECT_PROPOSAL_TITLE = "Complete Project Proposal"
private const val COMPLETE_PROJECT_PROPOSAL_DESCRIPTION =
    "Finish the Q2 project proposal document and submit to management for review"
private const val BUY_GROCERIES_ID = "2"
private const val BUY_GROCERIES_TITLE = "Buy Groceries"
private const val UPDATE_RESUME_ID = "6"
private const val UPDATE_RESUME_TITLE = "Update Resume"
private const val CATEGORY_WORK = "Category: Work"
private const val PRIORITY_HIGH = "Priority: High"
private const val STATUS_PENDING = "Status: Pending"
private const val DUE_DATE_2026_04_15 = "Due: 2026-04-15"
private const val FAVORITE_ADD_DESCRIPTION = "Add to favorites"
private const val FAVORITE_REMOVE_DESCRIPTION = "Remove from favorites"
private const val EXISTING_TASK_CATEGORY = "Work"
private const val EXISTING_TASK_PRIORITY = "High"
private const val EXISTING_TASK_DUE_DATE = "2026-04-15"
private const val RESUME_SEARCH_QUERY = "resume"

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskDetailActivityTest :
    BaseHiltActivityTest<TaskListActivity>(TaskListActivity::class.java) {

    private val taskListPage = TaskListPage()
    private val taskDetailPage = TaskDetailPage()
    private val taskFormPage = TaskFormPage()

    @Test
    fun detail_shows_full_task_data() {
        openTaskDetail(COMPLETE_PROJECT_PROPOSAL_ID)

        taskDetailPage
            .verifyScreenDisplayed()
            .verifyToolbarTitleDisplayed(TASK_DETAILS_TOOLBAR_TITLE)
            .verifyTitleDisplayed(COMPLETE_PROJECT_PROPOSAL_TITLE)
            .verifyDescriptionDisplayed(COMPLETE_PROJECT_PROPOSAL_DESCRIPTION)
            .verifyCategoryDisplayed(CATEGORY_WORK)
            .verifyPriorityDisplayed(PRIORITY_HIGH)
            .verifyStatusDisplayed(STATUS_PENDING)
            .verifyDueDateDisplayed(DUE_DATE_2026_04_15)
    }

    @Test
    fun detail_toggle_favorite_updates_and_persists() {
        openTaskDetail(BUY_GROCERIES_ID)

        taskDetailPage
            .verifyTitleDisplayed(BUY_GROCERIES_TITLE)
            .verifyFavoriteButtonContentDescription(FAVORITE_ADD_DESCRIPTION)
            .tapToggleFavorite()

        pressBack()
        taskListPage
            .verifyTaskListVisible()
            .verifyTaskVisible(BUY_GROCERIES_TITLE)
            .openTaskDetails(BUY_GROCERIES_TITLE)

        taskDetailPage
            .verifyTitleDisplayed(BUY_GROCERIES_TITLE)
            .verifyFavoriteButtonContentDescription(FAVORITE_REMOVE_DESCRIPTION)
    }

    @Test
    fun detail_edit_navigates_to_prefilled_form() {
        openTaskDetail(COMPLETE_PROJECT_PROPOSAL_ID)

        taskDetailPage
            .verifyTitleDisplayed(COMPLETE_PROJECT_PROPOSAL_TITLE)
            .tapEditTask()

        intended(hasComponent(TaskFormActivity::class.java.name))

        taskFormPage
            .verifyScreenDisplayed()
            .verifyToolbarTitleDisplayed(EDIT_TASK_TOOLBAR_TITLE)
            .verifySaveButtonText(UPDATE_BUTTON_TEXT)
            .verifyTitleValue(COMPLETE_PROJECT_PROPOSAL_TITLE)
            .verifyDescriptionValue(COMPLETE_PROJECT_PROPOSAL_DESCRIPTION)
            .verifyCategorySelected(EXISTING_TASK_CATEGORY)
            .verifyPrioritySelected(EXISTING_TASK_PRIORITY)
            .verifyDueDateValue(EXISTING_TASK_DUE_DATE)
    }

    @Test
    fun detail_delete_confirm_removes_task() {
        openTaskDetail(UPDATE_RESUME_ID)

        taskDetailPage
            .verifyTitleDisplayed(UPDATE_RESUME_TITLE)
            .tapDeleteTask()
            .confirmDelete()

        taskListPage
            .verifyTaskListVisible()
            .enterSearchQuery(RESUME_SEARCH_QUERY)
            .verifyTaskNotDisplayed(UPDATE_RESUME_TITLE)
    }

    private fun openTaskDetail(taskId: String) {
        activityRule.scenario.onActivity { activity ->
            val intent = Intent(activity, TaskDetailActivity::class.java).apply {
                putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
            }
            activity.startActivity(intent)
        }

        taskDetailPage.verifyScreenDisplayed()
    }
}
