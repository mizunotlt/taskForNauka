package com.testApi

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.testApi.dataBase.DataBaseWork
import com.testApi.dataBase.dataModels.*
import com.testApi.dataBase.enums.Positions
import com.testApi.postModels.*
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing



fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    val baseWork = DataBaseWork()
    val json = Gson()
    routing {
        get("/") {

        }
        get("/getDepart"){
            val id: Int? = call.request.queryParameters["id"]?.toInt()
            val response = baseWork.getDepart(id)
            call.respondText( json.toJson(DepartModel(response.size, response)), contentType = ContentType.Application.Json)
        }
        get("/getEmployee"){
            val id: Int? = call.request.queryParameters["id"]?.toInt()
            val response = baseWork.getEmployee(id)
            call.respondText( json.toJson(EmployeeModel(response.size, response)), contentType = ContentType.Application.Json)
        }
        post ("/insertDepart"){
            val part = call.receive<String>()
            val jsonObject: PostDepart = json.fromJson(part, PostDepart::class.java)
            val response = baseWork.insertDepart(jsonObject.name)
            call.respondText(response, contentType = ContentType.Application.Json)
        }
        post("/insertEmployee"){
            val part = call.receive<String>()
            val jsonObject: PostEmployee = json.fromJson(part, PostEmployee::class.java)
            val response = baseWork.insertEmployee(jsonObject.name, jsonObject.year, jsonObject.month, jsonObject.day,
                jsonObject.positions)
            call.respondText(json.toJson(EmployeeInsertResponse(200, response)), contentType = ContentType.Application.Json)
        }
        post("/updateDepart"){
            val part = call.receive<String>()
            val jsonObject: PostUpdateDepart = json.fromJson(part, PostUpdateDepart::class.java)
            val result = baseWork.updateDepart(jsonObject.name, jsonObject.newName)
            call.respondText(result, contentType = ContentType.Application.Json)
        }
        post("/updateEmployee"){
            val part = call.receive<String>()
            val jsonObject: PostUpdateEmployee = json.fromJson(part, PostUpdateEmployee::class.java)
            val response = baseWork.updateEmployee(jsonObject.id, jsonObject.name, jsonObject.newName, jsonObject.position,
                jsonObject.newPosition)
            call.respondText(json.toJson(EmployeeUpdateResponse(200, response)), contentType = ContentType.Application.Json)
        }
        post("/insertEmployeeDepart"){
            val part = call.receive<String>()
            val jsonObject: PostEmployeeDepart = json.fromJson(part, PostEmployeeDepart::class.java)
            val response = baseWork.insertEmployeeDepart(jsonObject.idEmp, jsonObject.idDep)
            call.respondText(json.toJson(DepartEmployeeInsertResponse(200, response)), contentType = ContentType.Application.Json)
        }
        post("/userReg"){
            val part = call.receive<String>()
            val jsonObject: PostRegister = json.fromJson(part, PostRegister::class.java)
            val response = baseWork.registerRequest(jsonObject.login, jsonObject.pos)
            call.respondText(response, contentType = ContentType.Application.Json)
        }
        post("/userLogin"){
            val part = call.receive<String>()
            val jsonObject: PostLogin= json.fromJson(part, PostLogin::class.java)
            val response = baseWork.authResponse(jsonObject.user)
            call.respondText(response, contentType = ContentType.Application.Json)
        }
        get("/getProdCalendar"){
            val response = baseWork.getProdCalendar()
            call.respondText( json.toJson(ProdCalendarResponse(response.size, response)), contentType = ContentType.Application.Json)
        }
        post("/deleteDepart"){
            val part = call.receive<String>()
            val jsonObject: PostDeleteDepart= json.fromJson(part, PostDeleteDepart::class.java)
            val response = baseWork.deleteDepart(jsonObject.id)
            call.respondText( json.toJson(PostDeleteResponse(200, response)), contentType = ContentType.Application.Json)
        }
        post("/deleteEmployee"){
            val part = call.receive<String>()
            val jsonObject: PostDeleteEmployee= json.fromJson(part, PostDeleteEmployee::class.java)
            val response = baseWork.deleteEmployee(jsonObject.id)
            call.respondText( json.toJson(PostDeleteResponse(200, response)), contentType = ContentType.Application.Json)
        }

    }
}


