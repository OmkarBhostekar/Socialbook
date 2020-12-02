package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_explore) {

    private var _bindng: FragmentExploreBinding? = null
    private val binding:FragmentExploreBinding
        get() =  _bindng!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentExploreBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}