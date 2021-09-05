package com.example.instaclone.comman

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import com.github.thunder413.datetimeutils.DateTimeUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Int.toLikeCount(): String {
    return if (this < 2)
        "$this Like"
    else
        "$this Likes"
}

@SuppressLint("SimpleDateFormat")
fun String.toTimeDiff(context: Context): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    sdf.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val time = sdf.parse(this).time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
        return DateTimeUtils.getTimeAgo(context,this)
    }
}
