package com.example.instaclone.ui.profile.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.instaclone.ui.auth.models.User
import com.example.instaclone.ui.profile.ProfileApi
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SearchProfilePagingSource(
    val api: ProfileApi,
    val token: String,
    val q: String
) : PagingSource<Int,User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: 1
        return try {
            val response = api.searchProfile(token,q)
            LoadResult.Page(
                data = response.Result!!.data,
                prevKey = if (position == 1) null else position-1,
                nextKey = if (position != response.Result.totalPages) position+1 else null
            )
        }catch (e: SocketTimeoutException){
            LoadResult.Error(e)
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return null
    }
}