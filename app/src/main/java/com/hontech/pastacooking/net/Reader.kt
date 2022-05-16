package com.hontech.pastacooking.net

import com.hontech.pastacooking.app.WorkExecutor
import com.hontech.pastacooking.app.close
import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.ext.toUInt16
import com.hontech.pastacooking.ext.toUInt32
import com.hontech.pastacooking.task.net.NetConnTask
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException
import java.net.Socket

class Reader(socket: Socket, private val sync: SyncValue<Frame>) : Thread() {

    private val input = socket.getInputStream()

    @Volatile
    var isConnect = true
        private set

    override fun run() {

        while (true) {
            try {
                exec()
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
        close(input)
        isConnect = false
        log("net-reader quit")
        WorkExecutor.postDelayed(NetConnTask(), 10 * 1000)
    }

    private fun exec() {
        val frame = read()
        if (frame.isNotify()) {
            execNotify(frame)
            return
        }
        sync.signal(frame)
    }

    private fun execNotify(frame: Frame) {

    }

    private fun readn(size: Int): ByteArray {
        val buf = ByteArray(size)
        var index = 0
        while (index < size) {
            val n = input.read(buf, index, size - index)
            if (n <= 0) {
                throw IOException("net disconnect")
            }
            index += n
        }
        return buf
    }

    private fun read(): Frame {
        val buf = ByteArray(8)
        val head = buf.toUInt16(0)
        if (head != Frame.Head) {
            throw IOException("head fail")
        }
        val size = buf.toUInt32(2)
        if (size < 8) {
            throw IOException("size fail")
        }
        val req = buf.toUInt16(6)
        if (size == 8) {
            return Frame(req, null)
        }
        val data = readn(size - 8)
        return Frame(req, data)
    }
}
