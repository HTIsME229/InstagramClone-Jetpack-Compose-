package com.example.instagramclone.data.remote.datasoure
import com.example.instagramclone.data.mapper.DTO.DataSearchResponse
import com.example.instagramclone.data.mapper.res.ResResult
import com.example.instagramclone.data.mapper.res.ResponseResult
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post
import kotlinx.coroutines.flow.Flow


interface RemotePostDataSource {
    suspend fun uploadPost(post: Post): ResponseResult
    fun loadListPostFollowing(userId: String): Flow<List<Post>?>
    fun loadLikedPost(userId: String): Flow<List<String>?>
    fun loadMyListPost(userId: String): Flow<List<Post>?>
    fun getPostById(postId: String): Flow<ResResult<Post>?>
    suspend fun toggleLikePost(userId: String, postId: String): ResponseResult
    suspend fun searchPostAndUser(query: String, type: String): DataSearchResponse
}