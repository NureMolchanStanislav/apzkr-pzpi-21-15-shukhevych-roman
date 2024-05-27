package com.example.smartwardrobeanalytics.activities.editActivities

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.NotificationDto
import com.example.smartwardrobeanalytics.dtos.updateDto.NotificationUpdateDto
import com.example.smartwardrobeanalytics.interfaces.iretrofit.ApiCallback
import com.example.smartwardrobeanalytics.services.NotificationServiceImpl

class EditNotificationActivity : AppCompatActivity() {

    private lateinit var editTextCondition: EditText
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonUpdate: Button
    private val apiService = NotificationServiceImpl()
    private var itemId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_notification)

        editTextCondition = findViewById(R.id.edit_text_condition)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        buttonUpdate = findViewById(R.id.button_update_notification)

        val notificationId = intent.getStringExtra("notification_id") ?: return

        loadNotification(notificationId)

        buttonUpdate.setOnClickListener {
            val condition = editTextCondition.text.toString().toIntOrNull()
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()

            if (condition != null && title.isNotEmpty() && description.isNotEmpty()) {
                itemId?.let { id ->
                    updateNotification(NotificationUpdateDto(notificationId, condition, title, description, id))
                } ?: Toast.makeText(this, "Item ID is missing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadNotification(notificationId: String) {
        apiService.getNotificationById(notificationId, object : ApiCallback<NotificationDto> {
            override fun onSuccess(result: NotificationDto) {
                editTextCondition.setText(result.condition.toString())
                editTextTitle.setText(result.title)
                editTextDescription.setText(result.description)
                itemId = result.itemId // Save the item ID for later use
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditNotificationActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateNotification(notificationUpdateDto: NotificationUpdateDto) {
        apiService.updateNotification(notificationUpdateDto, object : ApiCallback<Unit> {
            override fun onSuccess(result: Unit) {
                Toast.makeText(this@EditNotificationActivity, "Notification updated successfully", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onError(error: String) {
                Toast.makeText(this@EditNotificationActivity, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
