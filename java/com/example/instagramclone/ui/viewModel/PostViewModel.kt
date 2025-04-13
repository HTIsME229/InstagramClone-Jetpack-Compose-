package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.source.DefaultRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
private val defaultRepositoryImpl: DefaultRepositoryImpl):ViewModel() {

    fun uploadPost (post: Post, onSuccess: (Boolean) -> Unit,onError: (String?) -> Unit) {
        viewModelScope.launch {
            val result = defaultRepositoryImpl.uploadPost(post)
            if (result.success) {

                onSuccess(true)
            } else {
                onError(result.error)

            }
        }
    }
}