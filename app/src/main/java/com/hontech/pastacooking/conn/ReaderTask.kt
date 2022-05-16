package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.ext.toHex
import com.hontech.pastacooking.ext.toUInt16
import com.hontech.pastacooking.ext.toUInt8
import com.hontech.pastacooking.utils.Sync
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException

class ReaderTask (val port: SerialPort, val syncAck: Sync, val syncValue: SyncValue<Frame>) : Thread("read-task") {

    override fun run() {

        while (true) {

            try {
                exec()
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }

        log("reader task quit")
    }

    private fun exec() {
        val buf = port.read()
        // dest, src, req(2), data(n), sum, end
        if (buf == null) {
            throw IOException("串口读取异常")
        }

        val dest = buf.toUInt8(0)
        val src = buf.toUInt8(1)
        val req = buf.toUInt16(2)
        val frame = Frame(dest, src, req, buf)
        dispatch(frame)
    }

    private fun dispatch(frame: Frame) {

        if (frame.isAck()) {
            syncAck.signal()
            return
        }

        if (!frame.isNotify()) {
            syncValue.signal(frame)
            log("read:${frame.data.toHex()}")
            return
        }

        when (frame.src) {
            Addr.Main -> MainProto.onRecv(frame)
            Addr.Heator -> HeaterProto.onRecv(frame)
            Addr.Weight -> onForWeight(frame)
            else -> throw IllegalStateException("未知的设备地址")
        }
    }

    private fun onForWeight(frame: Frame) {

    }

}



