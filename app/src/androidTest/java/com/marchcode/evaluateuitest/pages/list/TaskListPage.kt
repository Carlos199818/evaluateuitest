package com.marchcode.evaluateuitest.pages.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import com.marchcode.evaluateuitest.R
import org.hamcrest.Matchers.allOf

class TaskListPage {

    fun verifyAddTaskButtonDisplayed(): TaskListPage {
        onView(withId(R.id.fab_add_task))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyTaskListVisible(): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyProgressBarNotVisible(): TaskListPage {
        onView(withId(R.id.progress_bar))
            .check(matches(withEffectiveVisibility(GONE)))
        return this
    }

    fun verifyErrorLayoutNotVisible(): TaskListPage {
        onView(withId(R.id.layout_error))
            .check(matches(withEffectiveVisibility(GONE)))
        return this
    }

    fun verifyEmptyLayoutNotVisible(): TaskListPage {
        onView(withId(R.id.layout_empty))
            .check(matches(withEffectiveVisibility(GONE)))
        return this
    }

    fun verifySeedTaskVisible(title: String): TaskListPage {
        onView(allOf(withId(R.id.text_task_title), withText(title)))
            .check(matches(isDisplayed()))
        return this
    }

    fun openTaskDetails(title: String): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(allOf(withId(R.id.text_task_title), withText(title))),
                    click()
                )
            )
        return this
    }
}