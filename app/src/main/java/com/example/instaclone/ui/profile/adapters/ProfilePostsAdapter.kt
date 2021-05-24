package com.example.instaclone.ui.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.databinding.ItemProfilePostBinding
import com.example.instaclone.extension.setImage
import com.example.instaclone.ui.home.adapters.PostsAdapter.Companion.POST_COMPARATOR
import com.example.instaclone.ui.home.models.Post

class ProfilePostsAdapter(
    val listener: OnClickListener
) : PagingDataAdapter<Post,ProfilePostsAdapter.ProfilePostViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        return ProfilePostViewHolder(ItemProfilePostBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ProfilePostViewHolder(private val binding: ItemProfilePostBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val post = getItem(position)
            post?.let {
                Glide.with(binding.root.context).load(post.image).into(binding.ivPostImage)
                binding.root.setOnClickListener {
                    listener.onClick(post)
                }
            }
        }
    }
    interface OnClickListener{
        fun onClick(post: Post)
    }
}