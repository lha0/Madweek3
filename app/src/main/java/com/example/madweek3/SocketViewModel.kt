package com.example.madweek3

import androidx.lifecycle.ViewModel
import io.socket.client.Socket
import org.json.JSONObject

class SocketViewModel : ViewModel() {

    var socket: Socket? = null

    fun joinRoom(roomId: String, userId: String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        socket?.emit("join", data)
    }

    fun leaveRoom(roomId: String) {
        socket?.emit("leave", roomId)
    }

    fun userReady(roomId: String, userId: String, totalNum: Int) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        data.put("totalNum", totalNum)
        socket?.emit("user ready", data)
    }

    fun userUnready(roomId: String, userId: String, totalNum: Int) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        data.put("totalNum", totalNum)
        socket?.emit("user unready", data)
    }

    fun sendMessage(roomId: String, userId: String, message: ChatMessage) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        data.put("message", message)
        println("send")
        socket?.emit("send message", data)
    }

}