package com.example.smartwardrobeanalytics.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.adapters.BonusAdapter
import com.example.smartwardrobeanalytics.dtos.BonusDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BonusServiceImpl

class BonusActivity : AppCompatActivity() {

    private lateinit var bonusAdapter: BonusAdapter
    private val bonusService = BonusServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bonus)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Bonus"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val bonusRecyclerView = findViewById<RecyclerView>(R.id.bonusRecyclerView)
        bonusRecyclerView.layoutManager = LinearLayoutManager(this)

        bonusAdapter = BonusAdapter(this, mutableListOf())
        bonusRecyclerView.adapter = bonusAdapter

        fetchBonuses()
    }

    private fun fetchBonuses() {
        bonusService.getUserBonuses(object : ApiCallback<List<BonusDto>> {
            override fun onSuccess(result: List<BonusDto>) {
                runOnUiThread {
                    bonusAdapter.updateBonuses(result)
                }
            }

            override fun onError(error: String) {
                Log.e("BonusActivity", "Failed to fetch bonuses: $error")
            }
        })
    }
}
