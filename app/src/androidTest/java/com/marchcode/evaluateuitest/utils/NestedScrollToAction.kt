package com.marchcode.evaluateuitest.utils

import android.graphics.Rect
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

fun nestedScrollTo(): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(
                withEffectiveVisibility(VISIBLE),
                isDescendantOfA(isAssignableFrom(NestedScrollView::class.java))
            )
        }

        override fun getDescription(): String {
            return "scroll to view inside NestedScrollView"
        }

        override fun perform(uiController: UiController, view: View) {
            val rect = Rect()
            view.getDrawingRect(rect)
            view.requestRectangleOnScreen(rect, true)
            uiController.loopMainThreadUntilIdle()
        }
    }
}

fun nestedScrollToAndClick(): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(
                withEffectiveVisibility(VISIBLE),
                isEnabled(),
                isDescendantOfA(isAssignableFrom(NestedScrollView::class.java))
            )
        }

        override fun getDescription(): String {
            return "scroll to view inside NestedScrollView and click it"
        }

        override fun perform(uiController: UiController, view: View) {
            val rect = Rect()
            view.getDrawingRect(rect)
            view.requestRectangleOnScreen(rect, true)
            uiController.loopMainThreadUntilIdle()
            view.performClick()
            uiController.loopMainThreadUntilIdle()
        }
    }
}
