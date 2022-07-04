package com.hontech.pastacooking.task.net

import com.hontech.pastacooking.app.NetTask
import com.hontech.pastacooking.net.Client
import com.hontech.pastacooking.net.Frame

class NetPollTask : Runnable {

    companion object {

        private const val Timeout = 3 * 1000L

        private val body = byteArrayOf()

        private val instance = NetPollTask()

        fun start() {
            NetTask.postDelayed(instance, Timeout)
        }
    }

    override fun run() {

        try {
            exec()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        start()
    }

    private fun exec() {
        if (!Client.isConnected()) {
            return
        }
        Client.write(Frame.Poll, body, 5 * 1000L)
    }
}


