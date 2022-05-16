package com.hontech.pastacooking.ext


fun Int.isSetBit(index: Int): Boolean {
    return (this and (0x01 shl index)) != 0
}

fun Int.isClrBit(index: Int) = !this.isSetBit(index)


