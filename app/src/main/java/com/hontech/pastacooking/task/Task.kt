package com.hontech.pastacooking.task

abstract class Task : Runnable {

    abstract fun exec()

    override fun run() {

        try {
            exec()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}