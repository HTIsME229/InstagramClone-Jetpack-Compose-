package com.example.instagramclone.presentation.ui.Home.componet

import androidx.compose.runtime.Composable
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post

import com.example.instagramclone.presentation.ui.post.view_post.PostList

@Composable
fun NewFeedScreen(
    listPost: List<Post>?,
    listPostLiked: List<String>,
    likePost: (postId: String,
              onSuccess: () -> Unit,
              onFailure: (String) -> Unit) -> Unit = { _, _, _ -> },
    onPostComment: (String, String, onSuccess: (newComment: Comment) -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _ -> },

    ) {
    PostList(
        posts = listPost,
        listPostLiked = listPostLiked,
        likePost = likePost,
        onPostComment = onPostComment,
    )
}
