package com.example.instagramclone.auth

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.instagramclone.ui.viewModel.AuthenticationViewModel
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.data.model.User

@Composable
fun SignUpScreen(
    navController: NavController,
    vm: AuthenticationViewModel
) {
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
Box(modifier = Modifier.fillMaxSize()
    .padding(top = 20.dp)
    .padding(horizontal = 20.dp)) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(

                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )

        }

        // Instagram logo
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Instagram",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign up to see photos and videos from your friends.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Facebook signup button
        Button(
            onClick = { /* TODO: Implement Facebook signup */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3897F0)),
            shape = RoundedCornerShape(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_dialog_info), // Replace with Facebook icon
                    contentDescription = "Facebook",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Continue with Facebook",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // OR divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE0E0E0)
            )
            Text(
                text = "OR",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 12.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color(0xFFE0E0E0)
            )
        }

        // Email field
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Full Name field
        TextField(
            value = fullName,
            onValueChange = { fullName = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Full Name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Username field
        TextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Username") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password field
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp)),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text("Password") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Terms and conditions text
        Text(
            text = "By signing up, you agree to our Terms, Privacy Policy and Cookies Policy.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sign up button
        Button(
            onClick = {
                submitSignUp(email, fullName, username, password, vm,
                    onStart = { isLoading = true; errorMessage = null },
                    onSuccess = {
                        isLoading = false
                        // Navigate to main screen after successful signup
                        navController.navigate("sign_in") {
                            // Clear the back stack so user can't go back to signup
                            popUpTo("sign_in") { inclusive = true }
                        }
                    },
                    onError = { error ->
                        isLoading = false
                        errorMessage = error
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3897F0)),
            shape = RoundedCornerShape(5.dp),
            enabled = !isLoading && email.isNotEmpty() && fullName.isNotEmpty() &&
                    username.isNotEmpty() && password.isNotEmpty()
        ) {
            if (isLoading) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Sign up",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        // Show error message if signup failed
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp
            )
        }



        // Login option
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Have an account? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Log in",
                color = Color(0xFF3897F0),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { navController.navigate("sign_in") }
            )
        }
    }
}
    }

// Function to handle signup submission
@SuppressLint("SuspiciousIndentation")
fun submitSignUp(
    email: String,
    fullName: String,
    username: String,
    password: String,
    vm: AuthenticationViewModel,
    onStart: () -> Unit,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    // Input validation
    if (email.isEmpty() || fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
        onError("All fields are required")
        return
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onError("Please enter a valid email address")
        return
    }

    if (password.length < 6) {
        onError("Password should be at least 6 characters")
        return
    }

    onStart()

    // Log for debugging


 val user = User(
        email = email,
        name = fullName,
        userName = username,
        password = password,
        imageUrl = "",
        bio = "",
        followers = emptyList(),
        following = emptyList(),

    )
    vm.register(
        user = user,
        onResult = { success, error ->
            if (success) {
                onSuccess()
            } else {
                onError(error ?: "An unknown error occurred")
            }
        }
    )


}