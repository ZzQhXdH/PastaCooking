package com.hontech.pastacooking.ext

private val HexList = charArrayOf(
    '0', '1', '2', '3',
    '4', '5', '6', '7',
    '8', '9', 'A', 'B',
    'C', 'D', 'E', 'F'
)

fun Byte.toHex(): String {
    val value = this.toUInt8()
    val l = value and 0x0F
    val h = (value shr 4) and 0x0F
    return String(charArrayOf(HexList[h], HexList[l]))
}

fun Int.toHex8(): String {
    val h = (this shr 4) and 0x0F
    val l = this and 0x0F
    return String(charArrayOf(HexList[h], HexList[l]))
}

fun Int.toHex16(): String {
    val h4 = (this shr 12) and 0x0F
    val h3 = (this shr 8) and 0x0F
    val h2 = (this shr 4) and 0x0F
    val h1 = this and 0x0F
    return String(charArrayOf(HexList[h4], HexList[h3], HexList[h2], HexList[h1]))
}

fun Int.toHex32(): String {
    val h8 = (this shr 28) and 0x0F
    val h7 = (this shr 24) and 0x0F
    val h6 = (this shr 20) and 0x0F
    val h5 = (this shr 16) and 0x0F
    val h4 = (this shr 12) and 0x0F
    val h3 = (this shr 8) and 0x0F
    val h2 = (this shr 4) and 0x0F
    val h1 = this and 0x0F
    return String(charArrayOf(
        HexList[h8], HexList[h7], HexList[h6], HexList[h5],
        HexList[h4], HexList[h3], HexList[h2], HexList[h1]
    ))
}

fun ByteArray.toHex(): String {
    val sb = StringBuilder()
    for (value in this) {
        sb.append(value.toHex())
        sb.append(" ")
    }
    return sb.toString()
}

fun ByteArray.formatMac(): String {
    val sb = StringBuilder()
    for (i in 0 until size) {
        val value = this[i]
        sb.append(value.toHex())
        if (i != (size - 1)) {
            sb.append(":")
        }
    }
    return sb.toString()
}

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


