package com.example.disasterresponseplatform

import android.app.Application
import com.example.disasterresponseplatform.managers.DiskStorageManager
import dagger.hilt.android.HiltAndroidApp

/** This is for Hilt
 *  It basically for hold the ActivationContext on Hilt, thanks to this class whenever Hilt needs an ActivationContext
 *  it comes from there
 *  Warning: You need to add Manifest because it's an activity otherwise you will get an error
 */
@HiltAndroidApp
class DarpApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DiskStorageManager.initialize(this)
    }
}