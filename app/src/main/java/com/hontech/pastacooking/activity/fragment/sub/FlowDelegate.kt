package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.setFlowParam

class FlowDelegate (view: View) {

    private val input =view.findViewById<EditText>(R.id.id_input_flow)
    private val btnSet = view.findViewById<Button>(R.id.id_btn_flow_set)

    init {
        btnSet.onClick(::onSet)
    }

    private suspend fun onSet() {
        val w = showProg(btnSet, "设置流量计参数")
        try {
            val flow = input.toInt()
            setFlowParam(flow)
            w.success("设置流量计参数成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}

