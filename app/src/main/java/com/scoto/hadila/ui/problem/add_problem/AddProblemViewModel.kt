package com.scoto.hadila.ui.problem.add_problem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.data.model.UserLog
import com.scoto.hadila.helper.FirebaseDbManager
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProblemViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager
) : ViewModel() {


    private val _isSuccessful: MutableLiveData<Resources<Boolean>> = MutableLiveData()
    val isSuccessful: MutableLiveData<Resources<Boolean>>
        get() = _isSuccessful

    fun addProblem(problem: Problem) {
        viewModelScope.launch {
            val docRef = dbManager.dbProblem().document()
            docRef.set(
                problem.apply {
                    pId = docRef.id
                }
            ).addOnSuccessListener {
                _isSuccessful.value = Resources.Success(true)
            }.addOnFailureListener {
                _isSuccessful.value = Resources.Error(it.message.toString())
            }
        }
    }

    fun saveLog(log: UserLog) {
        viewModelScope.launch {
            dbManager.dbUserLogs().document().set(log)
        }
    }


}