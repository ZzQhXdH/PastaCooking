package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import com.hontech.pastacooking.R
import com.hontech.pastacooking.task.conn.heatOtaCtx
import com.hontech.pastacooking.task.conn.mainOtaCtx
import com.hontech.pastacooking.task.conn.weightOtaCtx
import com.hontech.pastacooking.utils.Http

class DeviceOtaDelegate(view: View) {

    private val mainDlg = FirmDelegate(
        view,
        R.id.id_rv_main_firm,
        R.id.id_btn_main_get,
        Http.FileTypeMain,
        mainOtaCtx
    )
    private val heatDlg = FirmDelegate(
        view,
        R.id.id_rv_heat_firm,
        R.id.id_btn_heat_get,
        Http.FileTypeHeater,
        heatOtaCtx
    )
    private val weightDlg = FirmDelegate(
        view,
        R.id.id_rv_weight_firm,
        R.id.id_btn_weight_get,
        Http.FileTypeWeighter,
        weightOtaCtx
    )

}