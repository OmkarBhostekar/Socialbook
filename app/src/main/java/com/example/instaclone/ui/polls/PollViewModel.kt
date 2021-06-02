package com.example.instaclone.ui.polls

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.instaclone.comman.Constants
import com.example.instaclone.comman.Resource
import com.example.instaclone.ui.polls.models.NewOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PollViewModel @Inject constructor(
    private val repository: PollRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val voteAdded = MutableLiveData<String>()
    val pollCreated = MutableLiveData<Resource<String>>()

    fun getPolls() = repository.gePolls(dataStore).cachedIn(viewModelScope)

    fun createPoll(q: String, options: List<NewOption>) = viewModelScope.launch(Dispatchers.IO) {
        try{
            val body = HashMap<String,Any>()
            body["user"] = com.example.instaclone.data.DataStore.readStringFromDS(
                Constants.UID,
                dataStore
            ) ?: ""
            body["text"] = q
            body["options"] = options
            pollCreated.postValue(Resource.Loading())
            val response = repository.createPoll("Bearer ${
                com.example.instaclone.data.DataStore.readStringFromDS(
                    Constants.TOKEN,
                    dataStore
                )
            }",
            body)
            if (response.isSuccessful)
                pollCreated.postValue(Resource.Success("Poll Created Successfully"))
            else
                pollCreated.postValue(Resource.Error("An Error Occurred"))
        }catch(e: Exception){
            pollCreated.postValue(Resource.Error("An Error Occurred"))
        }catch(e: SocketTimeoutException){
            pollCreated.postValue(Resource.Error("An Error Occurred"))
        }
    }

    fun addOrUpdateVote(pollId: String,optionId: String) = viewModelScope.launch {
        try{
            val body = HashMap<String,Any>()
            body["optionId"] = optionId
            val response = repository.addOrUpdateVote(pollId,"Bearer ${
                com.example.instaclone.data.DataStore.readStringFromDS(
                    Constants.TOKEN,
                    dataStore
                )
            }",
            body)
            if (response.isSuccessful)
                voteAdded.postValue("Vote Added")
        }catch(e: Exception){
        }catch(e: SocketTimeoutException){
        }
    }
}