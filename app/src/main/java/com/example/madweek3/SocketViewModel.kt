package com.example.madweek3

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONArray
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

    fun leaveRoom(leaveUserId:String, roomId: String) {
        val data = JSONObject()
        data.put("leaveUserId", leaveUserId)
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

    fun assign_keywords(users: List<String>, roomId: String) {
        val data = JSONObject()
        val json_users = JSONArray(users)
        data.put("usersId", json_users)
        data.put("roomId", roomId)
        Log.d("socket_io test", "$roomId")
        socket.emit("assign keywords", data)
    }

    fun answerRight(roomId:String, userId: String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        data.put("userId", userId)
        socket.emit("answer right", data)
    }

    fun finishGame(roomId:String) {
        val data = JSONObject()
        data.put("roomId", roomId)
        socket.emit("finish game", data)
    }

}