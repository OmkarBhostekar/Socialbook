package com.example.instaclone.ui.polls.models

data class Option(
    val _id: String,
    val text: String,
    val voters: List<String>
)
