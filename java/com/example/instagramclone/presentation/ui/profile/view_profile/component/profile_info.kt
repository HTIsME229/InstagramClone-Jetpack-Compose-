package com.example.instagramclone.presentation.ui.profile.view_profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.domain.model.User
import com.example.instagramclone.presentation.ui.profile.view_profile.ProfileStat

@Composable
fun ProfileInfo(user: User, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Profile picture and stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture
            Image(
                painter = rememberAsyncImagePainter(user.imageUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Stats (Posts, Followers, Following)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStat(count = "54", title = "Posts")
                ProfileStat(count = "834", title = "Followers")
                ProfileStat(count = "162", title = "Following")
            }
        }

        // Name and bio
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = user.bio,
            fontSize = 14.sp
        )


        // Edit profile button
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                navController.navigate("edit_profile")


            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text("Edit Profile")
        }
    }
}