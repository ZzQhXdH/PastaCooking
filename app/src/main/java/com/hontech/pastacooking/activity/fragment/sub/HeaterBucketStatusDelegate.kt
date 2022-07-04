package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.view.CheckView
import com.hontech.pastacooking.conn.WeightProto

class HeaterBucketStatusDelegate(view: View) {

    private val cvInsideExist = view.findViewById<CheckView>(R.id.id_cv_inside_exist)
    private val cvExternExist = view.findViewById<CheckView>(R.id.id_cv_extern_exist)
    private val cvInsideOk = view.findViewById<CheckView>(R.id.id_cv_inside_ok)
    private val cvExternOk = view.findViewById<CheckView>(R.id.id_cv_extern_ok)
    private val tvInside = view.findViewById<TextView>(R.id.id_tv_inside_w)
    private val tvExtern = view.findViewById<TextView>(R.id.id_tv_extern_w)

    fun update() {
        val s = WeightProto.status
        cvInsideExist.setChecked(s.isInsideExist())
        cvExternExist.setChecked(s.isExternExist())
        cvInsideOk.setChecked(s.isInsideOk())
        cvExternOk.setChecked(s.isExternOk())
        tvInside.text = "内部水桶重量:${s.inside}g"
        tvExtern.text = "外部水桶重量:${s.external}g"
    }

}