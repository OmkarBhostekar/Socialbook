package com.example.instaclone.comman

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CustomItemAnimator : DefaultItemAnimator() {

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        return super.animateAdd(holder)
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        return super.animateRemove(holder)
    }
}