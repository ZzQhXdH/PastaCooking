package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.conn.Device
import com.hontech.pastacooking.conn.Proto
import com.hontech.pastacooking.event.OtaProgEvent
import com.hontech.pastacooking.serial.ByteView
import com.hontech.pastacooking.serial.UInt16
import com.hontech.pastacooking.serial.UInt32
import java.io.IOException
import java.security.MessageDigest
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OtaCtx (val addr: Int, val start: Int, val translate: Int, val complete: Int)

class OtaTask(val ctx: OtaCtx, val data: ByteArray, val result: Continuation<Unit>) : Runnable {

    private val pkgSize = data.size
    private var pkgIndex = 0
    private var pkgId = 0

    companion object {
        const val Timeout = 5 * 1000L
    }

    override fun run() {

        try {
            start()
            translate()
            complete()
            result.resume(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            result.resumeWithException(e)
        }
    }

    private fun start() {
        val ret = Device.request(Timeout,
            ctx.addr,
            ctx.start,
            arrayOf(UInt32(pkgSize)))
        if (ret != 0) {
            throw IOException("ota start 异常:${errMsg(ret)}")
        }
    }

    private fun translate() {
        while (pkgIndex < pkgSize) {
            var n = pkgSize - pkgIndex
            if (n > 64) {
                n = 64
            }
            val ret = Device.request(Timeout,
                ctx.addr,
                ctx.translate,
                arrayOf(UInt16(pkgId), ByteView(data, pkgIndex, n)))
            if (ret != 0) {
                throw IOException("ota translate 异常:${errMsg(ret)}")
            }
            pkgIndex += n
            bus.post(OtaProgEvent(ctx.addr,pkgIndex * 100 / pkgSize))
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

