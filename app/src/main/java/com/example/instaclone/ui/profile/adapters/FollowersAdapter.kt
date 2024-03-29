package com.example.instaclone.ui.profile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.Preferences
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.databinding.ItemFollowerBinding
import com.example.instaclone.ui.auth.models.User

class FollowersAdapter(
    private val users: List<User>,
    private val uid: String,
    private val listener: OnClickListener
) : RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFollowerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class ViewHolder(private val binding: ItemFollowerBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            binding.tvUserName.text = user.username
            binding.tvName.text = user.name
            Glide.with(binding.root.context).load(user.userImage).into(binding.ivUserImage)
            if (user._id == uid){
                binding.btnView.visibility = View.GONE
            }else{
                binding.btnView.visibility = View.VISIBLE
                binding.btnView.setOnClickListener {
                    listener.onViewProfileClick(user._id)
                }
            }
        }
    }
    interface OnClickListener{
        fun onViewProfileClick(uid: String)
    }
}