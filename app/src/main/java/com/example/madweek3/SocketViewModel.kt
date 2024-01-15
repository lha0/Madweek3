package com.example.madweek3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject

class SocketViewModel(application: Application) : AndroidViewModel(application) {
    val socket: Socket = getApplication<MyApp>().socket

    fun joinRoom(roomId: String, userId: String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        println("socket join "+ socket)
        socket.emit("join", data)
    }

    fun leaveRoom(roomId: String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        socket.emit("leave", data)
    }

    fun userReady(roomId: String, userId: String, totalNum: Int) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        data.put("totalNum", totalNum)
        socket.emit("user ready", data)
    }

    fun userUnready(roomId: String, userId: String, totalNum: Int) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        data.put("totalNum", totalNum)
        socket.emit("user unready", data)
    }

    fun sendMessage(roomId: String, userId: String, message: ChatMessage) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        data.put("message", JSONObject(Gson().toJson(message)))
        println("send")
        println("socket message "+ socket)
        socket.emit("send message", data)
    }

    fun gameStart(roomId: String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        socket.emit("game start", data)
    }

    fun nextUser(roomId: String, userIndex: Int) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userIndex", userIndex)
        socket.emit("next user", data)
    }

    fun select(roomId: String, whichClicked:String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("whichClicked", whichClicked)
        socket.emit("user action", data)
    }

}