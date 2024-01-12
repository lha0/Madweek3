package com.example.madweek3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.madweek3.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var nicknameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var suBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        suBtn = findViewById(R.id.signupBtn)
        nicknameInput = findViewById(R.id.suNickname)
        emailInput = findViewById(R.id.suEmail)
        passwordInput = findViewById(R.id.suPW)

        suBtn.setOnClickListener {
            signupUser()
        }
    }

    private fun signupUser() {
        val nickname = nicknameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        val userData = User(email, password, nickname, 0, 0)
        RetrofitClient.instance.signupUser(userData)
            .enqueue(object: Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        //회원가입 성공
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {

                        // 회원가입 실패 - 토스트 메시지 표시
                        Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t:Throwable) {
                    Log.e("error", t.toString())
                }
            })

    }
}