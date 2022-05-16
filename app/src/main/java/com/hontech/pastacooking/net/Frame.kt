package com.hontech.pastacooking.net

class Frame (val req: Int, val body: ByteArray?) {

    companion object {
        const val Head = 0xE11E

        const val NotifyMask = 0x4000
        const val ServerMask = 0x8000
    }

    fun isNotify(): Boolean {
        return (req and NotifyMask) != 0
    }

    fun request(): Int {
        return req and ((NotifyMask + ServerMask).inv())
    }
}

