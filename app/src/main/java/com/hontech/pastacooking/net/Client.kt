package com.hontech.pastacooking.net

import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Client {

    private var socket: Socket? = null
    private var reader:Reader? = null
    private var writer: Writer? = null

    const val Host = "106.14.180.8"
    const val Port = 32456

    fun connect(host: String, port: Int) {
        close()
        socket = Socket()
        socket!!.connect(InetSocketAddress(host, port), 5 * 1000)
        val sync = SyncValue<Frame>()
        reader = Reader(socket!!, sync)
        writer = Writer(socket!!, sync)
        reader!!.start()
    }

    private fun close() {
        writer = null
        reader = null
        socket = null
    }

    fun write(req: Int, body: ByteArray, timeout: Long): Frame {
        if (!isConnected()) {
            throw IOException("net disconnected")
        }
        return writer!!.write(req, body, timeout)
    }

    fun isConnected(): Boolean {
        if (reader == null) {
            return false
        }
        if (writer == null) {
            return false
        }
        return reader!!.isConnect && writer!!.isConnect
    }
}

