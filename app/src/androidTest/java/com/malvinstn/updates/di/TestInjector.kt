package com.malvinstn.updates.di

import androidx.test.platform.app.InstrumentationRegistry
import com.malvinstn.updates.UpdatesApplication

object TestInjector {
    fun inject(): TestUpdatesApplicationComponent {
        val application = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as UpdatesApplication
        return DaggerTestUpdatesApplicationComponent.factory()
            .create(application)
            .also { it.inject(application) } as TestUpdatesApplicationComponent
    }
}

