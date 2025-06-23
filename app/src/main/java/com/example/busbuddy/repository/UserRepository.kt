package com.example.busbuddy.repository


interface UR {
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun register(email: String, password: String, callback: (Boolean, String) -> Unit)
    fun resetPassword(email: String, callback: (Boolean, String) -> Unit)
    fun updateProfile(name: String, callback: (Boolean, String) -> Unit)
}
