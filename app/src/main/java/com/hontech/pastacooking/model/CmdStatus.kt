package com.hontech.pastacooking.model

interface CmdStatus {

    companion object {

        const val Ok = 0
        const val Prog = 1
        const val Fail = 2
    }


    fun titleInfo(): String

    fun progress(): Int

    fun result(): String

    fun setNotify(cb: ()->Unit)
}
