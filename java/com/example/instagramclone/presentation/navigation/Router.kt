package com.example.instagramclone.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.core.util.isValidUri
import com.example.instagramclone.core.util.showToast
import com.example.instagramclone.domain.model.Comment
import com.example.instagramclone.domain.model.Message
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.presentation.ui.auth.LoginScreen
import com.example.instagramclone.presentation.ui.auth.SignUpScreen
import com.example.instagramclone.presentation.ui.chat.list_inbox.ChatInboxScreen
import com.example.instagramclone.presentation.ui.chat.conversation.ChatScreen
import com.example.instagramclone.presentation.ui.main.InstagramMainScreen
import com.example.instagramclone.presentation.ui.select_media.InstagramStylePicker
import com.example.instagramclone.presentation.ui.post.create_post.NewPostScreen
import com.example.instagramclone.presentation.ui.post.view_post.PostDetail
import com.example.instagramclone.presentation.ui.profile.edit_profile.ProfileEditScreen
import com.example.instagramclone.presentation.ui.profile.view_profile.ProfileScreen
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.chat.ChatViewModel
import com.example.instagramclone.presentation.viewModel.post.PostViewModel
import com.example.instagramclone.presentation.viewModel.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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

@Composable
fun Router(
    vm: AuthenticationViewModel,
    profileViewModel: ProfileViewModel,
    postViewModel: PostViewModel,
    chatViewModel: ChatViewModel
) {
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
            InstagramMainScreen(navController = navController, vm, postViewModel, profileViewModel)
        }
        composable(DestinationScreen.Profile.route) {
            ProfileScreen(vm, profileViewModel, navController)
        }
        composable(DestinationScreen.editProfile.route) {
            ProfileEditScreen(
            navController,
                vm,
                profileViewModel,
            )
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
                    vm, postViewModel, navController
                )
            }
        }
        composable(DestinationScreen.message.route) {
            val currentUser = vm._profile.value
            val listConversation by chatViewModel.listConversation.collectAsState()
            LaunchedEffect(currentUser?.userId) {
                if (currentUser != null) {
                    chatViewModel.fetchConversation(userId = currentUser.userId)
                }
            }


            if (currentUser != null) {
                ChatInboxScreen(currentUser.userName, currentUser.userId, listConversation) {
                    chatViewModel.setCurrentConversation(it)
                    val userId = URLEncoder.encode(it.userId, StandardCharsets.UTF_8.toString())
                    navController.navigate("message/$userId")
                }
            }

        }
        composable(DestinationScreen.messageScreen.route) { backStackEntry ->
            val currentUser = vm._profile.value
            val listMessage by chatViewModel.listMessage.collectAsState()

            val encodedUri = backStackEntry.arguments?.getString("userId") ?: ""
            val decodedUri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString())
            val userId = Uri.parse(decodedUri)
            val currentConversation by chatViewModel.currentConversation.collectAsState()
            LaunchedEffect(currentUser?.userId) {
                if (currentUser != null) {
                    chatViewModel.fetchMessage(currentUser.userId, userId.toString())
                }
            }

            if (currentUser != null) {
                ChatScreen(currentConversation, listMessage, currentUser) { mess ->
                    if (isValidUri(mess))
                        vm.uploadFile(
                            context,
                            mess.toUri(),
                            onSuccess = {
                                chatViewModel.sendMessage(
                                    Message(
                                        currentUser.userId,
                                        userId.toString(),
                                        it,
                                        type = "image"
                                    )
                                )
                            }, onError = {


                            })
                    else
                        chatViewModel.sendMessage(
                            Message(
                                currentUser.userId,
                                userId.toString(),
                                mess
                            )
                        )
                }
            }
        }
        composable(DestinationScreen.postDetailScreen.route){ backStackEntry->
            val encodedUri = backStackEntry.arguments?.getString("postId") ?: ""
            val decodedUri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString())
            val postId = Uri.parse(decodedUri)
            if(postId != null) {
                val listPostLiked by postViewModel._listPostLiked.collectAsState()
                postViewModel.getPostById(postId = postId.toString(), onSuccess = {

                },
                    onError = {
                        showToast(context, it ?: "Post not found")
                    })
                val post by postViewModel._postSelected.collectAsState()
                val profile by vm._profile.observeAsState()
                post?.let {
                    PostDetail(
                        post = it,
                        isPlaying = false,
                        listPostLiked = listPostLiked,
                        onPostComment = { text, postId, onSuccess, onError ->
                            val comment = Comment(
                                userId = profile?.userId!!, // Will be filled by backend
                                postId = postId,
                                userCommentName = profile?.userName!!, // Will be filled by backend
                                description = text
                            )
                            postViewModel.createComment(
                                comment = comment,
                                onSuccess = {

                                    onSuccess(comment)
                                },
                                onError = { error ->
                                    onError(error ?: "Không thể đăng bình luận")
                                }
                            )

                        },
                    )
                }
            }
        }
    }
}


