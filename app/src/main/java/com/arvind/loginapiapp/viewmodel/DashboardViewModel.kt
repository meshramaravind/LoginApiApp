package com.arvind.loginapiapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DashboardViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

}