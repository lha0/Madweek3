package com.example.madweek3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import io.socket.client.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URISyntaxException

interface OnGameFinishedListener {
    fun onGameFinished()
}


class GameActivity : AppCompatActivity(), OnGameFinishedListener {
    private lateinit var roomId: String
    private lateinit var userList: ArrayList<User>

    override fun onGameFinished() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("returnToFragment", "readyFragment")
        startActivity(intent)
        finish() // 현재 GameActivity 종료
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        roomId = intent.getStringExtra("roomId") ?: ""
        userList = intent.getParcelableArrayListExtra<User>("userList") ?: ArrayList()

        val bundle = Bundle()
        bundle.putString("roomId", roomId)
        bundle.putParcelableArrayList("userList", userList)

        val gameFragment = GameFragment()
        gameFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.game_container, gameFragment)
            .commit()
    }





}