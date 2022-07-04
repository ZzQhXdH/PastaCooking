package com.hontech.pastacooking.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.FileProvider
import com.hontech.pastacooking.ext.formatMac
import com.hontech.pastacooking.model.Resp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.PrintWriter
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

fun close(io: Closeable?) {
    if (io == null) {
        return
    }
    try {
        io.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

inline fun defer(work: () -> Unit, closeCb: () -> Unit) {
    try {
        work()
    } catch (e: Exception) {
        throw e
    } finally {
        closeCb()
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

val defJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun ByteArray.toStrWithUtf8() = String(this, Charsets.UTF_8)

inline fun <reified T> T.toJson(): ByteArray {
    return defJson.encodeToString(this).toByteArray()
}

inline fun <reified T> T.toJsonString(): String {
    return defJson.encodeToString(this)
}

inline fun <reified T> ByteArray.parseJson(): T {
    return defJson.decodeFromString(this.toStrWithUtf8())
}

inline fun <reified T> ByteArray.parseRespBody(): T {
    return this.parseJson<Resp<T>>().body
}

val AppScope = CoroutineScope(Job() + Dispatchers.Main)

fun View.onClick(cb: KSuspendFunction0<Unit>) {
    this.setOnClickListener {
        AppScope.launch {
            try {
                cb()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun EditText.toInt(): Int {
    val text = this.text.toString()
    if (text.isEmpty()) {
        throw IllegalStateException("请输入参数")
    }
    return text.toInt()
}

fun installApk2(path: String) {
    log("开始安装:$path")
    val prcess = Runtime.getRuntime().exec("su")
    val pw = PrintWriter(prcess.outputStream)
    pw.println("pm install -r -t $path")
    pw.flush()
    pw.close()
    val v = prcess.waitFor()
    log("安装返回值:$v")
}

fun installApk(path: String) {
    log("开始安装:$path")

    val runtime = Runtime.getRuntime()
        .exec("su")
    val pw = PrintWriter(runtime.outputStream)
    pw.write("pm uninstall $AppId &&")
    pw.write("pm install -r -t -d $path &&")
    pw.write("am start -n ${AppId}/${AppActivityId}")
    pw.flush()
    pw.close()
    val v = runtime.waitFor()
    log("安装返回值:$v")
}

fun installApp(path: String) {
    val uri = FileProvider.getUriForFile(
        AppContext,
        "com.hontech.pastacooking.FileProvider",
        File(path)
    )
    val i = Intent(Intent.ACTION_VIEW)
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    i.addCategory("android.intent.category.DEFAULT")
    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    i.setDataAndType(uri, "application/vnd.android.package-archive")
    AppContext.startActivity(i)
}


fun grantPermission(name: String): Boolean {
    log("获取权限:$name")
    val runtime = Runtime.getRuntime()
        .exec("su")
    val pw = PrintWriter(runtime.outputStream)
    pw.write("pm grant $AppId $name")
    pw.flush()
    pw.close()
    val v = runtime.waitFor()
    return v == 0
}

fun checkAndGrantPermission(name: String): Boolean {
    val ret = AppContext.checkSelfPermission(name)
    if (ret == PackageManager.PERMISSION_GRANTED) {
        log("已经授权:$name")
        return true
    }
    return grantPermission(name)
}

fun killApp() {
    android.os.Process.killProcess(android.os.Process.myPid())
}

