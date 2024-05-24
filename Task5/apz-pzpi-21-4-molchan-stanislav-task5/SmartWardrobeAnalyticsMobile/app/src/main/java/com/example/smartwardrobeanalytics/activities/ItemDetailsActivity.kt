package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartwardrobeanalytics.ApiServiceImpl
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.adapters.ItemUsageAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityItemDetailsBinding
import com.example.smartwardrobeanalytics.dtos.StatisticDto
import com.example.smartwardrobeanalytics.dtos.UsageDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

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

        // Додавання кнопки дзвіночка
        val bellIcon: ImageView = findViewById(R.id.bell_icon)
        bellIcon.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            intent.putExtra("item_id", itemId)
            startActivity(intent)
        }
    }

    private fun fetchItemStatistics(itemId: String) {
        apiService.getItemStatistics(itemId, 12, object : ApiCallback<List<StatisticDto>> {
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
        val chart: PieChart = findViewById(R.id.chart)
        val entries = mutableListOf<PieEntry>()

        statistics.forEach {
            try {
                val parts = it.month.split("-")
                if (parts.size == 2) {
                    val monthYear = "${parts[1]}/${parts[0]}"
                    entries.add(PieEntry(it.usageCount.toFloat(), monthYear))
                } else {
                    Log.e("ItemDetailsActivity", "Invalid month format: ${it.month}")
                }
            } catch (e: Exception) {
                Log.e("ItemDetailsActivity", "Error parsing month: ${it.month}", e)
            }
        }

        if (entries.isEmpty()) {
            Log.e("ItemDetailsActivity", "No valid entries for the chart")
            return
        }

        val dataSet = PieDataSet(entries, "Usage Statistics")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // Set the colors for the chart

        val pieData = PieData(dataSet)
        chart.data = pieData
        chart.invalidate() // refresh
    }

    private fun fetchItemUsages(itemId: String) {
        apiService.getItemUsages(itemId, object : ApiCallback<List<UsageDto>> {
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
