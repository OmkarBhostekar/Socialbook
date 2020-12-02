package com.example.instaclone.ui.posts.models

import com.example.instaclone.ui.auth.models.User

data class Post(
    val _id: String,
    val user: User,
    val image: String,
    val description: String,
    val likes: List<String>,
    val comments: List<String>,
    val isLiked: Boolean,
    val createdAt: String
)