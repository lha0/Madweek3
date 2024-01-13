package com.example.madweek3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import io.socket.client.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URISyntaxException


class GameActivity : AppCompatActivity() {

    private lateinit var socketViewModel: SocketViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // SocketViewModel 초기화
        socketViewModel = ViewModelProvider(this).get(SocketViewModel::class.java)

        // 소켓 생성 및 ViewModel에 설정
        createSocket()

    }

    private fun createSocket() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // 소켓 생성
                val socket = IO.socket("http:/54.180.125.145:5000/")

                // ViewModel에 소켓 설정
                socketViewModel.socket = socket

                // 통신 시작 등의 작업 수행
                // ...

            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }
}