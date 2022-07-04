package com.hontech.pastacooking.net

import com.hontech.pastacooking.app.NetTask
import com.hontech.pastacooking.model.DeviceInfo

class Notify(val frame: Frame) : Runnable {

    companion object {

        fun exec(frame: Frame) {
            NetTask.post(Notify(frame))
        }
    }


    override fun run() {

        try {
            exec()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onLogin() {
        val info = frame.parse<DeviceInfo>()
        Client.info = info
    }

    private fun onOta() {
    }

    private fun exec() {
        val req = frame.request()
        when (req) {
            Frame.Login -> onLogin()
            Frame.NotifyOta -> onOta()
        }
    }
}