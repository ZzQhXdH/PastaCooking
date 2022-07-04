package com.hontech.pastacooking.task.net

import com.hontech.pastacooking.app.NetTask
import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.app.toJson
import com.hontech.pastacooking.model.DeviceInfo
import com.hontech.pastacooking.model.loginModel
import com.hontech.pastacooking.net.Client
import com.hontech.pastacooking.net.Frame

class NetConnTask : Runnable {

    companion object {

        private val instance = NetConnTask()

        private const val ReconnDelay = 10 * 1000L

        fun connect() {
            NetTask.post(instance)
        }

        fun reconnect() {
            NetTask.postDelayed(instance, ReconnDelay)
        }
    }

    override fun run() {

        try {
            exec()
        } catch (e: Exception) {
            e.printStackTrace()
            reconnect()
        }
    }

    private fun exec() {
        if (Client.isConnected()) {
            return
        }
        Client.connect(Client.Host, Client.Port)
        val frame = Client.write(Frame.Login, loginModel.toJson(), 10 * 1000L)
        val info = frame.parseBody<DeviceInfo>()
        Client.info = info
        log("登录成功:$info")
    }
}


