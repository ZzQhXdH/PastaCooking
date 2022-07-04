package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.setDrawParam

class DrawParamDelegate(view: View) {

    private val inputWater = view.findViewById<EditText>(R.id.id_input_draw_water_timeout)
    private val inputSteam = view.findViewById<EditText>(R.id.id_input_draw_steam_timeout)
    private val btnSet = view.findViewById<Button>(R.id.id_btn_draw_set)

    init {
        btnSet.onClick(::onSet)
    }

    private suspend fun onSet() {
        val w = showProg(btnSet, "设置抽水参数")
        try {
            val water = inputWater.toInt()
            val steam = inputSteam.toInt()
            setDrawParam(water, steam)
            w.success("设置成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}