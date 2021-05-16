package com.example.instaclone.response

data class PagingResponse<T>(
    val totalPages: Int,
    val currentPage: Int,
    val data: T
)