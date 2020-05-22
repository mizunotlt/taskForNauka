package com.testApi.dataBase.dataModels

import com.testApi.dataBase.enums.Positions

data class DepartEmployeeElement(val departId: Int,
                                 val employeeId: Int,
                                 val employeeName: String,
                                 val position: Positions)
