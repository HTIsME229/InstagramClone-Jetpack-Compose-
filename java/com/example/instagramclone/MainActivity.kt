package com.example.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.auth.LoginScreen
import com.example.instagramclone.auth.SignUpScreen
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
       Home()
        }
    }

}
@Composable
fun Home() {
    Text(text = "Home")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramCloneTheme {
InstagramApp()
    }
}