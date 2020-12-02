package com.example.instaclone.ui.posts

import com.example.instaclone.comman.MessageResponse
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.posts.models.Comment
import com.example.instaclone.ui.posts.models.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PostsApi {

    @POST("/posts/new")
    suspend fun createNewPost(
        @Body body: HashMap<String,Any>
    ) : Response<MessageResponse>

    @GET("/posts")
    suspend fun getAllPosts(
        @Query("uid") uid: String
    ): Response<List<Post>>

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