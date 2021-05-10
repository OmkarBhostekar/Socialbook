package com.example.instaclone.ui.home

import com.example.instaclone.comman.ApiResponse
import com.example.instaclone.comman.MessageResponse
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.home.models.Comment
import com.example.instaclone.ui.home.models.Post
import retrofit2.Response
import retrofit2.http.*

interface PostsApi {

    @POST("/posts/new")
    suspend fun createNewPost(
        @Body body: HashMap<String,Any>
    ) : Response<MessageResponse>

    @GET("/posts")
    suspend fun getAllPosts(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<Post>>>

    @POST("/posts/like")
    suspend fun likePost(
        @Body body: HashMap<String,Any>
    ) : Response<MessageResponse>

    @GET("/posts/like")
    suspend fun getLikedBy(
        @Query("postId") postId: String,
        @Query("uid") uid: String,
    ) : Response<List<User>>

    @POST("/posts/comment")
    suspend fun commentOnPost(
        @Body body: HashMap<String,Any>
    ) : Response<MessageResponse>

    @GET("/posts/comment")
    suspend fun getAllCommentsOfPost(
        @Query("postId") postId: String
    ) : Response<List<Comment>>
}