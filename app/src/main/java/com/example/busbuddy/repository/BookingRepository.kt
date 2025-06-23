package com.example.busbuddy.repository

import com.example.busbuddy.model.Booking

interface BookingRepository {
    fun addBooking(booking: Booking, callback: (Boolean, String) -> Unit)
    fun getBookings(userId: String, callback: (List<Booking>) -> Unit)
    fun updateBooking(booking: Booking, callback: (Boolean, String) -> Unit)
    fun deleteBooking(id: String, callback: (Boolean, String) -> Unit)
}
