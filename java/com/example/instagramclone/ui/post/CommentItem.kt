package com.example.instagramclone.ui.post
// Composable cho từng bình luận
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.data.model.Comment
import com.example.instagramclone.source.enum.StateMessage

@Composable
fun CommentItem(
    comment: Comment,
    isState: StateMessage = StateMessage.SUCCESS
) {
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
            Row {
                Text(
                    text = comment.userCommentName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isState == StateMessage.PENDING) Color.Gray else Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = comment.timeComment,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.description,
                    fontSize = 14.sp,
                    color = if (isState == StateMessage.PENDING) Color.Gray else Color.Unspecified
                )
                when (isState) {
                    StateMessage.PENDING -> {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    StateMessage.ERROR -> {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            rememberAsyncImagePainter(R.drawable.mark),
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    else -> {}
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trả lời",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { /* Xử lý trả lời */ }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Xem bản dịch",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { /* Xử lý xem bản dịch */ }
                )
            }
        }
        if (isState == StateMessage.SUCCESS) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Like comment",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = comment.likeCount.toString(),
                fontSize = 12.sp
            )
        }
    }
}