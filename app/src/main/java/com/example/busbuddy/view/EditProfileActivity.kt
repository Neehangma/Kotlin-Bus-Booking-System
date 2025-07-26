package com.example.busbuddy.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.busbuddy.viewmodel.UserViewModel // Assuming your ViewModel
import com.example.busbuddy.repository.UserRepositoryImpl // Assuming your Repository

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme { // Apply your app's theme
                Scaffold(
                    topBar = {
                        EditProfileTopAppBar(title = "Edit Profile", onNavigateUp = { finish() })
                    }

                ) { innerPadding ->
                    EditProfileScreen(
                        modifier = Modifier.padding(innerPadding) // Apply Scaffold's padding
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileTopAppBar(title: String, onNavigateUp: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onNavigateUp != null) {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}


@Composable
fun EditProfileScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity
    val userViewModel = remember { UserViewModel(UserRepositoryImpl()) }

    var displayName by remember { mutableStateOf("") }
    // State to manage loading indicator
    var isLoading by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = userViewModel) {
        isLoading = true

        try {
            kotlinx.coroutines.delay(500) // Simulate delay
            val fetchedDisplayName = userViewModel.getCurrentDisplayName() // Assuming such a method exists
            if (fetchedDisplayName != null) {
                displayName = fetchedDisplayName
            } else {
                Log.d("EditProfileScreen", "No existing display name found.")
            }
        } catch (e: Exception) {
            Log.e("EditProfileScreen", "Error fetching profile", e)
            Toast.makeText(context, "Failed to load profile data", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 24.dp))
        } else {
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (displayName.isNotBlank()) {
                        isLoading = true
                        userViewModel.updateProfile(displayName) { success, message ->
                            isLoading = false
                            Toast.makeText(context, message ?: "Profile update status.", Toast.LENGTH_LONG).show()
                            if (success) {

                                Log.d("EditProfileScreen", "Profile updated successfully.")
                            } else {
                                Log.e("EditProfileScreen", "Profile update failed: $message")
                            }
                        }
                    } else {
                        Toast.makeText(context, "Display name cannot be empty.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading && displayName.isNotBlank()) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Update Profile")
                }
            }
        }
    }
}


fun UserViewModel.getCurrentDisplayName(): String? {

    Log.d("UserViewModel", "getCurrentDisplayName called (dummy implementation)")

    return null
}
