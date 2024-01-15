package com.example.madweek3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception

class EndFragment : Fragment() {
    private var userList: ArrayList<User> ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_end, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_result_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        //예시 finish List
        val finish_List: List<String> = listOf(
            "65a104d05dc3f4e4c06d2975",
            "65a11a945dc3f4e4c06d2976",
            "65a120d15dc3f4e4c06d2977",
            "65a39886a68ba38b85cadd2c",)

        lifecycleScope.launch {
            val getFinishUserInfo = async { getFinishUserInfo(finish_List) }
            getFinishUserInfo.await()
            val adapter = EndAdapter(userList ?: emptyList())
            recyclerView.adapter = adapter

            if (userList?.isNotEmpty() == true) {
                // userList가 null이 아니고, 비어있지 않은 경우에만 실행되는 코드

            }
        }

        return view
    }

    suspend fun getFinishUserInfo(finish_list: List<String>) {
        try {
            val response = RetrofitClient.instance.getFinishUserInfo(finish_list)
            if (response.isSuccessful && response.body() != null) {
                userList = response!!.body()

            } else {
                Log.d("response of FinishUserInfo failed", "실패")
            }

        } catch(e: Exception) {
            Log.e("Get FinishUserinfo Error", "error: ${e.localizedMessage}}")
        }
    }

}