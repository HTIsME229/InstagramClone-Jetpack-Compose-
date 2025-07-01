package com.example.instagramclone.presentation.ui.post.view_post

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Post

@SuppressLint("SuspiciousIndentation")
@Composable
fun PostList(posts: List<Post>?, listPostLiked:List<String>, likePost: (postId: String,
                                                                        onSuccess: () -> Unit,
                                                                        onFailure: (String) -> Unit) ->
Unit = { _, _, _ -> }
             ,
             onPostComment: (String, String, onSuccess: (newComment: Comment) -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _ -> },

             )  {
    val listState = rememberLazyListState()
    var maxHeightItemIndex by remember { mutableStateOf(-1) }

    // Theo dõi thay đổi của LazyListState - CÁCH ĐÚNG
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo

                if (visibleItems.isNotEmpty()) {
                    // Cách 1: Tìm item có visible area lớn nhất
                    val viewportHeight = layoutInfo.viewportSize.height

                    val maxVisibleItem = visibleItems.maxByOrNull { item ->
                        // Tính phần visible của item
                        val itemTop = item.offset
                        val itemBottom = item.offset + item.size

                        // Viewport bounds
                        val viewportTop = 0
                        val viewportBottom = viewportHeight

                        // Tính visible area
                        val visibleTop = maxOf(itemTop, viewportTop)
                        val visibleBottom = minOf(itemBottom, viewportBottom)
                        val visibleHeight = maxOf(0, visibleBottom - visibleTop)


                        visibleHeight
                    }

                    maxHeightItemIndex = maxVisibleItem?.index ?: -1
                } else {
                    maxHeightItemIndex = -1
                }

            }
    }

    if (posts != null) {
        LazyColumn(
            state = listState
        ) {
            itemsIndexed(posts) { index, post ->
                val isPlaying = index == maxHeightItemIndex
                Log.d("maxHeightItemIndex", "isPlaying: $isPlaying, index: $index, maxHeightItemIndex: $maxHeightItemIndex")
                PostItem(
                    post = post,
                    isPlaying = isPlaying,
                    listPostLiked,
                    likePost,
                    onPostComment,
                )
                Spacer(modifier = Modifier.size(5.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
    }

}
