package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.statistics.SeasonalStatisticDto

class SeasonalStatisticsAdapter(
    private val context: Context,
    private var statistics: MutableList<SeasonalStatisticDto>
) : RecyclerView.Adapter<SeasonalStatisticsAdapter.StatisticsViewHolder>() {

    inner class StatisticsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val season: TextView = view.findViewById(R.id.season)
        val totalUsages: TextView = view.findViewById(R.id.total_usages)
        val itemUsages: TextView = view.findViewById(R.id.item_usages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seasonal_statistic, parent, false)
        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val statistic = statistics[position]
        holder.season.text = statistic.season
        holder.totalUsages.text = "Total Usages: ${statistic.totalUsages}"
        holder.itemUsages.text = statistic.itemUsages.entries.joinToString(", ") { "${it.key}: ${it.value}" }
    }

    override fun getItemCount(): Int {
        return statistics.size
    }

    fun updateStatistics(newStatistics: List<SeasonalStatisticDto>) {
        statistics.clear()
        statistics.addAll(newStatistics)
        notifyDataSetChanged()
    }
}
