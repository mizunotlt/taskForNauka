package com.example.testnauka.data

import com.example.testnauka.utils.Positions
import java.util.*

data class EmployeeData(
    val id: Int,
    val name: String,
    val birth: Date,
    val positions: Positions
)
