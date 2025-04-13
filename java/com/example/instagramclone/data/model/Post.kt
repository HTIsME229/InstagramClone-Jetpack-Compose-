package com.example.instagramclone.data.model

import okhttp3.MediaType


data class Post(
    var postId: String = "",
    var userId: String = "",
    var caption: String = "",
    var mediaUrl: String = "",
    var mediaType: MediaType? = null,
    var timePost : String = "",
    var hashtags: List<String> = emptyList(),
    var likeCount : Int = 0,
    var commentCount : Int = 0,
    var comments: List<Comment> = emptyList(),
    var visibility: Visibility = Visibility.PUBLIC,

)
data class Comment(
    var commentId: String = "",
    var userId: String = "",
    var postId: String = "",
    var description: String = "",
    var timeComment : String = "",
)
enum class Visibility {
    PUBLIC,
    PRIVATE,
    FOLLOWERS,
}