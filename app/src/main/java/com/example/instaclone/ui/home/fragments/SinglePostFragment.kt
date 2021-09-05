package com.example.instaclone.ui.home.fragments

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.toTimeDiff
import com.example.instaclone.databinding.FragmentSinglePostBinding
import com.example.instaclone.ui.home.HomeViewModel

class SinglePostFragment : Fragment(R.layout.fragment_single_post){

    private var _binding: FragmentSinglePostBinding? = null
    private val binding: FragmentSinglePostBinding
        get() = _binding!!
    private val args: SinglePostFragmentArgs by navArgs()
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSinglePostBinding.bind(view)

        args.post?.let { post ->
            binding.post.apply {
                Glide.with(binding.root.context).load(post.user.userImage).into(ivUserImage)
                tvUserName.text = post.user.username
                Glide.with(binding.root.context).load(post.image).into(ivPostImage)
                LikeAnimation.addAnimatorListener(object :
                    Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        LikeAnimation.visibility = View.GONE
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                        LikeAnimation.visibility = View.VISIBLE
                    }
                })
                ivPostImage.setOnTouchListener(
                    object : View.OnTouchListener {
                        val gestureDetector = GestureDetector(object :
                            GestureDetector.SimpleOnGestureListener() {
                            @SuppressLint("ClickableViewAccessibility")
                            override fun onDoubleTap(e: MotionEvent?): Boolean {
                                LikeAnimation.visibility = View.VISIBLE
                                LikeAnimation.playAnimation()
                                if (!post.isLiked) {
                                    homeViewModel.likePost(post._id)
                                    tvLikeCount.text = (post.likes.size + 1).toString()
                                    btnLike.setImageResource(R.drawable.liked)
                                    btnLike.imageTintList = null
                                }
                                return super.onDoubleTap(e)
                            }
                        })

                        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                            gestureDetector.onTouchEvent(p1)
                            return true
                        }
                    })

                tvLikeCount.text = post.likes.size.toString()
                tvLikeCount.setOnClickListener {
                    findNavController().navigate(R.id.fragmentBottomSheet,Bundle().apply {
                        putString("type","like")
                        putString("id",post._id)
                    })
                }
                tvCommentCount.text = post.comments.size.toString()
                tvPostDescription.text = SpannableStringBuilder()
                    .color(Color.BLACK){
                        bold { append(post.user.username) }
                    }
                    .append(" ${post.description}")
                if (post.isLiked) {
                    btnLike.setImageResource(R.drawable.liked)
                } else {
                    btnLike.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.black
                        )
                    )
                }
                tvTimeStamp.text = post.createdAt.toTimeDiff(root.context)
                root.setOnClickListener {
                    findNavController().navigate(
                        SinglePostFragmentDirections.actionSinglePostFragmentToPostDetailFragment(
                            post.user.username,
                            post.user.userImage,
                            post._id,
                            post.description,
                            post.user._id
                        )
                    )
                }
            }
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}