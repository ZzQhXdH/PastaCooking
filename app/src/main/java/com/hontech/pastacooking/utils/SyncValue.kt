package com.hontech.pastacooking.utils

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class SyncValue<T> {

    private val lock = ReentrantLock()
    private val cond = lock.newCondition()
    private var value: T? = null

    fun signal(value: T) {
        lock.lock()
        this.value = value
        cond.signal()
        lock.unlock()
    }

    fun await(timeout: Long): T? {
        lock.lock()
        val ret = cond.await(timeout, TimeUnit.MILLISECONDS)
        lock.unlock()
        return if (ret) this.value else null
    }
}


