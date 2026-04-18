package com.marchcode.evaluateuitest.pages.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.marchcode.evaluateuitest.R

class TaskListPage {

    fun verifyScreenDisplayed(): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyAddTaskButtonDisplayed(): TaskListPage {
        onView(withId(R.id.fab_add_task))
            .check(matches(isDisplayed()))
        return this
    }
}