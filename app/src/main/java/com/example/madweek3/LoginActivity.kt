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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        liBtn = findViewById<Button>(R.id.loginBtn)
        emailInput = findViewById<EditText>(R.id.liEmail)
        passwordInput = findViewById<EditText>(R.id.liPW)

        liBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        val userData = User(email, password, "", 0, 0)
        RetrofitClient.instance.loginUser(userData)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        Log.d("Login Success", "login success")
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
}