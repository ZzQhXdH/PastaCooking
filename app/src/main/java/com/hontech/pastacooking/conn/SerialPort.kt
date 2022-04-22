package com.hontech.pastacooking.conn

import java.io.IOException

class SerialPort(val name: String) {

    init {
        System.loadLibrary("conn-lib")
    }

    private var nativeRef: Long = 0

    fun open() {
        nativeRef = open(name)
        if (nativeRef < 0) {
            throw IOException("无法打开串口:$name")
        }
    }

    fun read() = read(nativeRef)

    fun write(buf: ByteArray) = write(nativeRef, buf)

    fun close() = close(nativeRef)

    private external fun open(name: String): Long
    private external fun read(fd: Long): ByteArray?
    private external fun write(fd: Long, buf: ByteArray)
    private external fun close(fd: Long)
}
