package com.example.instagramclone.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.example.instagramclone.data.model.Conversation
// Compose UI
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// Coil (load avatar/image từ URL)
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.util.isValidUri

@Composable
fun ChatInboxScreen(userName: String,userId:String, conversations: List<Conversation>,onItemChatClick:(Conversation)->Unit) {
    Column (modifier = Modifier.fillMaxSize().background(Color.White)) {
        TopBar(userName)
        SearchBar()
        StoryRow()
        ConversationList(conversations, {onItemChatClick(it)},userId )
    }
}
@Composable
fun TopBar(userName: String) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        Spacer(Modifier.weight(1f))
        Icon(Icons.Default.Edit, contentDescription = "Edit")
    }
}
@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { ("Hỏi Meta AI hoặc tìm kiếm") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
    )
}
@Composable
fun StoryRow() {
    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
        items(10) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 8.dp)) {
                Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.Gray))
                Text("User $it", fontSize = 12.sp)
            }
        }
    }
}
@Composable
fun ConversationList(conversations: List<Conversation>,onItemChatClick:(Conversation)->Unit,userId: String) {
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

