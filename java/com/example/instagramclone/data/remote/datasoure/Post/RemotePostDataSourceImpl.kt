package com.example.instagramclone.data.remote.datasoure

import com.example.instagramclone.R
import com.example.instagramclone.data.mapper.DTO.DataSearchResponse
import com.example.instagramclone.data.mapper.res.ResResult
import com.example.instagramclone.data.mapper.res.ResponseResult
import com.example.instagramclone.domain.model.Post
import kotlinx.coroutines.flow.Flow


class RemotePostDataSourceImpl : RemotePostDataSource{
    override suspend fun uploadPost(post: Post): ResponseResult {
        TODO("Not yet implemented")
    }

    override fun loadListPostFollowing(userId: String): Flow<List<Post>?> {
        TODO("Not yet implemented")
    }

    override fun loadLikedPost(userId: String): Flow<List<String>?> {
        TODO("Not yet implemented")
    }

    override fun loadMyListPost(userId: String): Flow<List<Post>?> {
        TODO("Not yet implemented")
    }

    override fun getPostById(postId: String): Flow<ResResult<Post>?> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleLikePost(userId: String, postId: String): ResponseResult {
        TODO("Not yet implemented")
    }

    override suspend fun searchPostAndUser(query: String, type: String): DataSearchResponse {
        TODO("Not yet implemented")
    }

}