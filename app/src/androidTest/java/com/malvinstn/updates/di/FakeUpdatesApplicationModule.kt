package com.malvinstn.updates.di

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.malvinstn.updates.UpdatesApplication
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import javax.inject.Named
import javax.inject.Singleton

@Module
object FakeUpdatesApplicationModule {

    @Singleton
    @Provides
    fun providesFakeInAppUpdateManager(application: UpdatesApplication): FakeAppUpdateManager {
        return FakeAppUpdateManager(application)
    }

    @Provides
    fun providesInAppUpdateManager(fakeAppUpdateManager: FakeAppUpdateManager): AppUpdateManager {
        return fakeAppUpdateManager
    }

    @Provides
    fun providesPlayServiceExecutor(): Executor {
        return Executor { it.run() }
    }
}
