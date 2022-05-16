package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.log
import java.io.IOException

class SerialPort(val name: String) {

    companion object {
        init {
            System.loadLibrary("conn-lib")
        }
    }


    private var nativeRef: Long = 0
    private var openFlag = false

    fun open() {
        if (openFlag) {
            return
        }
        nativeRef = open(name)
        if (nativeRef < 0) {
            throw IOException("无法打开串口:$name")
        }
        openFlag = true
        log("$name 打开成功")
    }

    fun isOpen() = openFlag

    fun read() = read(nativeRef)

    fun write(buf: ByteArray) = write(nativeRef, buf)

    fun close() {
        if (!openFlag) {
            return
        }
        close(nativeRef)
        openFlag = false
        log("$name 关闭")
    }

    fun delete() {
        delete(nativeRef)
        log("$name delete")
    }

    private external fun open(name: String): Long
    private external fun read(fd: Long): ByteArray?
    private external fun write(fd: Long, buf: ByteArray)
    private external fun close(fd: Long)
    private external fun delete(fd: Long)


}
