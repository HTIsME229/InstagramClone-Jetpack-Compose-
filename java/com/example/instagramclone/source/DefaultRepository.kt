package com.example.instagramclone.source

import com.example.instagramclone.data.model.User

interface DefaultRepository {
    interface  RemoteRepository{
        suspend fun updateProfile(user: User): ResponseResult
    }
    interface LocalRepository{}


}