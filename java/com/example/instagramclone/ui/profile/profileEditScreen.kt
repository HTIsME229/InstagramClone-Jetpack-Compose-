package com.example.instagramclone.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.Navigation
import com.example.instagramclone.R
import com.example.instagramclone.data.model.User
import com.example.instagramclone.ui.viewModel.AuthenticationViewModel
import com.example.instagramclone.ui.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ProfileEditScreen(vm:AuthenticationViewModel,
     onSave:(userSave:User)->Unit={},
    onCancel:()->Unit={},
) {
    val currentUser = vm._profile.value

    var name by remember { mutableStateOf(currentUser?.name?:"") }
    var username by remember { mutableStateOf(currentUser?.userName?:"") }
    var website by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf(currentUser?.bio?:"") }
    var email by remember { mutableStateOf(currentUser?.email?:"") }
    var phone by remember { mutableStateOf(currentUser?.phone?:"") }
    var gender by remember { mutableStateOf("Male") }
    val imageUrl :String  by remember { mutableStateOf(currentUser?.imageUrl?:"") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile",
                   ) },
                navigationIcon = {

                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Cancel",
                            tint = Color.Black,
                            modifier = Modifier
                                .height(32.dp)
                                .width(32.dp)
                                .padding(8.dp)
                                .clickable { onCancel() }
                        )

                },
                actions = {
                    TextButton(onClick = { onSave(User(currentUser?.tokenId?:"",currentUser?.userId?:""
                        ,email,username,name,imageUrl,phone )
                        ) }) {
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
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                // Replace with actual profile image
                // For this example, using a placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFD8C9))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Change Profile Photo",
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Handle photo change */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Fields
            ProfileField(label = "Name", value = name, onValueChange = { name = it })

            HorizontalDivider()

            ProfileField(label = "Username", value = username, onValueChange = { username = it })

            HorizontalDivider()

            ProfileField(label = "Website", value = website, onValueChange = { website = it }, placeholder = "Website")

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
            GenderDropdown(selectedGender = "Female", onGenderSelected = {gender= it})
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.width(100.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(placeholder, color = Color.LightGray)
                }
            },
            singleLine = singleLine,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdown(
    selectedGender: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val genderOptions = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedGender) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Giới tính",
            fontSize = 16.sp,
            modifier = Modifier.width(100.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select Gender", color = Color.LightGray) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    disabledContainerColor = Color(0xFFF5F5F5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                genderOptions.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedText = gender
                            onGenderSelected(gender)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
