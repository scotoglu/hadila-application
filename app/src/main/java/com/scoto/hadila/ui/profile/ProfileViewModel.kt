package com.scoto.hadila.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.scoto.hadila.data.model.User
import com.scoto.hadila.data.model.UserLog
import com.scoto.hadila.helper.DataStoreManager
import com.scoto.hadila.helper.FirebaseDbManager
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager,
    private val auth: FirebaseAuth
) : ViewModel() {
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    init {
      //  getUser()
        getLogs()
    }

    private val _user: MutableLiveData<Resources<User>> = MutableLiveData()
    val user: MutableLiveData<Resources<User>>
        get() = _user


    private val _logs: MutableLiveData<Resources<List<UserLog>>> = MutableLiveData()
    val logs: MutableLiveData<Resources<List<UserLog>>>
        get() = _logs

    private fun getUser() {
        viewModelScope.launch {
            dbManager.dbUser().get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (user in task.result?.documents!!) {
                        val u = user.toObject(User::class.java)
                        if (u != null) {
                            _user.value = Resources.Success(u)
                        }
                    }
                } else {
                    _user.value = Resources.Error(task.exception?.message.toString())
                }

            }
        }

    }

    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            dataStoreManager.removeAuthID()
        }

    }

    private fun getLogs() {
        viewModelScope.launch {
            dbManager.dbUserLogs().addSnapshotListener { value, err ->
                if (err != null) {
                    _logs.value = Resources.Error(
                        err.message.toString()
                    )
                    return@addSnapshotListener
                } else {

                    val logList: MutableList<UserLog> = mutableListOf()
                    for (l in value!!) {
                        val log = l.toObject(UserLog::class.java)
                        logList.add(log)
                    }
                    Log.d(TAG, "getLogs: Size : ${logList.size}")
                    _logs.value = Resources.Success(logList)
                }
            }
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}