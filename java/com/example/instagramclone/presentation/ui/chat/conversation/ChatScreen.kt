package com.example.instagramclone.presentation.ui.chat.conversation

import androidx.compose.runtime.Composable
import com.example.instagramclone.domain.model.Message
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.domain.model.Conversation
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.ui.chat.conversation.component.ChatInput
import com.example.instagramclone.presentation.ui.chat.conversation.component.MessageBubble
import com.example.instagramclone.presentation.ui.select_media.PickVisualMediaSample

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

                ChatInput (currentUser, onSend, { isShowPickImageScreen = it })
            }
        }
}


