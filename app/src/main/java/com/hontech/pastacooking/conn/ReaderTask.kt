package com.hontech.pastacooking.conn

import com.hontech.pastacooking.ext.toUInt16
import com.hontech.pastacooking.ext.toUInt8
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException

class ReaderTask (val port: SerialPort, val sync: Sync) : Thread("read-task") {

    override fun run() {

        while (true) {

            try {
                exec()
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }


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
        val req = frame.request()

        if (req == Proto.Ack) {
            sync.signal()
            return
        }
        when (frame.src) {
            Addr.Main -> onForMain(frame)
            Addr.Heator -> onForHeator(frame)
            Addr.Weight -> onForWeight(frame)
            else -> throw IllegalStateException("未知的设备地址")
        }
    }

    private fun onForMain(frame: Frame) {

    }

    private fun onForHeator(frame: Frame) {

    }

    private fun onForWeight(frame: Frame) {

    }
}

