package com.hontech.pastacooking.model

import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt32
import com.hontech.pastacooking.serial.UInt8

class MainStatus {

    val appVersion = UInt16()
    val sw2 = UInt32()
    val position = UInt16()
    val ch11 = UInt16()
    val ch12 = UInt16()
    val rotate = UInt8()
    val elevator = UInt8()
    val fetch = UInt8()
    val heat = UInt8()
    val pasta = UInt8()
    val extern = UInt8()
    val pick = UInt8()

    fun isRotateOrigin() = sw2.isSetBit(0)
    fun isRotateFault() = sw2.isSetBit(1)
    fun isElevatorOrigin() = sw2.isSetBit(2)
    fun isElevatorTopOrigin() = sw2.isSetBit(3)
    fun isElevatorFault() = sw2.isSetBit(4)
    fun isFetchFault() = sw2.isSetBit(5)
    fun isHeatOpen() = sw2.isSetBit(6)
    fun isHeatClose() = sw2.isSetBit(7)
    fun isExternOrigin() = sw2.isSetBit(8)
    fun isExternFault() = sw2.isSetBit(9)
    fun isPastaOpen() = sw2.isSetBit(10)
    fun isPastaClose() = sw2.isSetBit(11)
    fun isPickOpen() = sw2.isSetBit(12)
    fun isPickClose() = sw2.isSetBit(13)
    fun isPickRaster() = sw2.isSetBit(14)
    fun isPickProtected() = sw2.isSetBit(15)
    fun isPastaExist() = sw2.isSetBit(16)
    fun isCargoKeyClicked() = sw2.isSetBit(17)
    fun isFridgeDoorClose() = sw2.isSetBit(18)
    fun isDoorClose() = sw2.isSetBit(19)

    fun isThaw() = sw2.isSetBit(20)
    fun isClean() = sw2.isSetBit(21)
}
