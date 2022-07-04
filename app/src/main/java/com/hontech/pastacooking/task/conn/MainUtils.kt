package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.ConnTask
import com.hontech.pastacooking.conn.Addr
import com.hontech.pastacooking.conn.Frame
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.except.AckException
import com.hontech.pastacooking.except.ResponseTimeoutException
import com.hontech.pastacooking.serial.ByteView
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt8
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

suspend fun requestMain(timeout: Long, req: Int, args: Array<SerialType>, exceptMsg: String) =
    suspendCoroutine<Unit> {
        ConnTask.post(GenericTask(timeout, Addr.Main, req, args, exceptMsg, it))
    }


suspend fun setCleanParam(c: Int, waterVolume: Int, d: Int, steamTime: Int, duration: Int) {
    val timeout = 5 * 1000L
    requestMain(
        timeout, MainProto.SetCleanParam,
        arrayOf(UInt16(c), UInt16(waterVolume), UInt16(d), UInt16(steamTime), UInt16((duration))),
        "设置清洗参数"
    )
}

suspend fun testPick(col: Int, row: Int) {
    val timeout = 180 * 1000L
    requestMain(
        timeout, MainProto.TestPick,
        arrayOf(UInt8(col), UInt8(row)),
        "测试取货"
    )
}


suspend fun requestMain2(timeout: Long, req: Int, args: Array<SerialType>) =
    suspendCoroutine<Frame> {
        val task = GenericTask2(timeout, Addr.Main, req, args, it)
        ConnTask.post(task)
    }

suspend fun testScanner(): String {
    val timeout = 5 * 1000L
    val frame = requestMain2(timeout, MainProto.TestScanner, arrayOf())
    val ec = UInt8()
    val bw = ByteView()
    frame.parse(ec, bw)
    if (!ec.isOk()) {
        throw IOException("扫码异常:${MainProto.errMsg(ec.value)}")
    }
    return bw.toString()
}

suspend fun scan() {

    mainPreProc()

    val timeout = 600 * 1000L
    requestMain(
        timeout,
        MainProto.Scan,
        arrayOf(),
        "扫码库存"
    )
}

class CleanParam(
    val c: Int,
    val waterVolume: Int,
    val d: Int,
    val steamTime: Int
)

class CleanParam2(
    val c: Int,
    val waterVolume: Int,
    val d: Int,
    val steamTime: Int,
    val duration: Int
)

suspend fun mainPreProc() {

    for (i in 0 until 3) {

        try {
            requestMain(8000, MainProto.PreProc, arrayOf(), "主控板预处理")
            return
        } catch (e: AckException) {
            e.printStackTrace()
        } catch (e: ResponseTimeoutException) {
            e.printStackTrace()
        }
    }

    throw IOException("主控制预处理失败")
}

suspend fun queryCleanParam(): CleanParam2 {
    val timeout = 3 * 1000L
    val frame = requestMain2(timeout, MainProto.QueryCleanParam, arrayOf())
    val c = UInt16()
    val waterVolume = UInt16()
    val d = UInt16()
    val steamTime = UInt16()
    val duration = UInt16()
    frame.parse(c, waterVolume, d, steamTime, duration)
    return CleanParam2(c.value, waterVolume.value, d.value, steamTime.value, duration.value)
}

suspend fun pickupCooking(col: Int, row: Int, clean: CleanParam, cook: CookingParam) {

    mainPreProc()

    val args: Array<SerialType> = arrayOf(
        UInt8(col), UInt8(row),
        UInt16(clean.c), UInt16(clean.waterVolume), UInt16(clean.d), UInt16(clean.steamTime),

        UInt16(cook.k), UInt16(cook.preWater), UInt16(cook.f), UInt16(cook.g),
        UInt16(cook.defroze), UInt16(cook.e), UInt16(cook.pourWater),
        UInt16(cook.h), UInt16(cook.mixsoup), UInt16(cook.i),
        UInt16(cook.clogging)
    )

    val timeout = 4 * 60 * 1000L

    for (i in 0 until 3) {
        try {
            requestMain(timeout, MainProto.PickupCooking, args, "取货煮面")
            return
        } catch (e: AckException) {
            e.printStackTrace()
        }
    }

    throw IOException("发送出货命令失败")
}

suspend fun deviceReset() {
    requestMain(60 * 1000L, MainProto.DeviceReset, arrayOf(), "设备复位")
}

suspend fun ledCtrl(type: Int, value: Int) {
    requestMain(3000, MainProto.TestLed, arrayOf(UInt8(type), UInt8(value)), "Led控制")
}

