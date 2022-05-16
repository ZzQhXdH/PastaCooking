package com.hontech.pastacooking.app

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.hontech.pastacooking.task.net.NetConnTask

private lateinit var INSTANCE: Context

object AppContext : ContextWrapper(INSTANCE)

class PastaCookingApp : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        init()
    }

    private fun init() {
        WorkExecutor.post(NetConnTask())
        log("设备mac地址:$MacAddr")
    }
}