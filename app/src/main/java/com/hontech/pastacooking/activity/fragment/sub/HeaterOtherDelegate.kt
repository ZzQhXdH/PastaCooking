package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showHeaterPram
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.AppScope
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.model.HeaterStatus
import com.hontech.pastacooking.task.conn.ctrlWork
import com.hontech.pastacooking.task.conn.queryHeaterParam
import com.hontech.pastacooking.task.conn.testDeliveryFlag
import com.hontech.pastacooking.task.conn.testFan
import kotlinx.coroutines.launch

class HeaterOtherDelegate (view: View) {

    private val btnQuery = view.findViewById<Button>(R.id.id_btn_query)
    private val swFan = view.findViewById<SwitchCompat>(R.id.id_sw_fan)
    private val btnQuitDelivery = view.findViewById<Button>(R.id.id_btn_quit_delivery)
    private val btnEnterDelivery = view.findViewById<Button>(R.id.id_btn_enter_delivery)
    private val btnReset = view.findViewById<Button>(R.id.id_btn_reset)
    private val btnShutdown = view.findViewById<Button>(R.id.id_btn_shutdown)

    private var updateFlag = false

    init {
        btnQuery.onClick(::onQuery)
        btnQuitDelivery.onClick(::onQuitDelivery)
        btnEnterDelivery.onClick(::onEnterDelivery)
        btnReset.onClick(::onReset)
        btnShutdown.onClick(::onShutdown)

        swFan.setOnCheckedChangeListener {_, flag ->
            if (updateFlag) {
                return@setOnCheckedChangeListener
            }
            val value = if (flag) 0 else 1
            AppScope.launch {
                val w = showProg(btnQuery, "测试风扇")
                try {
                    testFan(value)
                    w.success("测试风扇成功")
                } catch (e: Exception) {
                    e.printStackTrace()
                    w.error(e.message!!)
                }
            }
        }
    }


    private suspend fun onShutdown() {
        val w = showProg(btnQuery, "停机")
        try {
            ctrlWork(1)
            w.success("停机成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onReset() {

        val w = showProg(btnQuery, "重启")
        try {
            ctrlWork(0)
            w.success("重启成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onEnterDelivery() {
        val w = showProg(btnQuery, "进入出货模式")
        try {
            testDeliveryFlag(1)
            w.success("进入出货模式成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onQuitDelivery() {
        val w = showProg(btnQuery, "退出出货模式")
        try {
            testDeliveryFlag(0)
            w.success("退出出货模式成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onQuery() {
        val w = showProg(btnQuery, "查询参数")
        try {
            val param = queryHeaterParam()
            w.dismiss()
            showHeaterPram(btnQuery, param)
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    fun update(status: HeaterStatus) {
        updateFlag = true
        swFan.isChecked = status.isFanOpen()
        updateFlag = false
    }
}