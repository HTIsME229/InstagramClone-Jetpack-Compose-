package com.example.instagramclone.ui.post
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.Story
@Composable
fun PostItem(post: Post) {
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

            Image(
                painter = rememberAsyncImagePainter("https://github.com/HTIsME229/LTC-_Chuong1/blob/bai1/avatar1.png?raw=true"),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                ,
                contentScale = ContentScale.Crop
            )
        }
Spacer(modifier = Modifier.height(20.dp))
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Like",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))

        Icon(
            painter = painterResource(id = R.drawable.chat),
            contentDescription = "Comment",
            modifier = Modifier.size(24.dp)
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
}


    }

