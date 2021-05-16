package com.example.instaclone.ui.home.models

import com.example.instaclone.ui.auth.models.User

data class Comment(
    val user: User,
    val text: String,
    val _id: String
)