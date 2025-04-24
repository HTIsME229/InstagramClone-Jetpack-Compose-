package com.example.instagramclone.ui.chat

import androidx.compose.runtime.Composable
import com.example.instagramclone.data.model.Message
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.data.model.Conversation
import com.example.instagramclone.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conv : Conversation?,
    messages: List<Message>?,
    currentUser: User,
    onSend: (String) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(conv?.avatarUrl),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(conv?.name?:"user", fontWeight = FontWeight.Bold)
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Call, contentDescription = "Call")
                }
                IconButton(onClick = {}) {
                  Icon(
                      painterResource(R.drawable.camera)
                      , contentDescription = "Video Call"
                  )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
                .align(Alignment.Start),

            reverseLayout = true
        ) {
            if (messages != null) {
                items(messages.reversed()) { message ->
                    MessageBubble(message, isOwn = message.senderId == currentUser.userId)
                }
            }
        }

        ChatInput(currentUser,onSend)
    }
}
@Composable
fun ChatInput(currentUser:User,onSend: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* open gallery */ }) {
            Image(
                painter = rememberAsyncImagePainter(currentUser.imageUrl),
                contentDescription = null,
                modifier = Modifier.size(36.dp).clip(CircleShape)
            )
        }
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Nhắn tin...") },
            modifier = Modifier.weight(1f),
            maxLines = 4
        )
        IconButton(
            onClick = {
                if (text.isNotBlank()) {
                    onSend(text)
                    text = ""
                }
            }
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
        }
    }
}

