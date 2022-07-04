package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.conn.Device
import com.hontech.pastacooking.conn.Frame
import com.hontech.pastacooking.serial.SerialType
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class GenericTask2(
    val timeout: Long,
    val addr: Int,
    val req: Int,
    val args: Array<SerialType>,
    val result: Continuation<Frame>
) : Runnable {

    override fun run() {

        try {
            val frame = exec()
            result.resume(frame)
        } catch (e: Exception) {
            e.printStackTrace()
            result.resumeWithException(e)
        }
    }

    private fun exec(): Frame {
        return Device.send(timeout, addr, req, args)
    }
}


