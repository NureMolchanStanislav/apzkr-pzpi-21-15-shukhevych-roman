package com.example.smartwardrobeanalytics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.activities.CreateCollectionActivity
import com.example.smartwardrobeanalytics.adapters.CollectionAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityMainBinding
import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.global.UserSession
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.google.android.material.navigation.NavigationView
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var collectionRecyclerView: RecyclerView
    private val apiService = ApiServiceImpl()
    private val ONESIGNAL_APP_ID = "2cb74d5d-c9c8-4985-ae88-6ffd94aa43e9"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
            Log.d("OneSignalToken", "OneSignal Token: $ONESIGNAL_APP_ID")
        }

        // Налаштування Toolbar
        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Додати кнопку "New"
        val buttonNew: Button = findViewById(R.id.button_new_collection)
        buttonNew.setOnClickListener {
            val intent = Intent(this, CreateCollectionActivity::class.java)
            startActivity(intent)
        }

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

    override fun onResume() {
        super.onResume()
        fetchCollections() // Оновлюємо колекції при поверненні до активності
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    private fun fetchCollections() {
        Log.d("MainActivity", "Fetching collections...")
        apiService.getCollections(1, 15, object : ApiCallback<List<CollectionDto>> {
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
