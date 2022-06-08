package com.feri.workshop

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.Person
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: WorkshopRepository) : ViewModel() {
    val selectedPerson = mutableStateOf<Person?>(null)
    val currentAccount = mutableStateOf(Firebase.auth.currentUser)
    val isLoadingGetPerson = mutableStateOf(false)
    val personList = mutableStateOf<List<Person>>(emptyList())
    val myuser = mutableStateOf<Person?>(null)

    init {
        checkAccount()
        getPerson()
    }

    private fun getPerson(
        refresh: Boolean = false,
        isLoading: (Boolean) -> Unit = {},
        onSuccesss: (List<Person>) -> Unit = {},
        onFailed: (String) -> Unit = {}
    ) {
        repository.getPersons(
            refresh = refresh,
            isLoading = {
                isLoadingGetPerson.value = it
                isLoading(it)
            },
            onSuccess = {
                personList.value = it
                onSuccesss(it)
            },
            onFailed = onFailed
        )
    }

    fun checkAccount() {
        Firebase.auth.addAuthStateListener {
            currentAccount.value = it.currentUser
            if (it.currentUser != null) getPerson(refresh = true, onSuccesss = { persons ->
                myuser.value =
                    persons.firstOrNull { person -> person.email == it.currentUser?.email }
            })
        }
    }

    fun gantiPassword(
        oldPassword: String,
        newPassword: String,
        onSuccesss: () -> Unit = {},
        onFailed: (String) -> Unit
    ) {
        val user = Firebase.auth.currentUser
        user?.let {
            val credential = EmailAuthProvider.getCredential(user.email.orEmpty(), oldPassword)
            user.reauthenticate(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener {
                        if (it.isSuccessful) {
                            onSuccesss()
                        } else {
                            onFailed(it.exception?.message.orEmpty())
                            println(it.exception?.message.orEmpty())
                        }
                    }
                } else {
                    onFailed(it.exception?.message.orEmpty())
                    println(it.exception?.message.orEmpty())
                }
            }
        }

    }

    fun logOut() {
        Firebase.auth.signOut()
    }

    fun simpanUser(
        person: Person?,
        isLoading: (Boolean) -> Unit,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        person?.let {
            repository.createPerson(
                person = it,
                isLoading = isLoading,
                onFailed = onFailed,
                onSuccess = {
                    getPerson()
                    onSuccess()
                })
        }
    }

}
