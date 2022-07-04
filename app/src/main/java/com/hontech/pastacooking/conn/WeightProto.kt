package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.event.WeightStatusEvent
import com.hontech.pastacooking.model.WeightStatus


object WeightProto {

    const val OtaStart = 0x02
    const val OtaTranslate = 0x03
    const val OtaComplete = 0x04

    const val Adj = 0x05

    val status = WeightStatus()

    fun onRecv(frame: Frame) {

        frame.parse(
            status.appVersion,
            status.sensor,
            status.inside,
            status.external
        )

        bus.post(WeightStatusEvent())
    }
}