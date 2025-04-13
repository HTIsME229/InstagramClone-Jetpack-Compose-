package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.instagramclone.data.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel

//@HiltViewModel
//class PostViewModel:ViewModel() {
//    private var postData :MutableLiveData<Post> = MutableLiveData<Post>()
//    val _post: MutableLiveData<Post>
//        get() = postData
//    fun setPost(post: Post) {
//        postData.value = post
//    }
//
//}