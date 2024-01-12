package com.example.madweek3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.madweek3.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var liBtn: Button
    private lateinit var suBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        liBtn = findViewById(R.id.loginBtn)
        suBtn = findViewById(R.id.TosignupBtn)
        emailInput = findViewById(R.id.liEmail)
        passwordInput = findViewById(R.id.liPW)

        liBtn.setOnClickListener {
            loginUser()
        }

        suBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
    }

    private fun loginUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        val userData = User(email, password, "", "", 0)
        RetrofitClient.instance.loginUser(userData)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val userId = response.body()!!.message
                        println("response " + response.body())
                        if (userId != null) {
                            saveUserId(userId)
                            println("login," + userId)
                        }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        //로그인 실패
                        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t:Throwable) {
                    Log.e("error", t.toString())
                }
            })
    }

    private fun saveUserId(userId: String) {
        val sharedPreference = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("userId", userId)
        editor.apply()
    }
}