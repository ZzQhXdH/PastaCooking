package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.task.conn.requestMain


class CargoFetchDelegate (view: View) {

    private val btnAdj = view.findViewById<Button>(R.id.id_btn_fetch_adj)
    private val btnLeft = view.findViewById<Button>(R.id.id_btn_fetch_left)
    private val btnCenter = view.findViewById<Button>(R.id.id_btn_fetch_center)
    private val btnRight = view.findViewById<Button>(R.id.id_btn_fetch_right)

    companion object {
        const val Timeout = 60 * 1000L
        const val Msg = "测试取货机械手"
        const val Req= MainProto.TestCargoFetch
    }

    private suspend fun exec(type: Int) {
        requestMain(Timeout, Req, arrayOf(UInt8(type)), Msg)
    }

    private suspend fun onAdj() {
        val w = showProg(btnAdj, "机械臂校准")
        try {
            exec(0)
            w.success("机械臂校准成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onLeft() {
        val w = showProg(btnAdj, "机械臂=>左边")
        try {
            exec(1)
            w.success("机械臂=>左边成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onCenter() {
        val w = showProg(btnAdj, "机械臂=>中间")
        try {
            exec(2)
            w.success("机械臂=>中间成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onRight() {
        val w= showProg(btnAdj, "机械臂=>右边")
        try {
            exec(3)
            w.success("机械臂=>右边成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }


    init {
        btnAdj.onClick(::onAdj)
        btnLeft.onClick(::onLeft)
        btnCenter.onClick(::onCenter)
        btnRight.onClick(::onRight)
    }
}