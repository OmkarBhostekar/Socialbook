package com.example.instaclone.ui.polls.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentPollsBinding
import com.example.instaclone.ui.polls.PollViewModel
import com.example.instaclone.ui.polls.adapters.PollAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PollsFragment : Fragment(R.layout.fragment_polls),PollAdapter.OnClickListener{

    private var _binding: FragmentPollsBinding? = null
    private val binding: FragmentPollsBinding
        get() = _binding!!
    private val viewModel: PollViewModel by viewModels()
    lateinit var adapter: PollAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPollsBinding.bind(view)

        adapter = PollAdapter(this)
        binding.rvPolls.adapter = adapter

        lifecycleScope.launch {
            viewModel.getPolls().collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }

//        adapter.addLoadStateListener { loadState ->
//            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
//        }

        viewModel.voteAdded.observe(viewLifecycleOwner,{
            adapter.refresh()
        })

    }

    override fun onOptionClick(pollId: String, optionId: String) {
        viewModel.addOrUpdateVote(pollId, optionId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}