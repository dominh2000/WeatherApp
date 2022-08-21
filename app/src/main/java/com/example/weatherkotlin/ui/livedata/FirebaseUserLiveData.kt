package com.example.weatherkotlin.ui.livedata

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    /**
     * Lắng nghe thay đổi trạng thái Auth mỗi khi có user sign-in/out
     * và cập nhật giá trị cho object LiveData
     */
    private val authStateListener = FirebaseAuth.AuthStateListener {
        value = it.currentUser
    }

    // Observe FirebaseAuth khi có active observer
    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    // Hủy observer FirebaseAuth khi không còn active observer
    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}