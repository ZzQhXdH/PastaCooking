package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.log
import java.io.IOException

class SerialPort (val ref: Long) {

    var isOpen = true
        private set

    companion object {

        init {
            System.loadLibrary("serialportlib")
        }

        private external fun open(name: String, baudRate: Int): Long

        fun open2(name: String, baudRate: Int): SerialPort {
            val fd = open(name, baudRate)
            if (fd < 0) {
                throw IOException("串口打开失败:${name}")
            }
            return SerialPort(fd)
        }
    }

    fun readBuf(buf: ByteArray) = readBuf(ref, buf)
    fun readUInt16() = readUInt16(ref)
    fun read(buf: ByteArray) = read(ref, buf)

    fun readByte() = readByte(ref)

    fun write(buf: ByteArray) = write(ref, buf)

    fun close() {
        if (!isOpen) {
            log("串口已经关闭")
            return
        }
        close(ref)
        isOpen = false
    }


    private external fun close(fd: Long)
    private external fun read(fd: Long, buf: ByteArray): Int
    private external fun write(fd: Long, buf: ByteArray): Int
    private external fun readByte(fd: Long): Int
    private external fun readUInt16(fd: Long): Int
    private external fun readBuf(fd: Long, buf: ByteArray): Int

}


