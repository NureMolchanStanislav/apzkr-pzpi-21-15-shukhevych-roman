package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.LoginActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.activities.createActivities.CreateBrandActivity
import com.example.smartwardrobeanalytics.activities.editActivities.EditBrandActivity
import com.example.smartwardrobeanalytics.adapters.BrandAdapter
import com.example.smartwardrobeanalytics.dtos.BrandDto
import com.example.smartwardrobeanalytics.dtos.RoleDto
import com.example.smartwardrobeanalytics.global.UserSession
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.BrandServiceImpl
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.MaterialToolbar

class BrandListActivity : AppCompatActivity() {

    private lateinit var brandAdapter: BrandAdapter
    private val brandService = BrandServiceImpl()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_list)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Update the menu based on user roles
        updateNavigationView(UserSession.currentUser?.roles ?: emptyList())

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // Handle logout button click
                    logout()
                    true
                }
                R.id.nav_combo_statistics -> {
                    // Handle Combo Statistics button click
                    val intent = Intent(this, ComboStatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_seasonal_statistics -> {
                    // Handle Seasonal Statistics button click
                    val intent = Intent(this, SeasonalStatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_popular_items_statistics -> {
                    // Handle Popular Items Statistics button click
                    val intent = Intent(this, PopularItemsStatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val brandRecyclerView = findViewById<RecyclerView>(R.id.brandRecyclerView)
        brandRecyclerView.layoutManager = LinearLayoutManager(this)

        brandAdapter = BrandAdapter(this, mutableListOf(),
            onEditClick = { brand ->
                val intent = Intent(this, EditBrandActivity::class.java).apply {
                    putExtra("brand_id", brand.id)
                    putExtra("brand_name", brand.name)
                }
                startActivityForResult(intent, EDIT_BRAND_REQUEST_CODE)
            },
            onDeleteClick = { brand ->
                brandService.deleteBrand(brand.id, object : ApiCallback<Unit> {
                    override fun onSuccess(result: Unit) {
                        fetchBrands()
                    }

                    override fun onError(error: String) {
                        Log.e("BrandListActivity", "Failed to delete brand: $error")
                        Toast.makeText(this@BrandListActivity, "Failed to delete brand: $error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        )
        brandRecyclerView.adapter = brandAdapter

        findViewById<Button>(R.id.button_new_brand).setOnClickListener {
            val intent = Intent(this, CreateBrandActivity::class.java)
            startActivityForResult(intent, CREATE_BRAND_REQUEST_CODE)
        }

        fetchBrands()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == CREATE_BRAND_REQUEST_CODE || requestCode == EDIT_BRAND_REQUEST_CODE)) {
            fetchBrands()
        }
    }

    private fun fetchBrands() {
        brandService.getBrandsByUser(object : ApiCallback<List<BrandDto>> {
            override fun onSuccess(result: List<BrandDto>) {
                runOnUiThread {
                    brandAdapter.updateBrands(result)
                }
            }

            override fun onError(error: String) {
                Log.e("BrandListActivity", "Failed to fetch brands: $error")
            }
        })
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateNavigationView(roles: List<RoleDto>) {
        val menu = navView.menu
        val comboStatisticsItem = menu.findItem(R.id.nav_combo_statistics)
        val seasonalStatisticsItem = menu.findItem(R.id.nav_seasonal_statistics)
        val popularItemsStatisticsItem = menu.findItem(R.id.nav_popular_items_statistics)
        val hasBusinessRole = roles.any { it.name == "Business" }
        comboStatisticsItem.isVisible = hasBusinessRole
        seasonalStatisticsItem.isVisible = hasBusinessRole
        popularItemsStatisticsItem.isVisible = hasBusinessRole

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    logout()
                    true
                }
                R.id.nav_combo_statistics -> {
                    val intent = Intent(this, ComboStatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_seasonal_statistics -> {
                    val intent = Intent(this, SeasonalStatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_popular_items_statistics -> {
                    val intent = Intent(this, PopularItemsStatisticsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        private const val CREATE_BRAND_REQUEST_CODE = 1
        private const val EDIT_BRAND_REQUEST_CODE = 2
    }
}
