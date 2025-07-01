package com.example.instagramclone.domain.DTO

import com.example.instagramclone.domain.model.Comment

data  class ResponseCommentResult (
    val success: Boolean,
    val message: String,
    val data: List<Comment>?
)