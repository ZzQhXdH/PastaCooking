package com.hontech.pastacooking.task.conn

import com.hontech.pastacooking.app.ConnTask
import com.hontech.pastacooking.conn.Addr
import com.hontech.pastacooking.conn.WeightProto
import com.hontech.pastacooking.serial.SerialType
import com.hontech.pastacooking.serial.UInt32
import com.hontech.pastacooking.serial.UInt8
import kotlin.coroutines.suspendCoroutine


suspend fun requestWeight(timeout: Long, req: Int, args: Array<SerialType>, exceptMsg: String) =
    suspendCoroutine<Unit> {
        ConnTask.post(GenericTask(timeout, Addr.Weight, req, args, exceptMsg, it))
    }

suspend fun adjWeight(id: Int, value: Int) {
    requestWeight(
        3 * 1000,
        WeightProto.Adj,
        arrayOf(UInt8(id), UInt32(value)),
        "校准电子秤"
    )
}

