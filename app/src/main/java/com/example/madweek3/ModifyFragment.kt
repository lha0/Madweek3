package com.example.madweek3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ModifyFragment : Fragment() {
    private lateinit var userId: String
    private lateinit var userEmail: String
    private lateinit var userNickName: String
    private lateinit var userPW: String
    private lateinit var userLevel: String
    private var userScore: Int = 0

    private lateinit var modifyNicknameView: EditText
    private lateinit var modifyEmailView: EditText
    private lateinit var modifyPasswordView: EditText
    private lateinit var NicknameToServer: String
    private lateinit var EmailToServer: String
    private lateinit var PasswordToServer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString("userID")?:""
            userEmail = it.getString("email")?:""
            userNickName = it.getString("nickname")?:""
            userPW = it.getString("password")?:""
            userLevel = it.getString("level")?:""
            userScore = it.getInt("score")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modify, container, false)
        modifyNicknameView = view.findViewById(R.id.editNickname)
        modifyEmailView = view.findViewById(R.id.editEmail)
        modifyPasswordView = view.findViewById(R.id.editPW)

        modifyNicknameView.setText(userNickName)
        modifyEmailView.setText(userEmail)

        val submitBtn = view.findViewById<Button>(R.id.modifyConfirmBtn)
        submitBtn.setOnClickListener {
            NicknameToServer = modifyNicknameView.text.toString()
            EmailToServer = modifyEmailView.text.toString()
            PasswordToServer = modifyPasswordView.text.toString()

            modifyInfo()

        }

        return view
    }

    private fun modifyInfo() {
        val sendInfo = User(
            _id = "",
            email = if (EmailToServer.isNotEmpty()) EmailToServer else "",
            password = if (PasswordToServer.isNotEmpty()) PasswordToServer else "",
            nickname = if (NicknameToServer.isNotEmpty()) NicknameToServer else "",
            level = userLevel,
            score = userScore
        )
        println("userid3" +userId)
        RetrofitClient.instance.modifyInfo(sendInfo, userId)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "수정 성공", Toast.LENGTH_SHORT).show()

                        val transaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.main_container, MypageFragment())
                        transaction.commit()
                    } else {
                        Toast.makeText(requireContext(), "수정 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("Mypge Modify error", t.toString())
                }
            })
    }
}