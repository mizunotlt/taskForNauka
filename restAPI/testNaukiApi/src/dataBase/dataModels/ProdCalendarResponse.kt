package com.testApi.dataBase.dataModels

data class ProdCalendarResponse(
    val countCalend: Int,
    val elements: ArrayList<ProdCalendarElement>
)