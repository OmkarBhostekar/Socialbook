package com.example.instaclone.ui.polls

import com.example.instaclone.comman.ApiResponse
import com.example.instaclone.response.PagingResponse
import com.example.instaclone.ui.polls.models.Poll
import retrofit2.Response
import retrofit2.http.*

interface PollApi {

    @GET("/poll/")
    suspend fun getPolls(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ) : ApiResponse<PagingResponse<List<Poll>>>

    @POST("/poll/")
    suspend fun createPoll(
        @Header("Authorization") token: String,
        @Body body: HashMap<String, Any>
    ) : Response<ApiResponse<Any>>

    @PATCH("/poll/{pollId}")
    suspend fun addOrUpdateVote(
        @Path("pollId") pollId: String,
        @Header("Authorization") token: String,
        @Body body: HashMap<String,Any>
    ) : Response<ApiResponse<String>>
}