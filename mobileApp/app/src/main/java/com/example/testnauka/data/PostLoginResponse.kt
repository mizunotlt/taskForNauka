package com.example.testnauka.data

import com.example.testnauka.utils.Positions

data class PostLoginResponse(
    val code: Int,
    val message: String,
    val positions: Positions?)

