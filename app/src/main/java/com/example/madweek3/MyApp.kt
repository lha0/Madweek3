package com.example.madweek3

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import io.socket.client.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URISyntaxException

class MyApp : Application() {
    lateinit var socket: io.socket.client.Socket

    override fun onCreate() {
        super.onCreate()

        // 소켓 초기화 및 설정
        createSocket()
    }

    private fun createSocket() {
        try {
            // 소켓 생성
            socket = IO.socket("http://54.180.125.145:5000/")

            // 소켓 연결
            socket.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }
}