package com.testApi.dataBase.tables

import org.jetbrains.exposed.sql.Table

object EmployeeDepart: Table() {
    val id = integer("ID").autoIncrement()
    val idEmployee = reference("id_employee", Employee.id)
    val idDepart = reference("id_depart", Department.id)
}