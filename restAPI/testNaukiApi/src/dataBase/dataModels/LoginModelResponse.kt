package com.testApi.dataBase.dataModels

import com.testApi.dataBase.enums.Positions

data class LoginModelResponse(
    val code: Int,
    val message: String,
    val positions: Positions?
)
