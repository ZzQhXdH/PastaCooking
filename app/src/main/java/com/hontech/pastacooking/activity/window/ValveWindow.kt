package com.hontech.pastacooking.activity.window

import androidx.appcompat.widget.SwitchCompat
import com.hontech.pastacooking.R
import com.hontech.pastacooking.app.dimenById

val valveWindow: ValveWindow by lazy { ValveWindow() }

private val width = dimenById(R.dimen.x400)
private val height = dimenById(R.dimen.y340)

class ValveWindow : MoveableWindow(R.layout.window_valve, width, height) {

    private val sw1 = view.findViewById<SwitchCompat>(R.id.id_sw_valve1)
    private val sw2 = view.findViewById<SwitchCompat>(R.id.id_sw_valve2)
    private val sw3 = view.findViewById<SwitchCompat>(R.id.id_sw_valve3)
    private val sw4 = view.findViewById<SwitchCompat>(R.id.id_sw_valve4)
    private val sw5 = view.findViewById<SwitchCompat>(R.id.id_sw_valve5)
    private val sw6 = view.findViewById<SwitchCompat>(R.id.id_sw_valve6)
    private val sw7 = view.findViewById<SwitchCompat>(R.id.id_sw_valve7)
    private val sw8 = view.findViewById<SwitchCompat>(R.id.id_sw_valve8)
    private val sw9 = view.findViewById<SwitchCompat>(R.id.id_sw_valve9)

    init {
        sw1.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 1) }
        sw2.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 2) }
        sw3.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 3) }
        sw4.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 4) }
        sw5.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 5) }
        sw6.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 6) }
        sw7.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 7) }
        sw8.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 8) }
        sw9.setOnCheckedChangeListener { _, isChecked -> onCheckChanged(isChecked, 9) }
    }

    private fun onCheckChanged(check: Boolean, valveId: Int) {

    }

    fun update() {

    }
}