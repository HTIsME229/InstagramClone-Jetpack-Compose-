package com.example.instagramclone.source.remote

import com.example.instagramclone.data.DTO.DataSearchResponse
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.ResponseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface InstagramService {
    @POST("/")
  suspend  fun updateProfile(@Body user: User): Response<ResponseResult>
  @POST("/")
    suspend fun findUserById(@Body request: Map<String,String>): Response<User?>
    @POST("/")
    suspend fun  uploadPost(@Body post: Post): Response<ResponseResult>
    @GET("/")
    suspend fun  loadListPostFollowing(@QueryMap userId: Map<String, String>):Response<List<Post>?>
    @POST("/")
    suspend fun searchPostAndUser(@Body query: Map<String,String>): Response<DataSearchResponse>

}