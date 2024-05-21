package com.example.smartwardrobeanalytics

import android.os.Bundle;
import com.example.smartwardrobeanalytics.databinding.ActivityRegisterBinding

import androidx.appcompat.app.AppCompatActivity;

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            // Реалізуйте логіку реєстрації
        }
    }
}