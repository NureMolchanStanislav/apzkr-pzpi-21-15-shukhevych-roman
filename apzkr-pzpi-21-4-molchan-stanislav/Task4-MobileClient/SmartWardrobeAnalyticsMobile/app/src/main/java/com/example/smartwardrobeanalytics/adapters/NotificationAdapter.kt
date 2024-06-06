package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.NotificationDto

class NotificationAdapter(
    private val context: Context,
    private val notifications: List<NotificationDto>,
    private val onEditClick: (NotificationDto) -> Unit,
    private val onDeleteClick: (NotificationDto) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val notificationTitle: TextView = view.findViewById(R.id.notification_title)
        val notificationDescription: TextView = view.findViewById(R.id.notification_description)
        val editIcon: ImageView = view.findViewById(R.id.edit_icon)
        val deleteIcon: ImageView = view.findViewById(R.id.delete_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        holder.notificationTitle.text = notification.title
        holder.notificationDescription.text = notification.description
        holder.editIcon.setOnClickListener { onEditClick(notification) }
        holder.deleteIcon.setOnClickListener { onDeleteClick(notification) }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}
