package com.marchcode.evaluateuitest.tests.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marchcode.evaluateuitest.R
import com.marchcode.evaluateuitest.base.BaseHiltActivityTest
import com.marchcode.evaluateuitest.pages.list.TaskListPage
import com.marchcode.evaluateuitest.ui.detail.TaskDetailActivity
import com.marchcode.evaluateuitest.ui.list.TaskListActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

private const val SEARCH_QUERY = "timeout"
private const val MATCHING_TASK_TITLE = "Fix Production Bug"
private const val KNOWN_SEED_TASK_TITLE = "Complete Project Proposal"
private const val SEARCH_QUERY_RESUME = "resume"
private const val FILTERED_TASK_TITLE = "Update Resume"

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskListActivityTest :
    BaseHiltActivityTest<TaskListActivity>(TaskListActivity::class.java) {

    private val taskListPage = TaskListPage()

    @Test
    fun taskList_isDisplayed_whenAppLaunches() {
        taskListPage.verifyTaskListVisible()
    }

    @Test
    fun addTaskButton_isDisplayed_whenTaskListLaunches() {
        taskListPage
            .verifyTaskListVisible()
            .verifyAddTaskButtonDisplayed()
    }

    @Test
    fun list_loads_seed_data_on_launch() {
        taskListPage
            .verifyTaskListVisible()
            .verifyProgressBarNotVisible()
            .verifyErrorLayoutNotVisible()
            .verifyEmptyLayoutNotVisible()
            .verifySeedTaskVisible(KNOWN_SEED_TASK_TITLE)
    }

    @Test
    fun list_opens_detail_for_selected_task() {
        taskListPage
            .verifyTaskListVisible()
            .verifySeedTaskVisible(KNOWN_SEED_TASK_TITLE)
            .openTaskDetails(KNOWN_SEED_TASK_TITLE)

        intended(hasComponent((TaskDetailActivity::class.java.name)))

        onView(withId(R.id.text_detail_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(KNOWN_SEED_TASK_TITLE)))
    }

    @Test
    fun list_search_filters_by_title_or_description() {
        taskListPage
            .verifyTaskListVisible()
            .enterSearchQuery(SEARCH_QUERY)
            .verifyTaskListVisible()
            .verifyTaskVisible(MATCHING_TASK_TITLE)
            .verifyTaskNotDisplayed(KNOWN_SEED_TASK_TITLE)
            .verifyErrorLayoutNotVisible()
            .verifyEmptyLayoutNotVisible()
    }

    @Test
    fun list_clear_search_restores_results() {
        taskListPage
            .verifyTaskListVisible()
            .enterSearchQuery(SEARCH_QUERY_RESUME)
            .verifyClearSearchButtonDisplayed()
            .verifyTaskVisible(FILTERED_TASK_TITLE)
            .tapClearSearch()
            .verifySearchQueryEmpty()
            .verifyClearSearchButtonNotVisible()
            .verifyTaskListVisible()
            .verifyTaskVisible(KNOWN_SEED_TASK_TITLE)
            .verifyErrorLayoutNotVisible()
            .verifyEmptyLayoutNotVisible()
    }

    @Test
    fun list_toggle_complete_updates_row() {
        taskListPage
            .verifyTaskListVisible()
            .verifyTaskVisible(KNOWN_SEED_TASK_TITLE)
            .verifyTaskCheckboxNotChecked(KNOWN_SEED_TASK_TITLE)
            .toggleTaskComplete(KNOWN_SEED_TASK_TITLE)
            .verifyTaskCheckboxChecked(KNOWN_SEED_TASK_TITLE)
            .verifyTaskTitleStruckThrough(KNOWN_SEED_TASK_TITLE)
    }
}
