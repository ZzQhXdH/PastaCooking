package com.hontech.pastacooking.net

import com.hontech.pastacooking.app.parseJson
import com.hontech.pastacooking.app.toStrWithUtf8
import com.hontech.pastacooking.ext.toHex16
import com.hontech.pastacooking.model.EmptyResp
import com.hontech.pastacooking.model.Resp

class Frame(val req: Int, val body: ByteArray?) {

    companion object {
        const val Head = 0xE11E

        const val NotifyMask = 0x4000
        const val ServerMask = 0x8000

        const val Ack = 0x00
        const val Login = 0x01
        const val Poll = 0x02
        const val SendMainStatus = 0x03
        const val SendFridgeStatus = 0x04
        const val SendHeaterStatus = 0x05
        const val SendWeighterStatus = 0x06
        const val NotifyOta = 0x07
    }

    fun isNotify(): Boolean {
        return (req and NotifyMask) != 0
    }

    fun request(): Int {
        return req and ((NotifyMask + ServerMask).inv())
    }

    inline fun <reified T> parse(): T {
        return body!!.parseJson()
    }

    fun parseResp(): EmptyResp {
        return parse()
    }

    fun assert() {
        parseResp().assert()
    }

    inline fun <reified T> parseBody(): T {
        val resp = body!!.parseJson<Resp<T>>()
        return resp.body
    }

    override fun toString(): String {
        val hex = req.toHex16()
        if (body == null) {
            return "req:$hex body=null"
        }
        return "req=$hex body=${body.toStrWithUtf8()}"
    }
}

