package com.example.busbuddy.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.busbuddy.model.Booking
import com.example.busbuddy.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth

class AddBookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AddBookingScreen()
        }
    }
}

@Composable
fun AddBookingScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val viewModel = remember { BookingViewModel() }
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""

    var busName by remember { mutableStateOf("") }
    var seatNumber by remember { mutableStateOf("") }
    var travelDate by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = busName,
            onValueChange = { busName = it },
            label = { Text("Bus Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = seatNumber,
            onValueChange = { seatNumber = it },
            label = { Text("Seat Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = travelDate,
            onValueChange = { travelDate = it },
            label = { Text("Travel Date") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val booking = Booking(
                userId = userId,
                busName = busName,
                seatNumber = seatNumber,
                travelDate = travelDate
            )
            viewModel.addBooking(booking) { success, message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                if (success) activity?.finish()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Booking")
        }
    }
}
