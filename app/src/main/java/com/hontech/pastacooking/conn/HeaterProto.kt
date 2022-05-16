package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.model.HeaterStatus
import com.hontech.pastacooking.model.HeaterStatusEvent

object HeaterProto {

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
    const val DeliveryCtrl = 0x13
    const val WorkCtrl = 0x14

    val status = HeaterStatus()

    private fun parseStatus(frame: Frame) {
        frame.parse(
            status.appVersion,
            status.nozzle,
            status.waterTemp,
            status.steamTemp,
            status.steamKpa,
            status.sensor,
            status.flag,
            status.count,
            status.workType
        )
        bus.post(HeaterStatusEvent())
    }

    fun onRecv(frame: Frame) {
        val req = frame.request()
        when (req) {
            StatusUpdate -> parseStatus(frame)
        }
    }

    fun errMsg(ec: Int): String {
        when (ec) {
            0x00 -> return "正常"
            0x01 -> return "Flash读写异常"
            0x02 -> return "Flash校验不通过"
            0x03 -> return "Flash擦除异常"
            0x04 -> return "协议头错误"
            0x05 -> return "协议size错误"
            0x06 -> return "协议尾部错误"
            0x07 -> return "协议校验和错误"
            0x08 -> return "参数解析失败"
            0x09 -> return "无效的参数"
            0x0A -> return "Ota Id无效"
            0x0B -> return "Ota Md5校验失败"
            0x0C -> return "温度传感器异常"
            0x0D -> return "电机堵转"
            0x0E -> return "抽水超时"
            0x0F -> return "电机超时"
            else -> return "未知错误"
        }
    }

    fun workMsg(type: Int): String {
        when (type) {
            0x00 -> return "抽开水锅炉的水"
            0x01 -> return "抽蒸汽锅炉的水"
            0x02 -> return "加热"
            0x03 -> return "空闲"
            0x04 -> return "停机"
            0x05 -> return "等待水桶有水"
            0x06 -> return "等待出货完成"
            else -> return "未知模式"
        }
    }
}




















