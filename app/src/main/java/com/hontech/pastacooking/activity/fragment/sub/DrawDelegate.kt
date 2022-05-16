package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.testNozzleDraw

class DrawDelegate (view: View) {

    private val input = view.findViewById<EditText>(R.id.id_input_draw)
    private val btnExec = view.findViewById<Button>(R.id.id_btn_draw)

    init {
        btnExec.onClick(::onExec)
    }

    private suspend fun onExec() {
        val w = showProg(btnExec, "测试放水")
        try {
            val ml = input.toInt()
            testNozzleDraw(ml)
            w.success("测试放水成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

}