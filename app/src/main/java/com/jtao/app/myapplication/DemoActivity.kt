package com.jtao.app.myapplication

import android.app.Activity
import android.os.Bundle

/**
 * Created by taoo on 18-3-17.
 */
abstract class DemoActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as DemoApplication).component.inject(this)
    }
}