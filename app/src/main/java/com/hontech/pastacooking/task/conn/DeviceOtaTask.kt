package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.ConnTask
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.conn.*
import com.hontech.pastacooking.event.OtaProgEvent
import com.hontech.pastacooking.serial.ByteView
import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt32
import java.io.IOException
import java.security.MessageDigest
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OtaCtx(val addr: Int, val start: Int, val translate: Int, val complete: Int)

val mainOtaCtx =
    OtaCtx(Addr.Main, MainProto.OtaStart, MainProto.OtaTranslate, MainProto.OtaComplete)
val heatOtaCtx =
    OtaCtx(Addr.Heator, HeaterProto.OtaStart, HeaterProto.OtaTranslate, HeaterProto.OtaComplete)
val weightOtaCtx =
    OtaCtx(Addr.Weight, WeightProto.OtaStart, WeightProto.OtaTranslate, WeightProto.OtaComplete)

class DeviceOtaTask(val ctx: OtaCtx, val data: ByteArray, val res: Continuation<Unit>) : Runnable {

    private val pkgSize = data.size
    private var pkgIndex = 0
    private var pkgId = 0

    companion object {
        const val Timeout = 5 * 1000L
        const val MaxPkg = 48
    }


    override fun run() {

        try {
            start()
            translate()
            complete()
            res.resume(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            res.resumeWithException(e)
        }
    }

    private fun start() {
        val ret = Device.request(
            Timeout,
            ctx.addr,
            ctx.start,
            arrayOf(UInt32(pkgSize))
        )
        if (ret != 0) {
            throw IOException("ota start 异常:${errMsg(ret)}")
        }
    }

    private fun translate() {
        while (pkgIndex < pkgSize) {
            var n = pkgSize - pkgIndex
            if (n > MaxPkg) {
                n = MaxPkg
            }
            val ret = Device.request(
                Timeout,
                ctx.addr,
                ctx.translate,
                arrayOf(UInt16(pkgId), ByteView(data, pkgIndex, n))
            )
            if (ret != 0) {
                throw IOException("ota translate 异常:${errMsg(ret)}")
            }
            pkgIndex += n
            pkgId++
            bus.post(OtaProgEvent(ctx.addr, pkgIndex * 100 / pkgSize))
        }
    }

    private fun complete() {
        val m5 = MessageDigest.getInstance("MD5").digest(data)
        val ret = Device.request(Timeout, ctx.addr, ctx.complete, arrayOf(ByteView(m5)))
        if (ret != 0) {
            throw IOException("ota complete 异常:${errMsg(ret)}")
        }
    }

    private fun errMsg(ec: Int): String {
        return Proto.errMsgByAddr(ctx.addr, ec)
    }
}


suspend fun runOta(ctx: OtaCtx, data: ByteArray) = suspendCoroutine<Unit> {
    ConnTask.post(DeviceOtaTask(ctx, data, it))
}

