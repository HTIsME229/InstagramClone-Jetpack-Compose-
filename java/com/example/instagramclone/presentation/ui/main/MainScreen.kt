package com.example.instagramclone.presentation.ui.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.instagramclone.presentation.navigation.DestinationScreen
import com.example.instagramclone.core.ui.Bar.BottomNavItem
import com.example.instagramclone.core.ui.Bar.MyBottomNavigation
import com.example.instagramclone.core.ui.Bar.MyTopAppBar
import com.example.instagramclone.presentation.ui.Home.Home
import com.example.instagramclone.presentation.ui.profile.view_profile.ProfileScreen
import com.example.instagramclone.presentation.ui.search.SearchScreen
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.post.PostViewModel
import com.example.instagramclone.presentation.viewModel.profile.ProfileViewModel

@Composable
fun InstagramMainScreen(
    navController: NavController,
    vm: AuthenticationViewModel,
    postViewModel: PostViewModel,
    profileViewModel: ProfileViewModel
) {
    val tabs = remember {
        listOf(
            BottomNavItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                route = "home"
            ),
            BottomNavItem(
                title = "Search",
                selectedIcon = Icons.Filled.Search,
                unselectedIcon = Icons.Outlined.Search,
                route = "search"
            ),
            BottomNavItem(
                title = "Add",
                selectedIcon = Icons.Filled.AddCircle,
                unselectedIcon = Icons.Outlined.AddCircle,
                route = "add"
            ),
            BottomNavItem(
                title = "Likes",
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder,
                route = "likes"
            ),
            BottomNavItem(
                title = "Profile",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                route = "profile"
            )
        )
    }
    val profile by vm._profile.observeAsState()
    val userId = profile?.userId
    val posts by postViewModel._listPostFollowing.observeAsState(initial = emptyList())
    LaunchedEffect(userId) {
        if (userId != null) {
            postViewModel.loadListPostFollowing(userId)
            profileViewModel.loadMyListPost(userId)
            postViewModel.loadLikedPost(userId)
        }

    }
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            if (selectedTabIndex == 0) {
                MyTopAppBar({ route -> navController.navigate(route) }) // 👈 Đặt ở đây
            }
        },
        bottomBar = {
            MyBottomNavigation(items = tabs, selectedItemIndex = selectedTabIndex) {
                selectedTabIndex = it

            }
        }
    ) { paddingValues ->
        // Nội dung chính của màn hình
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (selectedTabIndex == 2) {
                // Điều hướng đến Add nhưng không thay đổi selectedTabIndex
                selectedTabIndex = 0  // Hoặc bạn có thể lưu tab trước đó
                navController.navigate(DestinationScreen.Add.route)
            }

            when (selectedTabIndex) {
                0 -> Home(
                    posts, { postId, onSuccess, onFailure ->
                        postViewModel.toggleLikePost(
                            userId = userId.toString(),
                            postId = postId,
                            onSuccess = {

                            },
                            onError = {
                                onFailure(it ?: "Server error")
                            }

                        )

                    }, postViewModel, vm
                )

                1 -> SearchScreen(onSearchClick = { query ->
                    postViewModel.searchUserAndPost(query = query)

                }, postViewModel, navController = navController)

                4 -> ProfileScreen(vm, profileViewModel, navController)
                else -> Home(posts, { postId, onSuccess, onFailure ->
                    postViewModel.toggleLikePost(
                        userId = userId.toString(),
                        postId = postId,
                        onSuccess = {

                        },
                        onError = {
                            Log.d("TAGonFailure", "onFailure: $it")
                            onFailure(it ?: "Server error")
                        }
                    )
                }, postViewModel, vm)
            }

        }
    }

}