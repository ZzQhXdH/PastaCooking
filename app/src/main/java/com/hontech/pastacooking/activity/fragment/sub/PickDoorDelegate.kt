package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.task.conn.requestMain

class PickDoorDelegate(view: View) {

    private val btnOpen = view.findViewById<Button>(R.id.id_btn_pick_open)
    private val btnClose = view.findViewById<Button>(R.id.id_btn_pick_close)

    companion object {
        const val Timeout = 15 * 1000L
        const val Msg = "测试取物门"
        const val Req = MainProto.TestPickDoor
    }

    private suspend fun exec(type: Int) {
        requestMain(Timeout, Req, arrayOf(UInt8(type)), Msg)
    }

    private suspend fun onOpen() {
        val w = showProg(btnClose, "取物门打开")
        try {
            exec(0)
            w.success("取物门打开成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onClose() {
        val w = showProg(btnClose, "取物门关闭")
        try {
            exec(1)
            w.success("取物门关闭成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }


    init {
        btnOpen.onClick(::onOpen)
        btnClose.onClick(::onClose)
    }

}