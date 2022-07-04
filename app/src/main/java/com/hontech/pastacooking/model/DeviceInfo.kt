package com.hontech.pastacooking.model

import androidx.annotation.Keep
import com.hontech.pastacooking.app.MacAddr
import com.hontech.pastacooking.app.Version
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class DeviceInfo(
    val macAddr: String,
    val remoteAddr: String,
    val version: String,
    val name: String,
    val city: String
) {

    companion object {

        fun default(): DeviceInfo {
            return DeviceInfo(MacAddr, "未知", Version, "未知", "未知")
        }
    }
}
