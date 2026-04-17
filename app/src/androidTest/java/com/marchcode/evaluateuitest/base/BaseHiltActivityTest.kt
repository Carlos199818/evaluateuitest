package com.marchcode.evaluateuitest.base

import androidx.test.espresso.intent.Intents
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
abstract class BaseHiltActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

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
