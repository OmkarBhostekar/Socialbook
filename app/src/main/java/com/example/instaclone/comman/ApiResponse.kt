package com.example.instaclone.comman

data class ApiResponse<T>(
    val ResponseCode: Int?,
    val ResponseMessage: String?,
    val Comments: String?,
    val Result: T?,
)
