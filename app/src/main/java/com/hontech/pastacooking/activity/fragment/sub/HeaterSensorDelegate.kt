package com.hontech.pastacooking.activity.fragment.sub

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.view.CheckView
import com.hontech.pastacooking.conn.HeaterProto
import com.hontech.pastacooking.model.HeaterStatus

class HeaterSensorDelegate(view: View) {

    private val tvNozzle = view.findViewById<TextView>(R.id.id_tv_nozzle)
    private val tvWaterTemp = view.findViewById<TextView>(R.id.id_tv_water_temp)
    private val tvSteamTemp = view.findViewById<TextView>(R.id.id_tv_steam_temp)
    private val tvSteamKpa = view.findViewById<TextView>(R.id.id_tv_steam_kpa)
    private val tvFlow = view.findViewById<TextView>(R.id.id_tv_flow)
    private val tvDraw = view.findViewById<TextView>(R.id.id_tv_draw_type)
    private val tvHeat = view.findViewById<TextView>(R.id.id_tv_heat_type)

    private val cvNozzleOrigin = view.findViewById<CheckView>(R.id.id_cv_nozzle_origin)
    private val cvNozzleFault = view.findViewById<CheckView>(R.id.id_cv_nozzle_fault)
    private val cvWaterTempFail = view.findViewById<CheckView>(R.id.id_cv_water_temp_fault)
    private val cvNozzleBottomOrigin = view.findViewById<CheckView>(R.id.id_cv_nozzle_bottom_origin)
    private val cvSteamTempFail = view.findViewById<CheckView>(R.id.id_cv_steam_temp_fult)
    private val cvLeak = view.findViewById<CheckView>(R.id.id_cv_leak)
    private val cvWaterLow = view.findViewById<CheckView>(R.id.id_cv_water_low)
    private val cvWaterHigh = view.findViewById<CheckView>(R.id.id_cv_water_high)
    private val cvSteamLow = view.findViewById<CheckView>(R.id.id_cv_steam_low)
    private val cvSteamHigh = view.findViewById<CheckView>(R.id.id_cv_steam_high)

    fun update(status: HeaterStatus) {
        setTextViewMsg(tvNozzle, status.nozzle.value)
        tvWaterTemp.text = "${status.waterTemp.signedValue()}℃"
        tvSteamTemp.text = "${status.steamTemp.signedValue()}℃"
        tvSteamKpa.text = "${status.steamKpa.value}KPA"
        tvFlow.text = "${status.count.value}"
        tvDraw.text = HeaterProto.drawMsg(status.drawType.value)
        tvHeat.text = HeaterProto.heatMsg(status.heatType.value)

        cvNozzleOrigin.setChecked(status.isNozzleOrigin())
        cvNozzleFault.setChecked(status.isNozzleFault())
        cvWaterTempFail.setChecked(status.isWaterTempFault())
        cvNozzleBottomOrigin.setChecked(status.isNozzleBottomOrigin())
        cvSteamTempFail.setChecked(status.isSteamTempFault())
        cvLeak.setChecked(status.isLeak())
        cvWaterLow.setChecked(status.isWaterLow())
        cvWaterHigh.setChecked(status.isWaterHigh())
        cvSteamLow.setChecked(status.isSteamLow())
        cvSteamHigh.setChecked(status.isSteamHigh())
    }

    private fun setTextViewMsg(tv: TextView, ec: Int) {
        if (ec != 0) {
            tv.setTextColor(Color.RED)
        } else {
            tv.setTextColor(Color.BLACK)
        }
        tv.text = HeaterProto.errMsg(ec)
    }
}