package com.example.madweek3

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SocketViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SocketViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}