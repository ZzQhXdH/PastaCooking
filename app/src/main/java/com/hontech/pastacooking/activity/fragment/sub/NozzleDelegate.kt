package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.testNozzle

class NozzleDelegate (view: View) {

    private val input = view.findViewById<EditText>(R.id.id_input_nozzle)
    private val btnReset = view.findViewById<Button>(R.id.id_btn_nozzle_reset)
    private val btnExec = view.findViewById<Button>(R.id.id_btn_nozzle_exec)

    init {
        btnExec.onClick(::onExec)
        btnReset.onClick(::onReset)
    }

    private suspend fun onReset() {
        val w = showProg(btnExec, "喷嘴复位")
        try {
            testNozzle(0, 0)
            w.success("喷嘴复位成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onExec() {
        val w = showProg(btnExec, "喷嘴执行")
        try {
            val positon = input.toInt()
            testNozzle(1, positon)
            w.success("喷嘴执行成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}
