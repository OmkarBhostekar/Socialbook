package com.example.instaclone.ui.polls.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.databinding.ItemAddOptionBinding
import com.example.instaclone.ui.polls.models.NewOption
import timber.log.Timber

class AddOptionAdapter(
    val listener: OnClickListener
) : RecyclerView.Adapter<AddOptionAdapter.ViewHolder>() {

    var list: List<NewOption> = listOf()
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAddOptionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemAddOptionBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.tvOption.text = list[position].text
            binding.btnRemove.setOnClickListener {
                listener.onDeleteClick(position)
            }
        }
    }

    interface OnClickListener{
        fun onDeleteClick(position: Int)
    }
}