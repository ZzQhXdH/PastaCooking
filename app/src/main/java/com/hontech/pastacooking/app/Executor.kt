package com.hontech.pastacooking.app

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import org.greenrobot.eventbus.EventBus


private class ConnThread : HandlerThread("conn-task") {

    init {
        start()
    }
}

private class NetThread : HandlerThread("net-task") {
    init {
        start()
    }
}

object AppTask : Handler(Looper.getMainLooper())

object ConnTask : Handler(ConnThread().looper)

object NetTask : Handler(NetThread().looper)

val bus = EventBus.builder().logNoSubscriberMessages(true).build()