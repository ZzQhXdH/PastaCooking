package com.hontech.pastacooking.activity.fragment.sub

import android.view.View
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.view.CheckView
import com.hontech.pastacooking.model.MainStatus

class MainSensor(view: View) {

    private val cvRotateOrigin = view.findViewById<CheckView>(R.id.id_cv_rotate_origin)
    private val cvRotateFault = view.findViewById<CheckView>(R.id.id_cv_rotate_fault)
    private val cvElevatorOrigin = view.findViewById<CheckView>(R.id.id_cv_elevator_origin)
    private val cvElevatorTopOrigin = view.findViewById<CheckView>(R.id.id_cv_elevator_top_origin)
    private val cvElevatorFault = view.findViewById<CheckView>(R.id.id_cv_elevator_fault)

    private val cvFetchFault = view.findViewById<CheckView>(R.id.id_cv_fetch_fault)
    private val cvHeatOpen = view.findViewById<CheckView>(R.id.id_cv_heat_door_open)
    private val cvHeatClose = view.findViewById<CheckView>(R.id.id_cv_heat_door_close)
    private val cvExternOrigin = view.findViewById<CheckView>(R.id.id_cv_extern_push_origin)
    private val cvExternFault = view.findViewById<CheckView>(R.id.id_cv_extern_push_fault)

    private val cvPastaOpen = view.findViewById<CheckView>(R.id.id_cv_pasta_door_open)
    private val cvPastaClose = view.findViewById<CheckView>(R.id.id_cv_pasta_door_close)
    private val cvPickOpen = view.findViewById<CheckView>(R.id.id_cv_pick_door_open)
    private val cvPickClose = view.findViewById<CheckView>(R.id.id_cv_pick_door_close)
    private val cvPickRaster = view.findViewById<CheckView>(R.id.id_cv_pick_door_raster)

    private val cvPickProtect = view.findViewById<CheckView>(R.id.id_cv_pick_door_protect)
    private val cvExistPasta = view.findViewById<CheckView>(R.id.id_cv_pasta_exist)
    private val cvCargoKey = view.findViewById<CheckView>(R.id.id_cv_cargo_key)
    private val cvFridgeClose = view.findViewById<CheckView>(R.id.id_cv_fridge_close)
    private val cvDoorClose = view.findViewById<CheckView>(R.id.id_cv_door_close)

    private val cvThaw = view.findViewById<CheckView>(R.id.id_cv_thaw)
    private val cvClean = view.findViewById<CheckView>(R.id.id_cv_clean)

    fun update(status: MainStatus) {
        cvRotateOrigin.setChecked(status.isRotateOrigin())
        cvRotateFault.setChecked(status.isRotateFault())
        cvElevatorOrigin.setChecked(status.isElevatorOrigin())
        cvElevatorTopOrigin.setChecked(status.isElevatorTopOrigin())
        cvElevatorFault.setChecked(status.isElevatorFault())

        cvFetchFault.setChecked(status.isFetchFault())
        cvHeatOpen.setChecked(status.isHeatOpen())
        cvHeatClose.setChecked(status.isHeatClose())
        cvExternOrigin.setChecked(status.isExternOrigin())
        cvExternFault.setChecked(status.isExternFault())

        cvPastaOpen.setChecked(status.isPastaOpen())
        cvPastaClose.setChecked(status.isPastaClose())
        cvPickOpen.setChecked(status.isPickOpen())
        cvPickClose.setChecked(status.isPickClose())
        cvPickRaster.setChecked(status.isPickRaster())

        cvPickProtect.setChecked(status.isPickProtected())
        cvExistPasta.setChecked(status.isPastaExist())
        cvCargoKey.setChecked(status.isCargoKeyClicked())
        cvFridgeClose.setChecked(status.isFridgeDoorClose())
        cvDoorClose.setChecked(status.isDoorClose())

        cvThaw.setChecked(status.isThaw())
        cvClean.setChecked(status.isClean())
    }
}






















