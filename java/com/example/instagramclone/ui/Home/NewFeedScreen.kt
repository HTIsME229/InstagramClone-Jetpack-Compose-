package com.example.instagramclone.ui.Home

import androidx.compose.runtime.Composable
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.ui.post.PostList

@Composable
fun NewFeedScreen(listPost: List<Post>?) {
    PostList(posts = listPost)
}
