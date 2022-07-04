package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.task.conn.adjWeight

class AdjWeightDelegate(view: View) {

    private val etWeight = view.findViewById<EditText>(R.id.id_et_weight)
    private val btnInside = view.findViewById<Button>(R.id.id_btn_adj_inside)
    private val btnExtern = view.findViewById<Button>(R.id.id_btn_adj_extern)

    init {
        btnInside.onClick(::onClickInside)
        btnExtern.onClick(::onClickExtern)
    }

    suspend fun onClickInside() {

        val w = showProg(btnInside, "校准内部电子秤")

        try {
            adjWeight(1, etWeight.toInt())
            w.success("校准成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    suspend fun onClickExtern() {
        val w = showProg(btnInside, "校准外部电子秤")

        try {
            adjWeight(2, etWeight.toInt())
            w.success("校准成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

}