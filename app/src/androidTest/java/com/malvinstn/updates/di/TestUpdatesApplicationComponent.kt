package com.malvinstn.updates.di

import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import com.malvinstn.updates.UpdatesApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityModule::class,
        FakeUpdatesApplicationModule::class
    ]
)
interface TestUpdatesApplicationComponent : UpdatesApplicationComponent {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<UpdatesApplication>

    fun fakeAppUpdateManager(): FakeAppUpdateManager
}
