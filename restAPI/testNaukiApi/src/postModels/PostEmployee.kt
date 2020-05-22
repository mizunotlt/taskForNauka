package com.testApi.postModels

import com.testApi.dataBase.enums.Positions

data class PostEmployee (val name: String,
                         val year: Int,
                         val month: Int,
                         val day: Int,
                         val positions: Int)
