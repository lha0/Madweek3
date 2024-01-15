package com.example.madweek3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val rankingButton = view.findViewById<LinearLayout>(R.id.RankingButton)
        val enteringButton = view.findViewById<LinearLayout>(R.id.EnteringButton)
        val searchRoomButton = view.findViewById<LinearLayout>(R.id.RoomSearchButton)
        val mypageBtn = view.findViewById<LinearLayout>(R.id.MypageButton)
        val logoutBtn = view.findViewById<LinearLayout>(R.id.logoutButton)
        val endFragmentButton = view.findViewById<Button>(R.id.EndFragmentButton)

        rankingButton.setOnClickListener {
            val rankingFragment = RankingFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, rankingFragment)
                .addToBackStack(null)  // 백 스택에 추가
                .commit()
        }

        enteringButton.setOnClickListener {
            val roomListFragment = RoomListFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, roomListFragment)
                .addToBackStack(null)  // 백 스택에 추가
                .commit()
        }

        searchRoomButton.setOnClickListener {
            val searchRoomFragment = SearchRoomFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, searchRoomFragment)
                .addToBackStack(null)  // 백 스택에 추가
                .commit()
        }

        mypageBtn.setOnClickListener {
            val Mypage = MypageFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, Mypage)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        logoutBtn.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("userId")
            editor.apply()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        endFragmentButton.setOnClickListener {
            //EndFragment로 연결되도록 추가
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_container, EndFragment())
            transaction.addToBackStack(null)
            transaction.commit()

        }

        return view
    }


}