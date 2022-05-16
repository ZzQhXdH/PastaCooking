package com.hontech.pastacooking.serial

import com.hontech.pastacooking.ext.isSetBit
import com.hontech.pastacooking.ext.setUInt16
import com.hontech.pastacooking.ext.toUInt16

data class UInt16 (var value: Int = 0) : SerialType {

    override fun size(): Int {
        return 2
    }

    override fun encode(buf: ByteArray, index: Int) {
        buf.setUInt16(index, value)
    }

    override fun decode(buf: ByteArray, index: Int) {
        value = buf.toUInt16(index)
    }

    fun isSetBit(index: Int) = value.isSetBit(index)

    fun signedValue(): Int {
        if ((value and 0x8000) != 0) {
            return value - 0x10000
        }
        return value
    }

    override fun toString(): String {
        return "$value"
    }
}

