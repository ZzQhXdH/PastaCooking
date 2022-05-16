package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.ProgWindow
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.AppScope
import com.hontech.pastacooking.model.HeaterStatus
import com.hontech.pastacooking.task.conn.testValve
import kotlinx.coroutines.launch

class ValveDelegate (view: View) {

    private val v1 = view.findViewById<SwitchCompat>(R.id.id_sw_valve1)
    private val v2 = view.findViewById<SwitchCompat>(R.id.id_sw_valve2)
    private val v3 = view.findViewById<SwitchCompat>(R.id.id_sw_valve3)
    private val v4 = view.findViewById<SwitchCompat>(R.id.id_sw_valve4)
    private val v5 = view.findViewById<SwitchCompat>(R.id.id_sw_valve5)
    private val v6 = view.findViewById<SwitchCompat>(R.id.id_sw_valve6)
    private val v7 = view.findViewById<SwitchCompat>(R.id.id_sw_valve7)
    private val v8 = view.findViewById<SwitchCompat>(R.id.id_sw_valve8)
    private val v9 = view.findViewById<SwitchCompat>(R.id.id_sw_valve9)

    private var updateFlag = false

    init {
        v1.setOnCheckedChangeListener { _, isChecked -> onCheck(1, isChecked) }
        v2.setOnCheckedChangeListener { _, isChecked -> onCheck(2, isChecked) }
        v3.setOnCheckedChangeListener { _, isChecked -> onCheck(3, isChecked) }
        v4.setOnCheckedChangeListener { _, isChecked -> onCheck(4, isChecked) }
        v5.setOnCheckedChangeListener { _, isChecked -> onCheck(5, isChecked) }
        v6.setOnCheckedChangeListener { _, isChecked -> onCheck(6, isChecked) }
        v7.setOnCheckedChangeListener { _, isChecked -> onCheck(7, isChecked) }
        v8.setOnCheckedChangeListener { _, isChecked -> onCheck(8, isChecked) }
        v9.setOnCheckedChangeListener { _, isChecked -> onCheck(9, isChecked) }
    }

    private fun onCheck(id: Int, flag: Boolean) {
        if (updateFlag) {
            return
        }
        val value = if (flag) 0 else 1
        AppScope.launch {
            val w = showProg(v1, "测试电磁阀$id")
            try {
                testValve(id, value)
                w.success("电磁阀${id}操作成功")
            } catch (e: Exception) {
                e.printStackTrace()
                w.error(e.message!!)
            }
        }
    }

    fun update(status: HeaterStatus) {
        updateFlag = true
        v1.isChecked = status.isValve1Open()
        v2.isChecked = status.isValve2Open()
        v3.isChecked = status.isValve3Open()
        v4.isChecked = status.isValve4Open()
        v5.isChecked = status.isValve5Open()
        v6.isChecked = status.isValve6Open()
        v7.isChecked = status.isValve7Open()
        v8.isChecked = status.isValve8Open()
        v9.isChecked = status.isValve9Open()
        updateFlag = false
    }

}