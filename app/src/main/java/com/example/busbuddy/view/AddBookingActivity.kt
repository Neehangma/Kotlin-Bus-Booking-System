package com.example.busbuddy.view

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.busbuddy.model.Booking
import com.example.busbuddy.ui.common.CommonTextFieldColors // Ensure this path is correct
import com.example.busbuddy.ui.common.SimpleTopAppBar // Ensure this path is correct
import com.example.busbuddy.viewmodel.BookingResult
import com.example.busbuddy.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Locale
import kotlin.text.isNullOrBlank
import com.example.busbuddy.R
import androidx.compose.runtime.collectAsState

class AddBookingActivity : ComponentActivity() {
    private val bookingViewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Assuming you have a MaterialTheme defined in your ui.theme package
            // If not, use androidx.compose.material3.MaterialTheme {}
            MaterialTheme {
                Scaffold(
                    topBar = {
                        SimpleTopAppBar(
                            title = "Add New Booking",
                            onNavigateUp = { finish() }
                        )
                    },
                    containerColor = Color.Transparent // Or your desired background color
                ) { innerPadding ->
                    AddBookingScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = bookingViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookingScreen(modifier: Modifier = Modifier, viewModel: BookingViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var busName by remember { mutableStateOf("") }
    var seatNumber by remember { mutableStateOf("") }
    var travelDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val addBookingResultState by viewModel.addBookingOperationState.collectAsState()

    LaunchedEffect(addBookingResultState) {
        when (val result = addBookingResultState) {
            is BookingResult.Success -> {
                isLoading = false
                val toastMessage = result.message ?: "Booking added successfully!"
                Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
                Log.d("AddBookingScreen", "Booking added with ID: ${result.data}")
                viewModel.clearAddBookingOperationState() // Reset state
                activity?.finish() // Close activity on success
            }
            is BookingResult.Error -> {
                isLoading = false
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                Log.e("AddBookingScreen", "Failed to add booking: ${result.message}", result.exception)
                viewModel.clearAddBookingOperationState() // Reset state
            }
            is BookingResult.Loading -> {
                isLoading = true
            }
            is BookingResult.Idle -> {
                isLoading = false
            }
        }
    }

    val calendar = remember { Calendar.getInstance() }

    val datePickerDialog = remember(context) { // Key with context for stability
        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year: Int, month: Int, dayOfMonth: Int -> // Explicit listener
                travelDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.booking), // Make sure this drawable exists
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box( // Overlay
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)) // Semi-transparent overlay
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp), // Adjust padding as needed
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = busName,
                onValueChange = { busName = it },
                label = { Text("Bus Name", color = Color.White) }, // Text color for visibility on dark bg
                modifier = Modifier.fillMaxWidth(),
                colors = CommonTextFieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = seatNumber,
                onValueChange = { seatNumber = it },
                label = { Text("Seat Number (e.g., A5, 12)", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default,
                colors = CommonTextFieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = travelDate,
                onValueChange = {}, // Not directly editable
                readOnly = true,
                label = { Text("Travel Date (YYYY-MM-DD)", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isLoading) { datePickerDialog.show() },
                trailingIcon = {
                    IconButton(
                        onClick = { if (!isLoading) datePickerDialog.show() },
                        enabled = !isLoading
                    ) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_my_calendar),
                            contentDescription = "Select Travel Date",
                            tint = Color.LightGray // Icon tint for visibility
                        )
                    }
                },
                colors = CommonTextFieldColors(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (userId.isNullOrBlank()) {
                        Toast.makeText(context, "You must be logged in to add a booking.", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (busName.isBlank() || seatNumber.isBlank() || travelDate.isBlank()) {
                        Toast.makeText(context, "Please fill all booking details.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // ID will be auto-generated by Firestore
                    val newBooking = Booking(
                        userId = userId, // Ensure userId is not null
                        busName = busName.trim(),
                        seatNumber = seatNumber.trim(),
                        travelDate = travelDate.trim()
                        // bookingTimestamp will be handled by @ServerTimestamp in Booking model
                    )
                    viewModel.addBooking(newBooking)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00C853), // A green color
                    disabledContainerColor = Color.Gray,
                    contentColor = Color.Black, // Text color on the button
                    disabledContentColor = Color.DarkGray
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White // Spinner color on the button
                    )
                } else {
                    Text("Add Booking")
                }
            }
        }
    }
}
