package com.example.instaclone.ui.auth.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val _id: String,
        val name: String?,
        val email: String?,
        val username: String?,
        val userImage: String?,
        val bio: String?,
        val posts: Int?,
        val followers: List<String>?,
        val following: List<String>?,
        val createdAt: String?,
        val followed: Boolean?,
) : Parcelable