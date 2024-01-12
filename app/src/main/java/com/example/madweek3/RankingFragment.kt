package com.example.madweek3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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

        val userList: List<USER> = listOf(
            USER(email = "hyemin@naver.com", password = "1234", level=2, score=240, nickname="HM"),
            USER(email = "hayeong@naver.com", password = "5678", level=6, score=300, nickname="hayeong"),
            // 추가적인 User 객체들을 필요에 따라 추가할 수 있습니다.
        )


        val adapter = RankingAdapter(requireContext(), userList)

        recyclerView.adapter = adapter

        return view

    }

}