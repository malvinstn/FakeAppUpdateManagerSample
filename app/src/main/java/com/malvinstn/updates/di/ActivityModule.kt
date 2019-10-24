package com.malvinstn.updates.di

import com.malvinstn.updates.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {
    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity
}
