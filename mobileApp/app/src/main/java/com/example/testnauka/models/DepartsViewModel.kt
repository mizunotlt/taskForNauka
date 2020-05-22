package com.example.testnauka.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testnauka.data.*
import com.example.testnauka.di.annotation.DepartsViewModelScope
import com.example.testnauka.di.annotation.RestApiRepositoryScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.repository.ApiRepository
import kotlinx.coroutines.*
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import kotlin.coroutines.CoroutineContext

class DepartsViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutinesContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutinesContext)
    private val  repository: ApiRepository by inject()

    val departsLiveData = MutableLiveData<ArrayList<DepartsData>>().apply { value = arrayListOf() }
    val departUpdateLiveData = MutableLiveData<PostUpdateDepartResponse?>().apply { value = null }
    val departInsertLiveData = MutableLiveData<PostInsertDepartResponse?>().apply { value = null }
    private val departs: ArrayList<DepartsData> = arrayListOf()
    private val listDeparts: ArrayList<DepartsData> = arrayListOf()

    init {
        KTP.openScopes(DepartsViewModelScope::class.java)
            .openSubScope(RestApiRepositoryScope::class.java)
            .installModules(
                RepositoryModule()
            )
            .inject(this)
    }


    fun getDeparts(){
        departsLiveData.apply { value = arrayListOf() }
        departs.clear()
        listDeparts.clear()
        scope.launch {
            val id = null
            val responseDeparts = repository.getDeparts(id)
            if (responseDeparts != null){
                departs.addAll(responseDeparts.departs)
                listDeparts.addAll(departs)
                departsLiveData.postValue(listDeparts)
            }
        }
    }

    fun deleteDepart(departDeletePost: PostDeleteDepartRequest){
        scope.launch {
            val responseDeparts = repository.postDeleteDepart(departDeletePost)
        }
    }

    fun updateDepart(departUpdatePost: PostUpdateDepart){
        scope.launch {
            val responseDeparts = repository.postUpdateDepart(departUpdatePost)
            if (responseDeparts != null){
                departUpdateLiveData.postValue(responseDeparts)
            }
        }
    }

    fun insertDepart(departInsert: PostInsertDepart){
        scope.launch {
            val responseDeparts = repository.postInsertDepart(departInsert)
            if (responseDeparts != null){
                departInsertLiveData.postValue(responseDeparts)
            }
        }
    }

    fun cancelAllRequests() = coroutinesContext.cancel()


}
