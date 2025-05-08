package com.example.instagramclone.ui.post

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.instagramclone.data.model.Comment
import com.example.instagramclone.data.model.Post

@SuppressLint("SuspiciousIndentation")
@Composable
fun PostList(posts: List<Post>?,listPostLiked:List<String>, likePost: (postId: String,
                                                                       onSuccess: () -> Unit,
                                                                       onFailure: (String) -> Unit) ->
Unit = { _, _, _ -> }
             ,
             onPostComment: (String, String, onSuccess: () -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _, _ -> },
             onLoadComment: (postId: String,
                            onSuccess: () -> Unit,
                            onError: () -> Unit) -> Unit = { _, _, _ -> }
)  {

    if(posts  != null)
    LazyColumn {
        items(posts) { post ->

            PostItem(post = post,listPostLiked,likePost,onPostComment,onLoadComment)
            Spacer(modifier = Modifier.size(5.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.size(5.dp))

        }
    }
}