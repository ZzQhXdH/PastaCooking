package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.setWaterHeatParam

class WaterParamDelegate (view: View) {

    private val inputTemp = view.findViewById<EditText>(R.id.id_input_water_temp)
    private val inputTimeout = view.findViewById<EditText>(R.id.id_input_water_timeout)
    private val btnSet = view.findViewById<Button>(R.id.id_btn_water_set)

    init {
        btnSet.onClick(::onSet)
    }

    private suspend fun onSet() {
        val w = showProg(btnSet, "设置开水锅炉加热参数")
        try {
            val temp = inputTemp.toInt()
            val timeout = inputTimeout.toInt()
            setWaterHeatParam(temp, timeout)
            w.success("设置成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}