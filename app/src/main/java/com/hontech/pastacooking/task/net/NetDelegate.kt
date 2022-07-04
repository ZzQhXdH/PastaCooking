package com.hontech.pastacooking.task.net

import com.hontech.pastacooking.net.Frame

object NetDelegate {

    private var mainBody = byteArrayOf()
    private var fridgeBody = byteArrayOf()
    private var heaterBody = byteArrayOf()
    private var weighterBody = byteArrayOf()

    fun onRecvMain(body: ByteArray) {
        if (mainBody.contentEquals(body)) {
            return
        }
        mainBody = body
        NetSendStatusTask.send(Frame.SendMainStatus, mainBody)
    }

    fun onRecvHeater(body: ByteArray) {
        if (heaterBody.contentEquals(body)) {
            return
        }
        heaterBody = body
        NetSendStatusTask.send(Frame.SendHeaterStatus, heaterBody)
    }

    fun onRecvFridge(body: ByteArray) {
        if (fridgeBody.contentEquals(body)) {
            return
        }
        fridgeBody = body
        NetSendStatusTask.send(Frame.SendFridgeStatus, fridgeBody)
    }

    fun onRecvWeighter(body: ByteArray) {
        if (weighterBody.contentEquals(body)) {
            return
        }
        weighterBody = body
        NetSendStatusTask.send(Frame.SendWeighterStatus, weighterBody)
    }

}