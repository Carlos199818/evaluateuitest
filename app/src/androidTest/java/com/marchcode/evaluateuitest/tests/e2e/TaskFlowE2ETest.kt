package com.marchcode.evaluateuitest.tests.e2e

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marchcode.evaluateuitest.base.BaseHiltActivityTest
import com.marchcode.evaluateuitest.data.source.FakeTaskDataSource
import com.marchcode.evaluateuitest.pages.detail.TaskDetailPage
import com.marchcode.evaluateuitest.pages.form.TaskFormPage
import com.marchcode.evaluateuitest.pages.list.TaskListPage
import com.marchcode.evaluateuitest.ui.form.TaskFormActivity
import com.marchcode.evaluateuitest.ui.list.TaskListActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

private const val SCHEDULE_DENTIST_TITLE = "Schedule Dentist Appointment"
private const val UPDATE_RESUME_TITLE = "Update Resume"
private const val STATUS_PENDING = "Status: Pending"
private const val STATUS_COMPLETED = "Status: Completed"
private const val CATEGORY_OTHER = "Other"
private const val CATEGORY_OTHER_DETAIL = "Category: Other"
private const val PRIORITY_LOW = "Low"
private const val PRIORITY_LOW_DETAIL = "Priority: Low"
private const val RESUME_SEARCH_QUERY = "resume"

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskFlowE2ETest :
    BaseHiltActivityTest<TaskListActivity>(TaskListActivity::class.java) {

    @Inject
    lateinit var fakeTaskDataSource: FakeTaskDataSource

    private val taskListPage = TaskListPage()
    private val taskFormPage = TaskFormPage()
    private val taskDetailPage = TaskDetailPage()

    @Before
    fun resetSeedData() {
        fakeTaskDataSource.resetToSeedData()
        activityRule.scenario.onActivity { }
    }

    @Test
    fun create_task_then_view_detail() {
        val uniqueSuffix = System.currentTimeMillis()
        val title = "E2E Task $uniqueSuffix"
        val description = "End to end detail validation description $uniqueSuffix"
        val dueDate = "2030-12-31"

        taskListPage
            .verifyTaskListVisible()
            .verifyAddTaskButtonDisplayed()
            .tapAddTask()

        intended(hasComponent(TaskFormActivity::class.java.name))

        taskFormPage
            .verifyScreenDisplayed()
            .enterTitle(title)
            .enterDescription(description)
            .selectCategory(CATEGORY_OTHER)
            .selectPriority(PRIORITY_LOW)
            .selectDueDate(2030, 12, 31)
            .tapSave()

        taskListPage
            .verifyTaskListVisible()
            .verifyTaskVisible(title)
            .openTaskDetails(title)

        taskDetailPage
            .verifyScreenDisplayed()
            .verifyTitleDisplayed(title)
            .verifyDescriptionDisplayed(description)
            .verifyCategoryDisplayed(CATEGORY_OTHER_DETAIL)
            .verifyPriorityDisplayed(PRIORITY_LOW_DETAIL)
            .verifyDueDateDisplayed("Due: $dueDate")
            .verifyStatusDisplayed(STATUS_PENDING)
    }

    @Test
    fun edit_existing_task_and_preserve_state() {
        val uniqueSuffix = System.currentTimeMillis()
        val updatedTitle = "Updated Dentist Appointment $uniqueSuffix"
        val updatedDescription = "Updated dentist appointment description $uniqueSuffix"

        taskListPage
            .verifyTaskListVisible()
            .verifyTaskVisible(SCHEDULE_DENTIST_TITLE)
            .openTaskDetails(SCHEDULE_DENTIST_TITLE)

        taskDetailPage
            .verifyScreenDisplayed()
            .verifyTitleDisplayed(SCHEDULE_DENTIST_TITLE)
            .verifyStatusDisplayed(STATUS_COMPLETED)
            .tapEditTask()

        intended(hasComponent(TaskFormActivity::class.java.name))

        taskFormPage
            .verifyScreenDisplayed()
            .enterTitle(updatedTitle)
            .enterDescription(updatedDescription)
            .tapSave()

        taskDetailPage
            .verifyScreenDisplayed()
            .verifyTitleDisplayed(updatedTitle)
            .verifyDescriptionDisplayed(updatedDescription)
            .verifyStatusDisplayed(STATUS_COMPLETED)
    }

    @Test
    fun delete_task_end_to_end() {
        taskListPage
            .verifyTaskListVisible()
            .enterSearchQuery(RESUME_SEARCH_QUERY)
            .verifyTaskVisible(UPDATE_RESUME_TITLE)
            .openTaskDetails(UPDATE_RESUME_TITLE)

        taskDetailPage
            .verifyScreenDisplayed()
            .verifyTitleDisplayed(UPDATE_RESUME_TITLE)
            .tapDeleteTask()
            .confirmDelete()

        taskListPage
            .verifyClearSearchButtonDisplayed()
            .tapClearSearch()
            .enterSearchQuery(RESUME_SEARCH_QUERY)
            .verifyTaskNotDisplayed(UPDATE_RESUME_TITLE)
            .verifyEmptyLayoutVisible()
    }
}
