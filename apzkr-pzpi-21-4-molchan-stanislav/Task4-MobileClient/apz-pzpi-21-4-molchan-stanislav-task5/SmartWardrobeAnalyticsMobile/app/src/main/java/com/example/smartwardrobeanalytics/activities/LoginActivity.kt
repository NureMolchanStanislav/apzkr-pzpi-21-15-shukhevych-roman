package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.services.ApiServiceImpl
import com.example.smartwardrobeanalytics.databinding.ActivityLoginBinding
import com.example.smartwardrobeanalytics.dtos.RoleDto
import com.example.smartwardrobeanalytics.dtos.TokensModel
import com.example.smartwardrobeanalytics.dtos.User
import com.example.smartwardrobeanalytics.global.UserSession
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LoginActivity", "Config loaded: ONE_SIGNAL_APP_ID=${Config.ONE_SIGNAL_APP_ID}, BASE_URL=${Config.BASE_URL}")

        binding.btnLogin.setOnClickListener {
            val email = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            Log.d("LoginActivity", "Login button clicked. Email: $email, Password: $password")

            apiService.loginUser(email, password, object : ApiCallback<TokensModel> {
                override fun onSuccess(result: TokensModel) {
                    Log.d("LoginActivity", "Login successful. Tokens: $result")
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                    apiService.getUser(email, object : ApiCallback<User> {
                        override fun onSuccess(result: User) {
                            Log.d("LoginActivity", "User info retrieved: $result")
                            UserSession.currentUser = result
                            redirectToAppropriateActivity(result.roles)
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

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun redirectToAppropriateActivity(roles: List<RoleDto>) {
        val intent = when {
            roles.any { it.name == "Admin" } -> Intent(this, AdminActivity::class.java)
            roles.any { it.name == "Business" } -> Intent(this, BrandListActivity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
