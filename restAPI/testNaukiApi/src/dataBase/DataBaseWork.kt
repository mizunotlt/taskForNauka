package com.testApi.dataBase

import com.google.gson.Gson
import com.testApi.dataBase.dataModels.*
import com.testApi.dataBase.enums.DaysType
import com.testApi.dataBase.enums.Positions
import com.testApi.dataBase.tables.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class DataBaseWork {
    private val db = Database.connect("jdbc:postgresql://localhost:5432/testNauka?currentSchema=testNauka",
        driver = "org.postgresql.Driver", user = "postgres", password =  "mizunotlt")
    private val jsonBuild = Gson()

    fun insertDepart(nameDepart: String): String{
        var id = 0
        transaction(db) {
            addLogger(StdOutSqlLogger)
            id = Department.insert {
                it[name] = nameDepart
            } get Department.id
        }
        return jsonBuild.toJson(DepartInsertResponse(200, "OK"))
    }

    fun insertEmployee(nameEmployee: String, year: Int, month: Int, day: Int, pos: Int): String{
        var id = 0
        transaction(db) {
            addLogger(StdOutSqlLogger)
            val dateTim = DateTime(year,month,day,0,0)
            id = Employee.insert {
                it[name] = nameEmployee
                it[birth] = dateTim
                when(pos){
                    0 -> it[position] = Positions.ОбычныйРабочий
                    1 -> it[position] = Positions.Табельщик
                    2 -> it[position] = Positions.АдминистраторОтделов
                    3 -> it[position] = Positions.АдминистраторСотрудников
                }
            } get Employee.id
        }
        return "OK"
    }

    fun getDepartEmployee(): String{
        val arrayJsonObject: ArrayList<DepartEmployeeElement> = arrayListOf()

        transaction(db) {
            addLogger(StdOutSqlLogger)
            val c = EmployeeDepart.innerJoin(Employee, {Employee.id}, {EmployeeDepart.idEmployee})
                .innerJoin(Department, {Department.id}, {EmployeeDepart.idDepart})
                .slice(Department.id, Employee.id, Employee.name, Employee.position)
                .selectAll()
                .forEach {
                    val depId = it[Department.id]
                    val empId = it[Employee.id]
                    val empName = it[Employee.name]
                    val empPos = it[Employee.position]

                    val depEmpElem = DepartEmployeeElement(depId, empId, empName, empPos)
                    arrayJsonObject.add(depEmpElem)
                }
            print(jsonBuild.toJson(arrayJsonObject))
        }
        return jsonBuild.toJson(arrayJsonObject)
    }

    fun getDepart(id: Int?): ArrayList<DepartElement> {
        val arrayJsonObject: ArrayList<DepartElement> = arrayListOf()
        transaction(db) {
            addLogger(StdOutSqlLogger)
            if (id == null){
                val c = Department
                    .selectAll()
                    .forEach {
                        val depId = it[Department.id]
                        val depName = it[Department.name]
                        val depElem = DepartElement(depId,depName)
                        arrayJsonObject.add(depElem)
                    }
            }
            else{
                val c = Department
                    .select {Department.id eq id}
                    .forEach {
                        val depId = it[Department.id]
                        val depName = it[Department.name]
                        val depElem = DepartElement(depId,depName)
                        arrayJsonObject.add(depElem)
                    }
            }
        }
        return arrayJsonObject
    }

    fun getProdCalendar(): ArrayList<ProdCalendarElement>{
        val arrayJsonObject: ArrayList<ProdCalendarElement> = arrayListOf()
        transaction(db) {
            addLogger(StdOutSqlLogger)
            val c = ProductionCalendar
                .selectAll()
                .forEach {
                    val pcId = it[ProductionCalendar.id]
                    val pcdate = it[ProductionCalendar.day]
                    val pcDateType = it[ProductionCalendar.dayType]
                    val pcElem = ProdCalendarElement(pcId, pcdate.toDate(), pcDateType)
                    arrayJsonObject.add(pcElem)
                }
        }
        return arrayJsonObject
    }

    fun getEmployee(id: Int?): ArrayList<EmployeeElement> {
        val arrayJsonObject: ArrayList<EmployeeElement> = arrayListOf()
        transaction(db) {
            addLogger(StdOutSqlLogger)
            if (id == null){
                val c = Employee
                    .selectAll()
                    .forEach {
                        val empId = it[Employee.id]
                        val empName = it[Employee.name]
                        val empBirth = it[Employee.birth]
                        val empPos = it[Employee.position]
                        val empElem = EmployeeElement(empId, empName, empBirth.toDate(), empPos)
                        arrayJsonObject.add(empElem)
                    }
            }
            else{
                val c = Employee
                    .select {Employee.id eq id}
                    .forEach {
                        val empId = it[Employee.id]
                        val empName = it[Employee.name]
                        val empBirth = it[Employee.birth]
                        val empPos = it[Employee.position]
                        val empElem = EmployeeElement(empId, empName, empBirth.toDate(), empPos)
                        arrayJsonObject.add(empElem)
                    }
            }
        }
        return arrayJsonObject
    }

    fun updateDepart(name: String, newName: String): String {
        var response: DepartUpdateResponse? = null
        transaction(db) {
            addLogger(StdOutSqlLogger)
            Department.update ({Department.name eq name}){
                it[Department.name] = newName
            }
        }
        return jsonBuild.toJson(DepartUpdateResponse(200, "OK"))
    }

    fun updateEmployee(id: Int, name: String, newName: String, positions: Int, newPositions: Int): String{
        transaction(db) {
            addLogger(StdOutSqlLogger)
            Employee.update ({Employee.id eq id}){
                if (newName != "") it[Employee.name]= newName
                if (newPositions != -1) {
                    when(newPositions){
                        0 -> it[Employee.position] = Positions.ОбычныйРабочий
                        1 -> it[Employee.position] = Positions.Табельщик
                        2 -> it[Employee.position] = Positions.АдминистраторОтделов
                        3 -> it[Employee.position] = Positions.АдминистраторСотрудников
                    }
                }
            }
        }
        return "OK"
    }

    fun deleteDepart(id: Int): String {
        transaction(db) {
            addLogger(StdOutSqlLogger)
            EmployeeDepart.deleteWhere{EmployeeDepart.idDepart eq id}
        }
        transaction(db) {
            addLogger(StdOutSqlLogger)
            EmployeeDepartDay.deleteWhere{EmployeeDepartDay.idDepart eq id}
        }
        transaction(db) {
            addLogger(StdOutSqlLogger)
            Department.deleteWhere{Department.id eq id}
        }
        return "OK"
    }

    fun deleteEmployee(id: Int): String {
        transaction(db) {
            addLogger(StdOutSqlLogger)
            EmployeeDepart.deleteWhere{EmployeeDepart.idEmployee eq id}
        }
        transaction(db) {
            addLogger(StdOutSqlLogger)
            EmployeeDepartDay.deleteWhere{EmployeeDepartDay.idEmployee eq id}
        }
        transaction(db) {
            addLogger(StdOutSqlLogger)
            Employee.deleteWhere{Employee.id eq id}
        }
        return "OK"
    }


    fun insertEmployeeDepart(idEmp:Int, idDep: Int): String{
        var id = 0
        transaction(db) {
            addLogger(StdOutSqlLogger)
            id = EmployeeDepart.insert {
                it[idEmployee] = idEmp
                it[idDepart] = idDep
            } get EmployeeDepart.id
        }
        return "OK"
    }

    fun insertEmployDepartDay(): String = TODO()

    fun deleteEmployeeDepartDay(): String = TODO()

    fun updateEmployeeDepartDay(): String = TODO()

    fun updateEmployeeDepart(): String = TODO()

    fun registerRequest(log: String, pos: Int): String{
        var response: RegisterModelResponse? = null
        transaction(db) {
            addLogger(StdOutSqlLogger)
            try {
                val id = UsersApp.insert {
                    it[login] = log
                    when (pos) {
                        0 -> it[position] = Positions.ОбычныйРабочий
                        1 -> it[position] = Positions.Табельщик
                        2 -> it[position] = Positions.АдминистраторОтделов
                        3 -> it[position] = Positions.АдминистраторСотрудников
                    }
                } get UsersApp.id
                val code = 200
                response = RegisterModelResponse(code, "ОК")
            }
            catch (e: ExposedSQLException){
                val code = 400
                response =  RegisterModelResponse(code, "Введите другое имя это уже занято")
            }
        }
        return jsonBuild.toJson(response)
    }

    fun authResponse(us: String): String {
        var response: LoginModelResponse? = null
        transaction(db) {
            addLogger(StdOutSqlLogger)
            val check = UsersApp
                .select {UsersApp.login eq us}
                .empty()
            if (check){
                response = LoginModelResponse(400,"Failed", null)
            }
            else{
                val c = UsersApp
                    .select {UsersApp.login eq us}
                    .forEach {
                        val usId = it[UsersApp.id]
                        val usLog = it[UsersApp.login]
                        val usPos = it[UsersApp.position]
                        response = LoginModelResponse(200, "OK", usPos )
                    }
            }
        }
        return jsonBuild.toJson(response)
    }

    fun insertProdCalendar(){
        val dayCount = 365
        val date = Calendar.getInstance()
        for (i in 3..dayCount){
            date.set(2020,0,i)
            val ss = DateTime(date)
            val rnd = Random.nextInt(0,3)
            transaction(db) {
                addLogger(StdOutSqlLogger)
                val id = ProductionCalendar.insert {
                    it[day] = ss.toDateTime()
                    when (rnd) {
                        0 -> it[dayType] = DaysType.Выходной
                        1 -> it[dayType] = DaysType.Праздничный
                        2 -> it[dayType] = DaysType.Предпраздничный
                        3 -> it[dayType] = DaysType.Рабочий
                    }
            } get ProductionCalendar.id
            }
        }
    }

}