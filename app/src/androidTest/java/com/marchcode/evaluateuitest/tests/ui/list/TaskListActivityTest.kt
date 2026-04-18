package com.marchcode.evaluateuitest.tests.ui.list

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.marchcode.evaluateuitest.base.BaseHiltActivityTest
import com.marchcode.evaluateuitest.pages.list.TaskListPage
import com.marchcode.evaluateuitest.ui.list.TaskListActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TaskListActivityTest :
    BaseHiltActivityTest<TaskListActivity>(TaskListActivity::class.java) {

    private val taskListPage = TaskListPage()

    @Test
    fun taskList_isDisplayed_whenAppLaunches() {
        taskListPage.verifyScreenDisplayed()
    }

    @Test
    fun addTaskButton_isDisplayed_whenTaskListLaunches() {
        taskListPage
            .verifyScreenDisplayed()
            .verifyAddTaskButtonDisplayed()
    }
}