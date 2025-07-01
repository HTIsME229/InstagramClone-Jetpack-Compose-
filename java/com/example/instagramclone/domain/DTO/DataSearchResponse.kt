package com.example.instagramclone.domain.DTO

import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.User

data class DataSearchResponse (
    val  success:Boolean,
    val users: List<User> = emptyList(),
    val posts :List<Post> = emptyList(),
    val userStats : Stats? = null,
    val postStats : Stats?= null
    )
data class Stats(
    val total:Int = 0,
    val page :Int = 0,
    val totalPages :Int = 0,
)