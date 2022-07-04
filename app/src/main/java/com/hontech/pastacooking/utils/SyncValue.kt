package com.hontech.pastacooking.utils

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

class SyncValue<T> {

    private val lock = ReentrantLock()
    private val cond = lock.newCondition()

    @Volatile
    private var value: T? = null

    fun clear() {
        value = null
    }

    fun signal(value: T) {
        lock.lock()
        this.value = value
        cond.signal()
        lock.unlock()
    }

    fun await(timeout: Long): T? {

        do {
            lock.lock()
            if (value != null) {
                break
            }
            cond.await(timeout, TimeUnit.MILLISECONDS)
        } while (false)
        val r = value
        value = null
        lock.unlock()
        return r
    }
}


