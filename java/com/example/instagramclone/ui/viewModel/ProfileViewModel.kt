package com.example.instagramclone.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.DefaultRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: DefaultRepositoryImpl) :
    ViewModel() {


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
}