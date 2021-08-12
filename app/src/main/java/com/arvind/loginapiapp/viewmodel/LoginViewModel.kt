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
import com.arvind.loginapiapp.utils.Event
import com.arvind.loginapiapp.utils.Resource
import com.arvind.loginapiapp.utils.hasInternetConnection
import com.arvind.loginapiapp.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: LoginRepository
) :
    AndroidViewModel(application) {
    private val _loginData = MutableLiveData<Event<Resource<DataModelLoginStatus>>>()

    val loginData: LiveData<Event<Resource<DataModelLoginStatus>>> = _loginData

    fun loginUser(dataModelLoginBody: DataModelLoginBody) = viewModelScope.launch {
        getLogin(dataModelLoginBody)
    }

    suspend fun getLogin(dataModelLoginBody: DataModelLoginBody) {
        _loginData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<LoginApp>()) {
                val response = repository.getLogin(dataModelLoginBody)
                if (response.isSuccessful) {
                    if (response.body()!!.status == 200) {
                        val successresponse: DataModelLoginStatus? = response.body()
                        toast(getApplication(), successresponse!!.message)
                        _loginData.postValue(Event(Resource.Success(response.body()!!)))
                    } else if (response.body()!!.status == 401) {

                        val errorresponse: DataModelLoginStatus? = response.body()
                        toast(getApplication(), errorresponse!!.error)

                    } else if (response.body()!!.status == 412) {

                        val errorresponse: DataModelLoginStatus? = response.body()
                        toast(getApplication(), errorresponse!!.error)
                    }

                } else {
                    _loginData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _loginData.postValue(Event(Resource.Error("No Internet Connection")))
                toast(getApplication(), "No Internet Connection.!")
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _loginData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }

            }

        }
    }
}