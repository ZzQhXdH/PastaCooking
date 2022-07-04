package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.view.CheckView
import com.hontech.pastacooking.conn.MainProto

class FridgeDelegate(view: View) {

    private val cv0 = view.findViewById<CheckView>(R.id.id_cv_fridge_0)
    private val cv1 = view.findViewById<CheckView>(R.id.id_cv_fridge_1)
    private val cv2 = view.findViewById<CheckView>(R.id.id_cv_fridge_2)
    private val cv3 = view.findViewById<CheckView>(R.id.id_cv_fridge_3)
    private val cv4 = view.findViewById<CheckView>(R.id.id_cv_fridge_4)
    private val cv5 = view.findViewById<CheckView>(R.id.id_cv_fridge_5)
    private val cv6 = view.findViewById<CheckView>(R.id.id_cv_fridge_6)
    private val cv7 = view.findViewById<CheckView>(R.id.id_cv_fridge_7)
    private val cv8 = view.findViewById<CheckView>(R.id.id_cv_fridge_8)
    private val cv9 = view.findViewById<CheckView>(R.id.id_cv_fridge_9)

    private val tvTemp = view.findViewById<TextView>(R.id.id_tv_temp)

    fun update() {
        val s = MainProto.fridge

        cv0.setChecked(s.sw.isSetBit(0))
        cv1.setChecked(s.sw.isSetBit(1))
        cv2.setChecked(s.sw.isSetBit(2))
        cv3.setChecked(s.sw.isSetBit(3))
        cv4.setChecked(s.sw.isSetBit(4))
        cv5.setChecked(s.sw.isSetBit(5))
        cv6.setChecked(s.sw.isSetBit(6))
        cv7.setChecked(s.sw.isSetBit(7))
        cv8.setChecked(s.sw.isSetBit(8))
        cv9.setChecked(s.sw.isSetBit(9))
        tvTemp.text = "冰箱温度:${s.tempValue()}℃"
    }
}

