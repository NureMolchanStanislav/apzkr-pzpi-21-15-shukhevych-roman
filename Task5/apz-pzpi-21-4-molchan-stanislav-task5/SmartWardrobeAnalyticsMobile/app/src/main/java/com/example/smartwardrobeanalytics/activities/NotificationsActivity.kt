package com.example.smartwardrobeanalytics.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.activities.createActivities.CreateNotificationActivity
import com.example.smartwardrobeanalytics.activities.editActivities.EditNotificationActivity
import com.example.smartwardrobeanalytics.adapters.NotificationAdapter
import com.example.smartwardrobeanalytics.dtos.NotificationDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.NotificationServiceImpl
import com.google.android.material.appbar.MaterialToolbar

class NotificationsActivity : AppCompatActivity() {

    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var createButton: Button
    private val apiService = NotificationServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val itemId = intent.getStringExtra("item_id") ?: return

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notifications"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        notificationRecyclerView = findViewById(R.id.notificationRecyclerView)
        notificationRecyclerView.layoutManager = LinearLayoutManager(this)

        createButton = findViewById(R.id.button_create_notification)
        createButton.setOnClickListener {
            // Запуск активності для створення нового сповіщення
            val intent = Intent(this, CreateNotificationActivity::class.java)
            intent.putExtra("item_id", itemId)
            startActivityForResult(intent, CREATE_NOTIFICATION_REQUEST_CODE)
        }

        fetchNotifications(itemId)
    }

    private fun fetchNotifications(itemId: String) {
        apiService.getNotifications(itemId, object : ApiCallback<List<NotificationDto>> {
            override fun onSuccess(result: List<NotificationDto>) {
                runOnUiThread {
                    displayNotifications(result)
                }
            }

            override fun onError(error: String) {
                Log.e("NotificationsActivity", "Failed to fetch notifications: $error")
            }
        })
    }

    private fun displayNotifications(notifications: List<NotificationDto>) {
        val adapter = NotificationAdapter(this, notifications,
            onEditClick = { notification ->
                // Додайте обробку натискання кнопки редагування
                val intent = Intent(this, EditNotificationActivity::class.java)
                intent.putExtra("notification_id", notification.id)
                startActivityForResult(intent, EDIT_NOTIFICATION_REQUEST_CODE)
            },
            onDeleteClick = { notification ->
                // Додайте обробку натискання кнопки видалення
                apiService.deleteNotification(notification.id, object : ApiCallback<Unit> {
                    override fun onSuccess(result: Unit) {
                        fetchNotifications(notification.itemId)
                    }

                    override fun onError(error: String) {
                        Log.e("NotificationsActivity", "Failed to delete notification: $error")
                    }
                })
            }
        )
        notificationRecyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val itemId = intent.getStringExtra("item_id") ?: return
            fetchNotifications(itemId)
        }
    }

    companion object {
        private const val CREATE_NOTIFICATION_REQUEST_CODE = 1
        private const val EDIT_NOTIFICATION_REQUEST_CODE = 2
    }
}
