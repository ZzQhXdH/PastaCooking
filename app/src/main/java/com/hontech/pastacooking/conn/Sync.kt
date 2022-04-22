package com.hontech.pastacooking.conn

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class Sync {

    private val lock = ReentrantLock()
    private val cond = lock.newCondition()

    fun signal() {
        lock.lock()
        cond.signal()
        lock.unlock()
    }

    fun await(timeout: Long): Boolean {
        lock.lock()
        val ret = cond.await(timeout, TimeUnit.MILLISECONDS)
        lock.unlock()
        return ret
    }
}