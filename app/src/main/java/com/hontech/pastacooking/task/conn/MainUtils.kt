package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.WorkExecutor
import com.hontech.pastacooking.conn.Addr
import com.hontech.pastacooking.conn.Frame
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.serial.ByteView
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt8
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

suspend fun requestMain(timeout: Long, req: Int, args: Array<SerialType>, exceptMsg: String) = suspendCoroutine<Unit> {
    WorkExecutor.post(GenericTask(timeout, Addr.Main, req, args, exceptMsg, it))
}

suspend fun startMainOta(data: ByteArray) = suspendCoroutine<Unit> {
    val ctx = OtaCtx(Addr.Main, MainProto.OtaStart, MainProto.OtaTranslate, MainProto.OtaComplete)
    val task = OtaTask(ctx, data, it)
    WorkExecutor.post(task)
}

suspend fun setCleanParam(c: Int, waterVolume: Int, d: Int, steamTime: Int) {
    val timeout = 5 * 1000L
    requestMain(timeout, MainProto.SetCleanParam,
        arrayOf(UInt16(c), UInt16(waterVolume), UInt16(d), UInt16(steamTime)),
        "设置清洗参数")
}

suspend fun testPick(col: Int, row: Int) {
    val timeout = 180 * 1000L
    requestMain(timeout, MainProto.TestPick,
            arrayOf( UInt8(col), UInt8(row) ),
            "测试取货"
        )
}


suspend fun requestMain2(timeout: Long, req: Int, args: Array<SerialType>) = suspendCoroutine<Frame> {
    val task = GenericTask2(timeout, Addr.Main, req, args, it)
    WorkExecutor.post(task)
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
    val timeout = 180 * 1000L
    requestMain(timeout,
        MainProto.Scan,
        arrayOf(),
        "扫码库存"
    )
}

class CleanParam(val c: Int,
                 val waterVolume: Int,
                 val d: Int,
                 val steamTime: Int)

suspend fun queryCleanParam(): CleanParam {
    val timeout = 3 * 1000L
    val frame = requestMain2(timeout, MainProto.QueryCleanParam, arrayOf())
    val c = UInt16()
    val waterVolume = UInt16()
    val d = UInt16()
    val steamTime = UInt16()
    frame.parse(c, waterVolume, d, steamTime)
    return CleanParam(c.value, waterVolume.value, d.value, steamTime.value)
}

