package com.example.smartwardrobeanalytics.activities

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
import com.example.smartwardrobeanalytics.services.ApiServiceImpl
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.activities.editActivities.EditCollectionActivity
import com.example.smartwardrobeanalytics.adapters.CollectionAdapter
import com.example.smartwardrobeanalytics.databinding.ActivityMainBinding
import com.example.smartwardrobeanalytics.dtos.CollectionDto
import com.example.smartwardrobeanalytics.dtos.RoleDto
import com.example.smartwardrobeanalytics.global.UserSession
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.CollectionServiceImpl
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
    private val collectionApi = CollectionServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Config.loadConfig(this)  // Load config here
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        OneSignal.initWithContext(this, Config.ONE_SIGNAL_APP_ID)

        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(false)
            Log.d("OneSignalToken", "OneSignal Token: ${Config.ONE_SIGNAL_APP_ID}")
        }

        setSupportActionBar(binding.toolbar)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        updateNavigationView(UserSession.currentUser?.roles ?: emptyList())

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    logout()
                    true
                }
                R.id.nav_bonus -> {
                    val intent = Intent(this, BonusActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val buttonNew: Button = findViewById(R.id.button_new_collection)
        buttonNew.setOnClickListener {
            val intent = Intent(this, CreateCollectionActivity::class.java)
            startActivityForResult(intent, CREATE_COLLECTION_REQUEST_CODE)
        }

        val headerView = navView.getHeaderView(0)
        val userNameTextView: TextView = headerView.findViewById(R.id.user_name)
        userNameTextView.text = UserSession.currentUser?.email ?: "Unknown User"

        collectionRecyclerView = findViewById(R.id.collectionRecyclerView)
        collectionRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchCollections()
    }

    override fun onResume() {
        super.onResume()
        fetchCollections()
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
        val adapter = CollectionAdapter(this, collections,
            onEditClick = { collection ->
                val intent = Intent(this, EditCollectionActivity::class.java)
                intent.putExtra("collection_id", collection.id)
                startActivityForResult(intent, EDIT_COLLECTION_REQUEST_CODE)
            },
            onDeleteClick = { collection ->
                collectionApi.deleteCollection(collection.id, object : ApiCallback<Unit> {
                    override fun onSuccess(result: Unit) {
                        fetchCollections()
                    }

                    override fun onError(error: String) {
                        Log.e("MainActivity", "Failed to delete collection: $error")
                    }
                })
            }
        )
        collectionRecyclerView.adapter = adapter
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val CREATE_COLLECTION_REQUEST_CODE = 1
        private const val EDIT_COLLECTION_REQUEST_CODE = 2
    }

    private fun updateNavigationView(roles: List<RoleDto>) {
        val menu = navView.menu
        val statisticsItem = menu.findItem(R.id.nav_combo_statistics)
        statisticsItem.isVisible = roles.any { it.name == "Business" }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }
    }
}