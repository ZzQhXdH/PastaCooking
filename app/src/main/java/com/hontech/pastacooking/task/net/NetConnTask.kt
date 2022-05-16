package com.hontech.pastacooking.task.net

import com.hontech.pastacooking.app.WorkExecutor
import com.hontech.pastacooking.net.Client

class NetConnTask : Runnable {

    override fun run() {

        try {
            exec()
        } catch (e: Exception) {
            e.printStackTrace()
            WorkExecutor.postDelayed(this, 30 * 1000)
        }
    }

    private fun exec() {
        if (Client.isConnected()) {
            return
        }
        Client.connect(Client.Host, Client.Port)
    }
}

