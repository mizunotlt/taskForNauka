package com.testApi.dataBase.tables

import com.testApi.dataBase.tables.EmployeeDepart.autoIncrement
import org.jetbrains.exposed.sql.Table

object EmployeeDepartDay: Table(){

    val id = integer("ID").autoIncrement()
    val idEmployee = reference("id_employee", Employee.id)
    val idDepart = reference("id_depart", Department.id)

}