package com.example.testnauka.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testnauka.data.PostRegRequest
import com.example.testnauka.data.PostRegResponse
import com.example.testnauka.di.annotation.RestApiRepositoryScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.repository.ApiRepository
import kotlinx.coroutines.*
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import kotlin.coroutines.CoroutineContext

class RegistrationViewModel: ViewModel() {

    private val parentJob = Job()

    private val coroutinesContext: CoroutineContext
    get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutinesContext)
    val responseLiveData = MutableLiveData<PostRegResponse>()
    private val  repository: ApiRepository by inject()

    init {
        KTP.openScopes(RegistrationViewModel::class.java)
            .openSubScope(RestApiRepositoryScope::class.java)
            .installModules(
                RepositoryModule()
            )
            .inject(this)
    }


    fun postRegistration(name: String, pos: Int){
        scope.launch {
            val responseReg = repository.postRegistration(PostRegRequest(name,pos))
            if (responseReg != null) responseLiveData.postValue(responseReg)
        }
    }


    fun cancelAllRequests() = coroutinesContext.cancel()

}


