package com.example.smartwardrobeanalytics.activities.editActivities

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
import com.example.smartwardrobeanalytics.dtos.ItemDto
import com.example.smartwardrobeanalytics.dtos.updateDto.ItemUpdateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl
import com.example.smartwardrobeanalytics.services.CollectionServiceImpl
import com.example.smartwardrobeanalytics.services.ItemServiceImpl

class EditItemActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerCollection: Spinner
    private lateinit var spinnerBrand: Spinner
    private lateinit var buttonUpdate: Button
    private val itemService = ItemServiceImpl()
    private val brandService = BrandServiceImpl()
    private val collectionService = CollectionServiceImpl()
    private var itemId: String? = null
    private lateinit var collectionMap: Map<String, String>
    private lateinit var brandMap: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        editTextName = findViewById(R.id.edit_text_name)
        editTextDescription = findViewById(R.id.edit_text_description)
        spinnerCollection = findViewById(R.id.spinner_collection)
        spinnerBrand = findViewById(R.id.spinner_brand)
        buttonUpdate = findViewById(R.id.button_update_item)

        itemId = intent.getStringExtra("item_id")

        loadItemDetails(itemId)
        loadCollections()
        loadBrands()

        buttonUpdate.setOnClickListener {
            val name = editTextName.text.toString()
            val description = editTextDescription.text.toString()
            val collectionName = spinnerCollection.selectedItem as? String
            val brandName = spinnerBrand.selectedItem as? String
            val collectionId = collectionMap[collectionName]
            val brandId = brandMap[brandName]

            if (name.isNotEmpty() && description.isNotEmpty() && collectionId != null && brandId != null) {
                itemId?.let { id ->
                    updateItem(ItemUpdateDto(id, name, description, brandId, collectionId))
                } ?: Toast.makeText(this, "Item ID is missing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadItemDetails(itemId: String?) {
        itemId?.let { id ->
            itemService.getItemById(id, object : ApiCallback<ItemDto> {
                override fun onSuccess(result: ItemDto) {
                    editTextName.setText(result.name)
                    editTextDescription.setText(result.description)
                    selectSpinnerValue(spinnerCollection, result.collectionId)
                    selectSpinnerValue(spinnerBrand, result.brandId)
                }

                override fun onError(error: String) {
                    Toast.makeText(this@EditItemActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadCollections() {
        collectionService.getCollections(1, 15, object : ApiCallback<List<CollectionDto>> {
            override fun onSuccess(result: List<CollectionDto>) {
                collectionMap = result.associateBy({ it.name }, { it.id })
                val adapter = ArrayAdapter(this@EditItemActivity, android.R.layout.simple_spinner_item, collectionMap.keys.toList())
                spinnerCollection.adapter = adapter
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditItemActivity, "Failed to load collections: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadBrands() {
        brandService.getBrands(1, 15, object : ApiCallback<List<BrandDto>> {
            override fun onSuccess(result: List<BrandDto>) {
                brandMap = result.associateBy({ it.name }, { it.id })
                val adapter = ArrayAdapter(this@EditItemActivity, android.R.layout.simple_spinner_item, brandMap.keys.toList())
                spinnerBrand.adapter = adapter
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditItemActivity, "Failed to load brands: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun selectSpinnerValue(spinner: Spinner, value: String?) {
        val adapter = spinner.adapter as? ArrayAdapter<String>
        adapter?.let {
            val position = it.getPosition(value)
            if (position >= 0) {
                spinner.setSelection(position)
            }
        }
    }

    private fun updateItem(itemUpdateDto: ItemUpdateDto) {
        itemService.updateItem(itemUpdateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@EditItemActivity, "Item updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditItemActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
