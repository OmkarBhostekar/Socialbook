package com.example.instaclone.ui.posts.models

import com.example.instaclone.ui.auth.models.User

data class Comment(
    val user: User,
    val text: String
)