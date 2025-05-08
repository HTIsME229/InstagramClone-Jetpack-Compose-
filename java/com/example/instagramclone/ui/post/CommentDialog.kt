package com.example.instagramclone.ui.post

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.instagramclone.R
import com.example.instagramclone.data.model.Comment
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.instagramclone.source.enum.StateMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramCommentModalBottomSheet(
    postId: String,
    showSheet: Boolean,
    onDismiss: () -> Unit,
    onPostComment: (String,String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _ -> },
    comments: List<Comment>,
    isLoading: Boolean,
) {
    var localComments by remember { mutableStateOf(comments) }
    var pendingComment by remember { mutableStateOf<Comment?>(null) }
    var failedComment by remember { mutableStateOf<Comment?>(null) }

    // Update local comments when comments from parent change
    LaunchedEffect(comments) {
        localComments = comments
    }

    // Clean up failed comments when sheet is closed
    LaunchedEffect(showSheet) {
        if (!showSheet) {
            if (failedComment != null) {
                localComments = localComments.filter { it != failedComment }
                failedComment = null
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current
    val imeVisible = androidx.compose.foundation.layout.WindowInsets.ime.getBottom(density) > 0
    
    LaunchedEffect(showSheet, imeVisible) {
        if (showSheet) {
            if (imeVisible) {
                coroutineScope.launch {
                    sheetState.expand()
                }
            } else {
                sheetState.show()
            }
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                SheetContent(
                    postId = postId,
                    comments = localComments,
                    pendingComment = pendingComment,
                    failedComment = failedComment,
                    onDismiss = onDismiss,
                    onPostComment = { text ->
                        // Create temporary comment
                        val tempComment = Comment(
                            commentId = "temp_${System.currentTimeMillis()}",
                            userId = "", // Will be filled by backend
                            userCommentName = "", // Will be filled by backend
                            postId = postId,
                            description = text,
                            timeComment = "Vừa xong",
                            likeCount = 0
                        )
                        // Add to local comments immediately
                        pendingComment = tempComment
                        localComments = localComments + tempComment
                        
                        // Send to backend
                        onPostComment(
                            text, 
                            postId,
                            {
                                // Clear pending state
                                pendingComment = null
                            },
                            { error ->
                                // Mark as failed but don't remove yet
                                pendingComment = null
                                failedComment = tempComment
                            }
                        )
                    },
                    isLoading = isLoading,
                )
            }
        }
    )
}

@Composable
fun SheetContent(
    postId: String,
    comments: List<Comment>,
    pendingComment: Comment?,
    failedComment: Comment?,
    onDismiss: () -> Unit,
    onPostComment: (String) -> Unit = { _ -> },
    modifier: Modifier = Modifier,
    isLoading: Boolean,
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bình luận",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Outlined.Close, contentDescription = "Đóng")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (comments.isEmpty()) {
                    Text(
                        text = "Chưa có bình luận nào",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(comments) { comment ->
                CommentItem(
                    comment = comment,
                    isState = when {
                        comment == pendingComment -> StateMessage.PENDING
                        comment == failedComment -> StateMessage.ERROR
                        else -> StateMessage.SUCCESS
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = { Text("Thêm bình luận...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
                    .height(56.dp),
                trailingIcon = {
                    if (commentText.isNotEmpty()) {
                        Text(
                            text = "Đăng",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    onPostComment(commentText)
                                    commentText = ""
                                }
                                .padding(horizontal = 8.dp)
                        )
                    }
                }
            )
        }
    }


}

