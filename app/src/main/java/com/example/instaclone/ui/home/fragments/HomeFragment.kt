package com.example.instaclone.ui.home.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.databinding.FragmentHomeBinding
import com.example.instaclone.ui.home.HomeViewModel
import com.example.instaclone.ui.home.adapters.PostsAdapter
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), PostsAdapter.OnClickListener {

    private var _bindng: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() =  _bindng!!
    private val viewModel: HomeViewModel by viewModels()
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindng = FragmentHomeBinding.bind(view)

        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful)
                Timber.d(it.result)
        }

        viewModel.getAllPosts()
        val postsAdapter = PostsAdapter(this)
        binding.rvPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(activity)
            postponeEnterTransition()
            viewTreeObserver.addOnDrawListener {
                startPostponedEnterTransition()
                true
            }
        }

        viewModel.posts.observe(viewLifecycleOwner,{
            postsAdapter.posts = it
        })
    }

    override fun onLike(postId: String) {
        viewModel.likePost(postId)
    }

    override fun viewLikes(postId: String) {
        // show dialog that will show users who liked that post
        findNavController().navigate(R.id.action_homeFragment_to_fragmentBottomSheet,Bundle().apply {
            putString("type","like")
            putString("id",postId)
        })
    }

    override fun onClick(postId: String,image: String, ivPostImage: ImageView,tvUserName: TextView) {
        val extras = FragmentNavigatorExtras(
            ivPostImage to "post_image",
            tvUserName to "textview"
        )
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(image),extras)
    }
}