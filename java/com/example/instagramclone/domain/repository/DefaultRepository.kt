package com.example.instagramclone.domain.repository

import com.example.instagramclone.domain.DTO.DataSearchResponse
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.domain.res.ResResult
import com.example.instagramclone.domain.res.ResponseResult
import kotlinx.coroutines.flow.Flow

interface DefaultRepository {
    interface  RemoteRepository{
        suspend fun updateProfile(user: User): ResponseResult
        fun  findUserByUid(userId: String,tokenId:String):Flow<User?>
        suspend  fun uploadPost(post: Post) : ResponseResult
        fun loadListPostFollowing (userId: String): Flow<List<Post>?>
        fun loadLikedPost (userId: String): Flow<List<String>?>
        suspend fun searchPostAndUser(query:String, type:String): DataSearchResponse
        fun loadMyListPost(userId: String): Flow<List<Post>?>
      suspend  fun toggleLikePost(userId: String, postId: String): ResponseResult
      suspend fun  createComment(comment: Comment): ResponseResult
      fun getListCommentPost(postId: String): Flow<List<Comment>?>
      fun getPostById(postId: String): Flow<ResResult<Post>?>
    }
    interface LocalRepository{}


}