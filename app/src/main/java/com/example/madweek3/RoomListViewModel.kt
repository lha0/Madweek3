package com.example.madweek3

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomListViewModel(): ViewModel() {
    private val _gameRooms = MutableLiveData<List<Room>>()
    val gameRooms: LiveData<List<Room>> get() = _gameRooms

    // Coroutine을 사용하여 비동기 작업 수행
    fun loadGameRooms() {
        RetrofitClient.instance.getAllRooms()
            .enqueue(object : Callback<List<Room>> {
                override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                    if (response.isSuccessful) {
                        val roomList: List<Room>? = response.body()
                        roomList?.let {

                            _gameRooms.value = it

                        }
                    } else {
                        Log.e("error", "error")
                        _gameRooms.value= emptyList()
                    }
                }

                override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                    Log.e("error", t.toString())
                    _gameRooms.value= emptyList()
                }
            })

    }
}

