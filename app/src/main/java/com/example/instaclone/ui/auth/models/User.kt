package com.example.instaclone.ui.auth.models

data class User(
        val _id: String,
        val name: String?,
        val email: String?,
        val username: String?,
        val userImage: String?,
        val bio: String?,
        val posts: List<String>?,
        val followers: List<String>?,
        val following: List<String>?,
        val createdAt: String?,
        val isFollowed: Boolean?
)