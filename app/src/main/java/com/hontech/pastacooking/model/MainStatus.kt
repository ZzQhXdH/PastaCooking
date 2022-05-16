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

class MainStatus2(status: MainStatus) {

    val appVersion = status.appVersion.value
    val position = status.position.value
    val ch11 = status.ch11.value
    val ch12 = status.ch12.value
    val rotate = status.rotate.mainErrMsg()
    val elevator = status.elevator.mainErrMsg()
    val fetch = status.fetch.mainErrMsg()
    val heat = status.heat.mainErrMsg()
    val pasta = status.pasta.mainErrMsg()
    val extern = status.extern.mainErrMsg()
    val pick = status.pick.mainErrMsg()

    val rotateOrigin = status.isRotateOrigin()
    val rotateFault = status.isRotateFault()
    val elevatorOrigin = status.isElevatorOrigin()
    val elevatorTopOrigin = status.isElevatorTopOrigin()
    val elevatorFault = status.isElevatorFault()
    val fetchFault = status.isFetchFault()
    val heatOpen = status.isHeatOpen()
    val heatClose = status.isHeatClose()
    val externOrigin = status.isExternOrigin()
    val externFault = status.isExternFault()
    val pastaClose = status.isPastaClose()
    val pastaOpen = status.isPastaOpen()
    val pickOpen = status.isPickOpen()
    val pickClose = status.isPickClose()
    val pickRaster = status.isPickRaster()
    val pickProtected = status.isPickProtected()
    val pastaExist = status.isPastaExist()
    val cargoKey = status.isCargoKeyClicked()
    val fridgeClose = status.isFridgeDoorClose()
    val doorClose = status.isDoorClose()
    val thaw = status.isThaw()
    val clean = status.isClean()
}
