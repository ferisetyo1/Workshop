package com.feri.workshop

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.feri.workshop.data.WorkshopRepository
import com.feri.workshop.data.model.Person
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val repository: WorkshopRepository) : ViewModel() {
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

    fun logOut() {
        Firebase.auth.signOut()
    }

}
