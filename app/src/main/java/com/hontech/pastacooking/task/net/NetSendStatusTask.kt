package com.hontech.pastacooking.task.net

import com.hontech.pastacooking.app.NetTask
import com.hontech.pastacooking.net.Client

class NetSendStatusTask(val req: Int, val body: ByteArray) : Runnable {

    companion object {

        fun send(req: Int, body: ByteArray) {
            NetTask.post(NetSendStatusTask(req, body))
        }
    }

    override fun run() {

        try {
            exec()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun exec() {
        if (!Client.isConnected()) {
            return
        }
        Client.write(req, body, 5 * 1000L)
    }
}

