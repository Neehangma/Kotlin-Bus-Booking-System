package com.example.busbuddy.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Booking(
    val id: String = "", // Document ID from Firestore
    val userId: String = "",
    val userName: String? = null, // Assuming you might fetch this separately or denormalize
    val busName: String = "",
    val seatNumber: String = "", // You had this in AddBooking, maybe use it here?
    val travelDate: String = "", // Consistent with AddBookingActivity
    @ServerTimestamp
    val bookingTimestamp: Date? = null
    // You used "booking.date" in BookingCard, let's assume it maps to travelDate or another field
)
