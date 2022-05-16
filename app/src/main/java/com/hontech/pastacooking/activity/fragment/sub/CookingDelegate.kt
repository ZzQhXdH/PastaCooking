package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.CookingParam
import com.hontech.pastacooking.task.conn.cooking

class CookingDelegate (sub: View) {
    private val view = sub.findViewById<View>(R.id.id_sub_cooking)

    private val inputK = view.findViewById<EditText>(R.id.id_input_k)
    private val inputPreWater = view.findViewById<EditText>(R.id.id_input_pre_water)
    private val inputF = view.findViewById<EditText>(R.id.id_input_f)
    private val inputG = view.findViewById<EditText>(R.id.id_input_g)
    private val inputDefroze = view.findViewById<EditText>(R.id.id_input_defroze)
    private val inputE = view.findViewById<EditText>(R.id.id_input_e)
    private val inputPourWater = view.findViewById<EditText>(R.id.id_input_pour_water)
    private val inputH = view.findViewById<EditText>(R.id.id_input_h)
    private val inputMixsoup = view.findViewById<EditText>(R.id.id_input_mixsoup)
    private val inputI = view.findViewById<EditText>(R.id.id_input_i)
    private val inputClogging = view.findViewById<EditText>(R.id.id_input_clogging)
    private val btnCooking = view.findViewById<Button>(R.id.id_btn_cooking)

    init {
        btnCooking.onClick(::onCooking)
    }

    private suspend fun onCooking() {
        val w = showProg(btnCooking, "煮面")
        try {
            val k = inputK.toInt()
            val preWater = inputPreWater.toInt()
            val f = inputF.toInt()
            val g = inputG.toInt()
            val defroze = inputDefroze.toInt()
            val e = inputE.toInt()
            val pourWater = inputPourWater.toInt()
            val h = inputH.toInt()
            val mixsoup = inputMixsoup.toInt()
            val i = inputI.toInt()
            val clogging = inputClogging.toInt()
            val param = CookingParam(k, preWater, f, g, defroze, e, pourWater, h, mixsoup, i, clogging)
            cooking(param)
            w.success("煮面成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }
}