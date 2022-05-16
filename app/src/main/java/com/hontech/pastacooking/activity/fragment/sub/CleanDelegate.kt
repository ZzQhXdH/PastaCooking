package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.clean

class CleanDelegate (view: View) {

    private val sub = view.findViewById<View>(R.id.id_sub_clean)

    private val inputC = sub.findViewById<EditText>(R.id.id_input_c)
    private val inputPourWater = sub.findViewById<EditText>(R.id.id_input_pour_water)
    private val inputD = sub.findViewById<EditText>(R.id.id_input_d)
    private val inputSteamTime = sub.findViewById<EditText>(R.id.id_input_steam_time)
    private val btnClean = sub.findViewById<Button>(R.id.id_btn_clean)


    init {
        btnClean.onClick(::onClean)
    }

    private suspend fun onClean() {
        val w = showProg(btnClean, "清洗")
        try {
            val c = inputC.toInt()
            val pourWater = inputPourWater.toInt()
            val d = inputD.toInt()
            val steamTime = inputSteamTime.toInt()
            clean(c, pourWater, d, steamTime)
            w.success("清洗成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}