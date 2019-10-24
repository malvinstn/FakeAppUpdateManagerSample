package com.malvinstn.updates.di

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.tasks.TaskExecutors
import com.malvinstn.updates.UpdatesApplication
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Named

@Module
object UpdatesApplicationModule {
    @Provides
    fun providesInAppUpdateManager(application: UpdatesApplication): AppUpdateManager {
        return AppUpdateManagerFactory.create(application)
    }

    @Provides
    fun providesPlayServiceExecutor(): Executor {
        return TaskExecutors.MAIN_THREAD
    }
}
