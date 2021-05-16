package com.example.instaclone.ui.home.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.databinding.ItemCommentBinding
import com.example.instaclone.ui.home.models.Comment

class CommentsAdapter() : PagingDataAdapter<Comment,CommentsAdapter.ViewHolder>(COMMENT_COMPARATOR) {

    companion object{
        val COMMENT_COMPARATOR = object : DiffUtil.ItemCallback<Comment>(){
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: ItemCommentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val comment = getItem(position)
            comment?.let {
                binding.apply {
                    Glide.with(root.context).load(comment.user.userImage).into(circleImageView)
                    tvComment.text = SpannableStringBuilder()
                        .color(Color.BLACK){
                            bold { append("${comment.user.username}  ") }
                        }
                        .append(comment.text)
                }
            }
        }
    }
}