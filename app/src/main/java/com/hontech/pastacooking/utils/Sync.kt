package com.hontech.pastacooking.utils

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class Sync {

    private val lock = ReentrantLock()
    private val cond = lock.newCondition()

    @Volatile
    private var ackFlag = false

    fun clear() {
        ackFlag = false
    }

    fun signal() {
        lock.lock()
        ackFlag = true
        cond.signal()
        lock.unlock()
    }

    fun await(timeout: Long): Boolean {
        lock.lock()
        do {
            if (ackFlag) {
                break
            }
            cond.await(timeout, TimeUnit.MILLISECONDS)
        } while (false)
        val flag = ackFlag
        ackFlag = false
        lock.unlock()
        return flag
    }
}
