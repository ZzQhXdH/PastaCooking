package com.hontech.pastacooking.conn

import com.hontech.pastacooking.app.bus
import com.hontech.pastacooking.app.log
import com.hontech.pastacooking.event.SystemErrEvent
import com.hontech.pastacooking.ext.*
import com.hontech.pastacooking.utils.Sync
import com.hontech.pastacooking.utils.SyncValue
import java.io.IOException

class ReaderTask(val port: SerialPort, val syncAck: Sync, val syncValue: SyncValue<Frame>) :
    Thread("read-task") {

    override fun run() {

        while (true) {

            try {
                exec()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                bus.post(SystemErrEvent("串口异常:${e.message!!}"))
            } catch (e: Exception) {
                e.printStackTrace()
                bus.post(SystemErrEvent("串口读取退出:${e.message!!}"))
                break
            }
        }

        log("reader task quit")
    }

    private fun sync() {

        var flag = false
        while (true) {
            val n = port.readByte()
            if (n < 0) {
                throw IOException("串口被关闭?")
            }

            if (flag && (n == Proto.H1)) {
                return
            }
            flag = (n == Proto.H0)

            if (!flag) {
                log("异常字节:${n.toHex8()}")
            }
        }
    }

    private fun read(): ByteArray {
        sync()
        val len = port.readUInt16()
        if (len < 0) {
            throw IOException("串口被关闭?")
        }
        if (len < 10) {
            throw IllegalStateException("数据读取异常")
        }
        val buf = ByteArray(len - 4)

        val ret = port.readBuf(buf)
        if (ret < 0) {
            throw IOException("串口被关闭?")
        }

        val sum = buf.checkSum(4, len - 10)
        val s = buf[len - 4 - 2].toUInt8()
        val end = buf[len - 4 - 1].toUInt8()
        if (s != sum) {
            throw IllegalStateException("读取数据校验出错")
        }
        if (end != Proto.End) {
            throw IllegalStateException("读取数据尾部错误")
        }

        return buf
    }

    private fun exec() {
        val buf = read()
        // dest, src, req(2), data(n), sum, end

        val dest = buf.toUInt8(0)
        val src = buf.toUInt8(1)
        val req = buf.toUInt16(2)
        val frame = Frame(dest, src, req, buf)
        dispatch(frame)
    }

    private fun dispatch(frame: Frame) {

        if (frame.isAck()) {
            syncAck.signal()
            log("ack ")
            return
        }

        if (!frame.isNotify()) {
            syncValue.signal(frame)
            log("read:${frame.data.toHex()}")
            return
        }

        when (frame.src) {
            Addr.Main -> MainProto.onRecv(frame)
            Addr.Heator -> HeaterProto.onRecv(frame)
            Addr.Weight -> WeightProto.onRecv(frame)
            else -> throw IllegalStateException("未知的设备地址")
        }
    }

}



