package com.example.instaclone.ui.profile

import com.example.instaclone.comman.ApiResponse
import com.example.instaclone.response.PagingResponse
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.home.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApi {

    @GET("/profile/{id}")
    suspend fun getProfile(
        @Path("id") uid: String,
        @Header("Authorization") token: String
    ) : Response<ApiResponse<User>>

    @GET("/profile/{id}/posts")
    suspend fun getProfilePosts(
        @Path("id") uid: String,
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ) : ApiResponse<PagingResponse<List<Post>>>
    
}