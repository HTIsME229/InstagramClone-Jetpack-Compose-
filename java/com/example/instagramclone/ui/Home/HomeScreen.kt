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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.Story
import com.example.instagramclone.ui.Bar.MyTopAppBar

@Composable
fun Home( listPost : List<Post>?) {

    val sampleStories = listOf(
        Story("Your Story", "https://your-image-url.com/1.jpg"),
        Story("karennne", "https://your-image-url.com/2.jpg", isLive = true),
        Story("zackjohn", "https://your-image-url.com/3.jpg"),
        Story("kieron_d", "https://your-image-url.com/4.jpg"),
        Story("craig_", "https://your-image-url.com/5.jpg")
    )

    Column(modifier = Modifier.fillMaxSize()
    ) {
        StoryList(sampleStories)
        NewFeedScreen(listPost)
    }



}