package com.hontech.pastacooking.serial

import com.hontech.pastacooking.ext.setUInt16
import com.hontech.pastacooking.ext.toUInt16

class UInt16 (var value: Int = 0) : SerialType {

    override fun size(): Int {
        return 2
    }

    override fun encode(buf: ByteArray, index: Int) {
        buf.setUInt16(index, value)
    }

    override fun decode(buf: ByteArray, index: Int) {
        value = buf.toUInt16(index)
    }
}

