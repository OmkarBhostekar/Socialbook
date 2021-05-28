package com.example.instaclone.ui.polls.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentPollsBinding
import com.example.instaclone.ui.polls.PollViewModel
import com.example.instaclone.ui.polls.adapters.PollAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PollsFragment : Fragment(R.layout.fragment_polls){

    private var _binding: FragmentPollsBinding? = null
    private val binding: FragmentPollsBinding
        get() = _binding!!
    private val viewModel: PollViewModel by viewModels()
    lateinit var adapter: PollAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPollsBinding.bind(view)

        adapter = PollAdapter()
        binding.rvPolls.adapter = adapter

        lifecycleScope.launch {
            viewModel.getPolls().collectLatest {
                adapter.submitData(viewLifecycleOwner.lifecycle,it)
            }
        }


    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}