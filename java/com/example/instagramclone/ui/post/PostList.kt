package com.example.instagramclone.ui.post

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.instagramclone.data.model.Post

@SuppressLint("SuspiciousIndentation")
@Composable
fun PostList(posts: List<Post>?) {
    if(posts  != null)
    LazyColumn {
        items(posts) { post ->
            PostItem(post = post)
            Spacer(modifier = Modifier.size(5.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.size(5.dp))

        }
    }
}