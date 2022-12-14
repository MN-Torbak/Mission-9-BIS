package com.openclassrooms.firebaseREM

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception
import kotlin.Throws

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().context
        Assert.assertEquals("com.openclassrooms.go4lunch", appContext.packageName)
    }

    @Test
    @Throws(Exception::class)
    fun checkWifi() {
        val appContext = InstrumentationRegistry.getInstrumentation().context
        Assert.assertEquals(Utils.checkForInternet(appContext), true)
    }
}