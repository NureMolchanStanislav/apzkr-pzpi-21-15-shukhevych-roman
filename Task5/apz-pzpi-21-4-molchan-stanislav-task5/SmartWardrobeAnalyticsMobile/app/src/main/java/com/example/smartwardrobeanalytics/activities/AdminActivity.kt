package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.ApiServiceImpl
import com.example.smartwardrobeanalytics.LoginActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.adapters.UserAdapter
import com.example.smartwardrobeanalytics.dtos.User
import com.example.smartwardrobeanalytics.global.UserSession
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.AdminServiceImpl
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private val apiService = AdminServiceImpl()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    logout()
                    true
                }
                R.id.nav_download_csv -> {
                    showTableSelectionDialog()
                    true
                }
                else -> false
            }
        }

        val userRecyclerView = findViewById<RecyclerView>(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(this, mutableListOf(), onBanClick = { user ->
            apiService.banUser(user.id, object : ApiCallback<Unit> {
                override fun onSuccess(result: Unit) {
                    user.isDeleted = true
                    userAdapter.notifyDataSetChanged()
                }

                override fun onError(error: String) {
                    Log.e("AdminActivity", "Failed to ban user: $error")
                }
            })
        }, onUnbanClick = { user ->
            apiService.unbanUser(user.id, object : ApiCallback<Unit> {
                override fun onSuccess(result: Unit) {
                    user.isDeleted = false
                    userAdapter.notifyDataSetChanged()
                }

                override fun onError(error: String) {
                    Log.e("AdminActivity", "Failed to unban user: $error")
                }
            })
        })
        userRecyclerView.adapter = userAdapter

        fetchUsers()

        val searchByIdEditText = findViewById<EditText>(R.id.search_by_id)
        val searchByEmailEditText = findViewById<EditText>(R.id.search_by_email)

        searchByIdEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                userAdapter.filterById(query)
            }
        })

        searchByEmailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                userAdapter.filterByEmail(query)
            }
        })

        setNavDownloadCSVVisibility()
    }

    private fun fetchUsers() {
        apiService.getUsers(1, 100, object : ApiCallback<List<User>> {
            override fun onSuccess(result: List<User>) {
                runOnUiThread {
                    userAdapter.updateUsers(result)
                }
            }

            override fun onError(error: String) {
                Log.e("AdminActivity", "Failed to fetch users: $error")
            }
        })
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showTableSelectionDialog() {
        val tables = arrayOf("Users", "Brands", "Notifications", "Collections", "Items", "Usages", "Offers", "RFIDTags")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Table to Export")
        builder.setItems(tables) { _, which ->
            val selectedTable = tables[which]
            downloadCSV(selectedTable)
        }
        builder.show()
    }

    private fun downloadCSV(tableName: String) {
        val url = "http://192.168.50.234:5002/api/export/exportCVS/$tableName"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun setNavDownloadCSVVisibility() {
        val isAdmin = UserSession.currentUser?.roles?.any { it.name == "Admin" } ?: false
        navView.menu.findItem(R.id.nav_download_csv).isVisible = isAdmin
    }
}
