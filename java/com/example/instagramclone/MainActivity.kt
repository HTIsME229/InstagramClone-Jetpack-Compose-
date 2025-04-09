package com.example.instagramclone

import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.auth.LoginScreen
import com.example.instagramclone.auth.SignUpScreen
import com.example.instagramclone.ui.Bar.BottomNavItem
import com.example.instagramclone.ui.Bar.MyBottomNavigation
import com.example.instagramclone.ui.Bar.MyTopAppBar
import com.example.instagramclone.ui.profile.ProfileScreen
import com.example.instagramclone.ui.profile.TopBar
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.ui.viewModel.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint

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

}
@Composable
fun InstagramApp(){
val vm= hiltViewModel<AuthenticationViewModel>()
val navController = rememberNavController()
    NavHost(navController = navController, startDestination =DestinationScreen.SignIn.route ) {
     composable(DestinationScreen.SignIn.route){
         LoginScreen(navController = navController, vm =vm )
     }
        composable(DestinationScreen.SignUp.route){
            SignUpScreen(navController = navController, vm =vm)
        }
        composable(DestinationScreen.Home.route){
       InstagramMainScreen(navController = navController)

        }
        composable(DestinationScreen.Profile.route){
            ProfileScreen()
        }


    }

}
@Composable
fun Home() {
   MyTopAppBar()
    Text(text = "Home")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramCloneTheme {
InstagramApp()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramMainScreen(navController: NavController) {
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

    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(

        bottomBar = {
MyBottomNavigation(items = tabs, selectedItemIndex = selectedTabIndex) {
selectedTabIndex=it

}
        }
    ) {paddingValues ->
        // Nội dung chính của màn hình
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(paddingValues)
        ) {
when (selectedTabIndex) {
    0 -> Home()
    4 -> ProfileScreen()
    else -> Home()
}

        }
    }
}