package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.window.ProgWindow
import com.hontech.pastacooking.activity.window.showDialog
import com.hontech.pastacooking.activity.window.showProg
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.onClick
import com.hontech.pastacooking.app.toInt
import com.hontech.pastacooking.conn.HeaterProto
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.event.CookProgEvent
import com.hontech.pastacooking.task.conn.CleanParam
import com.hontech.pastacooking.task.conn.CookingParam
import com.hontech.pastacooking.task.conn.deviceReset
import com.hontech.pastacooking.task.conn.pickupCooking
import org.angmarch.views.NiceSpinner
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PickUpCookingDelegate(view: View) {

    private val cookingParamView = view.findViewById<View>(R.id.id_cooking_param)
    private val cleanParamView = view.findViewById<View>(R.id.id_clean_param)

    private val etK = cookingParamView.findViewById<EditText>(R.id.id_input_k)
    private val etPreWater = cookingParamView.findViewById<EditText>(R.id.id_input_pre_water)
    private val etF = cookingParamView.findViewById<EditText>(R.id.id_input_f)
    private val etG = cookingParamView.findViewById<EditText>(R.id.id_input_g)
    private val etDefroze = cookingParamView.findViewById<EditText>(R.id.id_input_defroze)
    private val etE = cookingParamView.findViewById<EditText>(R.id.id_input_e)
    private val etPourWater = cookingParamView.findViewById<EditText>(R.id.id_input_pour_water)
    private val etH = cookingParamView.findViewById<EditText>(R.id.id_input_h)
    private val etMixsoup = cookingParamView.findViewById<EditText>(R.id.id_input_mixsoup)
    private val etI = cookingParamView.findViewById<EditText>(R.id.id_input_i)
    private val etClogging = cookingParamView.findViewById<EditText>(R.id.id_input_clogging)

    private val etC = cleanParamView.findViewById<EditText>(R.id.id_input_c)
    private val etCleanPourWater = cleanParamView.findViewById<EditText>(R.id.id_input_pour_water)
    private val etD = cleanParamView.findViewById<EditText>(R.id.id_input_d)
    private val etSteamTime = cleanParamView.findViewById<EditText>(R.id.id_input_steam_time)

    private val spCol = view.findViewById<NiceSpinner>(R.id.sp_cooking_col)
    private val spRow = view.findViewById<NiceSpinner>(R.id.id_sp_cooking_row)
    private val btnCooking = view.findViewById<Button>(R.id.id_btn_pickup_cooking)
    private val btnReset = view.findViewById<Button>(R.id.id_btn_dev_reset)
    private var progWin: ProgWindow? = null
    private var cookingFlag = false

    init {
        spCol.attachDataSource(mutableListOf("1", "2", "3", "4", "5"))
        spRow.attachDataSource(mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"))
        btnCooking.onClick(::onClickCooking)
        btnReset.onClick(::onClickReset)
    }

    suspend fun onClickReset() {

        val w = showProg(btnCooking, "设备复位")

        try {
            deviceReset()
            w.success("设备复位成功")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun onProgEvent(env: CookProgEvent) {
        progWin?.show(btnCooking, env.msg)
    }

    suspend fun onClickCooking() {

        if (cookingFlag) {
            return
        }

        val s = HeaterProto.status
        if (!s.isHeatMode()) {
            showDialog(btnCooking, "锅炉还未开始加热")
        }

        if (!s.isWaterTempOk()) {
            showDialog(btnCooking, "开始锅炉温度不够:${s.waterTemp.value}℃")
        }

        if (!s.isSteamKpaOk()) {
            showDialog(btnCooking, "蒸汽锅炉压力不够:${s.steamKpa.value}KPA")
        }

        if (MainProto.status.isClean()) {
            showDialog(btnCooking, "设备处于清洗模式,继续吗?")
        }

        if (MainProto.status.isThaw()) {
            showDialog(btnCooking, "设备处于解冻模式,继续吗?")
        }

        val w = showProg(btnCooking, "煮面")
        progWin = w
        bus.register(this)
        try {
            cookingFlag = true
            val start = System.currentTimeMillis()
            pickupCooking(col(), row(), cleanParam(), cookingParam())
            val duration = System.currentTimeMillis() - start
            w.success("出货成功 耗时:${duration/1000}秒")
        } catch (e: Exception) {
            e.printStackTrace()
            w.error(e.message!!)
        } finally {
            bus.unregister(this)
            cookingFlag = false
            progWin = null
        }
    }

    fun col(): Int {
        return spCol.selectedIndex + 1
    }

    fun row(): Int {
        return spRow.selectedIndex + 1
    }

    fun cookingParam(): CookingParam {
        return CookingParam(
            etK.toInt(),
            etPreWater.toInt(),
            etF.toInt(),
            etG.toInt(),
            etDefroze.toInt(),
            etE.toInt(),
            etPourWater.toInt(),
            etH.toInt(),
            etMixsoup.toInt(),
            etI.toInt(),
            etClogging.toInt()
        )
    }

    fun cleanParam(): CleanParam {
        return CleanParam(
            etC.toInt(),
            etCleanPourWater.toInt(),
            etD.toInt(),
            etSteamTime.toInt()
        )
    }

}