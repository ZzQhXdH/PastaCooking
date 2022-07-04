package com.hontech.pastacooking.model

import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt32
import com.hontech.pastacooking.serial.UInt8

class WeightStatus {

    val appVersion = UInt16()
    val sensor = UInt8()
    val inside = UInt32()
    val external = UInt32()

    fun isInsideOk() = sensor.isSetBit(0)
    fun isExternOk() = sensor.isSetBit(1)
    fun isInsideExist() = sensor.isSetBit(2)
    fun isExternExist() = sensor.isSetBit(3)

}