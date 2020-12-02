package com.example.instaclone.comman

fun Int.toLikeCount(): String {
    return if (this < 2)
        "$this Like"
    else
        "$this Likes"
}
fun Int.toCommentCount(): String {
    return if (this < 2)
        "$this Comment"
    else
        "$this Comments"
}