package com.example.instagramclone.domain.res
data class ResponseResult (
    val success:Boolean,
    val error:String? = null,
    val data : Any? = null,
)