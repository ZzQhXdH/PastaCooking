package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.setSteamHeatParam

class SteamParamDelegate(view: View) {

    private val inputTemp = view.findViewById<EditText>(R.id.id_input_steam_temp)
    private val inputKpa = view.findViewById<EditText>(R.id.id_input_steam_kpa)
    private val inputTimeout = view.findViewById<EditText>(R.id.id_input_steam_timeout)
    private val btnSet = view.findViewById<Button>(R.id.id_btn_steam_set)

    init {
        btnSet.onClick(::onSet)
    }

    private suspend fun onSet() {
        val w = showProg(btnSet, "设置蒸汽锅炉加热参数")
        try {
            val temp = inputTemp.toInt()
            val kpa = inputKpa.toInt()
            val timeout = inputTimeout.toInt()
            setSteamHeatParam(temp, kpa, timeout)
            w.success("设置成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}