package com.hontech.pastacooking.activity.window

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.dimenById
import com.hontech.pastacooking.task.conn.HeaterParam

private val width = dimenById(R.dimen.x330)
private val height = dimenById(R.dimen.y300)

class HeaterParamWindow : MoveableWindow(R.layout.window_heater_param, width, height) {

    private val btnClose = view.findViewById<Button>(R.id.id_btn_close)

    private val tvWaterTemp = view.findViewById<TextView>(R.id.id_tv_water_temp)
    private val tvWaterTimeout = view.findViewById<TextView>(R.id.id_tv_water_timeout)
    private val tvSteamTemp = view.findViewById<TextView>(R.id.id_tv_steam_temp)
    private val tvSteamKpa = view.findViewById<TextView>(R.id.id_tv_steam_kpa)
    private val tvSteamTimeout = view.findViewById<TextView>(R.id.id_tv_steam_timeout)
    private val tvDrawWaterTimeout = view.findViewById<TextView>(R.id.id_tv_draw_water_timeout)
    private val tvDrawSteamTimeout = view.findViewById<TextView>(R.id.id_tv_draw_steam_timeout)
    private val tvFlow = view.findViewById<TextView>(R.id.id_tv_flow)

    init {
        btnClose.setOnClickListener { dismiss() }
    }

    fun show(parent: View, param: HeaterParam) {
        tvWaterTemp.text = "${param.waterTemp}℃"
        tvWaterTimeout.text = "${param.waterTimeout}分钟"
        tvSteamTemp.text = "${param.steamTemp}℃"
        tvSteamKpa.text = "${param.steamKpa}KPA"
        tvSteamTimeout.text = "${param.steamTimeout}分钟"
        tvDrawWaterTimeout.text = "${param.drawWaterTimeout}分钟"
        tvDrawSteamTimeout.text = "${param.drawSteamTimeout}分钟"
        tvFlow.text = "${param.flow}个/升"
        show(parent)
    }
}

fun showHeaterPram(parent: View, param: HeaterParam) {
    val w = HeaterParamWindow()
    w.show(parent, param)
}


