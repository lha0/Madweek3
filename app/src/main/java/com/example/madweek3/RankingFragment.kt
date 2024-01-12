package com.example.madweek3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RankingFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ranking, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView_ranking)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        val userList: List<User> = listOf(
            User(email = "hyemin@naver.com", password = "1234", level="꼬질이 세찬", score=240, nickname="HM"),
            User(email = "hayeong@naver.com", password = "5678", level="멋쟁이 세찬", score=300, nickname="hayeong"),

            // 추가적인 User 객체들을 필요에 따라 추가할 수 있습니다.
        )

        RetrofitClient.instance.getAllRanking()
            .enqueue(object: Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    if (response.isSuccessful) {
                        val sortedUsers_ByRanking: List<User>? = response.body()
                        sortedUsers_ByRanking?.let {

                            val adapter = RankingAdapter(requireContext(), it)
                            recyclerView.adapter = adapter

                        }
                    } else {

                        // 회원가입 실패 - 토스트 메시지 표시
                        Toast.makeText(requireContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<User>>, t:Throwable) {
                    Log.e("error", t.toString())
                    Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()

                }
            })



        return view

    }

}