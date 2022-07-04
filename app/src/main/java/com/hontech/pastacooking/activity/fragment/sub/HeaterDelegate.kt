package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.AppScope
import com.hontech.pastacooking.model.HeaterStatus
import com.hontech.pastacooking.task.conn.testHeater
import kotlinx.coroutines.launch

class HeaterDelegate(view: View) {

    private val water = view.findViewById<SwitchCompat>(R.id.id_sw_heater_water)
    private val steam = view.findViewById<SwitchCompat>(R.id.id_sw_heater_steam)

    private var updateFlag = false

    init {
        water.setOnCheckedChangeListener { _, isChecked -> onChecked(1, isChecked) }
        steam.setOnCheckedChangeListener { _, isChecked -> onChecked(2, isChecked) }
    }

    private fun onChecked(id: Int, flag: Boolean) {
        if (updateFlag) {
            return
        }
        val value = if (flag) 0 else 1
        AppScope.launch {
            val w = showProg(water, "测试加热器")
            try {
                testHeater(id, value)
                w.success("测试加热器成功")
            } catch (e: Exception) {
                e.printStackTrace()
                w.error(e.message!!)
            }
        }
    }

    fun update(status: HeaterStatus) {
        updateFlag = true
        water.isChecked = status.isWaterHeatOpen()
        steam.isChecked = status.isSteamHeatOpen()
        updateFlag = false
    }
}
