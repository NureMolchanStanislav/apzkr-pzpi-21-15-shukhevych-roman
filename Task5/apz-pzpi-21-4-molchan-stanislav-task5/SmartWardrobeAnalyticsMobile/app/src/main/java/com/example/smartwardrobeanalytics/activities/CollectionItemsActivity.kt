package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartwardrobeanalytics.activities.createActivities.CreateItemActivity
import com.example.smartwardrobeanalytics.activities.editActivities.EditItemActivity
import com.example.smartwardrobeanalytics.adapters.ItemAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityCollectionItemsBinding
import com.example.smartwardrobeanalytics.dtos.ItemDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.ItemServiceImpl

class CollectionItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionItemsBinding
    private val apiService = ItemServiceImpl()
    private lateinit var collectionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        collectionId = intent.getStringExtra("collection_id") ?: return
        val collectionName = intent.getStringExtra("collection_name") ?: "Collection Items"

        binding.collectionName.text = collectionName

        binding.itemRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.buttonCreateItem.setOnClickListener {
            val intent = Intent(this, CreateItemActivity::class.java)
            intent.putExtra("collection_id", collectionId)
            startActivityForResult(intent, CREATE_ITEM_REQUEST_CODE)
        }

        fetchCollectionItems(collectionId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == CREATE_ITEM_REQUEST_CODE || requestCode == EDIT_ITEM_REQUEST_CODE)) {
            fetchCollectionItems(collectionId)
        }
    }

    private fun fetchCollectionItems(collectionId: String) {
        apiService.getCollectionItems(collectionId, object : ApiCallback<List<ItemDto>> {
            override fun onSuccess(result: List<ItemDto>) {
                runOnUiThread {
                    displayCollectionItems(result)
                }
            }

            override fun onError(error: String) {
                Log.e("CollectionItemsActivity", "Failed to fetch collection items: $error")
            }
        })
    }

    private fun displayCollectionItems(items: List<ItemDto>) {
        val adapter = ItemAdapter(this, items,
            onEditClick = { item ->
                val intent = Intent(this, EditItemActivity::class.java)
                intent.putExtra("item_id", item.id)
                startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE)
            },
            onDeleteClick = { item ->
                apiService.deleteItem(item.id, object : ApiCallback<Unit> {
                    override fun onSuccess(result: Unit) {
                        fetchCollectionItems(collectionId)
                    }

                    override fun onError(error: String) {
                        Log.e("CollectionItemsActivity", "Failed to delete item: $error")
                    }
                })
            }
        )
        binding.itemRecyclerView.adapter = adapter
    }

    companion object {
        private const val CREATE_ITEM_REQUEST_CODE = 1
        private const val EDIT_ITEM_REQUEST_CODE = 2
    }
}
