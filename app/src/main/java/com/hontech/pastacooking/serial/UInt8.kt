package com.hontech.pastacooking.serial

import com.hontech.pastacooking.conn.HeaterProto
import com.hontech.pastacooking.conn.MainProto
import com.hontech.pastacooking.ext.isSetBit
import com.hontech.pastacooking.ext.setUInt8
import com.hontech.pastacooking.ext.toUInt8

data class UInt8(var value: Int = 0) : SerialType {

    override fun size(): Int {
        return 1
    }

    override fun decode(buf: ByteArray, index: Int) {
        value = buf.toUInt8(index)
    }

    override fun encode(buf: ByteArray, index: Int) {
        buf.setUInt8(index, value)
    }

    override fun toString(): String {
        return "$value"
    }

    fun isSetBit(index: Int) = value.isSetBit(index)

    fun mainErrMsg(): String {
        return MainProto.errMsg(value)
    }

    fun heaterErrMsg(): String {
        return HeaterProto.errMsg(value)
    }

    fun isOk() = value == 0
}

