package com.example.testnauka.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testnauka.data.ProdCalendarData
import com.example.testnauka.di.annotation.DepartsViewModelScope
import com.example.testnauka.di.annotation.RestApiRepositoryScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.repository.ApiRepository
import kotlinx.coroutines.*
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import kotlin.coroutines.CoroutineContext

class CalendarViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutinesContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutinesContext)
    private val  repository: ApiRepository by inject()

    val calendarLiveData = MutableLiveData<ArrayList<ProdCalendarData>>().apply { value = arrayListOf() }
    private val calendar: ArrayList<ProdCalendarData> = arrayListOf()
    private val listCalendar: ArrayList<ProdCalendarData> = arrayListOf()

    init {
        KTP.openScopes(DepartsViewModelScope::class.java)
            .openSubScope(RestApiRepositoryScope::class.java)
            .installModules(
                RepositoryModule()
            )
            .inject(this)
    }

    fun getProdCalendar(){
        calendarLiveData.apply { value = arrayListOf() }
        calendar.clear()
        listCalendar.clear()
        scope.launch {
            val responseCalendar = repository.getProdCalendar()
            if (responseCalendar != null){
                calendar.addAll(responseCalendar.elements)
                listCalendar.addAll(calendar)
                calendarLiveData.postValue(listCalendar)
            }
        }
    }

    fun cancelAllRequests() = coroutinesContext.cancel()
}
