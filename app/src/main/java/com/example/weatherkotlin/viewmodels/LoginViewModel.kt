package com.example.weatherkotlin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.weatherkotlin.livedata.FirebaseUserLiveData

enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED
}

class LoginViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map {
        if (it != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}

class LoginViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}