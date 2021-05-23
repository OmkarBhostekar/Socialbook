package com.example.instaclone.comman

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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

@SuppressLint("SimpleDateFormat")
fun String.toTimeDiff(): String {
    var diff= ""
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val time: Long = sdf.parse(this).time
        val now = System.currentTimeMillis()
        diff = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return diff
}
