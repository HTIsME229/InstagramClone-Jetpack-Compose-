package com.example.instagramclone.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        TopBar()

        // Profile info section
        ProfileInfo()

        // Highlights section
        HighlightsSection()

        // Tab section (Grid/Tagged)
        PostsTabSection()

        // Grid of posts
        PostsGrid()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Private Account",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "jacob_w",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown",
                modifier = Modifier.size(24.dp)
            )
        }

        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ProfileInfo() {
    Column(modifier = Modifier.padding(16.dp)) {
        // Profile picture and stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Stats (Posts, Followers, Following)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat(count = "54", title = "Posts")
                ProfileStat(count = "834", title = "Followers")
                ProfileStat(count = "162", title = "Following")
            }
        }

        // Name and bio
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Jacob West",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = "Digital goodies designer @pixsellz",
            fontSize = 14.sp
        )

        Text(
            text = "Everything is designed.",
            fontSize = 14.sp
        )

        // Edit profile button
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { /* Edit profile action */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text("Edit Profile")
        }
    }
}

@Composable
fun ProfileStat(count: String, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = title,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HighlightsSection() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HighlightItem(title = "New", icon = Icons.Default.Add)
            HighlightItem(title = "Friends", Icons.Default.Person)

        }
    }
}

@Composable
fun HighlightItem(
    title: String,
    icon: ImageVector? = null,
    resourceId: Int? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(1.dp, Color.LightGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            when {
                icon != null -> Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )
                resourceId != null -> Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PostsTabSection() {
    var selectedTab by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
    ) {
        TabItem(
            selected = selectedTab == 0,
            icon = Icons.Default.Menu,
            onClick = { selectedTab = 0 },
            modifier = Modifier.weight(1f)
        )

        TabItem(
            selected = selectedTab == 1,
            icon = Icons.Default.Person,
            onClick = { selectedTab = 1 },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TabItem(
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tint = if (selected) Color.Black else Color.Gray

    Box(
        modifier = modifier
            .padding(vertical = 10.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun PostsGrid() {
    // Create a list of post image resource IDs
//    val posts = listOf(
//        R.drawable.post1, R.drawable.post2, R.drawable.post3,
//        R.drawable.post4, R.drawable.post5, R.drawable.post6,
//        R.drawable.post7, R.drawable.post8, R.drawable.post9
//    )
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(3),
//        modifier = Modifier.height(800.dp) // Set a fixed height or calculate based on screen size
//    ) {
//        items(posts) { post ->
//            Image(
//                painter = painterResource(id = post),
//                contentDescription = null,
//                modifier = Modifier
//                    .aspectRatio(1f)
//                    .border(0.5.dp, Color.White),
//                contentScale = ContentScale.Crop
//            )
//        }
//    }
}