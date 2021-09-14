package com.dennisroters.android_utils_lib

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object VibrateUtils {

    fun vibrate(context: Context?, durationInMillis: Long) {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(durationInMillis, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            vibrator?.vibrate(durationInMillis)
        }
    }

}