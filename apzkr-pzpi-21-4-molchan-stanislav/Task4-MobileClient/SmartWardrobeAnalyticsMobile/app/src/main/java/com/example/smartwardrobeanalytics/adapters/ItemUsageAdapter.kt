package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.UsageDto
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ItemUsageAdapter(
    private val context: Context,
    private val usages: List<UsageDto>,
    private var is24HourFormat: Boolean
) : RecyclerView.Adapter<ItemUsageAdapter.UsageViewHolder>() {

    inner class UsageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usageName: TextView = view.findViewById(R.id.usage_name)
        val usageDate: TextView = view.findViewById(R.id.usage_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage, parent, false)
        return UsageViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsageViewHolder, position: Int) {
        val usage = usages[position]
        holder.usageName.text = usage.name
        holder.usageDate.text = formatDate(usage.date)
    }

    override fun getItemCount(): Int {
        return usages.size
    }

    private fun formatDate(date: String): String {
        val originalFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val dateObject = originalFormat.parse(date)

        return if (is24HourFormat) {
            val newFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
            dateObject?.let { newFormat.format(it) } ?: date
        } else {
            val newFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US)
            dateObject?.let { newFormat.format(it) } ?: date
        }
    }

    fun set24HourFormat(is24Hour: Boolean) {
        is24HourFormat = is24Hour
        notifyDataSetChanged()
    }
}
