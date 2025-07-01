package com.example.instagramclone.presentation.viewModel.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.data.repository.DefaultRepositoryImpl
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
    private  var postSelected :MutableStateFlow<Post?> = MutableStateFlow(null)
    val _postSelected: StateFlow<Post?> = postSelected
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
    fun getPostById(postId: String, onSuccess: (Post?) -> Unit, onError: (String?) -> Unit) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.getPostById(postId)
            result.collect {
                if (it != null) {
                    if (it.success) {
                        postSelected.value = it.data
                    } else {
                        postSelected.value = null
                        onError(it.error)
                    }

                } else {
                    postSelected.value = null
                    onError("Post not found")
                }
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
                getPostById(comment.postId,{},{})
                getListCommentPost(comment.postId,
                    onSuccess = {
                        onSuccess(true)
                    },
                    onError = {
                        onError(it)
                    })
                onSuccess(true)
            } else {
                onError(result.error)
            }
        }
    }

    fun getListCommentPost(postId: String,onSuccess: (Boolean) -> Unit, onError: (String?) -> Unit) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.getListCommentPost(postId)
            result.collect {
                if (it != null) {
                    listPostComment.value = it
                    onSuccess(true)
                } else {
                    listPostComment.value = emptyList()
                    onError
                }
            }
        }
    }
}