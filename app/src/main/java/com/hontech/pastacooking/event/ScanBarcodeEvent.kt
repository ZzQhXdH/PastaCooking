package com.hontech.pastacooking.event

import com.hontech.pastacooking.conn.MainProto

class ScanBarcodeEvent(
    val col: Int,
    val row: Int,
    val err: Int,
    val barcode: String
) {

    fun errString(): String {
        return MainProto.errMsg(err)
    }
}