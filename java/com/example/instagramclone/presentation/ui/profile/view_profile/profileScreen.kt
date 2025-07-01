package com.example.instagramclone.presentation.ui.profile.view_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.presentation.ui.profile.view_profile.component.HighlightItem
import com.example.instagramclone.presentation.ui.profile.view_profile.component.ProfileInfo
import com.example.instagramclone.presentation.ui.profile.view_profile.component.TabItem
import com.example.instagramclone.presentation.ui.profile.view_profile.component.TopBar
import com.example.instagramclone.presentation.ui.search.ExploreScreen
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.profile.ProfileViewModel


@Composable

fun ProfileScreen(vm: AuthenticationViewModel, profileViewModel: ProfileViewModel, navController: NavController) {
        var user  = vm._profile.value
    var listPost:List<Post> by remember { mutableStateOf(emptyList()) }
    vm._profile.observe(
        LocalLifecycleOwner.current
    ) { it ->
      user= it
    }
    profileViewModel._myListPost.observe(LocalLifecycleOwner.current) {
        if(it !=null)
        listPost= it
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Top bar
        TopBar(user!!)

        // Profile info section
        ProfileInfo(user!!,navController)

        // Highlights section
        HighlightsSection()

        // Tab section (Grid/Tagged)
        PostsTabSection()

        // Grid of posts
        ExploreScreen(listPost,navController)
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



