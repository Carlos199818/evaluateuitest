package com.marchcode.evaluateuitest.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    @JvmField
    val countingIdlingResource = CountingIdlingResource("GLOBAL")

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}
