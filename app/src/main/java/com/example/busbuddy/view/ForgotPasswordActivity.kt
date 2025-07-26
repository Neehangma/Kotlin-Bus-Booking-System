package com.example.busbuddy.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.busbuddy.ui.common.SimpleTopAppBar
import com.example.busbuddy.ui.common.CommonTextFieldColors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility // Added
import androidx.compose.material.icons.filled.VisibilityOff // Added
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation // Added
import androidx.compose.ui.unit.dp
import com.example.busbuddy.R // Ensure R is imported
import kotlinx.coroutines.launch

// Assuming UserViewModel and UserRepositoryImpl would be used for actual password reset logic
// import com.example.busbuddy.viewmodel.UserViewModel
// import com.example.busbuddy.repository.UserRepositoryImpl

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        SimpleTopAppBar(
                            title = "Reset Password", // Title changed slightly for clarity
                            onNavigateUp = { finish() }
                        )
                    },
                    // Make Scaffold background transparent so the Box's background is visible
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    ForgotPasswordScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(title: String, onNavigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(title) }, // Text color will be from MaterialTheme
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            // To make TopAppBar blend with the background image/overlay:
            containerColor = Color.Transparent,
            // Explicitly set title and icon colors if needed for contrast over background
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity
    // val userViewModel = remember { UserViewModel(UserRepositoryImpl()) } // For actual logic

    var newPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.forgotpassword), // Ensure this exists
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)) // Slightly darker overlay
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Set a New Password", // Slightly rephrased for clarity
                style = MaterialTheme.typography.headlineMedium, // Matched Register/Login
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (newPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle new password visibility", tint = Color.LightGray)
                    }
                },
                colors = commonTextFieldColors() // Using helper
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = "Toggle confirm password visibility", tint = Color.LightGray)
                    }
                },
                colors = commonTextFieldColors() // Using helper
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (newPassword.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(context, "Please fill in both password fields.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    // TODO: Add password strength validation (length, complexity) here

                    isLoading = true
                    // --- !!! TODO: Replace with actual ViewModel-based password reset !!! ---

                    kotlinx.coroutines.GlobalScope.launch { // Use ViewModelScope in ViewModel
                        kotlinx.coroutines.delay(1500)
                        val resetSuccess = true

                        activity?.runOnUiThread {
                            isLoading = false
                            if (resetSuccess) {
                                Toast.makeText(context, "Password has been reset successfully!", Toast.LENGTH_LONG).show()
                                Log.d("ForgotPassword", "Password reset successful.")
                                activity.finish() // Go back to the previous screen (e.g., Login)
                            } else {
                                Toast.makeText(context, "Failed to reset password. Please try again.", Toast.LENGTH_LONG).show()
                                Log.e("ForgotPassword", "Password reset failed.")
                            }
                        }
                    }
                    // --- End of TODO ---
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853),
                    disabledContainerColor = Color.Gray
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Reset Password", color = Color.Black) // Or Color.White
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun commonTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        containerColor = Color.Black.copy(alpha = 0.3f),
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.Gray,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray,
        // For leading/trailing icons if you add them:
        focusedTrailingIconColor = Color.White,
        unfocusedTrailingIconColor = Color.LightGray
    )
}
