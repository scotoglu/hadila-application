package com.scoto.hadila.ui.collections

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scoto.hadila.data.model.Collection
import com.scoto.hadila.helper.FirebaseDbManager
import com.scoto.hadila.helper.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(

    private val dbManager: FirebaseDbManager

) : ViewModel() {


    private val _collections: MutableLiveData<Resources<List<Collection>>> = MutableLiveData()

    val collection: MutableLiveData<Resources<List<Collection>>>
        get() = _collections

    init {
        getCollections()
    }

    private fun getCollections() {
        viewModelScope.launch {
            _collections.value = Resources.Loading(true)
            dbManager.dbCollections().addSnapshotListener { values, err ->
                if (err != null) {
                    _collections.value = Resources.Error(err.message.toString())
                    return@addSnapshotListener
                }
                val list: MutableList<Collection> = mutableListOf()
                for (t in values!!) {
                    val collection = t.toObject(Collection::class.java)
                    list.add(collection)
                }
                _collections.value = Resources.Success(list)
            }
        }
    }

    fun addCollection(collection: Collection) {
        viewModelScope.launch {
            dbManager.dbCollections().document().set(collection)
        }
    }
}