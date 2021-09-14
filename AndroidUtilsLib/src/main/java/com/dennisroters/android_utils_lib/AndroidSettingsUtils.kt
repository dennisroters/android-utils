package com.dennisroters.android_utils_lib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object AndroidSettingsUtils {

    fun openAppSettings(context: Context?) {
        if (context == null) return

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
    }

    fun openLocationSettings(context: Context?) {
        if (context == null) return

        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

        // Fix for: Calling startActivity() from outside of an Activity context
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intent)
    }

}