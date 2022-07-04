package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.task.conn.testPick
import org.angmarch.views.NiceSpinner

class TestPickupDelegate(view: View) {

    private val spCols = view.findViewById<NiceSpinner>(R.id.id_pickup_sp_cols)
    private val spRows = view.findViewById<NiceSpinner>(R.id.id_pickup_sp_rows)
    private val btnPickUp = view.findViewById<Button>(R.id.id_btn_only_pickup)

    init {
        spCols.attachDataSource(mutableListOf("1", "2", "3", "4", "5"))
        spRows.attachDataSource(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
        btnPickUp.onClick(::onPickUp)
    }

    private fun row(): Int {
        return spRows.selectedIndex + 1
    }

    private fun col(): Int {
        return spCols.selectedIndex + 1
    }

    suspend fun onPickUp() {
        val w = showProg(btnPickUp, "测试出货")

        try {
            testPick(col(), row())
            w.success("测试出货成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

}