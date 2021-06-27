package com.arvind.loginapiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arvind.loginapiapp.app.LoginApp
import com.arvind.loginapiapp.model.DataModelLoginBody
import com.arvind.loginapiapp.model.DataModelLoginStatus
import com.arvind.loginapiapp.repository.LoginRepository
import com.arvind.loginapiapp.utils.Resource
import com.arvind.loginapiapp.utils.hasInternetConnection
import com.arvind.loginapiapp.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: LoginRepository
) :
    AndroidViewModel(application) {
    private val _loginData = MutableLiveData<Resource<DataModelLoginStatus>>()

    val loginData: LiveData<Resource<DataModelLoginStatus>> = _loginData

    fun loginUser(dataModelLoginBody: DataModelLoginBody) = viewModelScope.launch {
        getLogin(dataModelLoginBody)
    }

    suspend fun getLogin(dataModelLoginBody: DataModelLoginBody) {
        _loginData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection<LoginApp>()) {
                val response = repository.getLogin(dataModelLoginBody)
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val successresponse: DataModelLoginStatus? = response.body()
                        toast(getApplication(), successresponse!!.message)
                        _loginData.postValue(Resource.Success(response.body()!!))
                    } else if (response.body()!!.status == 401) {

                        val errorresponse: DataModelLoginStatus? = response.body()
                        toast(getApplication(), errorresponse!!.error)

                    } else if (response.body()!!.status == 412) {

                        val errorresponse: DataModelLoginStatus? = response.body()
                        toast(getApplication(), errorresponse!!.error)
                    }

                } else {
                    _loginData.postValue(Resource.Error(response.message()))
                }
            } else {
                _loginData.postValue(Resource.Error("No Internet Connection"))
                toast(getApplication(), "No Internet Connection.!")
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginData.postValue(Resource.Error(t.message!!))
                }

            }
        }
    }
}