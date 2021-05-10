package com.example.instaclone.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.R

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story,parent,false))
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
//        Glide.with(holder.itemView.context).load("https://picsum.photos/200").into(holder.itemView.ivProfileImage)
    }

    override fun getItemCount(): Int {
        return 10
    }

    inner class StoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}