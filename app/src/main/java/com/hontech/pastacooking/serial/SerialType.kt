package com.hontech.pastacooking.serial

interface SerialType {

    fun size(): Int

    fun encode(buf: ByteArray, index: Int)

    fun decode(buf: ByteArray, index: Int)
}
