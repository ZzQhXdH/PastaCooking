package com.hontech.pastacooking.model

import com.hontech.pastacooking.ext.isSetBit
import com.hontech.pastacooking.serial.UInt16

class FridgeStatus {

    val sw = UInt16()
    val temp = UInt16()

    fun tempValue(): Int {
        val value = temp.value
        if (value.isSetBit(15)) {
            return -(0x10000 - value)
        }
        return value
    }
}