package com.example.busbuddy.repository

import com.example.busbuddy.model.Booking
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class BookingRepositoryImpl : BookingRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun addBooking(booking: Booking, callback: (Boolean, String) -> Unit) {
        val id = UUID.randomUUID().toString()
        val newBooking = booking.copy(id = id)
        db.collection("bookings").document(id).set(newBooking)
            .addOnSuccessListener { callback(true, "Booking added") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }

    override fun getBookings(userId: String, callback: (List<Booking>) -> Unit) {
        db.collection("bookings").whereEqualTo("userId", userId).get()
            .addOnSuccessListener { result ->
                val bookings = result.map { it.toObject(Booking::class.java) }
                callback(bookings)
            }
    }

    override fun updateBooking(booking: Booking, callback: (Boolean, String) -> Unit) {
        db.collection("bookings").document(booking.id).set(booking)
            .addOnSuccessListener { callback(true, "Booking updated") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }

    override fun deleteBooking(id: String, callback: (Boolean, String) -> Unit) {
        db.collection("bookings").document(id).delete()
            .addOnSuccessListener { callback(true, "Booking deleted") }
            .addOnFailureListener { callback(false, it.message ?: "Error") }
    }
}