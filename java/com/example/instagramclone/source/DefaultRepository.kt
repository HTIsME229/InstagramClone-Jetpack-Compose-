package com.example.instagramclone.source

import com.example.instagramclone.data.DTO.DataSearchResponse
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.User
import kotlinx.coroutines.flow.Flow

interface DefaultRepository {
    interface  RemoteRepository{
        suspend fun updateProfile(user: User): ResponseResult
        fun  findUserByUid(userId: String,tokenId:String):Flow<User?>
        suspend  fun uploadPost(post:Post) :ResponseResult
        fun loadListPostFollowing (userId: String): Flow<List<Post>?>
        suspend fun searchPostAndUser(query:String, type:String): DataSearchResponse
fun loadMyListPost(userId: String): Flow<List<Post>?>
    }
    interface LocalRepository{}


}