package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.task.conn.requestMain
import org.angmarch.views.NiceSpinner

class CargoElevatorDelegate (view: View) {

    private val spRows = view.findViewById<NiceSpinner>(R.id.id_sp_rows)
    private val btnReset = view.findViewById<Button>(R.id.id_btn_elevator_reset)
    private val btnHeat = view.findViewById<Button>(R.id.id_btn_elevator_heat)
    private val btnExec = view.findViewById<Button>(R.id.id_btn_elevator_exec)

    companion object {
        const val Timeout = 60 * 1000L
        const val Msg = "测试升降电机"
        const val Req = MainProto.TestCargoElevator
    }

    init {
        spRows.attachDataSource(mutableListOf(
            "1","2","3","4","5",
            "6","7","8","9","10"
        ))
        btnReset.onClick(::onReset)
        btnHeat.onClick(::onHeat)
        btnExec.onClick(::onExec)
    }

    private suspend fun exec(type: Int, row: Int) {
        val args: Array<SerialType> = arrayOf(UInt8(type), UInt8(row))
        requestMain(Timeout, Req, args, Msg)
    }

    private suspend fun onReset() {
        val w = showProg(spRows, "升降电机复位")
        try {
            exec(0, 1)
            w.success("升降电机复位成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onHeat() {
        val w = showProg(spRows, "升降电机移动到保温门")
        try {
            exec(2, 1)
            w.success("升降电机移动到保温门成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    private suspend fun onExec() {
        val row = spRows.selectedIndex + 1
        val w = showProg(spRows, "升降电机=>$row")
        try {
            exec(1, row)
            w.success("升降电机=>$row 成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

}