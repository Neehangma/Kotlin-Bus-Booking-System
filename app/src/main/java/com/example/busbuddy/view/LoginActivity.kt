package com.example.busbuddy.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.busbuddy.MainActivity // Assuming this is your main activity after login
import com.example.busbuddy.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

// Ensure these are imported if they are in different sub-packages of .view
// import com.example.busbuddy.view.ForgotPasswordActivity
// import com.example.busbuddy.view.RegisterActivity

// You would typically have a ViewModel for login logic
// import com.example.busbuddy.viewmodel.UserViewModel
// import com.example.busbuddy.repository.UserRepositoryImpl


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme { // Apply your app's theme
                Scaffold(
                    // Scaffold is good for top/bottom bars, FABs, and managing insets
                    // For a full-screen background image, ensure padding is handled correctly
                    // by the content that sits ON TOP of the background.
                ) { innerPadding -> // This padding is for content INSIDE the Scaffold
                    LoginScreen(
                        // Pass the padding to LoginScreen if its content needs to be inset
                        // from system bars. If LoginScreen is a full Box with its own
                        // background taking the whole screen, its *foreground* content
                        // might need this padding.
                        // For this full-screen background setup, the padding is applied
                        // to the root Box of LoginScreen.
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun LoginScreen(modifier: Modifier = Modifier) { // Accept modifier
    val context = LocalContext.current
    val activity = context as? Activity

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // For loading state

    // ViewModel should ideally handle SharedPreferences for testability and separation
    // val userViewModel = remember { UserViewModel(UserRepositoryImpl(context)) } // Example
    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    LaunchedEffect(Unit) {
        // Load saved preferences
        email = sharedPreferences.getString("email", "") ?: ""
        rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        if (rememberMe) {
            // Only load password if remember me was explicitly set, and be mindful of security.
            // Storing raw passwords in SharedPreferences is not recommended for production.
            // Consider secure alternatives like Android Keystore for sensitive data if needed,
            // or better, use authentication tokens.
            password = sharedPreferences.getString("password", "") ?: ""
        }
        Log.d("LoginScreen", "Loaded Prefs: email='${email}', rememberMe=$rememberMe")
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 1. Background Image
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = null, // Decorative
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dark Overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        )

        // 3. Login Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Padding for the content elements themselves, inside the overlay
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {
            

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email Icon") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = loginTextFieldColors() // Extracted for reuse
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password Icon") },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = Color.LightGray // Adjust tint if needed
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = loginTextFieldColors() // Extracted for reuse
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Remember Me & Forgot Password Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { rememberMe = !rememberMe } // Make whole row clickable
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF00C853), // Your green
                            uncheckedColor = Color.LightGray,
                            checkmarkColor = Color.Black // Or Color.White if green is dark enough
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Remember me", color = Color.White)
                }

                Text(
                    text = "Forgot Password?",
                    color = Color.Cyan, // Or a theme color
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, ForgotPasswordActivity::class.java))
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    val editor = sharedPreferences.edit()
                    if (rememberMe) {
                        editor.putString("email", email)
                        editor.putString("password", password) // Be cautious
                        editor.putBoolean("rememberMe", true)
                        Log.d("LoginScreen", "Saved Prefs: email='${email}', rememberMe=true")
                    } else {
                        editor.remove("email")
                        editor.remove("password")
                        editor.putBoolean("rememberMe", false)
                        Log.d("LoginScreen", "Cleared email/password, rememberMe=false in Prefs")
                    }
                    editor.apply()

                    // --- !!! TODO: Replace with actual ViewModel-based login !!! ---

                    kotlinx.coroutines.GlobalScope.launch { // Use ViewModelScope in ViewModel
                        kotlinx.coroutines.delay(1500) // Simulate network/processing delay
                        val loginSuccess = true // Simulate actual login result

                        activity?.runOnUiThread {
                            isLoading = false
                            if (loginSuccess) {
                                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                context.startActivity(intent)
                                activity.finish() // Finish LoginActivity
                            } else {
                                Toast.makeText(context, "Login failed. Invalid credentials.", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    // --- End of TODO section ---
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853), // Your green
                    disabledContainerColor = Color.Gray
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White, // Progress indicator color on button
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login", color = Color.Black) // Text color on button
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Text
            Row(
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, RegisterActivity::class.java))
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.White
                )
                Text(
                    text = "Register",
                    color = Color.Cyan // Or a theme color
                )
            }
        }
    }
}

// Helper Composable for TextField colors to reduce repetition
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun loginTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        disabledTextColor = Color.Gray,
        cursorColor = Color.Cyan,
        focusedBorderColor = Color.Cyan,
        unfocusedBorderColor = Color.Gray,
        disabledBorderColor = Color.DarkGray,
        focusedLabelColor = Color.Cyan,
        unfocusedLabelColor = Color.LightGray,
        disabledLabelColor = Color.DarkGray,
        containerColor = Color.Black.copy(alpha = 0.25f), // Slightly more opaque
        // For leading/trailing icons if you want specific colors:
        focusedLeadingIconColor = Color.Cyan,
        unfocusedLeadingIconColor = Color.LightGray,
        disabledLeadingIconColor = Color.DarkGray,
        focusedTrailingIconColor = Color.Cyan,
        unfocusedTrailingIconColor = Color.LightGray,
        disabledTrailingIconColor = Color.DarkGray
    )
}
