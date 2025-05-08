package com.example.instagramclone.ui.Home

import androidx.compose.runtime.Composable
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.ui.post.PostList

@Composable
fun NewFeedScreen(
    listPost: List<Post>?,
    listPostLiked: List<String>,
    likePost: (postId: String,
              onSuccess: () -> Unit,
              onFailure: (String) -> Unit) -> Unit = { _, _, _ -> },
    onPostComment: (String, String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _ -> },
    onLoadComment: (postId: String,
                   onSuccess: () -> Unit,
                   onError: () -> Unit) -> Unit = { _, _, _ -> }
) {
    PostList(
        posts = listPost,
        listPostLiked = listPostLiked,
        likePost = likePost,
        onPostComment = onPostComment,
        onLoadComment = onLoadComment
    )
}
