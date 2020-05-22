package com.testApi.postModels

data class PostUpdateEmployee (
    val id: Int,
    val name: String,
    val newName: String,
    val position: Int,
    val newPosition: Int
)
