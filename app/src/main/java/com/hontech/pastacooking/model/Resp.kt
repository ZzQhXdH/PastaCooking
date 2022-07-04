package com.hontech.pastacooking.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
class EmptyResp(val errMsg: String, val errCode: Int) {

    fun assert() {
        if (errCode != 0) {
            throw IllegalStateException("错误:$errMsg")
        }
    }
}

@Keep
@Serializable
class Resp<T>(val errMsg: String, val errCode: Int, val body: T)


