package com.example.instagramclone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.data.model.Post
import com.example.instagramclone.ui.auth.LoginScreen
import com.example.instagramclone.ui.auth.SignUpScreen
import com.example.instagramclone.ui.Bar.BottomNavItem
import com.example.instagramclone.ui.Bar.MyBottomNavigation
import com.example.instagramclone.ui.Bar.MyTopAppBar
import com.example.instagramclone.ui.Home.Home
import com.example.instagramclone.ui.post.InstagramStylePicker
import com.example.instagramclone.ui.post.NewPostScreen
import com.example.instagramclone.ui.profile.ProfileEditScreen
import com.example.instagramclone.ui.profile.ProfileScreen
import com.example.instagramclone.ui.profile.TopBar
import com.example.instagramclone.ui.search.ExploreScreen
import com.example.instagramclone.ui.search.SearchScreen
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.ui.viewModel.AuthenticationViewModel
import com.example.instagramclone.ui.viewModel.PostViewModel
import com.example.instagramclone.ui.viewModel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstagramCloneTheme {
                InstagramApp()

            }
        }
    }
}

sealed class DestinationScreen(val route: String) {
    object SignUp : DestinationScreen("sign_up")
    object SignIn : DestinationScreen("sign_in")
    object Home : DestinationScreen("home")
    object Profile : DestinationScreen("profile")
    object Search : DestinationScreen("search")
    object Add : DestinationScreen("add")
    object Likes : DestinationScreen("likes")
    object editProfile : DestinationScreen("edit_profile")
    object newPost : DestinationScreen("new_post/{encodedUri}")

}

@Composable
fun InstagramApp() {
    val vm = hiltViewModel<AuthenticationViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val postViewModel = hiltViewModel<PostViewModel>()

    val context = LocalContext.current
    val navController = rememberNavController()
    val startDestination = if (checkLogin(vm)) {
        DestinationScreen.Home.route
    } else {
        DestinationScreen.SignIn.route
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable(DestinationScreen.SignIn.route) {
            LoginScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.SignUp.route) {
            SignUpScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreen.Home.route) {
            InstagramMainScreen(navController = navController, vm,postViewModel)

        }
        composable(DestinationScreen.Profile.route) {
            ProfileScreen(vm, navController)
        }
        composable(DestinationScreen.editProfile.route) {
            ProfileEditScreen(vm,
                onSave = {
                    profileViewModel.updateProfile(it,
                        onSuccess = { success, user ->
                            vm.setProfile(user)
                            navController.popBackStack()
                        },
                        onError = {
                            // Handle error
                        })
                },
                onCancel = {
                    navController.popBackStack()
                })
        }
        composable(DestinationScreen.Add.route) {
            InstagramStylePicker {
                val encodedUri = URLEncoder.encode(it.toString(), StandardCharsets.UTF_8.toString())
                navController.navigate("new_post/$encodedUri")
            }
        }
        composable(DestinationScreen.newPost.route) { backStackEntry ->
            val encodedUri = backStackEntry.arguments?.getString("encodedUri") ?: ""
            val decodedUri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse(decodedUri)
            if (uri != null) {
                NewPostScreen(
                    uri = uri.toString(),
                    onPostSelected = { postUri, caption,hashTag, visibility ->
                        vm.uploadFile(
                            postUri.toUri(),
                            onSuccess =  { it ->
                                postViewModel.uploadPost(
                                    post = Post(
                                        userId = vm._profile.value?.userId.toString(),
                                        userNamePost =  vm._profile.value?.userName!!,
                                        mediaUrl = it,
                                        caption = caption,
                                        visibility = visibility,
                                        hashtags = hashTag

                                        ),
                                    onSuccess = {
                                        navController.navigate(DestinationScreen.Home.route) {
                                            popUpTo(0) { inclusive = true }
                                        }

                                    },
                                    onError = { exp ->
                                        showToast(context, exp.toString())

                                    }

                                )

                            },
                            onError =
                            {
                                showToast(context, it.message.toString())
                            }
                        )
                    }
                )
            }
        }


    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun checkLogin(vm: AuthenticationViewModel): Boolean {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (vm._profile.value == null)
        if (currentUser == null) {
            return false
        } else {
            val task = currentUser.getIdToken(false)
            if (task.isSuccessful) {
                val token = task.result?.token
                if (token != null) {
                    vm.refreshUser(currentUser.uid, token)
                }
            } else {
                return false
            }

        }
    return true
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramCloneTheme {
        InstagramApp()
    }
}

@Composable
fun InstagramMainScreen(navController: NavController, vm: AuthenticationViewModel,postViewModel: PostViewModel) {
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
        }
    }
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }



    Scaffold(
        topBar = {
            if (selectedTabIndex == 0) {
                MyTopAppBar() // 👈 Đặt ở đây
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
                0 -> Home(posts)
                1 -> SearchScreen()
                4 -> ProfileScreen(vm, navController)
                else -> Home(posts)
            }

        }
    }
}