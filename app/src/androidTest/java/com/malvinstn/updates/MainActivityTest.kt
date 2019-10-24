package com.malvinstn.updates

import androidx.appcompat.widget.AppCompatButton
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.malvinstn.updates.di.TestInjector
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    lateinit var fakeAppUpdateManager: FakeAppUpdateManager

    @Before
    fun setup() {
        val component = TestInjector.inject()
        fakeAppUpdateManager = component.fakeAppUpdateManager()
    }

    @Test
    fun testFlexibleUpdate_Completes() {
        // Setup flexible update.
        fakeAppUpdateManager.partiallyAllowedUpdateType = AppUpdateType.FLEXIBLE
        fakeAppUpdateManager.setUpdateAvailable(2)

        ActivityScenario.launch(MainActivity::class.java)

        // Validate that flexible update is prompted to the user.
        assertTrue(fakeAppUpdateManager.isConfirmationDialogVisible)

        // Simulate user's and download behavior.
        fakeAppUpdateManager.userAcceptsUpdate()

        fakeAppUpdateManager.downloadStarts()

        fakeAppUpdateManager.downloadCompletes()

        // Perform a click on the Snackbar to complete the update process.
        onView(
            allOf(
                isDescendantOfA(instanceOf(Snackbar.SnackbarLayout::class.java)),
                instanceOf(AppCompatButton::class.java)
            )
        ).perform(ViewActions.click())

        // Validate that update is completed and app is restarted.
        assertTrue(fakeAppUpdateManager.isInstallSplashScreenVisible)

        fakeAppUpdateManager.installCompletes()
    }

    @Test
    fun testImmediateUpdate_Completes() {
        // Setup immediate update.
        fakeAppUpdateManager.partiallyAllowedUpdateType = AppUpdateType.IMMEDIATE
        fakeAppUpdateManager.setUpdateAvailable(2)

        ActivityScenario.launch(MainActivity::class.java)

        // Validate that immediate update is prompted to the user.
        assertTrue(fakeAppUpdateManager.isImmediateFlowVisible)

        // Simulate user's and download behavior.
        fakeAppUpdateManager.userAcceptsUpdate()

        fakeAppUpdateManager.downloadStarts()

        fakeAppUpdateManager.downloadCompletes()

        // Validate that update is completed and app is restarted.
        assertTrue(fakeAppUpdateManager.isInstallSplashScreenVisible)
    }

    @Test
    fun testFlexibleUpdate_DownloadFails() {
        // Setup flexible update.
        fakeAppUpdateManager.partiallyAllowedUpdateType = AppUpdateType.FLEXIBLE
        fakeAppUpdateManager.setUpdateAvailable(2)

        ActivityScenario.launch(MainActivity::class.java)

        // Validate that flexible update is prompted to the user.
        assertTrue(fakeAppUpdateManager.isConfirmationDialogVisible)

        // Simulate user's and download behavior.
        fakeAppUpdateManager.userAcceptsUpdate()

        fakeAppUpdateManager.downloadStarts()

        fakeAppUpdateManager.downloadFails()

        // Perform a click on the Snackbar to retry the update process.
        onView(
            allOf(
                isDescendantOfA(instanceOf(Snackbar.SnackbarLayout::class.java)),
                instanceOf(AppCompatButton::class.java)
            )
        ).perform(ViewActions.click())

        // Validate that update is not completed and app is not restarted.
        assertFalse(fakeAppUpdateManager.isInstallSplashScreenVisible)

        // Validate that Flexible update is prompted to the user again.
        assertTrue(fakeAppUpdateManager.isConfirmationDialogVisible)
    }
}
