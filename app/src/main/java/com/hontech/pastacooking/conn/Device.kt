package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.ext.toHex
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt8
import com.hontech.pastacooking.utils.Sync
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException

object Device {

    private var mPort: SerialPort? = null
    private var mReader: ReaderTask? = null
    private val syncAck = Sync()
    private val syncValue = SyncValue<Frame>()

    fun open(name: String) {
        close()
        val port = SerialPort(name)
        port.open()
        val reader = ReaderTask(port, syncAck, syncValue)
        reader.start()
        mPort = port
        mReader = reader
    }

    fun isOpen(): Boolean {
        return mPort?.isOpen() ?: false
    }

    fun close() {
        if (isOpen()) {
            mPort?.close()
            mReader?.join()
            mPort?.delete()
        }
        mReader = null
        mPort = null
    }

    fun write(buf: ByteArray, timeout: Long): Frame? {
        log("write:${buf.toHex()}")
        if (!isOpen()) {
            throw IOException("串口没有打开")
        }
        mPort!!.write(buf)
        val ret = syncAck.await(500)
        if (!ret) {
            throw IOException("通信异常没有收到ack")
        }
        return syncValue.await(timeout)
    }

    fun send(timeout: Long, dest: Int, req: Int, args: Array<SerialType>): Frame {
        val buf = Proto.make(dest, req, args)
        val frame = write(buf, timeout)
        if (frame == null) {
            throw IOException("等待返回超时")
        }
        if (frame.req != (req + Proto.SalveFlag)) {
            throw IOException("协议流程错误")
        }
        return frame
    }

    fun request(timeout: Long, dest: Int, req: Int, args: Array<SerialType>): Int {
        val frame = send(timeout, dest, req, args)
        val value = UInt8()
        frame.parse(value)
        return value.value
    }

}















