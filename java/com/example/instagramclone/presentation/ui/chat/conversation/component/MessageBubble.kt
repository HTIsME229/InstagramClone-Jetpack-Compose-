package com.example.instagramclone.presentation.ui.chat.conversation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.domain.model.Message
import com.example.instagramclone.core.util.isValidUri

@Composable
fun MessageBubble(message: Message, isOwn: Boolean, avtUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),


        horizontalArrangement = if (isOwn) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isOwn)
            Image(
                painter = rememberAsyncImagePainter(avtUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .padding(end = 8.dp)
            )
        if (isValidUri(message.text)) {
            Image(
                painter = rememberAsyncImagePainter(model = message.text),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
        else
        Box(
            modifier = Modifier
                .background(
                    color = if (isOwn) Color(0xFF8338EC) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
        ) {

                Text(
                    text = message.text,
                    color = if (isOwn) Color.White else Color.Black
                )
        }
    }
}