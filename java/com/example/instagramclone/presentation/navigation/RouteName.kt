package com.example.instagramclone.presentation.navigation

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
    object message : DestinationScreen("message")
    object messageScreen : DestinationScreen("message/{userId}")
    object postDetailScreen : DestinationScreen("post_detail/{postId}")

}