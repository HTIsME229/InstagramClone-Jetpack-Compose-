package com.example.instagramclone.data.model

import java.util.Date

data class Message (
    val  senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = Date().time,
    val type :String = "text"
)