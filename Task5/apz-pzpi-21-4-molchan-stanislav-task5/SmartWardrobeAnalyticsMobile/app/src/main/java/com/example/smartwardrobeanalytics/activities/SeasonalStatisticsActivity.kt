package com.example.smartwardrobeanalytics.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.adapters.SeasonalStatisticsAdapter
import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.statistics.SeasonalStatisticDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl
import com.example.smartwardrobeanalytics.services.SeasonalStatisticsServiceImpl
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class SeasonalStatisticsActivity : AppCompatActivity() {

    private lateinit var seasonalStatisticsAdapter: SeasonalStatisticsAdapter
    private val statisticsService = SeasonalStatisticsServiceImpl()
    private val brandService = BrandServiceImpl()
    private val brandMap = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seasonal_statistics)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Seasonal Statistic"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val brandSpinner = findViewById<Spinner>(R.id.brand_spinner)
        loadBrands(brandSpinner)

        val statisticsRecyclerView = findViewById<RecyclerView>(R.id.statisticsRecyclerView)
        statisticsRecyclerView.layoutManager = LinearLayoutManager(this)

        seasonalStatisticsAdapter = SeasonalStatisticsAdapter(this, mutableListOf())
        statisticsRecyclerView.adapter = seasonalStatisticsAdapter

        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedBrandName = parent.getItemAtPosition(position) as String
                val selectedBrandId = brandMap[selectedBrandName]
                if (selectedBrandId != null) {
                    fetchStatistics(selectedBrandId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadBrands(brandSpinner: Spinner) {
        brandService.getBrandsByUser(object : ApiCallback<List<BrandDto>> {
            override fun onSuccess(result: List<BrandDto>) {
                val brandNames = result.map { it.name }
                val adapter = ArrayAdapter(this@SeasonalStatisticsActivity, android.R.layout.simple_spinner_item, brandNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                brandSpinner.adapter = adapter

                // Заповніть мапу відповідностей між іменем бренду та його ID
                result.forEach { brand ->
                    brandMap[brand.name] = brand.id
                }
            }

            override fun onError(error: String) {
                Log.e("SeasonalStatisticsActivity", "Не вдалося завантажити бренди: $error")
            }
        })
    }

    private fun fetchStatistics(brandId: String) {
        statisticsService.getSeasonalStatistics(brandId, object : ApiCallback<List<SeasonalStatisticDto>> {
            override fun onSuccess(result: List<SeasonalStatisticDto>) {
                runOnUiThread {
                    seasonalStatisticsAdapter.updateStatistics(result)
                    setupChart(result)
                }
            }

            override fun onError(error: String) {
                Log.e("SeasonalStatisticsActivity", "Не вдалося завантажити статистику: $error")
            }
        })
    }

    private fun setupChart(statistics: List<SeasonalStatisticDto>) {
        val chart = findViewById<BarChart>(R.id.chart)
        val entries = mutableListOf<BarEntry>()
        statistics.forEachIndexed { index, stat ->
            entries.add(BarEntry(index.toFloat(), stat.totalUsages.toFloat()))
        }
        val dataSet = BarDataSet(entries, "Total count of usages")
        val barData = BarData(dataSet)
        chart.data = barData
        chart.invalidate()
    }
}
