package com.example.testnauka.data

import com.example.testnauka.utils.DaysType
import java.util.*

data class ProdCalendarData(
    val id: Int,
    val date: Date,
    val dayType: DaysType
)

