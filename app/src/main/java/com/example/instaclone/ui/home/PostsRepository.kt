package com.example.instaclone.ui.home

import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: PostsApi
) {

    suspend fun getAllPosts(token: String) = api.getAllPosts(token)

    suspend fun createPost(body: HashMap<String,Any>) = api.createNewPost(body)

    suspend fun likePost(body: HashMap<String,Any>) = api.likePost(body)

    suspend fun getLikedBy(postId: String,uid: String) = api.getLikedBy(postId, uid)

    suspend fun commentOnPost(body: HashMap<String,Any>) = api.commentOnPost(body)

    suspend fun getAllCommentsOfPost(postId: String) = api.getAllCommentsOfPost(postId)
}