package com.hontech.pastacooking.conn

import com.hontech.pastacooking.serial.SerialType

class Frame (val dest: Int, val src: Int, val req: Int, val data: ByteArray) {

    companion object {
        val DataOffet = 4
    }

    fun request(): Int {
        return req and ((Proto.NotifyFlag + Proto.SalveFlag).inv())
    }

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
}
