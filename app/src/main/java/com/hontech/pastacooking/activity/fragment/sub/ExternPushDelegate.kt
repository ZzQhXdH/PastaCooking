package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.task.conn.requestMain

class ExternPushDelegate(view: View) {

    private val btnReset = view.findViewById<Button>(R.id.id_btn_extern_reset)
    private val btnFront = view.findViewById<Button>(R.id.id_btn_extern_front)
    private val btnBack = view.findViewById<Button>(R.id.id_btn_extern_back)
    private val btnAdj = view.findViewById<Button>(R.id.id_btn_extern_adj)
    private val btnExec = view.findViewById<Button>(R.id.id_btn_extern_exec)
    private val inputDistance = view.findViewById<EditText>(R.id.id_edit_distance)

    companion object {
        const val Timeout = 60 * 1000L
        const val Msg = "测试外推货"
        const val Req = MainProto.TestExternPush
    }

    private suspend fun exec(type: Int, distance: Int) {
        requestMain(Timeout, Req, arrayOf(UInt8(type), UInt16(distance)), Msg)
    }

    private suspend fun onReset() {
        val w = showProg(btnAdj, "外推货复位")
        try {
            exec(0, 0)
            w.success("外推货复位成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onFront() {
        val w = showProg(btnAdj, "外推货前进")
        try {
            exec(1, 0)
            w.success("外推货前进成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onBack() {
        val w = showProg(btnAdj, "外推货后退")
        try {
            exec(2, 0)
            w.success("外推货后退成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onAdj() {
        val w = showProg(btnAdj, "外推货校准")
        try {
            val distance = inputDistance.text.toString().toInt()
            exec(4, distance)
            w.success("外推货校准成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onExec() {
        val w = showProg(btnAdj, "外推货=>自定义位置")
        try {
            val distance = inputDistance.text.toString().toInt()
            exec(3, distance)
            w.success("外推货=>自定义位置成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }


    init {
        btnReset.onClick(::onReset)
        btnFront.onClick(::onFront)
        btnBack.onClick(::onBack)
        btnAdj.onClick(::onAdj)
        btnExec.onClick(::onExec)
    }
}
