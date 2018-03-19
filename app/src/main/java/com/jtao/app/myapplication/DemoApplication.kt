package com.jtao.app.myapplication

/**
 * Created by taoo on 18-3-17.
 */
class DemoApplication:BaseApplication() {
    lateinit var component:ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        val component = initDraggerComponent()!!
        component.inject(this)
        this.component = component
    }
}