package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _bindng: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
    get() = _bindng!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentProfileBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}