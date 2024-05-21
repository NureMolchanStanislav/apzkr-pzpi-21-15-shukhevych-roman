package com.example.smartwardrobeanalytics.activities

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartwardrobeanalytics.ApiServiceImpl
import com.example.smartwardrobeanalytics.adapters.ItemUsageAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityItemDetailsBinding
import com.example.smartwardrobeanalytics.dtos.StatisticDto
import com.example.smartwardrobeanalytics.dtos.UsageDto

class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailsBinding
    private val apiService = ApiServiceImpl()
    private lateinit var usageAdapter: ItemUsageAdapter
    private var is24HourFormat: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemId = intent.getStringExtra("item_id") ?: return
        val itemName = intent.getStringExtra("item_name") ?: "Item Details"

        binding.itemName.text = itemName

        // Налаштування RecyclerView для історії використання
        binding.usageRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchItemStatistics(itemId)
        fetchItemUsages(itemId)

        // Налаштування перемикача для формату часу
        binding.switchTimeFormat.setOnCheckedChangeListener { _, isChecked ->
            is24HourFormat = isChecked
            usageAdapter.set24HourFormat(is24HourFormat)
        }
    }

    private fun fetchItemStatistics(itemId: String) {
        apiService.getItemStatistics(itemId, 12, object : ApiServiceImpl.ApiCallback<List<StatisticDto>> {
            override fun onSuccess(result: List<StatisticDto>) {
                runOnUiThread {
                    displayItemStatistics(result)
                }
            }

            override fun onError(error: String) {
                Log.e("ItemDetailsActivity", "Failed to fetch item statistics: $error")
            }
        })
    }

    private fun displayItemStatistics(statistics: List<StatisticDto>) {
        val chartLayout = binding.chartLayout

        statistics.forEach { stat ->
            val textView = TextView(this)
            textView.text = "${stat.month}: ${stat.usageCount}"
            chartLayout.addView(textView)
        }
    }

    private fun fetchItemUsages(itemId: String) {
        apiService.getItemUsages(itemId, object : ApiServiceImpl.ApiCallback<List<UsageDto>> {
            override fun onSuccess(result: List<UsageDto>) {
                runOnUiThread {
                    displayItemUsages(result)
                }
            }

            override fun onError(error: String) {
                Log.e("ItemDetailsActivity", "Failed to fetch item usages: $error")
            }
        })
    }

    private fun displayItemUsages(usages: List<UsageDto>) {
        usageAdapter = ItemUsageAdapter(this, usages, is24HourFormat)
        binding.usageRecyclerView.adapter = usageAdapter
    }
}
