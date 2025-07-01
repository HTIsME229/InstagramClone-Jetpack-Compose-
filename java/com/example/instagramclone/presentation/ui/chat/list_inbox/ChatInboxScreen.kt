package com.example.instagramclone.presentation.ui.chat.list_inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.example.instagramclone.domain.model.Conversation
// Compose UI
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.example.instagramclone.R
import com.example.instagramclone.core.util.isValidUri
import com.example.instagramclone.presentation.ui.chat.list_inbox.component.ConversationList
import com.example.instagramclone.presentation.ui.chat.list_inbox.component.StoryRow

@Composable
fun ChatInboxScreen(userName: String, userId:String, conversations: List<Conversation>, onItemChatClick:(Conversation)->Unit) {
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



