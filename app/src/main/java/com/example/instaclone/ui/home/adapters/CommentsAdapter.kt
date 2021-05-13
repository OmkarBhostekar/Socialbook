package com.example.instaclone.ui.home.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.databinding.ItemCommentBinding
import com.example.instaclone.ui.home.models.Comment

class CommentsAdapter() : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    var comments: List<Comment> = listOf()
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class ViewHolder(private val binding: ItemCommentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val comment = comments[position]
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