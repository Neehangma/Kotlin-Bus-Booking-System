package com.example.busbuddy.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Keep this import
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // If ViewModel exposes state as Flow
import androidx.compose.runtime.getValue      // If ViewModel exposes state as Flow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.busbuddy.model.Booking
import com.example.busbuddy.viewmodel.BookingListViewModel // Ensure this path is correct
// Import your SimpleTopAppBar if you have one, or use Material3 TopAppBar
// import com.example.busbuddy.ui.common.SimpleTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingListScreen(viewModel: BookingListViewModel) {
    // Assuming your ViewModel exposes bookings as a SnapshotStateList (mutableStateListOf)
    // or a State<List<Booking>> via collectAsState()
    // For this example, let's assume viewModel.bookings is directly observable by Compose
    // (e.g., val bookings = mutableStateListOf<Booking>() in ViewModel)

    // Optional: If you add loading/error states to ViewModel
    // val isLoading by viewModel.isLoading.collectAsState()
    // val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar( // Or your SimpleTopAppBar
                title = { Text("View Bookings") }
                // navigationIcon = { /* If you want a back button */ },
                // colors = TopAppBarDefaults.topAppBarColors(
                // containerColor = MaterialTheme.colorScheme.primary,
                // titleContentColor = MaterialTheme.colorScheme.onPrimary
                // )
            )
        },
        containerColor = MaterialTheme.colorScheme.background // Good for theming
    ) { innerPadding ->

        // Handle loading state (optional, but good practice)
        // if (isLoading) {
        //     Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
        //         CircularProgressIndicator()
        //     }
        // } else if (errorMessage != null) {
        //     Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
        //         Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
        //     }
        // } else if (viewModel.bookings.isEmpty()) {
        //     Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
        //         Text("No bookings found.")
        //     }
        // } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Apply padding from Scaffold
                .padding(horizontal = 16.dp, vertical = 8.dp), // Additional padding for content
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = viewModel.bookings, // This list must be observable by Compose
                key = { booking -> booking.id } // Excellent for performance
            ) { booking ->
                BookingCard(
                    booking = booking,
                    onDelete = {
                        viewModel.deleteBooking(booking.id)
                    }
                )
            }
        }
        // } // End of else for loading/error/empty states
    }
}

@Composable
fun BookingCard(booking: Booking, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Use theme color
            // containerColor = Color(0xFFE9EEBD) // Or your specific color if not from theme
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Slightly reduced elevation
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // Ensure column takes full width for button alignment
            verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between Text elements
        ) {
            Text(
                text = "Passenger: ${booking.userName ?: "N/A"}", // Handle possible null userName
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Bus: ${booking.busName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Seat: ${booking.seatNumber}", // Assuming you have seatNumber
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Date: ${booking.travelDate}", // Use consistent field name
                style = MaterialTheme.typography.bodyMedium
            )
            // Spacer(modifier = Modifier.height(8.dp)) // Already handled by Arrangement.spacedBy
            Button(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.End), // Align button to the right
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error, // Theme error color
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = MaterialTheme.shapes.medium // Use theme shape
            ) {
                Text("Delete")
            }
        }
    }
}

