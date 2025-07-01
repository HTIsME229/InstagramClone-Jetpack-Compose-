package com.example.instagramclone.domain.model

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType


data class Post(
    var postId: String = "",
    @SerializedName("userName")
    var userNamePost :String = "",
    var userId: String = "",
    var caption: String = "",
    var mediaUrl: String = "",
    var mediaType: String = "",
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
    var userCommentName:String = "",
    var postId: String = "",
    var description: String = "",
    var timeComment : String = "",
    var likeCount: Int=0,
)
enum class Visibility {
    PUBLIC,
    PRIVATE,
    FOLLOWERS,
}