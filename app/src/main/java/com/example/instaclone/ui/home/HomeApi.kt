package com.example.instaclone.ui.home

import com.example.instaclone.comman.ApiResponse
import com.example.instaclone.comman.MessageResponse
import com.example.instaclone.response.PagingResponse
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.home.models.Comment
import com.example.instaclone.ui.home.models.Post
import retrofit2.Response
import retrofit2.http.*

interface HomeApi {

    @POST("/posts")
    suspend fun createNewPost(
        @Header("Authorization") token: String,
        @Body body: HashMap<String, Any>
    ) : Response<ApiResponse<Any>>

    @GET("/posts")
    suspend fun getAllPosts(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ): ApiResponse<PagingResponse<List<Post>>>

    @POST("/posts/{postId}/like")
    suspend fun likePost(
        @Path("postId") postId: String,
        @Header("Authorization") token: String
    ) : Response<ApiResponse<String>>

    @GET("/posts/{postId}/like")
    suspend fun getLikedBy(
        @Path("postId") postId: String,
        @Header("Authorization") token: String
    ) : Response<ApiResponse<List<User>>>

    @POST("/posts/{postId}/comment")
    suspend fun commentOnPost(
        @Path("postId") postId: String,
        @Header("Authorization") token: String,
        @Body body: HashMap<String,Any>
    ) : Response<ApiResponse<Any>>

    @DELETE("/posts/{postId}/comment")
    suspend fun deleteComment(
        @Path("postId") postId: String,
        @Header("Authorization") token: String
    ) : Response<ApiResponse<Any>>

    @GET("/posts/{postId}/comment")
    suspend fun getAllCommentsOfPost(
        @Path("postId") postId: String,
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ) : ApiResponse<PagingResponse<List<Comment>>>
}