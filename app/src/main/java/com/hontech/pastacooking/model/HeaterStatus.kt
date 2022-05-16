package com.hontech.pastacooking.model

import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt8

class HeaterStatus {

    val appVersion = UInt16()
    val nozzle = UInt8()
    val waterTemp = UInt16()
    val steamTemp = UInt16()
    val steamKpa = UInt16()
    val sensor = UInt16()
    val flag = UInt16()
    val count = UInt16()
    val workType = UInt8()

    fun isNozzleOrigin() = sensor.isSetBit(0)
    fun isNozzleFault() = sensor.isSetBit(1)
    fun isWaterTempFault() = sensor.isSetBit(2)
    fun isSteamTempFault() = sensor.isSetBit(3)
    fun isLeak() = sensor.isSetBit(4)
    fun isWaterLow() = sensor.isSetBit(5)
    fun isWaterHigh() = sensor.isSetBit(6)
    fun isSteamLow() = sensor.isSetBit(7)
    fun isSteamHigh() = sensor.isSetBit(8)
    fun isNozzleBottomOrigin() = sensor.isSetBit(9)

    fun isValve1Open() = flag.isSetBit(0)
    fun isValve2Open() = flag.isSetBit(1)
    fun isValve3Open() = flag.isSetBit(2)
    fun isValve4Open() = flag.isSetBit(3)
    fun isValve5Open() = flag.isSetBit(4)
    fun isValve6Open() = flag.isSetBit(5)
    fun isValve7Open() = flag.isSetBit(6)
    fun isValve8Open() = flag.isSetBit(7)
    fun isValve9Open() = flag.isSetBit(8)
    fun isWaterHeatOpen() = flag.isSetBit(9)
    fun isSteamHeatOpen() = flag.isSetBit(10)
    fun isWaterPumpOpen() = flag.isSetBit(11)
    fun isSteamPumpOpen() = flag.isSetBit(12)
    fun isNozzlePumpOpen() = flag.isSetBit(13)
    fun isFanOpen() = flag.isSetBit(14)
}
