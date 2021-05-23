package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentExploreBinding
import com.example.instaclone.ui.profile.ProfileViewModel
import com.example.instaclone.ui.profile.adapters.SearchProfileAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_explore),SearchProfileAdapter.OnClickListener {

    private var _bindng: FragmentExploreBinding? = null
    private val binding:FragmentExploreBinding
        get() =  _bindng!!
    private val viewModel: ProfileViewModel by viewModels()
    lateinit var adapter: SearchProfileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentExploreBinding.bind(view)

        adapter = SearchProfileAdapter(this)
        binding.rvSearch.adapter = adapter

        binding.etSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                lifecycleScope.launch {
                    viewModel.searchProfileFlow(it.toString()).collectLatest { data ->
                        adapter.submitData(viewLifecycleOwner.lifecycle,data)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }

    override fun onClick(uid: String) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSearchedProfileFragment(uid))
    }
}