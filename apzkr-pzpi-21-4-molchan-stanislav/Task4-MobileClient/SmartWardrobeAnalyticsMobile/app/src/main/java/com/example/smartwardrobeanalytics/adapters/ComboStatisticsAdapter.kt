package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.statistics.StatisticComboDto
import java.text.SimpleDateFormat
import java.util.*

class ComboStatisticsAdapter(
    private val context: Context,
    private var statistics: MutableList<StatisticComboDto>,
    private var is24HourFormat: Boolean
) : RecyclerView.Adapter<ComboStatisticsAdapter.StatisticsViewHolder>() {

    inner class StatisticsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val combination: TextView = view.findViewById(R.id.combination)
        val usageCount: TextView = view.findViewById(R.id.usage_count)
        val date: TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistic, parent, false)
        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val statistic = statistics[position]
        holder.combination.text = statistic.combination.joinToString(", ")
        holder.usageCount.text = "Usage Count: ${statistic.usageCount}"
        holder.date.text = formatDate(statistic.date)
    }

    override fun getItemCount(): Int {
        return statistics.size
    }

    fun updateStatistics(newStatistics: List<StatisticComboDto>) {
        statistics.clear()
        statistics.addAll(newStatistics)
        notifyDataSetChanged()
    }

    fun set24HourFormat(is24Hour: Boolean) {
        is24HourFormat = is24Hour
        notifyDataSetChanged()
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        val outputFormat = if (is24HourFormat) {
            SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        } else {
            SimpleDateFormat("dd.MM.yyyy hh:mm:ss a", Locale.getDefault())
        }
        return date?.let { outputFormat.format(it) } ?: dateString
    }
}
