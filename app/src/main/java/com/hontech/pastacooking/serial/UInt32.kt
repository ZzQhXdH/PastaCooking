package com.hontech.pastacooking.serial

import com.hontech.pastacooking.ext.setUInt32
import com.hontech.pastacooking.ext.toUInt32

class UInt32 (var value: Int = 0) : SerialType {

    override fun size(): Int {
        return 4
    }

    override fun decode(buf: ByteArray, index: Int) {
        value = buf.toUInt32(index)
    }

    override fun encode(buf: ByteArray, index: Int) {
        buf.setUInt32(index, value)
    }
}

