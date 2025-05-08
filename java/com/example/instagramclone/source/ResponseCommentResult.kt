package com.example.instagramclone.source

import com.example.instagramclone.data.model.Comment

data  class ResponseCommentResult (
    val success: Boolean,
    val message: String,
    val data: List<Comment>?
)