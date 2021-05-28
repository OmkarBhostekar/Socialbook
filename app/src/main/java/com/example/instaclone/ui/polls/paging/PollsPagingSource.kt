package com.example.instaclone.ui.polls.paging

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.instaclone.comman.Constants
import com.example.instaclone.ui.polls.PollApi
import com.example.instaclone.ui.polls.models.Poll
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class PollsPagingSource(
    private val api: PollApi,
    private val dataStore: DataStore<Preferences>
) : PagingSource<Int,Poll>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Poll> {
        val position = params.key ?: 1
        return try {
            val response = api.getPolls("Bearer ${
                com.example.instaclone.data.DataStore.readStringFromDS(
                    Constants.TOKEN,
                    dataStore
                )}", position)
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

    override fun getRefreshKey(state: PagingState<Int, Poll>): Int? {
        return null
    }
}