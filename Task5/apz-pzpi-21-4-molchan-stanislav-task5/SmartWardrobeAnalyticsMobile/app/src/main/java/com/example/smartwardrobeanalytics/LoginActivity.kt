package com.example.smartwardrobeanalytics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.databinding.ActivityLoginBinding
import com.example.smartwardrobeanalytics.dtos.TokensModel
import com.example.smartwardrobeanalytics.dtos.User
import com.example.smartwardrobeanalytics.global.UserSession

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            Log.d("LoginActivity", "Login button clicked. Email: $email, Password: $password")

            apiService.loginUser(email, password, object : ApiServiceImpl.ApiCallback<TokensModel> {
                override fun onSuccess(result: TokensModel) {
                    Log.d("LoginActivity", "Login successful. Tokens: $result")
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Отримайте інформацію про користувача після успішного логіну
                    apiService.getUser(email, object : ApiServiceImpl.ApiCallback<User> {
                        override fun onSuccess(result: User) {
                            Log.d("LoginActivity", "User info retrieved: $result")
                            UserSession.currentUser = result
                            // Перехід до MainActivity
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onError(error: String) {
                            Log.e("LoginActivity", "Failed to get user info: $error")
                            Toast.makeText(this@LoginActivity, "Failed to get user info: $error", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                override fun onError(error: String) {
                    Log.e("LoginActivity", "Login failed: $error")
                    Toast.makeText(this@LoginActivity, "Login failed: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
