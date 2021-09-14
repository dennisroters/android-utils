package com.dennisroters.android_utils_lib

import android.content.res.Resources

object ConversionUtils {

    fun toPixel(dp: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (dp * density).toInt()
    }

    fun toDp(pixel: Int): Int {
        val density = Resources.getSystem().displayMetrics.density
        return (pixel / density).toInt()
    }

    fun secondsToMillis(seconds: Double?): Long? {
        if (seconds == null) return null
        val millis = (seconds * 1000).toLong()
        return millis
    }

    fun toReadableFileSize(sizeInByte: Long): String {

        val kilo: Long = 1024
        val mega = kilo * kilo
        val giga = mega * kilo
        val tera = giga * kilo

        var sizeString = ""
        val kb = sizeInByte.toDouble() / kilo
        val mb = kb / kilo
        val gb = mb / kilo
        val tb = gb / kilo
        if (sizeInByte < kilo) {
            sizeString = "$sizeInByte B"
        } else if (sizeInByte >= kilo && sizeInByte < mega) {
            sizeString = String.format("%.2f", kb) + " KB"
        } else if (sizeInByte >= mega && sizeInByte < giga) {
            sizeString = String.format("%.2f", mb) + " MB"
        } else if (sizeInByte >= giga && sizeInByte < tera) {
            sizeString = String.format("%.2f", gb) + " GB"
        } else if (sizeInByte >= tera) {
            sizeString = String.format("%.2f", tb) + " TB"
        }

        return sizeString

    }

}
