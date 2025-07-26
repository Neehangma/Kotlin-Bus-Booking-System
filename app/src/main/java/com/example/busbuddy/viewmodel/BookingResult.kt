package com.example.busbuddy.viewmodel // Make sure this package matches your project structure


sealed class BookingResult<out T> { // Made generic to carry data of type T


    data class Success<out T>(val data: T, val message: String? = null) : BookingResult<T>()


    data class Error(val message: String, val exception: Exception? = null) : BookingResult<Nothing>() // Nothing for data type


    object Loading : BookingResult<Nothing>() // Nothing for data type


    object Idle : BookingResult<Nothing>() // Nothing for data type
}

