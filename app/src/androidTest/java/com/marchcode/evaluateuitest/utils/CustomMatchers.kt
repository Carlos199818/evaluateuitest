package com.marchcode.evaluateuitest.utils

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasStrikeThroughText(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("TextView with strike-through text")
        }

        override fun matchesSafely(view: View): Boolean {
            return view is TextView &&
                (view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) != 0
        }
    }
}

fun hasTextInputLayoutError(expectedError: String): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("TextInputLayout with error: $expectedError")
        }

        override fun matchesSafely(view: View): Boolean {
            return view is TextInputLayout && view.error?.toString() == expectedError
        }
    }
}

fun hasNoTextInputLayoutError(): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("TextInputLayout with no error")
        }

        override fun matchesSafely(view: View): Boolean {
            return view is TextInputLayout && view.error.isNullOrEmpty()
        }
    }
}
