package com.example.instagramclone.presentation.ui.profile.edit_profile

import GenderDropdown
import ProfileField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.core.util.toastError
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.ui.select_media.PickVisualMediaSample
import com.example.instagramclone.presentation.viewModel.auth.AuthenticationViewModel
import com.example.instagramclone.presentation.viewModel.profile.ProfileViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileEditScreen(
    navController: NavController,
    vm: AuthenticationViewModel,
    profileViewModel: ProfileViewModel
) {
    val currentUser = vm._profile.value
    var name by remember { mutableStateOf(currentUser?.name ?: "") }
    var username by remember { mutableStateOf(currentUser?.userName ?: "") }
    var website by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf(currentUser?.bio ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var gender by remember { mutableStateOf("Male") }
    var imageUrl: String by remember { mutableStateOf(currentUser?.imageUrl ?: "") }
    var uploadFile by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isShowPickImageScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (isShowPickImageScreen) {
        ModalBottomSheet(
            onDismissRequest = {
                isShowPickImageScreen = false
            },
            sheetState = bottomSheetState
        ) {
            PickVisualMediaSample {

                imageUrl = it.toString()
                uploadFile = true
                isShowPickImageScreen = false
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Profile",
                    )
                },
                navigationIcon = {

                    Icon(
                        painter = painterResource(id = R.drawable.back),
                        contentDescription = "Cancel",
                        tint = Color.Black,
                        modifier = Modifier
                            .height(32.dp)
                            .width(32.dp)
                            .padding(8.dp)
                            .clickable { onCancel(navController) }
                    )

                },
                actions = {

                    TextButton(
                        onClick =
                            {
                                val userUpdate = User(
                                    currentUser?.tokenId ?: "",
                                    currentUser?.userId ?: "",
                                    email,
                                    username,
                                    name,
                                    imageUrl,
                                    phone,
                                    bio
                                )
                                updateProfile(vm, profileViewModel, context, userUpdate);
                            }) {
                        Text("Done", color = Color.Blue)
                    }
                },
            )
            Modifier.padding(16.dp)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Photo
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Change Profile Photo",
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    isShowPickImageScreen = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Fields
            ProfileField(label = "Name", value = name, onValueChange = { name = it })

            HorizontalDivider()

            ProfileField(label = "Username", value = username, onValueChange = { username = it })

            HorizontalDivider()

            ProfileField(
                label = "Website",
                value = website,
                onValueChange = { website = it },
                placeholder = "Website"
            )

            HorizontalDivider()

            ProfileField(
                label = "Bio",
                value = bio,
                onValueChange = { bio = it },
                singleLine = false,
                modifier = Modifier.height(80.dp)
            )

            TextButton(
                onClick = { /* Handle switch to professional account */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(
                    text = "Switch to Professional Account",
                    color = Color.Blue,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Private Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            HorizontalDivider()

            ProfileField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )

            HorizontalDivider()

            ProfileField(
                label = "Phone",
                value = phone,
                onValueChange = { phone = it },
                keyboardType = KeyboardType.Phone
            )

            HorizontalDivider()
            GenderDropdown(selectedGender = "Female", onGenderSelected = { gender = it })
            Spacer(modifier = Modifier.height(24.dp))

            // Bottom bar indicator - similar to the one in image
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(2.5.dp))
                    .background(Color.DarkGray)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}

fun updateProfile(
    vm: AuthenticationViewModel, profileViewModel: ProfileViewModel,
    context: android.content.Context, userUpdate: User
) {
    if (userUpdate.imageUrl != "") {
        vm.uploadFile(
            context, userUpdate.imageUrl.toUri(),
            onSuccess = { url ->
                profileViewModel.updateProfile(
                    userUpdate,
                    onSuccess =
                        { _, user->
                            vm.setProfile(user);
                        },
                    onError = {
                        toastError(context, "Update profile failed")
                    })

            },
            onError = {
                toastError(context, "Upload image failed")
            }
        )
    }
    else
    {
        profileViewModel.updateProfile(
            userUpdate,
            onSuccess =
                { _, user->
                    vm.setProfile(user);
                },
            onError = {
                toastError(context, "Update profile failed")
            })
    }


}
fun onCancel(navController: NavController) {
    navController.popBackStack()

}


