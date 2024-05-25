package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.ApiServiceImpl
import com.example.smartwardrobeanalytics.LoginActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.сreateDto.UserCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private val userService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextFirstName = findViewById(R.id.edit_text_first_name)
        editTextLastName = findViewById(R.id.edit_text_last_name)
        editTextPhone = findViewById(R.id.edit_text_phone)
        editTextEmail = findViewById(R.id.edit_text_email)
        editTextPassword = findViewById(R.id.edit_text_password)
        buttonRegister = findViewById(R.id.button_register)

        buttonRegister.setOnClickListener {
            val firstName = editTextFirstName.text.toString()
            val lastName = editTextLastName.text.toString()
            val phone = editTextPhone.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (firstName.isNotEmpty() && lastName.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val userCreateDto = UserCreateDto(firstName, lastName, phone, email, password)
                registerUser(userCreateDto)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(userCreateDto: UserCreateDto) {
        userService.registerUser(userCreateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@RegisterActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
