package com.scoto.hadila.ui.categories

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
class CategoriesViewModel @Inject constructor(
    private val dbManager: FirebaseDbManager
) : ViewModel() {


    private val _categories: MutableLiveData<Resources<List<String>>> = MutableLiveData()

    val response: MutableLiveData<Resources<List<String>>>
        get() = _categories

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categories.value = Resources.Loading(true)
            dbManager.dbProblem().addSnapshotListener { value, err ->
                if (err != null) {
                    _categories.value = Resources.Error(err.message.toString())
                    return@addSnapshotListener
                }
                val categories: MutableList<String> = mutableListOf()
                for (doc in value!!) {
                    val problem = doc.toObject(Problem::class.java)
                    problem.category?.forEach {
                        if (!categories.contains(it)) {
                            categories.add(it)
                        }
                    }
                }
                _categories.value = Resources.Success(categories)
            }

        }
    }

}