package com.example.smartwardrobeanalytics.activities.editActivities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.updateDto.BrandUpdateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl

class EditBrandActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var buttonUpdate: Button
    private val brandService = BrandServiceImpl()
    private var brandId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_brand)

        editTextName = findViewById(R.id.edit_text_name)
        buttonUpdate = findViewById(R.id.button_update_brand)

        brandId = intent.getStringExtra("brand_id")
        val brandName = intent.getStringExtra("brand_name")

        editTextName.setText(brandName)

        buttonUpdate.setOnClickListener {
            val name = editTextName.text.toString()
            if (name.isNotEmpty() && brandId != null) {
                val brandUpdateDto = BrandUpdateDto(brandId!!, name)
                updateBrand(brandUpdateDto)
            } else {
                Toast.makeText(this, "Please enter a brand name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateBrand(brandUpdateDto: BrandUpdateDto) {
        brandService.updateBrand(brandUpdateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@EditBrandActivity, "Brand updated successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditBrandActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
