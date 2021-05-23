package com.example.instaclone.ui.profile.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.databinding.ItemSearchBinding
import com.example.instaclone.ui.auth.models.User

class SearchProfileAdapter(
    val listener: OnClickListener
) : PagingDataAdapter<User,SearchProfileAdapter.ViewHolder>(USER_COMPARATOR){

    companion object{
        val USER_COMPARATOR = object :  DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem._id == newItem._id
            }
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: ItemSearchBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            getItem(position)?.let { user ->
                binding.apply {
                    Glide.with(root.context).load(user.userImage).into(ivUserImage)
                    tvName.text = user.name
                    tvUserName.text = user.username
                    root.setOnClickListener {
                        listener.onClick(user._id)
                    }
                }
            }
        }
    }

    interface OnClickListener{
        fun onClick(uid: String)
    }
}