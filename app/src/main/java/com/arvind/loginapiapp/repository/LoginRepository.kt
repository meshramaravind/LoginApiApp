package com.arvind.loginapiapp.repository

import com.arvind.loginapiapp.model.DataModelLoginBody
import com.arvind.loginapiapp.model.DataModelLoginStatus
import com.arvind.loginapiapp.webapi.ApiService
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getLogin(dataModelLoginBody: DataModelLoginBody): Response<DataModelLoginStatus> {
        return apiService.getLogin(dataModelLoginBody)
    }
}