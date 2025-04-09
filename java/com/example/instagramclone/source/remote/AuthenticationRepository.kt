package com.example.instagramclone.source.remote

import android.annotation.SuppressLint
import android.util.Log
import com.example.instagramclone.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class AuthenticationRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    fun registerUser(
        user: User,
        onResult: (Boolean, String?) -> Unit
    ) {
        val usersRef = db.collection("users")

        usersRef.whereEqualTo("userName", user.userName)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.isEmpty) {
                        onResult(false, "The username is exits, please choose another one.")
                        return@addOnCompleteListener
                    }
                    // Nếu username chưa tồn tại, tiến hành đăng ký
                    auth.createUserWithEmailAndPassword(user.email, user.password)
                        .addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val uid =
                                    auth.currentUser?.uid ?: return@addOnCompleteListener onResult(
                                        false,
                                        "Không thể lấy UID"
                                    )

                                val userMap = hashMapOf(
                                    "uid" to uid,
                                    "email" to user.email,
                                    "userName" to user.userName,
                                    "imageUrl" to user.imageUrl,
                                    "password" to user.password,
                                    "bio" to user.bio,
                                    "followers" to user.followers,
                                    "following" to user.following,
                                    "name" to user.name,
                                    "createdAt" to FieldValue.serverTimestamp()
                                )

                                db.collection("users").document(uid)
                                    .set(userMap)
                                    .addOnCompleteListener { firestoreTask ->
                                        if (firestoreTask.isSuccessful) {
                                            onResult(true, null)
                                        } else {
                                            onResult(false, firestoreTask.exception?.message)
                                        }
                                    }
                            } else {
                                Log.e("FirebaseAuth", "Lỗi đăng nhập: ${task.exception?.message}")

                                onResult(false, authTask.exception?.message)
                            }
                        }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
    @Suppress("UNCHECKED_CAST")
    fun loginUser(
        email: String,
        password: String,
        onResult: (Boolean, String?, User?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        onResult(false, "Không lấy được UID", null)
                        return@addOnCompleteListener
                    }

                    val userRef = db.collection("users").document(uid)

                    userRef.get().addOnCompleteListener { firestoreTask ->
                        if (firestoreTask.isSuccessful) {
                            val document = firestoreTask.result

                            // 🔴 Kiểm tra document có tồn tại không
                            if (document == null || !document.exists()) {
                                onResult(false, "Not found user data", null)
                                return@addOnCompleteListener
                            }

                            // 🔄 Cập nhật token trước khi trả về

                                    val user = User(
                                        userId = document.getString("uid") ?: "",
                                        email = document.getString("email") ?: "",
                                        userName = document.getString("userName") ?: "",
                                        imageUrl = document.getString("imageUrl") ?: "",
                                        name = document.getString("name")?:"",
                                        followers = document.get("followers") as? MutableList<String> ?: mutableListOf(),
                                       following  = document.get("following") as? MutableList<String> ?: mutableListOf()
                                    )
                                    onResult(true, null, user)

                        } else {
                            val errorMsg = firestoreTask.exception?.message ?: "Lỗi không xác định"
                            onResult(false, "Lỗi lấy dữ liệu Firestore: $errorMsg", null)
                        }
                    }
                } else {
                    onResult(false, authTask.exception?.message, null)
                }
            }
    }
}