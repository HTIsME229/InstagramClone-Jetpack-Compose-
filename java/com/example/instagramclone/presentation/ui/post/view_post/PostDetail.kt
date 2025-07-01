package com.example.instagramclone.presentation.ui.post.view_post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post

@Composable
fun PostDetail(
    post: Post,
    isPlaying: Boolean,
    listPostLiked: List<String>,
    onBackClick: () -> Unit = {},
    likePost: (
        postId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) -> Unit = { _, _, _ -> },
    onPostComment: (
        postId: String,
        comment: String,
        onSuccess: (newComment: Comment) -> Unit,
        onError: (String) -> Unit
    ) -> Unit = { _, _, _, _ -> },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(Color.White)
    ) {
        // Top bar with back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            // Title section - centered
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bài viết",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text =  post.userNamePost, // Use actual username from post
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // More options button

        }

        // Post content
        PostItem(
            post = post,
            isPlaying = isPlaying,
            listPostLiked = listPostLiked,
            likePost = likePost,
            onPostComment = onPostComment,


        )
    }
}