package com.example.instaclone.ui.home.adapters

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.R
import com.example.instaclone.comman.toLikeCount
import com.example.instaclone.databinding.ItemPostBinding
import com.example.instaclone.ui.home.models.Post

class PostsAdapter(
    val listener: OnClickListener
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer<Post>(this, diffCallback)

    var posts: List<Post>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val post = posts[position]
        val isLastPost = posts.size - 1 == position
        holder.bind(post, isLastPost)
    }

    override fun getItemCount(): Int {
        return posts.size;
    }

    inner class PostsViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: Post, isLastPost: Boolean) {
            binding.apply {
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
                LikeAnimation.setOnTouchListener(
                    object : View.OnTouchListener {
                        val gestureDetector = GestureDetector(object :
                            GestureDetector.SimpleOnGestureListener() {
                            @SuppressLint("ClickableViewAccessibility")
                            override fun onDoubleTap(e: MotionEvent?): Boolean {
                                Log.d("MyActivity", "onDoubleTap: double tap")
                                LikeAnimation.visibility = View.VISIBLE
                                LikeAnimation.playAnimation()
                                if (!post.isLiked) {
                                    listener.onLike(post._id)
                                    tvLikeCount.text = (post.likes.size + 1).toLikeCount()
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
                tvLikeCount.setOnClickListener { listener.viewLikes(post._id) }
                tvCommentCount.text = post.comments.size.toString()
                tvPostDescription.text = post.description
                if (isLastPost) {
                    binding.root.setPadding(0, 0, 0, 150)
                }
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
                root.setOnClickListener {
                    listener.onClick(post._id,post.image,ivPostImage,tvUserName)
                }
            }
        }
    }

    interface OnClickListener{
        fun onLike(postId: String)
        fun viewLikes(postId: String)
        fun onClick(postId: String,image: String,ivPostImage: ImageView,tvUserName: TextView)
    }
}