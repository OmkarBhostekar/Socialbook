package com.example.instaclone.ui.polls.models

data class Poll(
    val _id: String,
    val user: String,
    val text: String,
    val image: String,
    val options: List<Option>,
    val createdAt: String,
    val voted: Boolean?,
    val votedIndex: Int?,
)
