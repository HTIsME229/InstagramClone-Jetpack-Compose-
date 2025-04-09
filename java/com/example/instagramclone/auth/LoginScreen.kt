package com.example.instagramclone.auth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.instagramclone.ui.viewModel.AuthenticationViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun LoginScreen(
navController: NavController,
vm: AuthenticationViewModel
) {
 var username by remember { mutableStateOf("") }
 var password by remember { mutableStateOf("") }
var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
 Column(
  modifier = Modifier
   .fillMaxSize()
   .padding(top = 20.dp)
   .padding(horizontal = 20.dp),

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
    modifier = Modifier.size(24.dp)
   )
   Spacer(modifier = Modifier.weight(1f))
  }

  // Instagram logo
  Spacer(modifier = Modifier.height(60.dp))
  Text(
   text = "Instagram",
   fontSize = 40.sp,
   fontWeight = FontWeight.Bold,
   fontFamily = androidx.compose.ui.text.font.FontFamily.Cursive
  )

  Spacer(modifier = Modifier.height(40.dp))

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
   placeholder = { Text("Phone number, username, or email") }
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
   placeholder = { Text("Password") }
  )

  // Forgot password
  Row(
   modifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 8.dp),
   horizontalArrangement = Arrangement.End
  ) {
   Text(
    text = "Forgot password?",
    color = Color(0xFF3897F0),
    fontSize = 14.sp
   )
  }

  Spacer(modifier = Modifier.height(16.dp))

  // Login button
  Button(
   onClick = {
    submitLogin(username,password,vm,
     onStart = { isLoading = true; errorMessage = null },
     onSuccess = {
      isLoading = false
      // Navigate to main screen after successful signup
      navController.navigate("home") {
       // Clear the back stack so user can't go back to signup
       popUpTo("home") { inclusive = true }
      }
     },
     onFailure = { error->
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
   enabled = !isLoading && username.isNotEmpty() && password.isNotEmpty()
  ) {
   if (isLoading) {
    androidx.compose.material3.CircularProgressIndicator(
     color = Color.White,
     modifier = Modifier.size(24.dp),
     strokeWidth = 2.dp
    )
   } else {
    Text(
     text = "Login",
     color = Color.White,
     fontSize = 16.sp
    )
   }
  }

  Spacer(modifier = Modifier.height(16.dp))

  // Facebook login
  Row(
   verticalAlignment = Alignment.CenterVertically,
   horizontalArrangement = Arrangement.Center,
   modifier = Modifier.fillMaxWidth()
  ) {
   Icon(
    painter = painterResource(id = android.R.drawable.ic_dialog_info), // Replace with Facebook icon
    contentDescription = "Facebook",
    tint = Color(0xFF3897F0),
    modifier = Modifier.size(18.dp)
   )
   Spacer(modifier = Modifier.width(8.dp))
   Text(
    text = "Log in with Facebook",
    color = Color(0xFF3897F0),
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold
   )
  }

  Spacer(modifier = Modifier.height(20.dp))
  errorMessage?.let {
   Spacer(modifier = Modifier.height(8.dp))
   Text(
    text = it,
    color = Color.Red,
    fontSize = 14.sp
   )
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

  // Sign up
  Row(
   modifier = Modifier.padding(bottom = 16.dp),
   horizontalArrangement = Arrangement.Center,
   verticalAlignment = Alignment.CenterVertically
  ) {
   Text(
    text = "Don't have an account? ",
    color = Color.Gray,
    fontSize = 14.sp
   )
   Text(

    text = "Sign up",
    color = Color(0xFF3897F0),
    fontWeight = FontWeight.Bold,
    fontSize = 14.sp,
  modifier = Modifier.clickable {
   navController.navigate("sign_up")
  },
   )

  }

  // Footer
  Text(
   text = "Instagram or Facebook",
   textAlign = TextAlign.Center,
   color = Color.Gray,
   fontSize = 12.sp,
   modifier = Modifier.padding(bottom = 8.dp)
  )

  HorizontalDivider(
   modifier = Modifier
    .width(100.dp)
    .padding(bottom = 16.dp),
   thickness = 2.dp,
   color = Color.Black
  )
 }

}
fun submitLogin(   username: String,
                   password: String,vm: AuthenticationViewModel,
                   onStart:() -> Unit ,
                     onSuccess:() -> Unit,
                        onFailure:(String) -> Unit
) {
 onStart()
 Log.d("TAG", "submitLogin: $username $password")
    vm.login(username, password,) { success, message ->
        if (success) {
onSuccess()
        } else {
onFailure(message?:"Password or username is incorrect")
        }
    }
}


