package com.example.instagramclone.presentation.viewModel.auth

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.data.repository.DefaultRepositoryImpl
import com.example.instagramclone.data.remote.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private  val  authenticationRepository: AuthenticationRepository,
    private val defaultRepositoryImpl: DefaultRepositoryImpl
)  :ViewModel() {
    private var profile : MutableLiveData<User?> = MutableLiveData()
    val _profile: MutableLiveData<User?> get() = profile

    fun setProfile(user: User) {
        profile.postValue(user)
    }
fun login(email:String,password:String,onResult: (Boolean, String?) -> Unit) {
    viewModelScope.launch {
        authenticationRepository.loginUser(email, password,) { success, message, user ->
            if (success) {
                if (user != null) {
                    profile.postValue(user)
                    onResult(true, null)

                } else {
                    profile.postValue(null)
                    onResult(false, message)
                }
            } else {
                profile.postValue(null)
                onResult(false, message)
            }
        }
    }
}
    fun register(user: User, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            authenticationRepository.registerUser(user) { success, message ->
                if (success) {
                    onResult(true, null)
                } else if (message != null) {
                    Log.e("TAGUser", "Đăng ký thất bại: $message")
                    onResult(false, message)
                } else {
                    onResult(false, "server error")
                }
            }
        }
    }
    fun refreshUser(userId:String,idToken:String) {
        viewModelScope.launch {
           val result= defaultRepositoryImpl.findUserByUid(userId,idToken)
            result.collect { user ->
                if (user != null) {
                    profile.postValue(user)
                } else {
                    profile.postValue(null
                    )
                }
            }
        }
    }
    fun uploadFile(context:Context,uri: Uri,onSuccess: (String) -> Unit, onError:  (Exception) -> Unit = {}) {
        authenticationRepository.uploadFile(context,uri,
                 {
                     onSuccess(it)
                 }, {
                     onError(it)
                 })
    }
}