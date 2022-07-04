package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.task.conn.requestMain
import org.angmarch.views.NiceSpinner

class CargoRotateDelegate(view: View) {

    val spCols = view.findViewById<NiceSpinner>(R.id.id_sp_cols)
    val btnReset = view.findViewById<Button>(R.id.id_btn_rotate_reset)
    val btnExec = view.findViewById<Button>(R.id.id_btn_rotate_exec)

    companion object {
        const val Timeout = 60 * 1000L
        const val Msg = "测试旋转货架"
        const val Req = MainProto.TestCargoRoate
    }

    private suspend fun exec(type: Int, col: Int) {
        requestMain(Timeout, Req, arrayOf(UInt8(type), UInt8(col)), Msg)
    }

    private suspend fun onReset() {
        val w = showProg(spCols, "旋转货架复位")
        try {
            exec(0, 1)
            w.success("旋转货架复位成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onExec() {
        val col = spCols.selectedIndex + 1
        val w = showProg(spCols, "旋转货架=>${col}")
        try {
            exec(1, col)
            w.success("旋转货架=>${col}成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    init {
        spCols.attachDataSource(
            mutableListOf(
                "1", "2", "3", "4", "5"
            )
        )
        btnReset.onClick(::onReset)
        btnExec.onClick(::onExec)
    }

}