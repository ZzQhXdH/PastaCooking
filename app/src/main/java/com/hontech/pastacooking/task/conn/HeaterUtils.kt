package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.ConnTask
import com.hontech.pastacooking.conn.Addr
import com.hontech.pastacooking.conn.Frame
import com.hontech.pastacooking.conn.HeaterProto
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt8
import kotlin.coroutines.suspendCoroutine

suspend fun requestHeater(timeout: Long, req: Int, args: Array<SerialType>, exceptMsg: String) =
    suspendCoroutine<Unit> {
        ConnTask.post(GenericTask(timeout, Addr.Heator, req, args, exceptMsg, it))
    }

suspend fun requestHeater2(timeout: Long, req: Int, args: Array<SerialType>) =
    suspendCoroutine<Frame> {
        val task = GenericTask2(timeout, Addr.Heator, req, args, it)
        ConnTask.post(task)
    }

suspend fun testValve(id: Int, value: Int) {
    val timeout = 4 * 1000L
    val req = HeaterProto.TestValve
    val arg: Array<SerialType> = arrayOf(UInt8(id), UInt8(value))
    requestHeater(timeout, req, arg, "测试电磁阀")
}

suspend fun testPump(id: Int, value: Int) {
    val timeout = 4 * 1000L
    val req = HeaterProto.TestPump
    val arg: Array<SerialType> = arrayOf(UInt8(id), UInt8(value))
    requestHeater(timeout, req, arg, "测试水泵")
}

suspend fun testHeater(id: Int, value: Int) {
    val timeout = 4 * 1000L
    val req = HeaterProto.TestHeator
    val arg: Array<SerialType> = arrayOf(UInt8(id), UInt8(value))
    requestHeater(timeout, req, arg, "测试加热器")
}

suspend fun testNozzle(type: Int, position: Int) {
    val timeout = 15 * 1000L
    val req = HeaterProto.TestNozzle
    val arg: Array<SerialType> = arrayOf(UInt8(type), UInt16(position))
    requestHeater(timeout, req, arg, "测试喷嘴")
}

suspend fun testNozzleDraw(ml: Int) {
    val timeout = 30 * 1000L
    val req = HeaterProto.TestDraw
    val arg: Array<SerialType> = arrayOf(UInt16(ml))
    requestHeater(timeout, req, arg, "测试喷嘴抽水")
}

suspend fun setWaterHeatParam(temp: Int, timeout: Int) {
    val tm = 5 * 1000L
    val req = HeaterProto.SetWaterParam
    val arg: Array<SerialType> = arrayOf(UInt16(temp), UInt8(timeout))
    requestHeater(tm, req, arg, "设置开水锅炉加热参数")
}

suspend fun setSteamHeatParam(temp: Int, kpa: Int, timeout: Int) {
    val tm = 5 * 1000L
    val req = HeaterProto.SetSteamParam
    val arg: Array<SerialType> = arrayOf(UInt16(temp), UInt16(kpa), UInt8(timeout))
    requestHeater(tm, req, arg, "设置蒸汽锅炉加热参数")
}

suspend fun setDrawParam(waterTimeout: Int, steamTimeout: Int) {
    val timeout = 5 * 1000L
    val req = HeaterProto.SetDrawParam
    val arg: Array<SerialType> = arrayOf(UInt8(waterTimeout), UInt8(steamTimeout))
    requestHeater(timeout, req, arg, "设置抽水参数")
}

suspend fun setFlowParam(flow: Int) {
    val timeout = 5 * 1000L
    val req = HeaterProto.SetFlowParam
    val arg: Array<SerialType> = arrayOf(UInt16(flow))
    requestHeater(timeout, req, arg, "设置流量计参数")
}

class HeaterParam(
    val waterTemp: Int,
    val waterTimeout: Int,
    val steamTemp: Int,
    val steamKpa: Int,
    val steamTimeout: Int,
    val drawWaterTimeout: Int,
    val drawSteamTimeout: Int,
    val flow: Int
)

suspend fun queryHeaterParam(): HeaterParam {
    val timeout = 3 * 1000L
    val req = HeaterProto.QueryParam
    val frame = requestHeater2(timeout, req, arrayOf())
    val waterTemp = UInt16()
    val waterTimeout = UInt8()
    val steamTemp = UInt16()
    val steamKpa = UInt16()
    val steamTimeout = UInt8()
    val drawWaterTimeout = UInt8()
    val drawSteamTimeout = UInt8()
    val flow = UInt16()
    frame.parse(
        waterTemp,
        waterTimeout,
        steamTemp,
        steamKpa,
        steamTimeout,
        drawWaterTimeout,
        drawSteamTimeout,
        flow
    )
    return HeaterParam(
        waterTemp.value,
        waterTimeout.value,
        steamTemp.value,
        steamKpa.value,
        steamTimeout.value,
        drawWaterTimeout.value,
        drawSteamTimeout.value,
        flow.value
    )
}

suspend fun heatPreproc() {
    val timeout = 3 * 2000L
    requestHeater(timeout, HeaterProto.PreProc, arrayOf(), "加热板预处理")
}


suspend fun clean(c: Int, waterVolume: Int, d: Int, steamTime: Int) {
    heatPreproc()

    val timeout = 60 * 1000L
    val req = HeaterProto.Clean
    val arg: Array<SerialType> =
        arrayOf(UInt16(c), UInt16(waterVolume), UInt16(d), UInt16((steamTime)))
    requestHeater(timeout, req, arg, "清洗")
}

class CookingParam(
    val k: Int,
    val preWater: Int,
    val f: Int,
    val g: Int,
    val defroze: Int,
    val e: Int,
    val pourWater: Int,
    val h: Int,
    val mixsoup: Int,
    val i: Int,
    val clogging: Int
)

suspend fun cooking(param: CookingParam) {
    heatPreproc()

    val timeout = 180 * 1000L
    val req = HeaterProto.Cooking
    val arg: Array<SerialType> = arrayOf(
        UInt16(param.k),
        UInt16(param.preWater),
        UInt16(param.f),
        UInt16(param.g),
        UInt16(param.defroze),
        UInt16(param.e),
        UInt16(param.pourWater),
        UInt16(param.h),
        UInt16(param.mixsoup),
        UInt16(param.i),
        UInt16(param.clogging)
    )
    requestHeater(timeout, req, arg, "煮面")
}

suspend fun testFan(value: Int) {
    val timeout = 5 * 1000L
    val req = HeaterProto.TestFan
    val arg: Array<SerialType> = arrayOf(
        UInt8(value)
    )
    requestHeater(timeout, req, arg, "测试风扇")
}

suspend fun testDeliveryFlag(value: Int) {
    val timeout = 5 * 1000L
    val req = HeaterProto.DeliveryCtrl
    val arg: Array<SerialType> = arrayOf(
        UInt8(value)
    )
    requestHeater(timeout, req, arg, "出货模式标记")
}

suspend fun ctrlWork(value: Int) {
    val timeout = 5 * 1000L
    val req = HeaterProto.WorkCtrl
    val arg: Array<SerialType> = arrayOf(
        UInt8(value)
    )
    requestHeater(timeout, req, arg, "加热/抽水控制")
}












