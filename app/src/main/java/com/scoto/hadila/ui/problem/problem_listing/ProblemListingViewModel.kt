package com.scoto.hadila.ui.problem.problem_listing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scoto.hadila.data.model.Problem
import com.scoto.hadila.helper.FirebaseDbManager
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProblemListingViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager

) : ViewModel() {


    private val _problems: MutableLiveData<Resources<List<Problem>>> = MutableLiveData()
    val problems: MutableLiveData<Resources<List<Problem>>>
        get() = _problems

    private val _favourites: MutableLiveData<Resources<List<Problem>>> = MutableLiveData()
    val favourites: MutableLiveData<Resources<List<Problem>>>
        get() = _favourites

    private val _byCollection: MutableLiveData<Resources<List<Problem>>> = MutableLiveData()
    val byCollection: MutableLiveData<Resources<List<Problem>>>
        get() = _byCollection


    private val _byCategory: MutableLiveData<Resources<List<Problem>>> = MutableLiveData()
    val byCategory: MutableLiveData<Resources<List<Problem>>>
        get() = _byCategory

    init {
        getProblems()
        getFavourites()
    }

    private fun getProblems() {
        viewModelScope.launch {
            _problems.value = Resources.Loading(true)
            dbManager.dbProblem().addSnapshotListener { values, err ->
                if (err != null) {
                    _problems.value = Resources.Error(err.message.toString())
                    return@addSnapshotListener
                }
                val list: MutableList<Problem> = mutableListOf()
                for (doc in values!!) {
                    val problem = doc.toObject(Problem::class.java)
                    list.add(problem)
                }
                _problems.value = Resources.Success(list)
            }
        }
    }

    private fun getFavourites() {
        viewModelScope.launch {
            dbManager.dbProblem()
                .whereEqualTo("favourite", true)
                .addSnapshotListener { values, err ->
                    if (err != null) {
                        _favourites.value = Resources.Error(err.message.toString())
                        return@addSnapshotListener
                    }
                    val list: MutableList<Problem> = mutableListOf()
                    for (doc in values!!) {
                        val problem = doc.toObject(Problem::class.java)
                        list.add(problem)
                    }
                    _favourites.value = Resources.Success(list)
                }
        }
    }

    fun getProblemByCollection(title: String) {
        viewModelScope.launch {
            dbManager.dbProblem()
                .whereEqualTo("collection", title)
                .addSnapshotListener { values, err ->
                    if (err != null) {
                        _byCollection.value = Resources.Error(err.message.toString())
                        return@addSnapshotListener
                    }
                    val list: MutableList<Problem> = mutableListOf()
                    for (doc in values!!) {
                        val problem = doc.toObject(Problem::class.java)
                        list.add(problem)
                    }
                    _byCollection.value = Resources.Success(list)
                }
        }
    }

    fun getProblemByCategory(category: String) {
        viewModelScope.launch {
            dbManager.dbProblem()
                .whereArrayContains("category", category)
                .addSnapshotListener { values, err ->
                    if (err != null) {
                        _byCategory.value = Resources.Error(err.message.toString())
                        return@addSnapshotListener
                    }
                    val list: MutableList<Problem> = mutableListOf()
                    for (doc in values!!) {
                        val problem = doc.toObject(Problem::class.java)
                        list.add(problem)
                    }
                    _byCategory.value = Resources.Success(list)
                }
        }
    }
}