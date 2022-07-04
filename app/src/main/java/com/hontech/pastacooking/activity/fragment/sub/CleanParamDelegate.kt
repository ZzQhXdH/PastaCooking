package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.queryCleanParam
import com.hontech.pastacooking.task.conn.setCleanParam

class CleanParamDelegate(view: View) {

    private val etC = view.findViewById<EditText>(R.id.id_query_c)
    private val etWater = view.findViewById<EditText>(R.id.id_query_water)
    private val etD = view.findViewById<EditText>(R.id.id_query_d)
    private val etSteam = view.findViewById<EditText>(R.id.id_query_steam)
    private val etDuration = view.findViewById<EditText>(R.id.id_query_duration)

    private val btnSet = view.findViewById<Button>(R.id.id_btn_clean_set)
    private val btnGet = view.findViewById<Button>(R.id.id_btn_clean_query)

    init {
        btnSet.onClick(::onClickSet)
        btnGet.onClick(::onClickGet)
    }

    private suspend fun onClickSet() {

        val w = showProg(btnGet, "设置清洗参数")

        try {
            setCleanParam(
                etC.toInt(),
                etWater.toInt(),
                etD.toInt(),
                etSteam.toInt(),
                etDuration.toInt()
            )
            w.success("设置成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onClickGet() {
        val w = showProg(btnGet, "查询清洗参数")

        try {
            val param = queryCleanParam()
            etC.setText("${param.c}")
            etWater.setText("${param.waterVolume}")
            etD.setText("${param.d}")
            etSteam.setText("${param.steamTime}")
            etDuration.setText("${param.duration}")
            w.success("查询成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}