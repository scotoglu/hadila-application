package com.scoto.hadila.ui.login.reset_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPassViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var auth: FirebaseAuth


    private val _response: MutableLiveData<Resources<Boolean>> = MutableLiveData()
    val response: MutableLiveData<Resources<Boolean>>
        get() = _response

    fun resetPassword(email: String) {
        _response.value = Resources.Loading(true)
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            _response.value = Resources.Success(true)
        }.addOnFailureListener {
            _response.value = Resources.Error(it.message.toString())
        }

    }
}