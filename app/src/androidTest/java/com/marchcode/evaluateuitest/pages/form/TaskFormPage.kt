package com.marchcode.evaluateuitest.pages.form

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.marchcode.evaluateuitest.utils.assertTextVisibleOnDevice
import com.marchcode.evaluateuitest.utils.hasNoTextInputLayoutError
import com.marchcode.evaluateuitest.utils.hasTextInputLayoutError
import com.marchcode.evaluateuitest.R
import com.marchcode.evaluateuitest.utils.nestedScrollToAndClick
import com.marchcode.evaluateuitest.utils.nestedScrollTo
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.`is`

class TaskFormPage {

    fun verifyScreenDisplayed(): TaskFormPage {
        onView(withId(R.id.edit_text_title))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifyTitleFieldDisplayed(): TaskFormPage {
        onView(withId(R.id.edit_text_title))
            .check(matches(isDisplayed()))
        return this
    }

    fun enterTitle(title: String): TaskFormPage {
        onView(withId(R.id.edit_text_title))
            .perform(replaceText(title), closeSoftKeyboard())
        return this
    }

    fun verifyDescriptionFieldDisplayed(): TaskFormPage {
        onView(withId(R.id.edit_text_description))
            .perform(nestedScrollTo())
            .check(matches(isDisplayed()))
        return this
    }

    fun enterDescription(description: String): TaskFormPage {
        onView(withId(R.id.edit_text_description))
            .perform(nestedScrollTo(), replaceText(description), closeSoftKeyboard())
        return this
    }

    fun verifyCategorySpinnerDisplayed(): TaskFormPage {
        onView(withId(R.id.spinner_category))
            .perform(nestedScrollTo())
            .check(matches(isDisplayed()))
        return this
    }

    fun selectCategory(category: String): TaskFormPage {
        onView(withId(R.id.spinner_category))
            .perform(nestedScrollTo(), click())
        onData(`is`(category))
            .perform(click())
        return this
    }

    fun verifyPrioritySpinnerDisplayed(): TaskFormPage {
        onView(withId(R.id.spinner_priority))
            .perform(nestedScrollTo())
            .check(matches(isDisplayed()))
        return this
    }

    fun selectPriority(priority: String): TaskFormPage {
        onView(withId(R.id.spinner_priority))
            .perform(nestedScrollTo(), click())
        onData(`is`(priority))
            .perform(click())
        return this
    }

    fun verifyDueDateFieldDisplayed(): TaskFormPage {
        onView(withId(R.id.edit_text_due_date))
            .perform(nestedScrollTo())
            .check(matches(isDisplayed()))
        return this
    }

    fun selectDueDate(year: Int, month: Int, day: Int): TaskFormPage {
        onView(withId(R.id.edit_text_due_date))
            .perform(nestedScrollTo(), click())
        onView(withClassName(containsString("DatePicker")))
            .perform(PickerActions.setDate(year, month, day))
        onView(withId(android.R.id.button1))
            .perform(click())
        return this
    }

    fun verifySaveButtonDisplayed(): TaskFormPage {
        onView(withId(R.id.button_save))
            .perform(nestedScrollTo())
            .check(matches(isDisplayed()))
        return this
    }

    fun tapSave(): TaskFormPage {
        onView(withId(R.id.button_save))
            .perform(nestedScrollToAndClick())
        return this
    }

    fun verifyTitleError(error: String): TaskFormPage {
        onView(withId(R.id.text_input_layout_title))
            .check(matches(hasTextInputLayoutError(error)))
        return this
    }

    fun verifyDescriptionError(error: String): TaskFormPage {
        onView(withId(R.id.text_input_layout_description))
            .perform(nestedScrollTo())
            .check(matches(hasTextInputLayoutError(error)))
        return this
    }

    fun verifyTitleErrorCleared(): TaskFormPage {
        onView(withId(R.id.text_input_layout_title))
            .check(matches(hasNoTextInputLayoutError()))
        return this
    }

    fun verifyDescriptionErrorCleared(): TaskFormPage {
        onView(withId(R.id.text_input_layout_description))
            .perform(nestedScrollTo())
            .check(matches(hasNoTextInputLayoutError()))
        return this
    }

    fun verifyToolbarTitleDisplayed(title: String): TaskFormPage {
        onView(withText(title))
            .check(matches(isDisplayed()))
        return this
    }

    fun verifySaveButtonText(text: String): TaskFormPage {
        onView(withId(R.id.button_save))
            .perform(nestedScrollTo())
            .check(matches(withText(text)))
        return this
    }

    fun verifyTitleValue(title: String): TaskFormPage {
        onView(withId(R.id.edit_text_title))
            .check(matches(withText(title)))
        return this
    }

    fun verifyDescriptionValue(description: String): TaskFormPage {
        onView(withId(R.id.edit_text_description))
            .perform(nestedScrollTo())
            .check(matches(withText(description)))
        return this
    }

    fun verifyCategorySelected(category: String): TaskFormPage {
        onView(withId(R.id.spinner_category))
            .perform(nestedScrollTo())
            .check(matches(withSpinnerText(containsString(category))))
        return this
    }

    fun verifyPrioritySelected(priority: String): TaskFormPage {
        onView(withId(R.id.spinner_priority))
            .perform(nestedScrollTo())
            .check(matches(withSpinnerText(containsString(priority))))
        return this
    }

    fun verifyDueDateValue(date: String): TaskFormPage {
        onView(withId(R.id.edit_text_due_date))
            .perform(nestedScrollTo())
            .check(matches(withText(date)))
        return this
    }

    fun verifySuccessMessageDisplayed(message: String): TaskFormPage {
        assertTextVisibleOnDevice(message)
        return this
    }
}
