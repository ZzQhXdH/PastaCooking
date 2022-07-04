package com.hontech.pastacooking.app

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.hontech.pastacooking.task.net.NetConnTask
import com.hontech.pastacooking.task.net.NetPollTask

private lateinit var INSTANCE: Context

const val AppId = "com.hontech.pastacooking"
const val AppActivityId = "com.hontech.pastacooking.activity.MainActivity"

object AppContext : ContextWrapper(INSTANCE)

class PastaCookingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        init()
    }

    private fun checkPermission() {
        var r = checkAndGrantPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (!r) {
            throw IllegalStateException("无法获取写权限")
        }
        r = checkAndGrantPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!r) {
            throw IllegalStateException("无法获取读权限")
        }
    }

    private fun init() {
        log("设备mac地址:$MacAddr")
        //  checkPermission()
        NetConnTask.connect()
        NetPollTask.start()
    }
}