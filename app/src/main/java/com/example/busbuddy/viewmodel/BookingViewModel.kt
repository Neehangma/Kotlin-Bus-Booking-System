package com.example.busbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.busbuddy.model.Booking
import com.example.busbuddy.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Sealed class to represent loading states (already exists in your code)
sealed class BookingResult {
    object Idle : BookingResult()
    object Loading : BookingResult()
    data class Success<T>(val data: T, val message: String? = null) : BookingResult()
    data class Error(val message: String) : BookingResult()
}

class BookingViewModel : ViewModel() {

    val addBookingOperationState: Any

    // State for list of user bookings
    private val _userBookingsState = MutableStateFlow<BookingResult>(BookingResult.Idle)
    val userBookingsState: StateFlow<BookingResult> = _userBookingsState

    // State for update booking operation
    private val _updateBookingOperationState = MutableStateFlow<BookingResult>(BookingResult.Idle)
    val updateBookingOperationState: StateFlow<BookingResult> = _updateBookingOperationState
    // In BookingViewModel
    private val _addBookingOperationState = MutableStateFlow<BookingResult>(BookingResult.Idle) // Assuming BookingResult.Idle is your initial
    val addBookingOperationState: StateFlow<BookingResult> = _addBookingOperationState // Or .asStateFlow()

    // TODO: Implement your repository or data source
    private val repository = BookingRepository()

    fun fetchUserBookings(userId: String) {
        viewModelScope.launch {
            _userBookingsState.value = BookingResult.Loading
            try {
                val bookings = repository.getBookingsByUser(userId)
                _userBookingsState.value = BookingResult.Success(bookings)
            } catch (e: Exception) {
                _userBookingsState.value = BookingResult.Error("Failed to load bookings: ${e.message}")
            }
        }
    }

    // Recommended: Fetch single booking by ID (optional; not used in EditBookingScreen below)
    fun fetchBookingById(bookingId: String) {
        viewModelScope.launch {
            _userBookingsState.value = BookingResult.Loading
            try {
                val booking = repository.getBookingById(bookingId)
                if (booking != null) {
                    _userBookingsState.value = BookingResult.Success(listOf(booking))
                } else {
                    _userBookingsState.value = BookingResult.Error("Booking not found")
                }
            } catch (e: Exception) {
                _userBookingsState.value = BookingResult.Error("Failed to load booking: ${e.message}")
            }
        }
    }

    fun updateBooking(updatedBooking: Booking) {
        viewModelScope.launch {
            _updateBookingOperationState.value = BookingResult.Loading
            try {
                repository.updateBooking(updatedBooking)
                _updateBookingOperationState.value = BookingResult.Success(updatedBooking, "Booking updated successfully")
            } catch (e: Exception) {
                _updateBookingOperationState.value = BookingResult.Error("Failed to update booking: ${e.message}")
            }
        }
    }

    fun clearUpdateBookingOperationState() {
        _updateBookingOperationState.value = BookingResult.Idle
    }
}
