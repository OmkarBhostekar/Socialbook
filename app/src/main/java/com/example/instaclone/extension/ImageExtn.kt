package com.example.instaclone.extension

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.instaclone.R
import java.io.File

fun ImageView.setImage(url: String, placeholder: Int = R.drawable.ic_launcher_background) {
    Glide.with(this.context)
        .load(url)
        .placeholder(placeholder)
        .into(this)
}

fun ImageView.setImageFromFile(file: File, placeholder: Int = R.drawable.ic_launcher_background) {
    Glide.with(this.context)
        .load(file)
        .placeholder(placeholder)
        .into(this)
}

fun ImageView.setImageFromUri(uri: Uri, placeholder: Int = R.drawable.ic_launcher_background) {
    Glide.with(this.context)
        .load(uri)
        .placeholder(placeholder)
        .into(this)
}

fun ImageView.setBit(drawable: Int, placeholder: Int = R.drawable.ic_launcher_background) {
    Glide.with(this.context)
        .load(drawable)
        .placeholder(placeholder)
        .into(this)
}