package com.sf.healthylifestyle.data.dto.auth.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequest(
    @SerialName("username") val username: String,
    @SerialName("login") val login: String,
 )
