package com.jtao.app.myapplication

import com.jtao.app.myapplication.ui.HomeActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by taoo on 18-3-17.
 */
@Singleton
@Component(modules = arrayOf(AndroidModule::class))
interface ApplicationComponent {
    fun inject(application: BaseApplication)
    fun inject(demoActivity: DemoActivity)
    fun inject(homeActivity: HomeActivity)
}