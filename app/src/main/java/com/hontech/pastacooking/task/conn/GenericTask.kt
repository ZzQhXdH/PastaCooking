package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.conn.*
import com.hontech.pastacooking.except.ExecFailException
import com.hontech.pastacooking.serial.SerialType
import java.io.IOException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GenericTask(
    val timeout: Long,
    val addr: Int,
    val req: Int,
    val args: Array<SerialType>,
    val exceptMsg: String,
    val result: Continuation<Unit>
) : Runnable {

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
        if ((ec and 0x7F) != 0) {
            throw ExecFailException("$exceptMsg:${errMsg(ec)}")
        }
    }
}




