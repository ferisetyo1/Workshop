package com.feri.workshop.component.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onErrorEmail: (String) -> Unit,
        onErrorPassword: (String) -> Unit,
        isLoading: (Boolean) -> Unit
    ) {
        isLoading(true)
        Firebase
            .auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isLoading(false)
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    if (it.exception is FirebaseAuthException) {
                        val errorCode =
                            (it.exception as FirebaseAuthException).errorCode
                        if (errorCode == "ERROR_USER_NOT_FOUND") {
                            onErrorEmail("Email tidak ditemukan")
                        } else if (errorCode == "ERROR_WRONG_PASSWORD") {
                            onErrorPassword("Password salah")
                        } else {
                            onError(it.exception?.message.orEmpty())
                        }
                    } else {
                        onError(it.exception?.message.orEmpty())
                    }
                }
            }

    }
}
