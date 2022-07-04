package com.hontech.pastacooking.model

import androidx.annotation.Keep
import com.hontech.pastacooking.app.MacAddr
import com.hontech.pastacooking.app.Version
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class LoginModel(val macAddr: String = MacAddr, val version: String = Version)

val loginModel = LoginModel()

