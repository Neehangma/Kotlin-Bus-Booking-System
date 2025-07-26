package com.example.busbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.busbuddy.model.Booking

class BookingListViewModel : ViewModel() {

    // Sample list
    private val _bookings = mutableStateListOf(
        Booking("1", "Neehangma", "Greenline", "2025-07-28"),
        Booking("2", "Rinzin", "Deluxe", "2025-07-30"),
        Booking("3", "Karma", "Express", "2025-08-01")
    )

    val bookings: SnapshotStateList<Booking> = _bookings

    fun deleteBooking(bookingId: String) {
        _bookings.removeAll { it.id == bookingId }
    }
}
