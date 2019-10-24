package com.malvinstn.updates.di

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
        UpdatesApplicationModule::class
    ]
)
interface UpdatesApplicationComponent : AndroidInjector<UpdatesApplication> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<UpdatesApplication>
}
