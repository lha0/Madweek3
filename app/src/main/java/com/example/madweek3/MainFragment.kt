package com.example.madweek3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        return view
    }


}