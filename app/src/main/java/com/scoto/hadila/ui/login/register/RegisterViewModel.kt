package com.scoto.hadila.ui.login.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.scoto.hadila.helper.FirebaseDbManager
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager
) : ViewModel() {


    @Inject
    lateinit var auth: FirebaseAuth


    private var _response: MutableLiveData<Resources<Boolean>> = MutableLiveData()

    val response: MutableLiveData<Resources<Boolean>>
        get() = _response

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            _response.value = Resources.Success(true)
        }.addOnFailureListener {
            _response.value = Resources.Error(it.message.toString())
        }
    }

}