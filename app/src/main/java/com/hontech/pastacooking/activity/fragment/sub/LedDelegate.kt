package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.AppScope
import com.hontech.pastacooking.task.conn.ledCtrl
import kotlinx.coroutines.launch

class LedDelegate (view: View) {

    private val swPastaLed = view.findViewById<SwitchCompat>(R.id.id_sw_pasta_led)
    private val swCargoLed = view.findViewById<SwitchCompat>(R.id.id_sw_fridge_led)
    private val swExternLed = view.findViewById<SwitchCompat>(R.id.id_sw_extern_led)
    private val swTableLed = view.findViewById<SwitchCompat>(R.id.id_sw_table_led)

    init {
        swPastaLed.setOnCheckedChangeListener { _, flag ->
            val value = if (flag) 0 else 1
            AppScope.launch {
                exec(1, value, "煮面室LED")
            }
        }

        swTableLed.setOnCheckedChangeListener { _, flag ->
            val value = if (flag) 0 else 1
            AppScope.launch {
                exec(2, value, "餐具口LED")
            }
        }

        swCargoLed.setOnCheckedChangeListener { _, flag ->
            val value = if (flag) 0 else 1
            AppScope.launch {
                exec(3, value, "冰箱LED")
            }
        }

        swExternLed.setOnCheckedChangeListener { _, flag ->
            val value = if (flag) 0 else 1
            AppScope.launch {
                exec(4, value, "外部照明LED")
            }
        }
    }

    private suspend fun exec(type: Int, value: Int, name: String) {

        val msg = if (value == 0) "打开" else "关闭"

        val w = showProg(swCargoLed, "$name:$msg")

        try {
            ledCtrl(type, value)
            w.success("$name:$msg 成功" )
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

}