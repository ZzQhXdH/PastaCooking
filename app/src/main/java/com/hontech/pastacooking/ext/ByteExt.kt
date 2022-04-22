package com.hontech.pastacooking.ext

fun Byte.toUInt8() = this.toInt() and 0xFF

fun ByteArray.toUInt16(index: Int): Int {
    return (this[0 + index].toUInt8() shl 8) +
            this[1 + index].toUInt8()
}

fun ByteArray.toUInt8(index: Int) = this[index + 0].toUInt8()

fun ByteArray.toUInt32(index: Int): Int {
    return  (this[0 + index].toUInt8() shl 24) +
            (this[1 + index].toUInt8() shl 16) +
            (this[2 + index].toUInt8() shl 8) +
            (this[3 + index].toUInt8())
}

fun ByteArray.setUInt8(index: Int, value: Int) {
    this[index] = value.toByte()
}

fun ByteArray.setUInt16(index: Int, value: Int) {
    this[index] = (value shr 8).toByte()
    this[index + 1] = value.toByte()
}

fun ByteArray.setUInt32(index: Int, value: Int) {
    this[index] = (value shr 24).toByte()
    this[index + 1] = (value shr 16).toByte()
    this[index + 2] = (value shr 8).toByte()
    this[index + 3] = value.toByte()
}

fun ByteArray.checkSum(index: Int, size: Int): Int {
    var sum = 0
    for (i in index until (index + size)) {
        sum = sum xor (this[i].toUInt8())
    }
    return sum
}
