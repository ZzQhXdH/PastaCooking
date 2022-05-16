package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.event.MainExceptEvent
import com.hontech.pastacooking.ext.MainStatusEvent
import com.hontech.pastacooking.model.FridgeStatus
import com.hontech.pastacooking.model.MainStatus
import com.hontech.pastacooking.serial.ByteView
import com.hontech.pastacooking.serial.UInt8

object MainProto {

    const val Except = 0x01
    const val OtaStart = 0x02
    const val OtaTranslate = 0x03
    const val OtaComplete = 0x04
    const val FridgeStatusUpload = 0x05
    const val StatusUpload = 0x06
    const val TestCargoRoate = 0x07
    const val TestCargoElevator = 0x08
    const val TestCargoFetch = 0x09
    const val TestHeatDoor = 0x0A
    const val TestExternPush = 0x0B
    const val TestPastaDoor = 0x0C
    const val TestPickDoor = 0x0D
    const val TestPick = 0x0E
    const val TestScanner = 0x0F
    const val Scan = 0x10
    const val SetCleanParam = 0x11
    const val QueryCleanParam = 0x12

    val status = MainStatus()
    val fridge = FridgeStatus()

    fun errMsg(ec: Int): String {
        when(ec) {
            0x00 -> return "正常"
            0x01 -> return "Flash读写异常"
            0x02 -> return "Flash校验不通过"
            0x03 -> return "Flash擦除异常"
            0x04 -> return "协议头错误"
            0x05 -> return "协议Size错误"
            0x06 -> return "协议尾部错误"
            0x07 -> return "协议校验和错误"
            0x08 -> return "参数解析失败"
            0x09 -> return "无效的参数"
            0x0A -> return "Ota Id无效"
            0x0B -> return "Ota Md5 校验失败"
            0x0C -> return "电机堵转"
            0x0D -> return "电机超时"
            0x0E -> return "温控器通信超时"
            0x0F -> return "温控器数据异常"
            0x10 -> return "光栅被遮挡"
            0x11 -> return "防夹手保护"
            0x12 -> return "扫码异常"
            0x13 -> return "清洗超时"
            0x14 -> return "煮面超时"
            0x15 -> return "加热板通信超时"
            0x16 -> return "煮面室内有东西"
            0x17 -> return "设置出货模式超时"
            else -> return "未知错误"
        }
    }

    private fun parseStatus(frame: Frame) {
        frame.parse(
            status.appVersion,
            status.sw2,
            status.position,
            status.ch11,
            status.ch12,
            status.rotate,
            status.elevator,
            status.fetch,
            status.heat,
            status.pasta,
            status.extern,
            status.pick
        )
        bus.post(MainStatusEvent())
    }

    private fun parseFridge(frame: Frame) {
        frame.parse(fridge.sw, fridge.temp)
    }

    private fun onExcept(frame: Frame) {
        val ec = UInt8()
        val msg = ByteView()
        frame.parse(ec, msg)
        bus.post(MainExceptEvent(ec.value, msg.toString()))
    }

    fun onRecv(frame: Frame) {
        val req = frame.request()

        when (req) {
            Except -> onExcept(frame)
            StatusUpload -> parseStatus(frame)
            FridgeStatusUpload -> parseFridge(frame)
        }
    }
}