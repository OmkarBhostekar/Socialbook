package com.example.instaclone.ui.home

import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: HomeApi
) {

    suspend fun getAllPosts(token: String) = api.getAllPosts(token)

    suspend fun createPost(token: String,body: HashMap<String,Any>) = api.createNewPost(token, body)

    suspend fun likePost(postId: String,token: String) = api.likePost(postId, token)

    suspend fun getLikedBy(postId: String,token: String) = api.getLikedBy(postId, token)

    suspend fun commentOnPost(postId: String,token: String,body: HashMap<String,Any>)
    = api.commentOnPost(postId, token, body)

    suspend fun getAllCommentsOfPost(postId: String,token: String) = api.getAllCommentsOfPost(postId, token)
}