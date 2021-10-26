package com.scoto.hadila.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.helper.FirebaseDbManager
import com.scoto.hadila.helper.Resources
import com.scoto.hadila.util.HOME_RECYCLERVIEW_DATA_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager,
    ) : ViewModel() {


    private val _problems: MutableLiveData<Resources<List<Problem>>> = MutableLiveData()
    val problems: MutableLiveData<Resources<List<Problem>>>
        get() = _problems


    init {
        getProblems()

    }


    private fun getProblems() {
        viewModelScope.launch {
            _problems.value = Resources.Loading(true)
            dbManager.dbProblem()
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(HOME_RECYCLERVIEW_DATA_LIMIT)
                .addSnapshotListener { value, err ->
                    if (err != null) {
                        _problems.value = Resources.Error(err.message.toString())
                    }
                    val list: MutableList<Problem> = mutableListOf()
                    for (doc in value!!) {
                        val problem = doc.toObject(Problem::class.java)
                        list.add(problem)
                    }
                    _problems.value = Resources.Success(list)
                }
        }
    }

    companion object {
        private const val KEY_PROBLEM = "PROBLEM"
    }

}