package com.example.instagramclone.presentation.ui.post.create_post

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import com.example.instagramclone.core.util.extractHashtags
import com.example.instagramclone.core.util.isVideoUri
import com.example.instagramclone.core.util.showToast
import com.example.instagramclone.domain.model.Post
import com.example.instagramclone.domain.model.Visibility
import com.example.instagramclone.presentation.navigation.DestinationScreen
import com.example.instagramclone.presentation.ui.post.create_post.component.SettingItem
import com.example.instagramclone.presentation.ui.post.create_post.component.VisibilitySelector
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.post.PostViewModel

@Composable
fun NewPostScreen(
    uri: String,
    vm: AuthenticationViewModel,
    postViewModel: PostViewModel,
    navController: NavController
) {
    var caption by remember { mutableStateOf("") }
    var hashTag: List<String> by remember { mutableStateOf(emptyList()) }
    var isAiTagEnabled by remember { mutableStateOf(false) }
    var selectedVisibility by remember { mutableStateOf(Visibility.PUBLIC) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(top = 25.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (isVideoUri(context = LocalContext.current, uri.toUri()))
                    AsyncImage(
                        model = uri,//your video here
                        imageLoader = ImageLoader.Builder(LocalContext.current)
                            .components {
                                add(VideoFrameDecoder.Factory())
                            }
                            .build(),
                        contentDescription = "Video thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .aspectRatio(1f)
                    ) else
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .aspectRatio(1f)
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            TextField(
                value = caption,
                onValueChange = {
                    caption = it
                    hashTag = extractHashtags(it)
                },
                placeholder = { Text("Thêm chú thích...") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingItem(icon = Icons.Default.Person, label = "Gắn thẻ người khác")
            SettingItem(icon = Icons.Default.LocationOn, label = "Thêm vị trí")
            SettingItem(icon = Icons.Default.Add, label = "Thêm nhạc")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Thêm nhãn AI", fontWeight = FontWeight.Medium)
                Switch(checked = isAiTagEnabled, onCheckedChange = { isAiTagEnabled = it })
            }

            HorizontalDivider()
            VisibilitySelector (
                selectedVisibility,
                onVisibilitySelected = { it ->
                    selectedVisibility = it


                }
            )
            SettingItem(label = "Cũng chia sẻ trên...", trailing = { Text("Facebook") })
            SettingItem(label = "Lựa chọn khác")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    isLoading = true
                    uploadPost(
                        context,
                        uri = uri,
                        caption = caption,
                        hashTag = hashTag,
                        visibility = selectedVisibility,
                        isAiTagEnabled = isAiTagEnabled,
                        vm = vm,
                        postViewModel =  postViewModel,
                        navController = navController
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Chia sẻ")
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        }
    }
}

fun uploadPost(
    context: Context, uri: String, caption: String, hashTag: List<String>,
    visibility: Visibility, isAiTagEnabled: Boolean,
    vm: AuthenticationViewModel, postViewModel: PostViewModel, navController: NavController
)
{
    vm.uploadFile(
        context =context ,
      uri = uri.toUri(),
        onSuccess = { it ->
            postViewModel.uploadPost(
                post = Post(
                    userId = vm._profile.value?.userId.toString(),
                    userNamePost = vm._profile.value?.userName!!,
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





