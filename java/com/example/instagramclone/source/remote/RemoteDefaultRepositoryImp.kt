package com.example.instagramclone.source.remote

import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.DefaultRepository
import com.example.instagramclone.source.ResponseResult
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDefaultRepositoryImp: DefaultRepository.RemoteRepository {
    override suspend fun updateProfile(user: User): ResponseResult {
            val baseUrl = "https://updateuserprofile-ba53qvmrba-uc.a.run.app"
        val retrofit = createRetrofitService(baseUrl).create(InstagramService::class.java)
try {
    val  response = retrofit.updateProfile(user)
    if(response.isSuccessful) {
        return ResponseResult(true, null)
    }
    else{
        return ResponseResult(false, response.errorBody()?.string())
    }

}
catch (e: Exception) {
    return ResponseResult(false, e.message)
}
    }

    override fun findUserByUid(userId: String,tokenId:String): Flow<User?> = flow {
        val data = hashMapOf(
            "userId" to userId,
            "idToken" to tokenId
        )
        val baseUrl = "https://finduserwithuid-ba53qvmrba-uc.a.run.app"
        val retrofit = createRetrofitService(baseUrl).create(InstagramService::class.java)
        try {
         val response = retrofit.findUserById(data)
            if (response.isSuccessful) {
                emit(response.body())
            } else {
                emit(null)
            }

        }
        catch (e: Exception) {
            emit(null)
        }
        }

    private fun createRetrofitService(baseUrl: String): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Log toàn bộ request/response
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))  // Sử dụng Gson với TypeAdapter
            .client(client)
            .build()
    }
}