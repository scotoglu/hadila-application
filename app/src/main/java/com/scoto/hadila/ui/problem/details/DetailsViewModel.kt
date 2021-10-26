package com.scoto.hadila.ui.problem.details

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
class DetailsViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager
) : ViewModel() {


    private val _isSuccessful: MutableLiveData<Resources<Boolean>> = MutableLiveData()
    val isSuccessful: MutableLiveData<Resources<Boolean>>
        get() = _isSuccessful


    fun update(problem: Problem) {
        viewModelScope.launch {
            //root/uid/problems/p_id
            val docRef = dbManager.dbProblem().document(problem.pId.toString())
            docRef.set(problem)
        }
    }

    fun delete(problem: Problem) {
        viewModelScope.launch {
            dbManager.dbProblem()
                .document(problem.pId.toString())
                .delete()
                .addOnSuccessListener {
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

    companion object {
        private const val TAG = "DetailsViewModel"
    }
}