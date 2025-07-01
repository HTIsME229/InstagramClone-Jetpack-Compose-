package com.example.instagramclone.presentation.ui.post.view_post

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.presentation.ui.post.comment_post.InstagramCommentModalBottomSheet
import com.example.instagramclone.presentation.ui.select_media.VideoPlayer

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
    isPlaying: Boolean,
    listPostLiked: List<String>,
    likePost: (
        postId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) -> Unit =
        { _, _, _ -> },
    onPostComment: (String, String, onSuccess: (newComment: Comment) -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _ -> },

    ) {
    var isLiked by remember { mutableStateOf(listPostLiked.contains(post.postId)) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var isLoadingComments by remember { mutableStateOf(false) }
    // Load comments only when postId changes or bottom sheet is shown


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(

                painter = painterResource(R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier

                    .size(36.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = post.userNamePost, fontWeight = FontWeight.Bold)
                Text(text = post.timePost, style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.MoreVert, contentDescription = null)
        }

        // Post Image (pager nếu nhiều ảnh)
        if (post.mediaUrl.contains("https://res.cloudinary.com/dwbsddywc/video/upload")) {
            VideoPlayer(videoUri = post.mediaUrl.toUri(),isPlaying,Modifier.height(400.dp),
                AspectRatioFrameLayout.RESIZE_MODE_ZOOM)

        } else {
            Image(
                painter = rememberAsyncImagePainter(post.mediaUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentScale = ContentScale.Crop
            )
        }

    }
    Spacer(modifier = Modifier.height(20.dp))
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            if (isLiked)
                rememberAsyncImagePainter(R.drawable.like)
            else
                rememberAsyncImagePainter(R.drawable.unlike),
            contentDescription = "Like",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    isLiked = !isLiked
                    likePost(
                        post.postId,
                        {
                        },
                        {
                            Log.d("failLog", "fail")
                            isLiked = !isLiked
                        }
                    )

                },

            )
        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            painter = painterResource(id = R.drawable.chat),
            contentDescription = "Comment",
            modifier = Modifier
                .size(24.dp)
                .clickable {



                        showBottomSheet = true

                }
        )
        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            painter = painterResource(id = R.drawable.send),
            contentDescription = "Share",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(id = R.drawable.bookmark),
            contentDescription = "Save",
            modifier = Modifier.size(24.dp)
        )
    }
    Column(Modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = "Liked by ${post.likeCount} and ${post.likeCount} others",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Caption
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(post.userNamePost)
                    }
                    append(" ${post.caption}")
                }
            )
        }

        // Show comment count
        if (post.commentCount > 0) {
            Text(
                text = "Xem tất cả ${post.commentCount} bình luận",
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clickable {

                            showBottomSheet = true

                    }
            )
        }
    }
    if (showBottomSheet) {
        InstagramCommentModalBottomSheet(
            postId = post.postId,
            showSheet = showBottomSheet,
            onDismiss = { showBottomSheet = false },
            comments = post.comments,
            onPostComment = onPostComment,
            isLoading = isLoadingComments
        )
    }
}


