package com.example.instagramclone.domain.res

data class ResResult<T>(
    val success: Boolean,
    val error: String? ,
    val data: T? = null,
)
