package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.adapters.ComboStatisticsAdapter
import com.example.smartwardrobeanalytics.dtos.statistics.StatisticComboDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.StatisticsServiceImpl

class ComboStatisticsActivity : AppCompatActivity() {

    private lateinit var comboStatisticsAdapter: ComboStatisticsAdapter
    private val statisticsService = StatisticsServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_combo_statistics)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Combo Statistics"

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, BrandListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val switchTimeFormat = findViewById<Switch>(R.id.switch_time_format)
        val statisticsRecyclerView = findViewById<RecyclerView>(R.id.statisticsRecyclerView)
        statisticsRecyclerView.layoutManager = LinearLayoutManager(this)

        comboStatisticsAdapter = ComboStatisticsAdapter(this, mutableListOf(), switchTimeFormat.isChecked)
        statisticsRecyclerView.adapter = comboStatisticsAdapter

        switchTimeFormat.setOnCheckedChangeListener { _, isChecked ->
            comboStatisticsAdapter.set24HourFormat(isChecked)
        }

        fetchStatistics()
    }

    private fun fetchStatistics() {
        statisticsService.getComboStatistics(object : ApiCallback<List<StatisticComboDto>> {
            override fun onSuccess(result: List<StatisticComboDto>) {
                runOnUiThread {
                    comboStatisticsAdapter.updateStatistics(result)
                }
            }

            override fun onError(error: String) {
                Log.e("ComboStatisticsActivity", "Failed to fetch statistics: $error")
            }
        })
    }
}
