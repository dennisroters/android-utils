package com.dennisroters.android_utils_lib

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import java.io.File
import java.util.*


object ExternalUtils {

    fun callPhone(context: Context?, number: String?) {
        if (context == null) return

        val intent = Intent(Intent.ACTION_DIAL)

        // check if there are any services that can handle a phone call
        // if not, add number to clipboard
        val possibleIntents =
            context.packageManager?.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (possibleIntents == null || possibleIntents.isEmpty()) {
            addToClipboard(context, number)
        }

        // start intent chooser
        intent.data = Uri.parse("tel:$number")
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun addToClipboard(context: Context?, content: String?, label: String = "copy") {
        if (context == null) return

        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return
        val clip = ClipData.newPlainText(label, content)
        clipboard.setPrimaryClip(clip)
    }

    fun sendMail(
        context: Context?,
        email: String? = null,
        subject: String? = null,
        message: String? = null,
        image: Uri? = null
    ) {
        if (context == null) return

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")

        if (!subject.isNullOrEmpty()) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (!message.isNullOrEmpty()) {
            intent.putExtra(Intent.EXTRA_TEXT, message)
        }
        if (image != null) {
            intent.putExtra(Intent.EXTRA_STREAM, image)
            intent.type = "application/image"
        }

        context.startActivity(Intent.createChooser(intent, null))
    }

    fun openBrowser(context: Context?, url: String?) {
        if (context == null) return

        // abort if url is null/empty
        if (url.isNullOrEmpty()) return
        var thisUrl = url

        // apply prefix if missing (to prevent parse errors with some browsers)
        if (!thisUrl.toLowerCase().contains("http://") && !thisUrl.toLowerCase()
                .contains("https://")
        ) {
            thisUrl = "http://$thisUrl"
        }

        // create intent and open chooser
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(thisUrl).normalizeScheme()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun openMaps(
        context: Context?,
        label: String = "",
        latitude: Double = 0.0,
        longitude: Double = 0.0,
        address: String? = null
    ) {
        if (context == null) return

        val prefix = "geo:$latitude,$longitude"
        var query = Uri.encode("$latitude,$longitude ($label)")
        if (!address.isNullOrEmpty()) {
            query = Uri.encode("$address")
        }

        val uri = Uri.parse("$prefix?q=$query&z016")

        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
    }

    fun openGoogleMapsNavigation(context: Context?, latitude: Double?, longitude: Double?) {
        if (context == null) return

        if (latitude == null || longitude == null) return

        val uri = Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }

    fun shareText(context: Context?, message: String? = null) {
        if (context == null) return

        if (message == null) return

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "text/plain"
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun shareImage(context: Context?, image: Uri? = null) {
        if (context == null) return

        if (image == null) return

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, image)
        intent.type = "image/*"
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun shareImages(context: Context?, images: ArrayList<Uri>? = null) {
        if (context == null) return

        if (images == null || images.isEmpty()) return

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images)
        intent.type = "image/*"
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun sharePdf(context: Context?, pdf: Uri? = null) {
        if (context == null) return

        if (pdf == null) return

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, pdf)
        intent.type = "application/pdf"
        context.startActivity(Intent.createChooser(intent, null))
    }

    fun openPdf(context: Context?, pdfUrl: String? = null) {
        if (context == null) return

        if (pdfUrl == null) return

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }

    fun shareApp(
        context: Context?,
        @StringRes titlesRes: Int,
        @StringRes messageRes: Int,
        @StringRes linkRes: Int,
        @StringRes shareChooserTextRes: Int
    ) {
        if (context == null) return

        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(titlesRes))
            var shareMessage = context.getString(messageRes)
            shareMessage =
                shareMessage + "\n" + context.getString(linkRes)
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(shareChooserTextRes)
                )
            )
        } catch (e: Exception) {
            Log.e("ContexExt", "shareApp Error - ${e.localizedMessage}")
        }
    }

    fun openAppSettings(context: Context?) {
        if (context == null) return

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    fun openFile(context: Context?, filePath: String) {
        if (context == null) return

        try {
            val file = File(filePath)
            val uri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                file
            )
            val mimeType = getMimeType(file.absolutePath)


            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, mimeType)
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(intent)
        } catch (t: Throwable) {
            val errorMsg = t.message
        }

    }

    private fun getMimeType(url: String): String? {
        val parts = url.split("\\.".toRegex()).toTypedArray()
        val extension = parts[parts.size - 1]
        var type: String? = null
        if (extension != null) {
            val mime = MimeTypeMap.getSingleton()
            type = mime.getMimeTypeFromExtension(extension)
        }
        return type
    }


}





