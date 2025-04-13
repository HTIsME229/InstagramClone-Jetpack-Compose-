package com.example.instagramclone.source.remote

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.example.instagramclone.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class AuthenticationRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
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
                    var tokenId = ""
                auth.currentUser?.getIdToken(false).let {
                        it?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                 tokenId = task.result?.token?:""
                            } else {
                                Log.e("TAG", "Error getting token: ${task.exception?.message}")
                            }
                        }
                    }
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
                                        tokenId = tokenId,
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
    fun uploadFile(
        fileUri: Uri,
        onSuccess: (String) -> Unit,
        onError:  (Exception) -> Unit = {}
    ) {
        val storageRef = storage.reference
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val fileRef = storageRef.child(fileName)

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString()) // Gọi hàm khi upload thành công
                }
            }
            .addOnFailureListener { exception ->
                onError(exception) // Gọi hàm khi lỗi
            }
    }
}