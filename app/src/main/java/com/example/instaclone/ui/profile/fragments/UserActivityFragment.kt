package com.example.instaclone.ui.profile.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentUserActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivityFragment : Fragment(R.layout.fragment_user_activity) {

    private var _bindng: FragmentUserActivityBinding? = null
    private val binding: FragmentUserActivityBinding
        get() = _bindng!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentUserActivityBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bindng = null
    }
}