package com.example.busbuddy.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.busbuddy.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth

class BookingListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookingListScreen()
        }
    }
}

@Composable
fun BookingListScreen() {
    val viewModel = remember { BookingViewModel() }
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""

    val bookings by viewModel.bookings.collectAsState()
    val context = LocalContext.current // ✅ Moved here (inside @Composable scope)

    LaunchedEffect(Unit) {
        viewModel.loadBookings(userId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Your Bookings", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        bookings.forEach { booking ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    val intent = Intent(context, EditBookingActivity::class.java)
                    intent.putExtra("bookingId", booking.id)
                    context.startActivity(intent)
                }) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Bus: ${booking.busName}")
                    Text("Seat: ${booking.seatNumber}")
                    Text("Date: ${booking.travelDate}")
                }
            }
        }
    }
}
