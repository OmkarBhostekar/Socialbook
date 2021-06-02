package com.example.instaclone.ui.home.models

import android.os.Parcelable
import com.example.instaclone.ui.auth.models.User
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val _id: String,
    val user: User,
    val image: String,
    val description: String,
    val likes: List<String>,
    val comments: List<String>,
    val isLiked: Boolean,
    val createdAt: String
) : Parcelable