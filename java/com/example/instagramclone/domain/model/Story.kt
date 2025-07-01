package com.example.instagramclone.domain.model
data class Story(
    val username: String,
    val imageUrl: String,
    val isLive: Boolean = false
)