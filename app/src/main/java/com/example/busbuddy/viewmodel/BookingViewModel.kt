package com.example.busbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.example.busbuddy.model.Booking
import com.example.busbuddy.repository.BookingRepository
import com.example.busbuddy.repository.BookingRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel(private val repo: BookingRepository = BookingRepositoryImpl()) : ViewModel() {

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings: StateFlow<List<Booking>> = _bookings

    fun loadBookings(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.getBookings(userId) {
                _bookings.value = it
            }
        }
    }

    fun addBooking(booking: Booking, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.addBooking(booking, callback)
        }
    }

    fun updateBooking(booking: Booking, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.updateBooking(booking, callback)
        }
    }

    fun deleteBooking(id: String, callback: (Boolean, String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteBooking(id, callback)
        }
    }
}
