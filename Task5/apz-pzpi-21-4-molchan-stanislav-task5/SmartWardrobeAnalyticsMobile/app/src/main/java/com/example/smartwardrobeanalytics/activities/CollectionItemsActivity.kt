package com.example.smartwardrobeanalytics.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartwardrobeanalytics.ApiServiceImpl
import com.example.smartwardrobeanalytics.adapters.ItemAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityCollectionItemsBinding
import com.example.smartwardrobeanalytics.dtos.ItemDto

class CollectionItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollectionItemsBinding
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val collectionId = intent.getStringExtra("collection_id") ?: return
        val collectionName = intent.getStringExtra("collection_name") ?: "Collection Items"

        binding.collectionName.text = collectionName

        // Налаштування RecyclerView для елементів колекції
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchCollectionItems(collectionId)
    }

    private fun fetchCollectionItems(collectionId: String) {
        apiService.getCollectionItems(collectionId, object : ApiServiceImpl.ApiCallback<List<ItemDto>> {
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
        val adapter = ItemAdapter(this, items)
        binding.itemRecyclerView.adapter = adapter
    }
}
