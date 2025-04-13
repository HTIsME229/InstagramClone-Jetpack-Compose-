package com.example.instagramclone.source.remote

import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.ResponseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface InstagramService {
    @POST("/")
  suspend  fun updateProfile(@Body user: User): Response<ResponseResult>
  @POST("/")
    suspend fun findUserById(@Body request: Map<String,String>): Response<User?>
}