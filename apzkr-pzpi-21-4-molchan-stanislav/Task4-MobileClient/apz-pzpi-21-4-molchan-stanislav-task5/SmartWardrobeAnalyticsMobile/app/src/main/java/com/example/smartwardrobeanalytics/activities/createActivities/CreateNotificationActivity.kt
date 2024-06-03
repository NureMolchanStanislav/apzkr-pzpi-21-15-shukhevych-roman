package com.example.smartwardrobeanalytics.activities.createActivities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.сreateDto.NotificationCreateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.NotificationServiceImpl

class CreateNotificationActivity : AppCompatActivity() {

    private lateinit var editTextCondition: EditText
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonCreate: Button
    private val apiService = NotificationServiceImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)

        editTextCondition = findViewById(R.id.edit_text_condition)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        buttonCreate = findViewById(R.id.button_create_notification)

        val itemId = intent.getStringExtra("item_id") ?: return

        buttonCreate.setOnClickListener {
            val condition = editTextCondition.text.toString().toIntOrNull()
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()

            if (condition != null && title.isNotEmpty() && description.isNotEmpty()) {
                createNotification(NotificationCreateDto(condition, title, description, itemId))
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createNotification(notificationCreateDto: NotificationCreateDto) {
        apiService.createNotification(notificationCreateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@CreateNotificationActivity, "Notification created successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@CreateNotificationActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
