package com.marchcode.evaluateuitest.pages.detail

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.marchcode.evaluateuitest.R
import com.marchcode.evaluateuitest.utils.nestedScrollTo
import com.marchcode.evaluateuitest.utils.nestedScrollToAndClick

class TaskDetailPage {

    fun verifyScreenDisplayed(): TaskDetailPage {
        onView(withId(R.id.text_detail_title))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyToolbarTitleDisplayed(title: String): TaskDetailPage {
        onView(withText(title))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyTitleDisplayed(title: String): TaskDetailPage {
        onView(withId(R.id.text_detail_title))
            .check(matches(withText(title)))
        return this
    }

    fun verifyDescriptionDisplayed(description: String): TaskDetailPage {
        onView(withId(R.id.text_detail_description))
            .check(matches(withText(description)))
        return this
    }

    fun verifyCategoryDisplayed(categoryText: String): TaskDetailPage {
        onView(withId(R.id.text_detail_category))
            .perform(nestedScrollTo())
            .check(matches(withText(categoryText)))
        return this
    }

    fun verifyPriorityDisplayed(priorityText: String): TaskDetailPage {
        onView(withId(R.id.text_detail_priority))
            .perform(nestedScrollTo())
            .check(matches(withText(priorityText)))
        return this
    }

    fun verifyStatusDisplayed(statusText: String): TaskDetailPage {
        onView(withId(R.id.text_detail_status))
            .perform(nestedScrollTo())
            .check(matches(withText(statusText)))
        return this
    }

    fun verifyDueDateDisplayed(dueDateText: String): TaskDetailPage {
        onView(withId(R.id.text_detail_due_date))
            .perform(nestedScrollTo())
            .check(matches(withText(dueDateText)))
        return this
    }

    fun verifyFavoriteButtonContentDescription(description: String): TaskDetailPage {
        onView(withId(R.id.button_toggle_favorite))
            .check(matches(withContentDescription(description)))
        return this
    }

    fun tapToggleFavorite(): TaskDetailPage {
        onView(withId(R.id.button_toggle_favorite))
            .perform(click())
        return this
    }

    fun tapEditTask(): TaskDetailPage {
        onView(withId(R.id.button_edit_task))
            .perform(nestedScrollToAndClick())
        return this
    }

    fun tapDeleteTask(): TaskDetailPage {
        onView(withId(R.id.button_delete_task))
            .perform(nestedScrollToAndClick())
        return this
    }

    fun confirmDelete(): TaskDetailPage {
        onView(withId(android.R.id.button1))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        return this
    }
}
