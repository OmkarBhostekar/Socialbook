package com.example.instaclone.ui.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.R

class ProfilePostsAdapter : RecyclerView.Adapter<ProfilePostsAdapter.ProfilePostViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        return ProfilePostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile_post,parent,false))
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 20;
    }

    inner class ProfilePostViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
}