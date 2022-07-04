package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.TextView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.view.CheckView
import com.hontech.pastacooking.conn.WeightProto

class WeightStatusDelegate(view: View) {

    private val tvInsideWeight = view.findViewById<TextView>(R.id.id_tv_inside_weight)
    private val tvExternWeight = view.findViewById<TextView>(R.id.id_tv_extern_weight)

    private val cvInside = view.findViewById<CheckView>(R.id.id_cv_inside)
    private val cvExtern = view.findViewById<CheckView>(R.id.id_cv_extern)
    private val cvInsideOk = view.findViewById<CheckView>(R.id.id_cv_inside_ok)
    private val cvExternOk = view.findViewById<CheckView>(R.id.id_cv_extern_ok)

    fun update() {
        val status = WeightProto.status

        tvInsideWeight.text = "${status.inside.value}g"
        tvExternWeight.text = "${status.external.value}g"

        cvInside.setChecked(status.isInsideExist())
        cvExtern.setChecked(status.isExternExist())

        cvInsideOk.setChecked(status.isInsideOk())
        cvExternOk.setChecked(status.isExternOk())
    }
}