package com.jtao.app.myapplication

import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by taoo on 18-3-17.
 */
@Module
class AndroidModule(private val application: BaseApplication) {
    @Provides @Singleton @ForApplication
    fun provideApplicationContext(): BaseApplication {
        return application
    }

    @Provides @Singleton
    fun provideLocationManager(): LocationManager {
        return application.getSystemService(LOCATION_SERVICE) as LocationManager
    }
}