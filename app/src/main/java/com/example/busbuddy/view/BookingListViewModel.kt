package com.example.busbuddy.view


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.busbuddy.model.Booking
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
// ... other imports

class BookingListViewModel : ViewModel() {
    val bookings = mutableStateListOf<Booking>() // This is the crucial part for your LazyColumn
    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchBookings() // Or some other way to load bookings
    }

    private fun fetchBookings() {
        // Example: Fetch logic, make sure to update `bookings` list
        // This is a simplified example, you'd handle errors, user-specific data, etc.
        viewModelScope.launch {
            firestore.collection("bookings")
                // .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid) // If user-specific
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        // Handle error
                        return@addSnapshotListener
                    }
                    if (snapshots != null) {
                        val newBookings = snapshots.map { it.toObject(Booking::class.java).copy(id = it.id) }
                        bookings.clear()
                        bookings.addAll(newBookings)
                    }
                }
        }
    }

    fun deleteBooking(bookingId: String) {
        viewModelScope.launch {
            firestore.collection("bookings").document(bookingId).delete()
                .addOnSuccessListener {
                    // Optionally, you might not need to manually remove from `bookings`
                    // if your `fetchBookings` uses `addSnapshotListener` as it will update automatically.
                    // If not using a snapshot listener, you'd remove it manually here:
                    // bookings.removeAll { it.id == bookingId }
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }
}
