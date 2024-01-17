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
    private lateinit var viewModel: SocketViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val returnToFragment = intent.getStringExtra("returnToFragment")

        if (returnToFragment == "readyFragment") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, RoomListFragment())
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment())
                .commit()
        }

        viewModel = ViewModelProvider(this).get(SocketViewModel::class.java)

    }



}
