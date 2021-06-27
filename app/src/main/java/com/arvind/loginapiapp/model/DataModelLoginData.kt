package com.arvind.loginapiapp.model

import com.arvind.loginapiapp.response.LoginResponseOTPinfo
import com.google.gson.annotations.SerializedName

data class DataModelLoginData(
    @SerializedName("UserID") val userID: Int,
    @SerializedName("OTPinfo") val oTPinfo: List<LoginResponseOTPinfo>
)
