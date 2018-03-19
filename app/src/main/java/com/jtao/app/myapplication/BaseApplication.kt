package com.jtao.app.myapplication

import android.app.Application

/**
 * Created by taoo on 18-3-17.
 */
abstract class BaseApplication:Application() {
    protected fun initDraggerComponent(): ApplicationComponent? {
        return DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
    }
}