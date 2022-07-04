package com.hontech.pastacooking.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class OtaModel(val deviceId: Int, val firmId: Int, val type: String)
