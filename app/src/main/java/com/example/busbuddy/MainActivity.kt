package com.example.busbuddy

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.busbuddy.view.AddBookingActivity
import com.example.busbuddy.view.BookingListActivity
import com.example.busbuddy.view.EditBookingActivity
import com.example.busbuddy.view.EditProfileActivity
import com.example.busbuddy.view.LoginActivity
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                delay(2000)
                showSplash = false
            }

            if (showSplash) {
                SplashScreen()
            } else {
                DashboardScreen() // Replace with your actual main content
            }
        }

    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text("Welcome to BusBuddy", style = MaterialTheme.typography.headlineMedium)
    }
}
@Composable
fun DashboardScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to BusBuddy",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Button(onClick = {
            context.startActivity(Intent(context, BookingListActivity::class.java))
        }) {
            Text("View Bookings")
        }

        Button(onClick = {
            context.startActivity(Intent(context, AddBookingActivity::class.java))
        }) {
            Text("Add New Booking")
        }

        Button(onClick = {
            context.startActivity(Intent(context, EditProfileActivity::class.java))
        }) {
            Text("Edit Profile")
        }

        Button(onClick = {
            context.startActivity(Intent(context, EditBookingActivity::class.java))
        }) {
            Text("Edit Booking")
        }
    }
}
