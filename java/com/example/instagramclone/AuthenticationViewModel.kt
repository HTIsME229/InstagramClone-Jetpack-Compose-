package com.example.instagramclone

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authentication: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
)  :ViewModel() {
fun login(email:String,password:String) {
    viewModelScope.launch {
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Đăng nhập thành công
                val user = authentication.currentUser
                Log.d("TAGUser", "Đăng nhập thành công: ${user?.uid}")
            } else {
                // Đăng nhập thất bại
                val errorMessage = task.exception?.message
                Log.e("TAGUser", "Đăng nhập thất bại: $errorMessage")
                // Xử lý lỗi tại đây
            }
        }
    }
}
}