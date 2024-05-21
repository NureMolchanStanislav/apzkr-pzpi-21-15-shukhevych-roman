package com.example.smartwardrobeanalytics

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.adapters.CollectionAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityMainBinding
import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.global.UserSession
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var collectionRecyclerView: RecyclerView
    private val apiService = ApiServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        // Налаштування Toolbar
        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Відображення імені користувача у верхній частині бокового меню
        val headerView = navView.getHeaderView(0)
        val userNameTextView: TextView = headerView.findViewById(R.id.user_name)
        userNameTextView.text = UserSession.currentUser?.email ?: "Unknown User"

        // Налаштування RecyclerView для колекцій
        collectionRecyclerView = findViewById(R.id.collectionRecyclerView)
        collectionRecyclerView.layoutManager = LinearLayoutManager(this)

        // Отримання і відображення колекцій
        fetchCollections()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    private fun fetchCollections() {
        Log.d("MainActivity", "Fetching collections...")
        apiService.getCollections(1, 15, object : ApiServiceImpl.ApiCallback<List<CollectionDto>> {
            override fun onSuccess(result: List<CollectionDto>) {
                Log.d("MainActivity", "Fetched ${result.size} collections")
                runOnUiThread {
                    displayCollections(result)
                }
            }

            override fun onError(error: String) {
                Log.e("MainActivity", "Failed to fetch collections: $error")
            }
        })
    }

    private fun displayCollections(collections: List<CollectionDto>) {
        val adapter = CollectionAdapter(this, collections)
        collectionRecyclerView.adapter = adapter
    }
}
