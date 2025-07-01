package com.example.instagramclone.presentation.ui.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.Story
import com.example.instagramclone.presentation.ui.Home.componet.NewFeedScreen
import com.example.instagramclone.presentation.ui.Home.componet.StoryList
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.post.PostViewModel

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
            if (listPostComment.isNotEmpty() && post.postId == listPostComment[0].postId) {
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
                        onSuccess(comment)
                    },
                    onError = { error ->
                        onError(error ?: "Không thể đăng bình luận")
                    }
                )
            },

        )
    }
}




