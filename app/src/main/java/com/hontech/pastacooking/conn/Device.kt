package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.except.AckException
import com.hontech.pastacooking.except.ResponseTimeoutException
import com.hontech.pastacooking.ext.toHex
import com.hontech.pastacooking.ext.toHex16
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.utils.Sync
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException

object Device {

    const val BaudRate = 115200

    private var mPort: SerialPort? = null
    private var mReader: ReaderTask? = null
    private val syncAck = Sync()
    private val syncValue = SyncValue<Frame>()

    fun open(name: String) {
        close()
        val port = SerialPort.open2(name, BaudRate)
        val reader = ReaderTask(port, syncAck, syncValue)
        reader.start()
        mPort = port
        mReader = reader
    }

    fun isOpen(): Boolean {
        return mPort?.isOpen ?: false
    }

    fun close() {
        if (isOpen()) {
            mPort?.close()
            mReader?.join()
        }
        mReader = null
        mPort = null
    }

    fun writeAck(dest: Int) {
        val buf = Proto.make(dest, Proto.Ack, arrayOf())
        mPort!!.write(buf)
    }

    fun write(buf: ByteArray, timeout: Long): Frame? {
        log("write:${buf.toHex()}")
        if (!isOpen()) {
            throw IOException("串口没有打开")
        }
        syncValue.clear()
        syncAck.clear()
        mPort!!.write(buf)
        val ret = syncAck.await(500)
        if (!ret) {
            throw AckException("通信异常没有收到ack")
        }
        return syncValue.await(timeout)
    }

    fun send(timeout: Long, dest: Int, req: Int, args: Array<SerialType>): Frame {
        val buf = Proto.make(dest, req, args)
        val frame = write(buf, timeout)
        if (frame == null) {
            throw ResponseTimeoutException("等待返回超时")
        }
        sendAck(frame)
        if (frame.req != (req + Proto.SalveFlag)) {
            throw IllegalStateException("协议流程错误 ${frame.req.toHex16()}")
        }
        return frame
    }

    private fun sendAck(frame: Frame) {
        val src = frame.src
        writeAck(src)
    }

    fun request(timeout: Long, dest: Int, req: Int, args: Array<SerialType>): Int {
        val frame = send(timeout, dest, req, args)
        val value = UInt8()
        frame.parse(value)
        return value.value
    }

}















