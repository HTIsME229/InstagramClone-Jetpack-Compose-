package com.example.instagramclone.presentation.ui.chat.conversation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.domain.model.User

@Composable
fun ChatInput(currentUser: User, onSend: (String) -> Unit, onShowPickImageScreen:(Boolean)->Unit ) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { /* open gallery */ }) {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.camera),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )
        }
        TextField(
            value = text,
            onValueChange = { text = it },

            placeholder = { Text("Nhắn tin...") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            maxLines = 4
        )
        if (text.isEmpty()) {
            IconButton(onClick = {

            }) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.micro),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
            IconButton(onClick = {
                onShowPickImageScreen(true)

            }) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.post),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
            IconButton(onClick = { /* open gallery */ }) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.plus),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }


        } else {
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
}
