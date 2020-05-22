package com.testApi.dataBase.tables

import com.testApi.dataBase.enums.DaysType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.date
import org.postgresql.util.PGobject

object ProductionCalendar: Table() {

    class PGEnum<T:Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
        init {
            value = enumValue?.name
            type = enumTypeName
        }
    }

    val id = integer("ID").autoIncrement()
    val day = date("date")
    val dayType = customEnumeration(
        "daystype",
        "days_type",
        { value ->
            when (value) {
                is PGobject -> DaysType.valueOf(value.value)
                is String -> DaysType.valueOf(value)
                else -> error("Ошибка в формате")
            }
        },
        { PGEnum("days_type", it) }
    )
}