package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.WorkExecutor
import com.hontech.pastacooking.conn.*
import com.hontech.pastacooking.serial.SerialType
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GenericTask (val timeout: Long,
                   val addr: Int,
                   val req: Int,
                   val args: Array<SerialType>,
                   val exceptMsg: String,
                   val result: Continuation<Unit>) : Runnable {

    override fun run() {

        try {
            exec()
            result.resume(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            result.resumeWithException(e)
        }
    }

    private fun errMsg(ec: Int): String {
        return Proto.errMsgByAddr(addr, ec)
    }

    private fun exec() {
        val ec = Device.request(timeout, addr, req, args)
        if (ec != 0) {
            throw IOException("$exceptMsg:${errMsg(ec)}")
        }
    }
}




