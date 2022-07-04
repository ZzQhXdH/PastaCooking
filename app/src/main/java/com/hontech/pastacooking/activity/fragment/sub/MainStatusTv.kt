package com.hontech.pastacooking.activity.fragment.sub

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.model.MainStatus
import com.hontech.pastacooking.serial.UInt8

class MainStatusTv(view: View) {

    private val tvPosition = view.findViewById<TextView>(R.id.id_tv_position)
    private val tvAdc11 = view.findViewById<TextView>(R.id.id_tv_adc11)
    private val tvAdc12 = view.findViewById<TextView>(R.id.id_tv_adc12)
    private val tvRotate = view.findViewById<TextView>(R.id.id_tv_rotate)
    private val tvElevator = view.findViewById<TextView>(R.id.id_tv_elevator)
    private val tvFetch = view.findViewById<TextView>(R.id.id_tv_fetch)
    private val tvHeat = view.findViewById<TextView>(R.id.id_tv_heat)
    private val tvPasta = view.findViewById<TextView>(R.id.id_tv_pasta)
    private val tvExtern = view.findViewById<TextView>(R.id.id_tv_extern)
    private val tvPick = view.findViewById<TextView>(R.id.id_tv_pick)

    fun update(status: MainStatus) {
        tvPosition.text = "${status.position}"
        tvAdc11.text = "${status.ch11}"
        tvAdc12.text = "${status.ch12}"

        setTextErrMsg(tvRotate, status.rotate)
        setTextErrMsg(tvElevator, status.elevator)
        setTextErrMsg(tvFetch, status.fetch)
        setTextErrMsg(tvHeat, status.heat)
        setTextErrMsg(tvPasta, status.pasta)
        setTextErrMsg(tvExtern, status.extern)
        setTextErrMsg(tvPick, status.pick)
    }

    private fun setTextErrMsg(textView: TextView, value: UInt8) {
        if (value.isOk()) {
            textView.setTextColor(Color.BLACK)
        } else {
            textView.setTextColor(Color.RED)
        }
        textView.text = value.mainErrMsg()
    }
}