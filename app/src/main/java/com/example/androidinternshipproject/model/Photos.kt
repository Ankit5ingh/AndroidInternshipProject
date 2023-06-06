package com.example.androidinternshipproject.model

data class Photos(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: MutableList<Photo>,
    val total: Int
)