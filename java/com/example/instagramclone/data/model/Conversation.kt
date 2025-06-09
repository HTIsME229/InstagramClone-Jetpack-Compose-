package com.example.instagramclone.data.model

data class Conversation(
    val userId:String,
    val name: String,
    val avatarUrl: String,
    val lastMessage: LastMessage,

    val timestamp: Long,
    val isOnline: Boolean = false,
    val showCameraIcon: Boolean = true
)
data class LastMessage(
    val text:String,
    val isOwn :Boolean
)