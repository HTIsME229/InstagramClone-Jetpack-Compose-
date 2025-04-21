package com.example.instagramclone.ui.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.data.model.User
import com.example.instagramclone.ui.viewModel.PostViewModel

@Composable
fun ExploreScreen(listPost:List<Post>) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(listPost) { index ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f) // Đảm bảo ô vuông
                    .background(Color.LightGray)
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://github.com/HTIsME229/LTC-_Chuong1/blob/bai1/avatar1.png?raw=true"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Icon(
                    imageVector = Icons.Default.PlayArrow, // Icon reels
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(16.dp)
                )
            }
        }
    }
}
@Composable
fun TopBarWithSearch(onSearchClick: (query: String) -> Unit) {

        // Thanh tìm kiếm
        var searchText:String by remember { mutableStateOf("") }

        TextField(
            value = searchText,

            onValueChange = { searchText = it },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search // Hiển thị nút "Search" trên bàn phím
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                            onSearchClick(searchText)
                }
            ),
            placeholder = { Text("Tìm kiếm") },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors (
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()

        )
    }

@Composable
fun  SearchScreen(onSearchClick:(query:String)->Unit = {},postViewModel: PostViewModel){
    var listAccount:List<User> by remember { mutableStateOf(emptyList()) }
    var listPost:List<Post> by remember { mutableStateOf(emptyList()) }
    postViewModel._listPostSearched.observe(
        LocalLifecycleOwner.current
    ) { it ->
        if(it != null)
        {
            listPost= it
        }
    }
    postViewModel._listUserSearched.observe(
        LocalLifecycleOwner.current
    )
    {
        if(it != null)
        {
            listAccount= it
        }
    }
    Column (modifier = Modifier.fillMaxSize()){
        TopBarWithSearch(onSearchClick)
        if(listAccount.isNotEmpty()) {
            Text("Tài khoản", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            AccountList(listAccount)

            Spacer(Modifier.height(8.dp))
        }
        if(listPost.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Bài Viết", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            ExploreScreen(listPost)
        }

    }
}
@Composable
fun AccountList(accounts: List<User>) {
    Column {
        accounts.forEach {
            AccountItem(it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AccountItem(account: User) {
    Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Image(
            painter = rememberAsyncImagePainter("https://github.com/HTIsME229/LTC-_Chuong1/blob/bai1/avatar1.png?raw=true"),
            contentDescription = null,
            modifier = Modifier.size(40.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(account.userName, fontWeight = FontWeight.Bold)
//                if (account.isVerified) {
//                    Icon(Icons.Default.Verified, contentDescription = null, tint = Color.Blue, modifier = Modifier.size(16.dp).padding(start = 4.dp))
//                }
            }
            if(account.followers.isNotEmpty()) {
                Text("${account.name}·Có ${account.followers[0]} theo dõi + ${account.followers.size} nữa theo dõi", fontSize = 12.sp, color = Color.Gray)

            }
            else{
                Text(account.name, color = Color.Gray)

            }

        }
    }
}
