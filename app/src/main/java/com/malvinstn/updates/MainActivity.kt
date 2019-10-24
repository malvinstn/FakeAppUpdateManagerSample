package com.malvinstn.updates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.OnSuccessListener
import dagger.android.AndroidInjection
import java.util.concurrent.Executor
import javax.inject.Inject

private const val IMMEDIATE_UPDATE_REQUEST_CODE = 1867
private const val FLEXIBLE_UPDATE_REQUEST_CODE = 4244

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appUpdateManager: AppUpdateManager

    @Inject
    lateinit var playServiceExecutor: Executor

    private val listener = { state: InstallState ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()
        } else if (state.installStatus() == InstallStatus.FAILED) {
            popupSnackbarForRetryUpdate()
        }
        // Show module progress, log state, or install the update.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)

        appUpdateManager.registerListener(listener)

        checkInAppUpdate()
    }

    private fun checkInAppUpdate() {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener(playServiceExecutor, OnSuccessListener { appUpdateInfo ->
                when (appUpdateInfo.updateAvailability()) {
                    UpdateAvailability.UPDATE_AVAILABLE -> when {
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> startFlexibleUpdate(
                            appUpdateInfo
                        )
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> startImmediateUpdate(
                            appUpdateInfo
                        )
                        else -> {
                            // No update is allowed
                        }
                    }
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> startImmediateUpdate(
                        appUpdateInfo
                    )
                    else -> {
                        // No op
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener(playServiceExecutor, OnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
    }

    private fun startImmediateUpdate(appUpdateInfo: AppUpdateInfo?) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.IMMEDIATE,
            this,
            IMMEDIATE_UPDATE_REQUEST_CODE
        )
    }

    private fun startFlexibleUpdate(appUpdateInfo: AppUpdateInfo?) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.FLEXIBLE,
            this,
            FLEXIBLE_UPDATE_REQUEST_CODE
        )
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.main_text),
            "An update has just been downloaded.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RESTART") { appUpdateManager.completeUpdate() }
            show()
        }
    }

    private fun popupSnackbarForRetryUpdate() {
        Snackbar.make(
            findViewById(R.id.main_text),
            "Unable to download update.",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction("RETRY") { checkInAppUpdate() }
            show()
        }
    }
}
