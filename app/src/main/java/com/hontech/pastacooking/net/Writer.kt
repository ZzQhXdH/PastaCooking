package com.hontech.pastacooking.net

import com.hontech.pastacooking.app.close
import com.hontech.pastacooking.ext.setUInt16
import com.hontech.pastacooking.ext.setUInt32
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException
import java.net.Socket


class Writer(private val socket: Socket, private val sync: SyncValue<Frame>) {

    companion object {

        fun makeHead(req: Int, size: Int): ByteArray {
            val buf = ByteArray(8)
            buf.setUInt16(0, Frame.Head)
            buf.setUInt32(2, size)
            buf.setUInt16(6, req)
            return buf
        }
    }

    @Volatile
    var isConnect = true
        private set

    private val output = socket.getOutputStream()

    fun write(req: Int, body: ByteArray, timeout: Long = 10 * 1000): Frame {
        val buf = makeHead(req, body.size + 8)
        sync.clear()
        output.write(buf)
        if (body.size != 0) {
            output.write(body)
        }
        val frame = sync.await(timeout)
        if (frame == null) {
            close()
            throw IOException("recv timeout")
        }
        return frame!!
    }

    private fun close() {
        close(socket)
        close(output)
        isConnect = false
    }
}


