package com.example.smartwardrobeanalytics.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.adapters.PopularItemsStatisticsAdapter
import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.statistics.PopularItemStatisticDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl
import com.example.smartwardrobeanalytics.services.PopularItemsStatisticsServiceImpl
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.util.*

class PopularItemsStatisticsActivity : AppCompatActivity() {

    private lateinit var popularItemsStatisticsAdapter: PopularItemsStatisticsAdapter
    private val statisticsService = PopularItemsStatisticsServiceImpl()
    private val brandService = BrandServiceImpl()
    private val brandMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_items_statistics)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Popular Items Statistics"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val brandSpinner = findViewById<Spinner>(R.id.brand_spinner)
        loadBrands(brandSpinner)

        val statisticsRecyclerView = findViewById<RecyclerView>(R.id.statisticsRecyclerView)
        statisticsRecyclerView.layoutManager = LinearLayoutManager(this)

        popularItemsStatisticsAdapter = PopularItemsStatisticsAdapter(this, mutableListOf())
        statisticsRecyclerView.adapter = popularItemsStatisticsAdapter

        val startDateEditText = findViewById<EditText>(R.id.start_date)
        val endDateEditText = findViewById<EditText>(R.id.end_date)
        val topCountEditText = findViewById<EditText>(R.id.top_count)
        val fetchButton = findViewById<Button>(R.id.fetch_button)
        val chart = findViewById<BarChart>(R.id.chart)

        startDateEditText.setOnClickListener { showDatePickerDialog(startDateEditText) }
        endDateEditText.setOnClickListener { showDatePickerDialog(endDateEditText) }

        fetchButton.setOnClickListener {
            val selectedBrandName = brandSpinner.selectedItem as String
            val selectedBrandId = brandMap[selectedBrandName]
            if (selectedBrandId != null) {
                val startDate = startDateEditText.text.toString()
                val endDate = endDateEditText.text.toString()
                val topCount = topCountEditText.text.toString().toIntOrNull() ?: 10
                fetchStatistics(selectedBrandId, startDate, endDate, topCount, chart)
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun loadBrands(brandSpinner: Spinner) {
        brandService.getBrandsByUser(object : ApiCallback<List<BrandDto>> {
            override fun onSuccess(result: List<BrandDto>) {
                val brandNames = result.map { it.name }
                val adapter = ArrayAdapter(this@PopularItemsStatisticsActivity, android.R.layout.simple_spinner_item, brandNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                brandSpinner.adapter = adapter

                // Заповніть мапу відповідностей між іменем бренду та його ID
                result.forEach { brand ->
                    brandMap[brand.name] = brand.id
                }
            }

            override fun onError(error: String) {
                Log.e("PopularItemsStatisticsActivity", "Не вдалося завантажити бренди: $error")
            }
        })
    }

    private fun fetchStatistics(brandId: String, startDate: String, endDate: String, topCount: Int, chart: BarChart) {
        statisticsService.getPopularItemsStatistics(brandId, startDate, endDate, topCount, object : ApiCallback<List<PopularItemStatisticDto>> {
            override fun onSuccess(result: List<PopularItemStatisticDto>) {
                runOnUiThread {
                    popularItemsStatisticsAdapter.updateStatistics(result)
                    updateChart(result, chart)
                }
            }

            override fun onError(error: String) {
                Log.e("PopularItemsStatisticsActivity", "Не вдалося завантажити статистику: $error")
            }
        })
    }

    private fun updateChart(statistics: List<PopularItemStatisticDto>, chart: BarChart) {
        val entries = statistics.mapIndexed { index, stat ->
            BarEntry(index.toFloat(), stat.usageCount.toFloat())
        }
        val dataSet = BarDataSet(entries, "Usage Count")
        val barData = BarData(dataSet)
        chart.data = barData
        chart.invalidate()
    }
}
