package com.hontech.pastacooking.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class FirmModel(
    val id: Int,
    val name: String,
    val type: String,
    val version: String,
    val size: Int,
    val timestamp: Long
) {
}
