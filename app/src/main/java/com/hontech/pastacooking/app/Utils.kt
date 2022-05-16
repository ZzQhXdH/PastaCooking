package com.hontech.pastacooking.app

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.hontech.pastacooking.ext.formatMac
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Closeable
import java.io.IOException
import java.lang.IllegalStateException
import java.net.NetworkInterface
import kotlin.reflect.KSuspendFunction0


fun stringById(id: Int): String {
    return AppContext.resources.getString(id)
}

fun dimenById(id: Int): Int {
    return AppContext.resources.getDimension(id).toInt()
}

fun log(msg: String) = Log.d("debug", msg)

private val width = AppContext.resources.displayMetrics.widthPixels
private val height = AppContext.resources.displayMetrics.heightPixels

val screenWidth = if (width > height) width else height
val screenHeight = if (width < height) width else height

fun hideSoftKey(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (!imm.isActive) {
        return
    }
    val view = activity.currentFocus
    if (view == null) {
        return
    }
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun close(io: Closeable) {
    try {
        io.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


private fun getWlanMacAddr(): String {
    val ins = NetworkInterface.getNetworkInterfaces()
    for (net in ins) {
        if (net.name == "wlan0") {
            return net.hardwareAddress.formatMac()
        }
    }
    throw IOException("无法获取mac地址")
}

val MacAddr = getWlanMacAddr()

val Json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

inline fun<reified T> T.toJson(): ByteArray {
    return Json.encodeToString(this).toByteArray()
}

inline fun <reified T> T.toJsonString(): String {
    return Json.encodeToString(this)
}

val AppScope = CoroutineScope(Job() + Dispatchers.Main)

fun View.onClick(cb: KSuspendFunction0<Unit>) {
    this.setOnClickListener {
        AppScope.launch { cb() }
    }
}

fun EditText.toInt(): Int {
    val text = this.text.toString()
    if (text.isEmpty()) {
        throw IllegalStateException("请输入参数")
    }
    return text.toInt()
}

