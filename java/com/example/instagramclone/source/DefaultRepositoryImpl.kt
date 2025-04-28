package com.example.instagramclone.source

import com.example.instagramclone.data.DTO.DataSearchResponse
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.local.LocalDefaultRepositoryImp
import com.example.instagramclone.source.remote.RemoteDefaultRepositoryImp
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(
    val remoteRepository: RemoteDefaultRepositoryImp,
    val localRepository: LocalDefaultRepositoryImp
) : DefaultRepository.RemoteRepository,
    DefaultRepository.LocalRepository {
    override suspend fun updateProfile(user: User): ResponseResult {
        return remoteRepository.updateProfile(user)
    }

    override fun findUserByUid(userId: String,tokenId:String): Flow<User?> {
       return remoteRepository.findUserByUid(userId,tokenId)
    }

    override suspend fun uploadPost(post: Post): ResponseResult {
        return remoteRepository.uploadPost(post)
    }

    override fun loadListPostFollowing(userId: String): Flow<List<Post>?> {
return remoteRepository.loadListPostFollowing(userId)   }

    override fun loadLikedPost(userId: String): Flow<List<String>?> {
        return remoteRepository.loadLikedPost(userId)
    }

    override suspend fun searchPostAndUser(query: String, type: String): DataSearchResponse {
        return remoteRepository.searchPostAndUser(query,type)
    }
    override fun loadMyListPost(userId: String): Flow<List<Post>?> {
        return remoteRepository.loadMyListPost(userId)
    }

    override suspend fun toggleLikePost(userId: String, postId: String): ResponseResult {
        return remoteRepository.toggleLikePost(userId,postId)
    }
}