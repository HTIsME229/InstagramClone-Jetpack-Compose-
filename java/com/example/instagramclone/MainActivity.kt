package com.example.instagramclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cloudinary.android.MediaManager
import com.example.instagramclone.core.theme.InstagramCloneTheme
import com.example.instagramclone.presentation.navigation.Router
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.chat.ChatViewModel
import com.example.instagramclone.presentation.viewModel.post.PostViewModel
import com.example.instagramclone.presentation.viewModel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = hashMapOf<String, Any>(
            "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME,
            "secure" to true
        )

        MediaManager.init(this, config)
        enableEdgeToEdge()
        setContent {
            InstagramCloneTheme {
                InstagramApp()

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstagramCloneTheme {
        InstagramApp()
    }
}

@Composable
fun InstagramApp() {
    var vm = hiltViewModel<AuthenticationViewModel>()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val postViewModel = hiltViewModel<PostViewModel>()
    val chatViewModel = hiltViewModel<ChatViewModel>()
    Router(
        vm = vm,
        postViewModel = postViewModel,
        profileViewModel = profileViewModel,
        chatViewModel = chatViewModel
    )
}


