package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.data.model.Comment
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.DefaultRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val defaultRepositoryImpl: DefaultRepositoryImpl
) : ViewModel() {
    private var listPostFollowing: MutableLiveData<List<Post>?> = MutableLiveData()
    val _listPostFollowing: LiveData<List<Post>?> = listPostFollowing
    private var listPostLiked: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val _listPostLiked: StateFlow<List<String>> = listPostLiked
    var hadFetchListPost = false
    val listUserSearched = MutableLiveData<List<User>?>()
    val _listUserSearched: LiveData<List<User>?> = listUserSearched
    val listPostSearched = MutableLiveData<List<Post>?>()
    val _listPostSearched: LiveData<List<Post>?> = listPostSearched
    val listPostComment : MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
    val _listPostComment: StateFlow<List<Comment>> = listPostComment
    var hadLikedPostFetch = false
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

    fun toggleLikePost(
        userId: String,
        postId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (String?) -> Unit
    ) {
        val currentList = listPostLiked.value
        val newList = if (currentList.contains(postId)) {
            currentList - postId
        } else {
            currentList + postId
        }
        listPostLiked.value = newList
        viewModelScope.launch {
            val result = defaultRepositoryImpl.toggleLikePost(userId, postId)
            if (result.success) {
                onSuccess(true)
            } else {
                listPostLiked.value = currentList
                onError(result.error)
            }
        }
    }
    fun loadLikedPost(userId: String) {
        if(!hadLikedPostFetch)
        viewModelScope.launch {
            val result = defaultRepositoryImpl.loadLikedPost(userId)
            result.collect {
                if(it !=null)
                listPostLiked.value = it
            }
        }
    }
    fun loadMyListPost(userId: String) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.loadMyListPost(userId)
            result.collect {
                listPostFollowing.postValue(it)
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

    fun createComment(
        comment: Comment,
        onSuccess: (Boolean) -> Unit,
        onError: (String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.createComment(comment)
            if (result.success) {
                getListCommentPost(comment.postId)
                onSuccess(true)
            } else {
                onError(result.error)
            }
        }
    }

    fun getListCommentPost(postId: String) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.getListCommentPost(postId)
            result.collect {
                if (it != null) {
                    listPostComment.value = it
                } else {
                    listPostComment.value = emptyList()
                }
            }
        }
    }
}