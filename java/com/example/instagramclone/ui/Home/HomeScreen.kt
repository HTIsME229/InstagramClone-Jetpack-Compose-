package com.example.instagramclone.ui.Home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.instagramclone.data.model.Comment
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.Story
import com.example.instagramclone.ui.Bar.MyTopAppBar
import com.example.instagramclone.ui.viewModel.AuthenticationViewModel
import com.example.instagramclone.ui.viewModel.PostViewModel
import com.example.instagramclone.ui.viewModel.ProfileViewModel

@Composable
fun Home(
    listPost: List<Post>?,
    likePost: (postId: String,
              onSuccess: () -> Unit,
              onFailure: (String) -> Unit) -> Unit = { _, _, _ -> },
    postViewModel: PostViewModel,
    authenticationViewModel: AuthenticationViewModel

) {
    val listPostLiked by postViewModel._listPostLiked.collectAsState()
    val listPostComment by postViewModel._listPostComment.collectAsState()

    // Update posts with comments when they are loaded
    val updatedPosts = remember(listPost, listPostComment) {
        listPost?.map { post ->
            if (listPostComment.isNotEmpty()) {
                post.copy(comments = listPostComment)
            } else {
                post
            }
        }
    }
    val profile by authenticationViewModel._profile.observeAsState()
    val sampleStories = listOf(
        Story("Your Story", "https://your-image-url.com/1.jpg"),
        Story("karennne", "https://your-image-url.com/2.jpg", isLive = true),
        Story("zackjohn", "https://your-image-url.com/3.jpg"),
        Story("kieron_d", "https://your-image-url.com/4.jpg"),
        Story("craig_", "https://your-image-url.com/5.jpg")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        StoryList(sampleStories)
        NewFeedScreen(
            listPost = updatedPosts,
            listPostLiked = listPostLiked,
            likePost = likePost,
            onPostComment = { text, postId, onSuccess, onError ->
                val comment = Comment(
                    userId = profile?.userId!!, // Will be filled by backend
                    postId = postId,
                    userCommentName = profile?.userName!!, // Will be filled by backend
                    description = text
                )
                postViewModel.createComment(
                    comment = comment,
                    onSuccess = {
                        onSuccess()
                    },
                    onError = { error ->
                        onError(error ?: "Không thể đăng bình luận")
                    }
                )
            },
            onLoadComment = { postId, onSuccess, onError ->
                postViewModel.getListCommentPost(postId)
                onSuccess()
            }
        )
    }
}




