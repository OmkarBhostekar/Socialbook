package com.example.instaclone.ui.polls.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.databinding.ItemPollBinding
import com.example.instaclone.ui.home.models.Post
import com.example.instaclone.ui.polls.models.Poll

class PollAdapter : PagingDataAdapter<Poll,PollAdapter.ViewHolder>(POLL_COMPARATOR) {

    companion object{
        val POLL_COMPARATOR = object : DiffUtil.ItemCallback<Poll>() {
            override fun areItemsTheSame(oldItem: Poll, newItem: Poll): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Poll, newItem: Poll): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPollBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(private val binding: ItemPollBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            getItem(position)?.let { poll ->
                binding.apply {
                    tvQuestion.text = poll.text
                    val list = listOf(option1,option2,option3,option4)
                    poll.options.forEachIndexed { index, option ->
                        list[index].root.visibility = View.VISIBLE
                        list[index].tvOption.text = option.text
                    }
                }
            }
        }
    }
}