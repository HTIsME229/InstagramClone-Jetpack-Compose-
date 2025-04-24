package com.example.instagramclone.ui.Bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onNavigationClick: (route:String) -> Unit) {
    TopAppBar(

        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Instagram",
                    fontFamily = FontFamily.Cursive, // hoặc custom font nếu có
                    fontSize = 28.sp
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Camera click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera"
                )
            }
        },
        actions = {
            // IGTV Icon with red dot
            Box {
                IconButton(onClick = { /* IGTV click */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.broadcast),
                        contentDescription = "IGTV"
                    )
                }
                // Red dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, shape = CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                )
            }

            // Message Icon
            IconButton(onClick = {
                onNavigationClick("message")

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = "Direct"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}
