package com.testApi.dataBase.dataModels

import com.testApi.dataBase.enums.Positions
import org.joda.time.DateTime
import java.util.Date


data class EmployeeElement (
    val id: Int,
    val name: String,
    val birth: Date,
    val positions: Positions
)
