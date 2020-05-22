package com.testApi.dataBase.tables

import com.testApi.dataBase.enums.Positions
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

object UsersApp: Table() {

    class PGEnum<T:Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
        init {
            value = enumValue?.name
            type = enumTypeName
        }
    }

    val id = integer("ID").autoIncrement()
    val login = varchar("login", 256)
    val position = customEnumeration(
        "position",
        "positions",
        { value ->
            when (value) {
                is PGobject -> Positions.valueOf(value.value)
                is String -> Positions.valueOf(value)
                else -> error("Ошибка в формате")
            }
        },
        { PGEnum("positions", it) }
    )
}