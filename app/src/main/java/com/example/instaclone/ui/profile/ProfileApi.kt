package com.example.instaclone.ui.profile

import com.example.instaclone.comman.ApiResponse
import com.example.instaclone.response.PagingResponse
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.home.models.Post
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("/profile/{id}")
    suspend fun getProfile(
        @Path("id") uid: String?,
        @Header("Authorization") token: String?
    ) : Response<ApiResponse<User>>

    @GET("/profile/{id}/posts")
    suspend fun getProfilePosts(
        @Path("id") uid: String,
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1
    ) : ApiResponse<PagingResponse<List<Post>>>

    @POST("/profile/{id}/follow")
    suspend fun followUser(
        @Path("id") uid: String,
        @Header("Authorization") token: String,
    )

    @POST("/profile/{id}/unfollow")
    suspend fun unfollowUser(
        @Path("id") uid: String,
        @Header("Authorization") token: String,
    )

    @PATCH("/profile/")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body body: HashMap<String, Any>
    )

    @GET("/profile/{id}/followers/")
    suspend fun getUserFollowers(
        @Path("id") uid: String,
        @Header("Authorization") token: String,
    ) : Response<ApiResponse<List<User>>>

    @GET("/profile/{id}/following/")
    suspend fun getUserFollowing(
        @Path("id") uid: String,
        @Header("Authorization") token: String,
    ) : Response<ApiResponse<List<User>>>

    @GET("/profile/search")
    suspend fun searchProfile(
        @Header("Authorization") token: String,
        @Query("q") query: String
    ) : ApiResponse<PagingResponse<List<User>>>
}