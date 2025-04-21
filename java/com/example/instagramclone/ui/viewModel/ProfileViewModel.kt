package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.DefaultRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: DefaultRepositoryImpl) :
    ViewModel() {
       private var myListPost = MutableLiveData<List<Post>?>()
    val _myListPost: MutableLiveData<List<Post>?> get() = myListPost
var hadFetchMyListPost=false



    fun updateProfile(user: User,onSuccess : (Boolean,User) -> Unit,
                      onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = repository.updateProfile(user)
            if (result.success) {
                onSuccess(true,user)
            } else {
                onError(result.error ?: "Unknown error")
            }
        }

    }
    fun loadMyListPost(userId: String,forcedUpdate:Boolean=false) {
        if(!hadFetchMyListPost || forcedUpdate)
        viewModelScope.launch {
            val result = repository.loadMyListPost(userId)
            result.collect {
                if (it != null) {
                    myListPost.postValue(it)
                } else {
                    myListPost.postValue(null)
                }
            }
        }
    }

}