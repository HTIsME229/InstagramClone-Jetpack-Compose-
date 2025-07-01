package com.example.instagramclone.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import com.example.instagramclone.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback
import com.example.instagramclone.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

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
                    Log.e("FirebaseAuth", "Lỗi đăng nhập: ${task.exception?.message}")
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
        context: Context,
        fileUri: Uri,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        // Sử dụng coroutine để không block UI thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = uploadToCloudinary(context, fileUri)

                // Chuyển về Main thread để update UI
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        onSuccess(result)
                    } else {
                        onError(Exception("Upload failed - no URL returned"))
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }
    private suspend fun uploadToCloudinary(context: Context, fileUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(fileUri) ?: "application/octet-stream"
                val isVideo = mimeType.startsWith("video")
                val isImage = mimeType.startsWith("image")

                val inputStream = contentResolver.openInputStream(fileUri)
                val fileBytes = inputStream?.readBytes()
                inputStream?.close()

                if (fileBytes == null) throw Exception("Cannot read file")

                val fileName = if (isVideo) "upload.mp4" else "upload.jpg"

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        fileName,
                        fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())
                    )
                    .addFormDataPart("upload_preset", BuildConfig.CLOUDINARY_UPLOAD_PRESET)
                    .build()

                val mediaTypePath = when {
                    isVideo -> "video"
                    isImage -> "image"
                    else -> throw Exception("Unsupported media type: $mimeType")
                }

                val request = Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/${BuildConfig.CLOUDINARY_CLOUD_NAME}/$mediaTypePath/upload")
                    .post(requestBody)
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody ?: "")
                    jsonResponse.getString("secure_url")
                } else {
                    throw Exception("HTTP ${response.code}: ${response.message}")
                }

            } catch (e: Exception) {
                throw e
            }
        }
    }

}
