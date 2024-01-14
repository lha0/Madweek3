package com.example.madweek3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import io.socket.client.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    private lateinit var socketViewModel: SocketViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // SocketViewModel 초기화
        socketViewModel = ViewModelProvider(this).get(SocketViewModel::class.java)

        println("start1")
        // 소켓 생성 및 ViewModel에 설정
        createSocket()


        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment())
            .commit()

    }

    private fun createSocket() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // 소켓 생성
                val socket = IO.socket("http://54.180.125.145:5000/")

                // ViewModel에 소켓 설정
                socketViewModel.socket = socket

            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
    }

}
