package com.example.testnauka.api

import com.example.testnauka.data.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface RestApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("userReg")
    fun postRegistrationAsync(@Body data: PostRegRequest): Deferred<Response<PostRegResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("userLogin")
    fun postLoginAsync(@Body data: PostLoginRequest): Deferred<Response<PostLoginResponse>>

    @GET("getDepart")
    fun getDepartAsync(@Query("id") id: Int?): Deferred<Response<ResponseDeparts>>

    @GET("getEmployee")
    fun getEmployeeAsync(@Query("id") id: Int?): Deferred<Response<ResponseEmployee>>

    @GET("getProdCalendar")
    fun getProdCalendAsync(): Deferred<Response<ResponseProdCalendar>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("updateDepart")
    fun postUpdateDepartAsync(@Body data: PostUpdateDepart): Deferred<Response<PostUpdateDepartResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("insertDepart")
    fun postInsertDepartAsync(@Body data: PostInsertDepart): Deferred<Response<PostInsertDepartResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("insertEmployeeDepart")
    fun postInsertDepartEmployeeAsync(@Body data: PostInsertEmployeeDepartRequest): Deferred<Response<PostInsertEmployeeDepartResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("insertEmployee")
    fun postInsertEmployeeAsync(@Body data: PostEmployeeRequest): Deferred<Response<PostEmployeeResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("deleteDepart")
    fun postDeleteDepartAsync(@Body data: PostDeleteDepartRequest): Deferred<Response<PostDeleteDepartResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("deleteEmployee")
    fun postDeleteEmployeeAsync(@Body data: PostDeleteEmployeeRequest): Deferred<Response<PostDeleteEmployeeResponse>>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("updateEmployee")
    fun postUpdateEmployeeAsync(@Body data: PostUpdateEmployeeRequest): Deferred<Response<PostUpdateEmployeeResponse>>

}