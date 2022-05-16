package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.AppScope
import com.hontech.pastacooking.model.HeaterStatus
import com.hontech.pastacooking.task.conn.testPump
import kotlinx.coroutines.launch

class PumpDelegate (view: View) {

    private val waterPump = view.findViewById<SwitchCompat>(R.id.id_sw_pump_water)
    private val steamPump = view.findViewById<SwitchCompat>(R.id.id_sw_pump_steam)
    private val nozzlePump = view.findViewById<SwitchCompat>(R.id.id_sw_pump_nozzle)

    private var updateFlag = false

    init {
        waterPump.setOnCheckedChangeListener { _, isChecked -> onChecked(1, isChecked) }
        steamPump.setOnCheckedChangeListener { _, isChecked -> onChecked(2, isChecked) }
        nozzlePump.setOnCheckedChangeListener { _, isChecked -> onChecked(3, isChecked) }
    }

    private fun onChecked(id: Int, flag: Boolean) {
        if (updateFlag) {
            return
        }
        val value = if (flag) 0 else 1
        AppScope.launch {
            val w = showProg(waterPump, "测试水泵")
            try {
                testPump(id, value)
                w.success("测试水泵成功")
            } catch (e: Exception) {
                e.printStackTrace()
                w.error(e.message!!)
            }
        }
    }

    fun update(status: HeaterStatus) {
        updateFlag = true
        waterPump.isChecked = status.isWaterPumpOpen()
        steamPump.isChecked = status.isSteamPumpOpen()
        nozzlePump.isChecked = status.isNozzlePumpOpen()
        updateFlag = false
    }
}