package com.hontech.pastacooking.app

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import org.greenrobot.eventbus.EventBus


private class AsyncTask : HandlerThread("async") {

    init {
        start()
    }
}

object AppExecutor : Handler(Looper.getMainLooper())

object WorkExecutor : Handler(AsyncTask().looper)

val bus = EventBus.getDefault()




