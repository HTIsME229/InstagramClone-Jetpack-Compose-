package com.example.instagramclone.source.remote

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User


class AuthenticationRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//    fun registerUser(
//        user: User,
//        onResult: (Boolean, String?) -> Unit
//    ) {
//        val usersRef = db.collection("users")
//
//        usersRef.whereEqualTo("userName", user.userName)
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    if (!task.result.isEmpty) {
//                        onResult(false, "Tên người dùng đã được sử dụng, hãy chọn tên khác.")
//                        return@addOnCompleteListener
//                    }
//                    // Nếu username chưa tồn tại, tiến hành đăng ký
//                    auth.createUserWithEmailAndPassword(user.email, user.password)
//                        .addOnCompleteListener { authTask ->
//                            if (authTask.isSuccessful) {
//                                val uid =
//                                    auth.currentUser?.uid ?: return@addOnCompleteListener onResult(
//                                        false,
//                                        "Không thể lấy UID"
//                                    )
//
//                                val userMap = hashMapOf(
//                                    "uid" to uid,
//                                    "email" to user.email,
//                                    "userName" to user.userName,
//                                    "avatar" to user.avatar,
//                                    "token" to user.token,
//                                    "display_name" to user.displayName,
//                                    "friends" to user.friends,
//                                    "friendRequests" to user.friendRequests,
//                                    "createdAt" to FieldValue.serverTimestamp()
//                                )
//
//                                db.collection("users").document(uid)
//                                    .set(userMap)
//                                    .addOnCompleteListener { firestoreTask ->
//                                        if (firestoreTask.isSuccessful) {
//                                            onResult(true, null)
//                                        } else {
//                                            onResult(false, firestoreTask.exception?.message)
//                                        }
//                                    }
//                            } else {
//                                Log.e("FirebaseAuth", "Lỗi đăng nhập: ${task.exception?.message}")
//
//                                onResult(false, authTask.exception?.message)
//                            }
//                        }
//                } else {
//                    onResult(false, task.exception?.message)
//                }
//            }
//    }
fun loginUser(
    email: String,
    password: String,
    @SuppressLint("RestrictedApi") onResult: (Boolean, String?, User?) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val uid = auth.currentUser?.uid

                if (uid == null) {
                    onResult(false, "Không lấy được UID", null)
                    return@addOnCompleteListener
                }
            } else {
                onResult(false, authTask.exception?.message, null)
            }
        }
}
}