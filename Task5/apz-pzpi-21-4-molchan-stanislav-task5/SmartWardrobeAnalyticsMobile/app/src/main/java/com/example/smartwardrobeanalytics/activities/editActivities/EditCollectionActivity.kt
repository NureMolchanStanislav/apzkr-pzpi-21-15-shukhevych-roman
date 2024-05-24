package com.example.smartwardrobeanalytics.activities.editActivities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.updateDto.CollectionUpdateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.CollectionServiceImpl

class EditCollectionActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonUpdate: Button
    private val collectionService = CollectionServiceImpl()
    private var collectionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_collection)

        editTextName = findViewById(R.id.edit_text_name)
        editTextDescription = findViewById(R.id.edit_text_description)
        buttonUpdate = findViewById(R.id.button_update_collection)

        collectionId = intent.getStringExtra("collection_id")

        loadCollectionDetails(collectionId)

        buttonUpdate.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && collectionId != null) {
                val collectionUpdateDto = CollectionUpdateDto(collectionId!!, name, description)
                updateCollection(collectionUpdateDto)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCollectionDetails(collectionId: String?) {
        collectionId?.let { id ->
            collectionService.getCollectionById(id, object : ApiCallback<CollectionDto> {
                override fun onSuccess(result: CollectionDto) {
                    editTextName.setText(result.name)
                    editTextDescription.setText(result.description)
                }

                override fun onError(error: String) {
                    Toast.makeText(this@EditCollectionActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun updateCollection(collectionUpdateDto: CollectionUpdateDto) {
        collectionService.updateCollection(collectionUpdateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@EditCollectionActivity, "Collection updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditCollectionActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
