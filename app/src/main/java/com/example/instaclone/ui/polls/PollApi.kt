package com.example.instaclone.ui.polls

import com.example.instaclone.comman.ApiResponse
import com.example.instaclone.response.PagingResponse
import com.example.instaclone.ui.polls.models.Poll
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PollApi {

    @GET("/poll/")
    suspend fun getPolls(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ) : ApiResponse<PagingResponse<List<Poll>>>
}