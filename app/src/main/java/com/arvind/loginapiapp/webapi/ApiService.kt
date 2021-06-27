package com.arvind.loginapiapp.webapi

import com.arvind.loginapiapp.model.DataModelLoginBody
import com.arvind.loginapiapp.model.DataModelLoginStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    //login
    @POST("login/verify")
    suspend fun getLogin(
        @Body dataModelLoginBody: DataModelLoginBody
    ): Response<DataModelLoginStatus>
}