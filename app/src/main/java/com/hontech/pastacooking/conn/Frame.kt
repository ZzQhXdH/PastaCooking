package com.hontech.pastacooking.conn

import com.hontech.pastacooking.ext.toHex
import com.hontech.pastacooking.ext.toHex16
import com.hontech.pastacooking.ext.toHex8
import com.hontech.pastacooking.serial.SerialType

class Frame (val dest: Int, val src: Int, val req: Int, val data: ByteArray) {

    companion object {
        val DataOffet = 4
    }

    fun request(): Int {
        return req and ((Proto.NotifyFlag + Proto.SalveFlag).inv())
    }

    fun isAck() = request() == Proto.Ack

    fun isNotify(): Boolean {
        return (req and Proto.NotifyFlag) != 0
    }

    fun parse(vararg args: SerialType) {
        var index = DataOffet
        for (arg in args) {
            arg.decode(data, index)
            index += arg.size()
        }
    }

    override fun toString(): String {
        return "dest:${dest.toHex8()} src:${src.toHex8()} req:${req.toHex16()} data:${data.toHex()}"
    }
}
