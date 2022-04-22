package com.hontech.pastacooking.conn

import com.hontech.pastacooking.serial.SerialType

object HeatorProto {

    const val Except = 0x01
    const val OtaStart = 0x02
    const val OtaTranslate = 0x03
    const val OtaComplete = 0x04
    const val TestValve = 0x05
    const val TestPump = 0x06
    const val TestHeator = 0x07
    const val TestNozzle = 0x08
    const val TestDraw = 0x09
    const val StatusUpdate = 0x0A
    const val SetWaterParam = 0x0B
    const val SetSteamParam = 0x0C
    const val SetDrawParam = 0x0D
    const val SetFlowParam = 0x0E
    const val QueryParam = 0x0F
    const val Clean = 0x10
    const val Cooking = 0x11
    const val TestFan = 0x12
    const val DrawCtrl = 0x13
    const val WorkCtrl = 0x14


    fun make(req: Int, vararg args: SerialType): ByteArray {
        return Proto.make(Addr.Heator, req, *args)
    }
}