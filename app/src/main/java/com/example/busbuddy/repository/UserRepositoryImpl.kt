package com.example.busbuddy.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.busbuddy.repository.UserRepository

interface UserRepository {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun register(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun resetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun updateProfile(name: String, callback: (Boolean, String) -> Unit)
}

class UserRepositoryImpl : UserRepository {
    private val auth = Firebase.auth

    override fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) callback(true, "Login success")
                else callback(false, it.exception?.message ?: "Login failed")
            }
    }

    override fun register(email: String, password: String, callback: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) callback(true, "Registration success")
                else callback(false, it.exception?.message ?: "Registration failed")
            }
    }

    override fun resetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                callback(it.isSuccessful, if (it.isSuccessful) "Check your email" else it.exception?.message ?: "Failed")
            }
    }

    override fun updateProfile(name: String, callback: (Boolean, String) -> Unit) {
        val user = auth.currentUser
        val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener {
                callback(it.isSuccessful, if (it.isSuccessful) "Profile Updated" else it.exception?.message ?: "Failed")
            }
    }
}
