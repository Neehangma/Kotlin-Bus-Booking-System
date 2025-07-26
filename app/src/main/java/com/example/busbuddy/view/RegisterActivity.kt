package com.example.busbuddy.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Keep for whole row clicks if needed
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.busbuddy.R
import com.example.busbuddy.viewmodel.UserViewModel
import com.example.busbuddy.repository.UserRepositoryImpl
// Assuming LoginActivity is in the same package or imported correctly.

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold { innerPadding ->
                    RegisterScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity
    // Consider injecting ViewModel using Hilt or other DI framework for better testability
    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Added for loading state

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.register),
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
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp) // Increased bottom padding
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }, // Uses colors from TextFieldDefaults
                modifier = Modifier.fillMaxWidth(),
                colors = registerTextFieldColors(), // Using extracted colors
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp)) // Increased spacing

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") }, // Uses colors from TextFieldDefaults
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    // Use IconButton for better accessibility and touch target size
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = Color.LightGray // Adjust tint as needed
                        )
                    }
                },
                colors = registerTextFieldColors(), // Using extracted colors
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp)) // Increased spacing

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    // Consider adding more robust email/password validation (e.g., format, length)

                    isLoading = true
                    userViewModel.register(email, password) { success, message ->
                        isLoading = false // Reset loading state regardless of outcome
                        if (success) {
                            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                            Log.d("RegisterScreen", "Registration successful for: $email")
                            // Navigate to LoginActivity
                            val intent = Intent(context, LoginActivity::class.java).apply {
                                // Clear task to ensure user can't go back to Register screen
                                // and also provides a fresh Login screen if they log out later.
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)
                            activity?.finish() // Finish RegisterActivity
                        } else {
                            Toast.makeText(context, message ?: "Registration failed. Please try again.", Toast.LENGTH_LONG).show()
                            Log.e("RegisterScreen", "Registration failed for $email: $message")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading, // Disable button when loading
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853), // Your green
                    disabledContainerColor = Color.Gray // Appearance when disabled
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White, // Progress indicator color
                        strokeWidth = 2.dp
                    )
                } else {
                    // Text("Register", color = Color.White) // White text on green button
                    Text("Register", color = Color.Black) // Or Black text for consistency with Login
                }
            }
        }
    }
}

// Helper Composable for Register Screen TextField colors
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun registerTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        disabledTextColor = Color.Gray,
        cursorColor = Color.White, // Or a theme accent color like Color.Cyan
        focusedBorderColor = Color.White, // Or a theme accent color
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.DarkGray,
        focusedLabelColor = Color.White, // Or a theme accent color
        unfocusedLabelColor = Color.LightGray,
        disabledLabelColor = Color.DarkGray,
        containerColor = Color.Black.copy(alpha = 0.3f),
        // For leading/trailing icons if you add them:
        focusedLeadingIconColor = Color.White,
        unfocusedLeadingIconColor = Color.LightGray,
        disabledLeadingIconColor = Color.DarkGray,
        focusedTrailingIconColor = Color.White, // For password toggle when focused
        unfocusedTrailingIconColor = Color.LightGray, // For password toggle when unfocused
        disabledTrailingIconColor = Color.DarkGray
    )
}
