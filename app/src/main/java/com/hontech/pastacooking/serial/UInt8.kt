package com.hontech.pastacooking.serial

import com.hontech.pastacooking.ext.setUInt8
import com.hontech.pastacooking.ext.toUInt8

class UInt8 (var value: Int = 0) : SerialType {

    override fun size(): Int {
        return 1
    }

    override fun decode(buf: ByteArray, index: Int) {
        value = buf.toUInt8(index)
    }

    override fun encode(buf: ByteArray, index: Int) {
        buf.setUInt8(index, value)
    }
}

