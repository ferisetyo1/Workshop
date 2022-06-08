package com.feri.workshop.component.viewmodel

import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.Person
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: WorkshopRepository) : ViewModel() {
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

    fun register(
        nama: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        isLoading: (Boolean) -> Unit
    ) {
        isLoading(true)
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            isLoading(false)
            if (it.isSuccessful) {
                repository.createPerson(
                    person = Person(nama = nama, email = email, level = 10,active = false),
                    isLoading = {
                        isLoading(it)
                    },
                    onSuccess = onSuccess,
                    onFailed = onError
                )
            } else {
                onError(it.exception?.message.orEmpty())
            }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailed: (String) -> Unit) {
        Firebase.auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful){
                onSuccess()
            }else{
                onFailed(it.exception?.message.orEmpty())
            }
        }
    }
}
