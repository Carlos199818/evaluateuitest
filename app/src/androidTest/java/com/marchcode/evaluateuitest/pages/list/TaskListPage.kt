package com.marchcode.evaluateuitest.pages.list

import android.view.View
import androidx.appcompat.R as AppCompatR
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.marchcode.evaluateuitest.R
import com.marchcode.evaluateuitest.utils.hasStrikeThroughText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.StringDescription

class TaskListPage {

    fun verifyAddTaskButtonDisplayed(): TaskListPage {
        onView(withId(R.id.fab_add_task))
            .check(matches(isDisplayed()))
        return this
    }

    fun tapAddTask(): TaskListPage {
        onView(withId(R.id.fab_add_task))
            .perform(click())
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

    fun verifyEmptyLayoutVisible(): TaskListPage {
        onView(withId(R.id.layout_empty))
            .check(matches(withEffectiveVisibility(VISIBLE)))
        return this
    }

    fun enterSearchQuery(query: String): TaskListPage {
        onView(withId(AppCompatR.id.search_src_text))
            .perform(click(), replaceText(query), closeSoftKeyboard())
        return this
    }

    fun verifyClearSearchButtonDisplayed(): TaskListPage {
        onView(withId(R.id.button_clear_search))
            .check(matches(isDisplayed()))
        return this
    }

    fun tapClearSearch(): TaskListPage {
        onView(withId(R.id.button_clear_search))
            .perform(click())
        return this
    }

    fun verifyClearSearchButtonNotVisible(): TaskListPage {
        onView(withId(R.id.button_clear_search))
            .check(matches(withEffectiveVisibility(GONE)))
        return this
    }

    fun verifySearchQueryEmpty(): TaskListPage {
        onView(withId(AppCompatR.id.search_src_text))
            .check(matches(withText("")))
        return this
    }

    fun toggleTaskComplete(title: String): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    taskRowMatcher(title),
                    clickChildViewWithId(R.id.checkbox_task_complete)
                )
            )
        return this
    }

    fun verifyTaskCheckboxChecked(title: String): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    taskRowMatcher(title),
                    assertChildViewMatches(R.id.checkbox_task_complete, isChecked())
                )
            )
        return this
    }

    fun verifyTaskCheckboxNotChecked(title: String): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    taskRowMatcher(title),
                    assertChildViewMatches(R.id.checkbox_task_complete, isNotChecked())
                )
            )
        return this
    }

    fun verifyTaskTitleStruckThrough(title: String): TaskListPage {
        onView(withId(R.id.recycler_view_tasks))
            .perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    taskRowMatcher(title),
                    assertChildViewMatches(R.id.text_task_title, hasStrikeThroughText())
                )
            )
        return this
    }

    fun verifyTaskVisible(title: String): TaskListPage {
        scrollToTask(title)
        onView(taskTitleMatcher(title))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyTaskNotDisplayed(title: String): TaskListPage {
        onView(allOf(withId(R.id.text_task_title), withText(title)))
            .check(doesNotExist())
        return this
    }

    fun verifySeedTaskVisible(title: String): TaskListPage {
        return verifyTaskVisible(title)
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

    private fun scrollToTask(title: String) {
        onView(withId(R.id.recycler_view_tasks))
            .perform(scrollTo<RecyclerView.ViewHolder>(taskRowMatcher(title)))
    }

    private fun taskRowMatcher(title: String): Matcher<View> {
        return hasDescendant(taskTitleMatcher(title))
    }

    private fun taskTitleMatcher(title: String): Matcher<View> {
        return allOf(withId(R.id.text_task_title), withText(title))
    }

    private fun clickChildViewWithId(viewId: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)

            override fun getDescription(): String = "click child view with id $viewId"

            override fun perform(uiController: UiController, view: View) {
                view.findViewById<View>(viewId).performClick()
                uiController.loopMainThreadUntilIdle()
            }
        }
    }

    private fun assertChildViewMatches(viewId: Int, matcher: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)

            override fun getDescription(): String = "assert child view with id $viewId matches"

            override fun perform(uiController: UiController, view: View) {
                val child = view.findViewById<View>(viewId)
                    ?: throw AssertionError("No child view found with id $viewId")

                if (!matcher.matches(child)) {
                    val description = StringDescription()
                    matcher.describeTo(description)
                    throw AssertionError("Child view with id $viewId does not match: $description")
                }
            }
        }
    }
}
