package com.hontech.pastacooking.net

import com.hontech.pastacooking.app.DevPort
import com.hontech.pastacooking.app.ServeAddr
import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.event.LoginChangedEvent
import com.hontech.pastacooking.model.DeviceInfo
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Client {

    private var socket: Socket? = null
    private var reader: Reader? = null
    private var writer: Writer? = null

    const val Host = ServeAddr
    const val Port = DevPort

    var info = DeviceInfo.default()
        set(value) {
            field = value
            bus.post(LoginChangedEvent())
        }

    fun connect(host: String, port: Int) {
        close()
        socket = Socket()
        socket!!.connect(InetSocketAddress(host, port), 5 * 1000)
        val sync = SyncValue<Frame>()
        reader = Reader(socket!!, sync)
        writer = Writer(socket!!, sync)
        reader!!.start()
        log("dev server 连接成功")
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
        val frame = writer!!.write(req, body, timeout)
        return frame
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

