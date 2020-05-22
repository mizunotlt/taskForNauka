package com.testApi.dataBase.dataModels

import com.testApi.dataBase.enums.DaysType
import java.util.*

data class ProdCalendarElement (
    val id: Int,
    val date: Date,
    val dayType: DaysType
)
