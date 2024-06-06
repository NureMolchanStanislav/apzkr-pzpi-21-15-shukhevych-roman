package com.example.smartwardrobeanalytics.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.services.ApiServiceImpl
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.сreateDto.CollectionCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback

class CreateCollectionActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonCreate: Button
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_collection)

        editTextName = findViewById(R.id.edit_text_collection_name)
        editTextDescription = findViewById(R.id.edit_text_collection_description)
        buttonCreate = findViewById(R.id.button_create_collection)

        buttonCreate.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty()) {
                createCollection(name, description)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createCollection(name: String, description: String) {
        val collectionCreateDto = CollectionCreateDto(name, description)
        apiService.createCollection(collectionCreateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@CreateCollectionActivity, "Collection created successfully", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@CreateCollectionActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
