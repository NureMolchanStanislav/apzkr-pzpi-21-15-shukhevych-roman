package com.example.smartwardrobeanalytics.activities.createActivities

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.сreateDto.ItemCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl
import com.example.smartwardrobeanalytics.services.CollectionServiceImpl
import com.example.smartwardrobeanalytics.services.ItemServiceImpl

class CreateItemActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerCollection: Spinner
    private lateinit var spinnerBrand: Spinner
    private lateinit var buttonCreate: Button
    private val itemService = ItemServiceImpl()
    private val brandService = BrandServiceImpl()
    private val collectionService = CollectionServiceImpl()
    private lateinit var collectionMap: Map<String, String>
    private lateinit var brandMap: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        editTextName = findViewById(R.id.edit_text_name)
        editTextDescription = findViewById(R.id.edit_text_description)
        spinnerCollection = findViewById(R.id.spinner_collection)
        spinnerBrand = findViewById(R.id.spinner_brand)
        buttonCreate = findViewById(R.id.button_create_item)

        loadCollections()
        loadBrands()

        buttonCreate.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val collectionName = spinnerCollection.selectedItem as? String
            val brandName = spinnerBrand.selectedItem as? String
            val collectionId = collectionMap[collectionName]
            val brandId = brandMap[brandName]

            if (name.isNotEmpty() && description.isNotEmpty() && collectionId != null && brandId != null) {
                createItem(ItemCreateDto(name, description, brandId, collectionId))
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCollections() {
        collectionService.getCollections(1, 15, object : ApiCallback<List<CollectionDto>> {
            override fun onSuccess(result: List<CollectionDto>) {
                collectionMap = result.associateBy({ it.name }, { it.id })
                val adapter = ArrayAdapter(this@CreateItemActivity, android.R.layout.simple_spinner_item, collectionMap.keys.toList())
                spinnerCollection.adapter = adapter
            }

            override fun onError(error: String) {
                Toast.makeText(this@CreateItemActivity, "Failed to load collections: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadBrands() {
        brandService.getBrands(1, 15, object : ApiCallback<List<BrandDto>> {
            override fun onSuccess(result: List<BrandDto>) {
                brandMap = result.associateBy({ it.name }, { it.id })
                val adapter = ArrayAdapter(this@CreateItemActivity, android.R.layout.simple_spinner_item, brandMap.keys.toList())
                spinnerBrand.adapter = adapter
            }

            override fun onError(error: String) {
                Toast.makeText(this@CreateItemActivity, "Failed to load brands: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createItem(itemCreateDto: ItemCreateDto) {
        itemService.createItem(itemCreateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@CreateItemActivity, "Item created successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@CreateItemActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
