package com.scoto.hadila.ui.login.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {

    @Inject
    lateinit var auth: FirebaseAuth

    private val _isLogin: MutableLiveData<Resources<String>> = MutableLiveData()
    val isLogin: MutableLiveData<Resources<String>>
        get() = _isLogin


    fun login(email: String, password: String) {
        _isLogin.value = Resources.Loading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid.toString()
                    _isLogin.value = Resources.Success(uid)
                } else {
                    _isLogin.value = task.exception?.message?.let { Resources.Error(it) }
                }
            }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}