package com.hontech.pastacooking.serial

import com.hontech.pastacooking.ext.setUInt16
import com.hontech.pastacooking.ext.toUInt16

class ByteView (var value: ByteArray = byteArrayOf(), var offset: Int = 0, var size: Int = value.size - offset) : SerialType {

    override fun size(): Int {
        return 2 + size
    }

    override fun decode(buf: ByteArray, index: Int) {
        val len = buf.toUInt16(index)
        value = buf
        offset = index + 2
        size = len
    }

    override fun encode(buf: ByteArray, index: Int) {
        buf.setUInt16(index, size)
        System.arraycopy(value, offset, buf, index + 2, size)
    }

    override fun toString(): String {
        return String(value, offset, size, Charsets.UTF_8)
    }
}
