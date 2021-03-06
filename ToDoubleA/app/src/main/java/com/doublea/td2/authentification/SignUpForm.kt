package com.doublea.td2.authentification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpForm(
    @SerialName("firstname")
    val firstname: String,
    @SerialName("lastname")
    val lastname: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("confirm")
    val confirm: String
)