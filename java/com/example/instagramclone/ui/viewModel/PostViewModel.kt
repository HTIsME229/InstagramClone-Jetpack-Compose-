package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.LiveData
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
class PostViewModel @Inject constructor(
    private val defaultRepositoryImpl: DefaultRepositoryImpl
) : ViewModel() {
    private var listPostFollowing: MutableLiveData<List<Post>?> = MutableLiveData()
    val _listPostFollowing: LiveData<List<Post>?> = listPostFollowing
    var hadFetchListPost = false
    val listUserSearched = MutableLiveData<List<User>?>()
    val _listUserSearched: LiveData<List<User>?> = listUserSearched
    val listPostSearched = MutableLiveData<List<Post>?>()
    val _listPostSearched: LiveData<List<Post>?> = listPostSearched
    fun uploadPost(post: Post, onSuccess: (Boolean) -> Unit, onError: (String?) -> Unit) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.uploadPost(post)
            if (result.success) {

                onSuccess(true)
            } else {
                onError(result.error)

            }
        }
    }

    fun loadListPostFollowing(userId: String) {
        viewModelScope.launch {
            if (!hadFetchListPost) {
                val result = defaultRepositoryImpl.loadListPostFollowing(userId)
                result.collect {
                    if (it != null) {
                        listPostFollowing.postValue(it)
                        hadFetchListPost = true

                    } else {
                        hadFetchListPost = true
                        listPostFollowing.postValue(null)
                    }
                }
            }
        }


    }
    fun searchUserAndPost(query:String, type:String = "all"){
        viewModelScope.launch {
            val result= defaultRepositoryImpl.searchPostAndUser(query,type)
            if(result.success){
                listPostSearched.postValue(result.posts)
                listUserSearched.postValue(result.users)

            }
            else{
                listPostSearched.postValue(null)
                listUserSearched.postValue(null)
            }
        }
    }
}