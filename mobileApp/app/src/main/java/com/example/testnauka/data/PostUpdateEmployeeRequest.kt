package com.example.testnauka.data

data class PostUpdateEmployeeRequest(
    val id: Int,
    val name: String,
    val newName: String,
    val position: Int,
    val newPosition: Int
)