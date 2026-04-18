package com.marchcode.evaluateuitest.base

import android.app.Activity
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseHiltActivityTest<T : Activity>(
    activityClass: Class<T>
) {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(activityClass)

    @Before
    open fun setUpBase() {
        hiltRule.inject()
        Intents.init()
    }

    @After
    open fun tearDownBase() {
        Intents.release()
    }
}