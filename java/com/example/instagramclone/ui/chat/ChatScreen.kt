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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.navigation.Navigation
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.data.model.Conversation
import com.example.instagramclone.data.model.User
import com.example.instagramclone.ui.post.InstagramStylePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conv: Conversation?,
    messages: List<Message>?,
    currentUser: User,
    onSend: (String) -> Unit
) {

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isShowPickImageScreen by remember { mutableStateOf(false) }
        if(isShowPickImageScreen){
            ModalBottomSheet(
                onDismissRequest = {
                    isShowPickImageScreen = false
                },
                sheetState = bottomSheetState
            ) {
                PickVisualMediaSample {
                    onSend(it.toString())
                    isShowPickImageScreen =false
                }
            }
        }
    else {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberAsyncImagePainter(conv?.avatarUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(conv?.name ?: "user", fontWeight = FontWeight.Bold)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Call, contentDescription = "Call")
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                painterResource(R.drawable.camera),
                                contentDescription = "Video Call"
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
                            MessageBubble(
                                message,
                                isOwn = message.senderId == currentUser.userId,
                                avtUrl = conv?.avatarUrl!!
                            )
                        }
                    }
                }

                ChatInput(currentUser, onSend, { isShowPickImageScreen = it })
            }
        }
}

@Composable
fun ChatInput(currentUser: User, onSend: (String) -> Unit,onShowPickImageScreen:(Boolean)->Unit ) {
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

