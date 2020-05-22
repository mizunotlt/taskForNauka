package com.testApi.dataBase.tables

import org.jetbrains.exposed.sql.Table

object Department: Table(){
    val id = integer("ID").autoIncrement() // Column<Int>
    val name = varchar("name", 256) // Column<String>
}
