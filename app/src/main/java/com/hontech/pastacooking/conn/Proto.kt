package com.hontech.pastacooking.conn

import com.hontech.pastacooking.ext.checkSum
import com.hontech.pastacooking.ext.setUInt16
import com.hontech.pastacooking.ext.setUInt8
import com.hontech.pastacooking.serial.SerialType

object Proto {

    const val SalveFlag = 0x8000
    const val NotifyFlag = 0x4000

    const val Head = 0xE11E
    const val End = 0xEF

    const val Ack = 0x00

    fun make(dest: Int, req: Int, vararg args: SerialType): ByteArray {
        val size = size() + 10
        val buf = ByteArray(size)
        buf.setUInt16(0, Head)
        buf.setUInt16(2, size)
        buf.setUInt8(4, dest)
        buf.setUInt8(5, Addr.Ipc)
        buf.setUInt16(6, req)
        var index = 8
        for (arg in args) {
            arg.encode(buf, index)
            index += arg.size()
        }
        val sum = buf.checkSum(8, size - 10)
        buf.setUInt8(size - 2, sum)
        buf.setUInt8(size - 1, End)
        return buf
    }

    private fun size(vararg args: SerialType): Int {
        var index = 0
        for (arg in args) {
            index += arg.size()
        }
        return index
    }
}

object Addr {
    const val Ipc = 0
    const val Main = 1
    const val Heator = 2
    const val Weight = 3
}



