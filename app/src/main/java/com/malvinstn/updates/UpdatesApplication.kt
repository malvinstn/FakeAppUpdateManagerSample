package com.malvinstn.updates

import com.malvinstn.updates.di.DaggerUpdatesApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class UpdatesApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerUpdatesApplicationComponent.factory()
            .create(this)
    }
}
