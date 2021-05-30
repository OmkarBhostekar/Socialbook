package com.example.instaclone.ui.polls.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.R
import com.example.instaclone.databinding.ItemPollBinding
import com.example.instaclone.ui.home.models.Post
import com.example.instaclone.ui.polls.models.Poll

class PollAdapter(
    val listener: OnClickListener
) : PagingDataAdapter<Poll,PollAdapter.ViewHolder>(POLL_COMPARATOR) {

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
        @SuppressLint("ClickableViewAccessibility")
        fun bind(position: Int){
            getItem(position)?.let { poll ->
                binding.apply {
                    var total = 0
                    poll.options.forEach {
                        total += it.voters.size
                    }
                    tvQuestion.text = poll.text
                    val list = listOf(option1,option2,option3,option4)
                    if (poll.voted != null && poll.voted){
                        poll.options.forEachIndexed { index, option ->
                            val progress = countProgress(option.voters.size,total)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                list[index].seekBar.setProgress(progress,true)
                            }
                            list[index].tvPercentage.text = "$progress %"
                            list[index].root.visibility = View.VISIBLE
                            list[index].tvPercentage.visibility = View.VISIBLE
                            list[index].tvOption.text = option.text
                            list[index].seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                                    list[index].seekBar.progress = progress
                                    if (poll.votedIndex != index)
                                        listener.onOptionClick(poll._id,option._id)
                                }

                                override fun onStartTrackingTouch(p0: SeekBar?) {

                                }

                                override fun onStopTrackingTouch(p0: SeekBar?) {

                                }
                            })
                            if (index == poll.votedIndex!!){
                                list[index].seekBar.progressDrawable = ContextCompat.getDrawable(binding.root.context,
                                    R.drawable.progress_track_selected)
                            }else{
                                list[index].seekBar.progressDrawable = ContextCompat.getDrawable(binding.root.context,
                                    R.drawable.progress_track)
                            }
//                            list[index].seekBar.isEnabled = false
//                            list[index].seekBar.setOnTouchListener { _, _ -> true }
                            list[index].seekBar.setOnClickListener {
                            }
                        }
                    }else{
                        poll.options.forEachIndexed { index, option ->
                            list[index].root.visibility = View.VISIBLE
                            list[index].tvOption.text = option.text
//                            list[index].seekBar.isEnabled = false
//                            list[index].seekBar.setOnTouchListener { _, _ -> true }
                            list[index].seekBar.setOnClickListener {
                                listener.onOptionClick(poll._id,option._id)
                            }
                            list[index].seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                                    list[index].seekBar.progress = 0
                                    if (poll.votedIndex != index)
                                        listener.onOptionClick(poll._id,option._id)
                                }

                                override fun onStartTrackingTouch(p0: SeekBar?) {

                                }

                                override fun onStopTrackingTouch(p0: SeekBar?) {

                                }
                            })
                        }
                    }
                }
            }
        }
    }

    interface OnClickListener{
        fun onOptionClick(pollId: String, optionId: String)
    }

    private fun countProgress(count: Int, total: Int): Int {
        return count * 100 / total
    }
}