package com.example.instagramclone.presentation.ui.chat.list_inbox.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.instagramclone.core.util.isValidUri
import com.example.instagramclone.domain.model.Conversation

@Composable
fun ConversationList(conversations: List<Conversation>, onItemChatClick:(Conversation)->Unit, userId: String) {
    LazyColumn {
        items(conversations) { conv ->
            Row(
                Modifier.fillMaxWidth().padding(12.dp)
                    .clickable {
                        onItemChatClick(conv)
                    },

                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(conv.avatarUrl),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {

                    Text(conv.name, fontWeight = FontWeight.Bold)
                    if(isValidUri(conv.lastMessage.text))
                        Text( " ${if(conv.lastMessage.isOwn) "You" else conv.name} Sent Image", color = Color.Gray, fontSize = 13.sp)
                    else
                        Text((if(conv.lastMessage.isOwn) "You:" else "") + conv.lastMessage.text, color = Color.Gray, fontSize = 13.sp)
                }
                Spacer(Modifier.weight(1f))
                if (conv.showCameraIcon) {
                    Icon(
                        painterResource(  R.drawable.camera)
                        , contentDescription = null)
                }
            }
        }
    }
}