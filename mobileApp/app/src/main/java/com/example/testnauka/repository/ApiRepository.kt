package com.example.testnauka.repository

import com.example.testnauka.api.RestApi
import com.example.testnauka.data.*
import com.example.testnauka.di.annotation.ApplicationScope
import toothpick.InjectConstructor
import javax.inject.Singleton

@Singleton
@InjectConstructor
@ApplicationScope
open class ApiRepository (private val restApi: RestApi): BaseRepository() {

    //Post запрос на регистрацию
    suspend fun postRegistration(dataRequest: PostRegRequest): PostRegResponse?{
        return safeApiCall(
        call = { restApi.postRegistrationAsync(dataRequest).await()},
        errorMessage = "Error register new user")
    }
    //Post запрос на авторизацию
    suspend fun postLogin(dataRequest: PostLoginRequest): PostLoginResponse?{
        return safeApiCall(
            call = { restApi.postLoginAsync(dataRequest).await()},
            errorMessage = "Error login user")
    }
    //Get запрос на получене departs
    suspend fun getDeparts(id: Int?): ResponseDeparts?{
        return safeApiCall(
            call = { restApi.getDepartAsync(id).await()},
            errorMessage = "Error  getDepart")
    }
    //Get запрос на получене employee
    suspend fun getEmployee(id: Int?): ResponseEmployee?{
        return safeApiCall(
            call = { restApi.getEmployeeAsync(id).await()},
            errorMessage = "Error  getEmployee")
    }
    //Get запрос на получене prodCalendar
    suspend fun getProdCalendar(): ResponseProdCalendar?{
        return safeApiCall(
            call = { restApi.getProdCalendAsync().await()},
            errorMessage = "Error  getProdCalendar")
    }
    //Post запрос на обновление отдела
    suspend fun postUpdateDepart(dataRequest: PostUpdateDepart): PostUpdateDepartResponse?{
        return safeApiCall(
            call = { restApi.postUpdateDepartAsync(dataRequest).await()},
            errorMessage = "Error updateDepart")
    }
    //Post запрос на добавление отдела
    suspend fun postInsertDepart(dataRequest: PostInsertDepart): PostInsertDepartResponse?{
        return safeApiCall(
            call = { restApi.postInsertDepartAsync(dataRequest).await()},
            errorMessage = "Error insertDepart")
    }

    //Post запрос на добавление сотрудника
    suspend fun postInsertEmployee(dataRequest: PostEmployeeRequest): PostEmployeeResponse?{
        return safeApiCall(
            call = { restApi.postInsertEmployeeAsync(dataRequest).await()},
            errorMessage = "Error insert Employee")
    }

    suspend fun postInsertEmployeeDepart(dataRequest: PostInsertEmployeeDepartRequest): PostInsertEmployeeDepartResponse?{
        return safeApiCall(
            call = { restApi.postInsertDepartEmployeeAsync(dataRequest).await()},
            errorMessage = "Error insert EmployeeDepart")
    }

    suspend fun postDeleteDepart(dataRequest: PostDeleteDepartRequest): PostDeleteDepartResponse?{
        return safeApiCall(
            call = { restApi.postDeleteDepartAsync(dataRequest).await()},
            errorMessage = "Error delete Depart")
    }

    suspend fun postDeleteEmployee(dataRequest: PostDeleteEmployeeRequest): PostDeleteEmployeeResponse?{
        return safeApiCall(
            call = { restApi.postDeleteEmployeeAsync(dataRequest).await()},
            errorMessage = "Error delete Employee")
    }

    suspend fun postUpdateEmployee(dataRequest: PostUpdateEmployeeRequest): PostUpdateEmployeeResponse?{
        return safeApiCall(
            call = { restApi.postUpdateEmployeeAsync(dataRequest).await()},
            errorMessage = "Error updateEmployee")
    }

}