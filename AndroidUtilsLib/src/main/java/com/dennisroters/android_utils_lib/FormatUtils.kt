package com.dennisroters.android_utils_lib

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

object FormatUtils {

    val defaultSimpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.GERMANY)

    val productsSimpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.GERMANY).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }

    fun dateToString(date: Date?, pattern: String = "yyyyMMdd-HHmmss", locale: Locale = Locale.GERMANY, timeZoneId: String? = null): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return try {
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun parseDateStringToDate(
            dateString: String?,
            inPattern: String = "yyyy-MM-dd_HH-mm-ss",
            inLocale: Locale = Locale.GERMANY,
            inTimeZoneId: String? = null
    ): Date? {
        return try {
            val inSdf = SimpleDateFormat(inPattern, inLocale).apply {
                if (!inTimeZoneId.isNullOrBlank()) {
                    timeZone = TimeZone.getTimeZone(inTimeZoneId)
                }
            }

            return inSdf.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    fun reformatDateString(
            dateString: String?,
            inPattern: String = "yyyy-MM-dd_HH-mm-ss",
            inLocale: Locale = Locale.GERMANY,
            inTimeZoneId: String? = null,
            outPattern: String = "yyyy-MM-dd_HH-mm-ss",
            outLocale: Locale = Locale.GERMANY,
            outTimeZoneId: String? = null
    ): String {
        return try {
            val inSdf = SimpleDateFormat(inPattern, inLocale).apply {
                if (!inTimeZoneId.isNullOrBlank()) {
                    timeZone = TimeZone.getTimeZone(inTimeZoneId)
                }
            }
            val outSdf = SimpleDateFormat(outPattern, outLocale).apply {
                if (!outTimeZoneId.isNullOrBlank()) {
                    timeZone = TimeZone.getTimeZone(outTimeZoneId)
                }
            }

            val date = inSdf.parse(dateString)
            outSdf.format(date)
        } catch (e: Exception) {
            "-"
        }
    }

    fun dateStringToReadableDateString(dateString: String?): String {
        return reformatDateString(dateString, outPattern = "dd.MM.YYYY, HH:mm:ss")
    }

}