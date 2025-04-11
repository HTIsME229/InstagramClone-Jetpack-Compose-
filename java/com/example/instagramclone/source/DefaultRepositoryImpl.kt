package com.example.instagramclone.source

import com.example.instagramclone.data.model.User
import com.example.instagramclone.source.local.LocalDefaultRepositoryImp
import com.example.instagramclone.source.remote.RemoteDefaultRepositoryImp
import javax.inject.Inject

class DefaultRepositoryImpl @Inject constructor(
    val remoteRepository: RemoteDefaultRepositoryImp,
    val localRepository: LocalDefaultRepositoryImp
) : DefaultRepository.RemoteRepository,
    DefaultRepository.LocalRepository {
    override suspend fun updateProfile(user: User): ResponseResult {
        return remoteRepository.updateProfile(user)
    }

}