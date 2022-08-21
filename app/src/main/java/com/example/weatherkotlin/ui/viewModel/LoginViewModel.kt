package com.example.weatherkotlin.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.weatherkotlin.ui.livedata.FirebaseUserLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class AuthenticationState {
    AUTHENTICATED, UNAUTHENTICATED
}

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map {
        if (it != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}