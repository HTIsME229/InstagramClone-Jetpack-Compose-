package com.example.instagramclone.data.remote

import com.example.instagramclone.domain.DTO.DataSearchResponse
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.domain.res.ResResult
import com.example.instagramclone.domain.DTO.ResponseCommentResult
import com.example.instagramclone.domain.res.ResponseResult
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
  @GET("/")
  suspend fun  loadMyListPost(@QueryMap userId: Map<String, String>):Response<List<Post>?>
@POST("/")
suspend fun  toggleLikePost(@Body request: Map<String,String>): Response<ResponseResult>
  @GET("/")
  suspend fun  loadLikedPost(@QueryMap userId: Map<String, String>):Response<List<String>?>
  @POST("/")
  suspend fun createComment(@Body comment: Comment): Response<ResponseResult>
  @GET("/")
  suspend fun getListCommentPost(@QueryMap postId: Map<String, String>):Response<ResponseCommentResult>
  @GET("/")
    suspend fun getPostById(@QueryMap postId: Map<String, String>): Response<ResResult<Post>>

}