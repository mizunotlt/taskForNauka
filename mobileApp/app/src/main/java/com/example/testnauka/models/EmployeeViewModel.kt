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

class EmployeeViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutinesContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutinesContext)
    private val  repository: ApiRepository by inject()

    val employeeLiveData = MutableLiveData<ArrayList<EmployeeData>>().apply { value = arrayListOf() }
    //val employeeUpdateLiveData = MutableLiveData<PostUpdateDepartResponse?>().apply { value = null }
    val employeeDepartInsertLiveData = MutableLiveData<PostInsertEmployeeDepartResponse?>().apply { value = null }
    val employeeInsertLiveData = MutableLiveData<PostEmployeeResponse?>().apply { value = null }
    private val employee: ArrayList<EmployeeData> = arrayListOf()
    private val listEmployee: ArrayList<EmployeeData> = arrayListOf()
    val departsLiveData = MutableLiveData<ArrayList<DepartsData>>().apply { value = arrayListOf() }
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

    fun insertDepartEmployee(empId: Int, depId: Int){
        scope.launch {
            val responseDeparts = repository.postInsertEmployeeDepart(PostInsertEmployeeDepartRequest(empId, depId))
            if (responseDeparts != null){
                employeeDepartInsertLiveData.postValue(responseDeparts)
            }
        }
    }


    fun insertEmployee(empData: PostEmployeeRequest){
        scope.launch {
            val responseEmployee = repository.postInsertEmployee(empData)
            if (responseEmployee != null){
                employeeInsertLiveData.postValue(responseEmployee)
            }
        }
    }

    fun deleteEmployee(deleteEmployeePost: PostDeleteEmployeeRequest){
        scope.launch {
            val responseEmployee = repository.postDeleteEmployee(deleteEmployeePost)
        }
    }

    fun updateEmployee(updateEmployee: PostUpdateEmployeeRequest){
        scope.launch {
            val responseEmployee = repository.postUpdateEmployee(updateEmployee)
        }
    }

    fun getEmployee(){
        employeeLiveData.apply { value = arrayListOf() }
        employee.clear()
        listEmployee.clear()
        scope.launch {
            val id = null
            val responseEmployee = repository.getEmployee(id)
            if (responseEmployee != null){
                employee.addAll(responseEmployee.elements)
                listEmployee.addAll(employee)
                employeeLiveData.postValue(listEmployee)
            }
        }
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

    /*
    //Добавить штуку чтобы посмотреть обновилось ли это или нет
    fun updateDepart(departUpdatePost: PostUpdateDepart){
        scope.launch {
            val responseDeparts = repository.postUpdateDepart(departUpdatePost)
            if (responseDeparts != null){
                departUpdateLiveData.postValue(responseDeparts)
            }
        }
    }

     */

    fun cancelAllRequests() = coroutinesContext.cancel()
}
