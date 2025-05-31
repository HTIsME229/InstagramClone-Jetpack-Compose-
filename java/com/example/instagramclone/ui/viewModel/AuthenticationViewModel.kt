package com.example.instagramclone.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.DefaultRepositoryImpl
import com.example.instagramclone.source.remote.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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
    fun register(user: User,onResult: (Boolean, String?) -> Unit) {
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