package com.example.madweek3

import androidx.lifecycle.ViewModel
import io.socket.client.Socket

class SocketViewModel : ViewModel() {

    var socket: Socket? = null
}