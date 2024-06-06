package com.example.smartwardrobeanalytics.activities.createActivities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.сreateDto.BrandCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl

class CreateBrandActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var buttonCreate: Button
    private val brandService = BrandServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_brand)

        editTextName = findViewById(R.id.edit_text_name)
        buttonCreate = findViewById(R.id.button_create_brand)

        buttonCreate.setOnClickListener {
            val name = editTextName.text.toString()
            if (name.isNotEmpty()) {
                val brandCreateDto = BrandCreateDto(name)
                createBrand(brandCreateDto)
            } else {
                Toast.makeText(this, "Please enter a brand name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createBrand(brandCreateDto: BrandCreateDto) {
        brandService.createBrand(brandCreateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@CreateBrandActivity, "Brand created successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@CreateBrandActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
