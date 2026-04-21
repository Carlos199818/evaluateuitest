package com.marchcode.evaluateuitest.utils

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

fun assertTextVisibleOnDevice(text: String, timeoutMs: Long = 5_000L) {
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    check(device.wait(Until.hasObject(By.text(text)), timeoutMs)) {
        "Text '$text' was not visible on device within ${timeoutMs}ms"
    }
}
