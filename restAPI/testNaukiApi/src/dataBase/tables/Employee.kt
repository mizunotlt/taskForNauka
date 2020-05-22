package com.testApi.dataBase.tables

import com.testApi.dataBase.enums.Positions
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import org.postgresql.util.PGobject

object Employee: Table() {

    class PGEnum<T:Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
        init {
            value = enumValue?.name
            type = enumTypeName
        }
    }

    val id = integer("ID").autoIncrement()
    val name = varchar("name", 256)
    val birth = date("birth")
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