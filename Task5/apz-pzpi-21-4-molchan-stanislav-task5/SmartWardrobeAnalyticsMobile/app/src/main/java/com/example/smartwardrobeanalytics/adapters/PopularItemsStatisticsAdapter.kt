package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.statistics.PopularItemStatisticDto

class PopularItemsStatisticsAdapter(
    private val context: Context,
    private var statistics: MutableList<PopularItemStatisticDto>
) : RecyclerView.Adapter<PopularItemsStatisticsAdapter.StatisticsViewHolder>() {

    inner class StatisticsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val usageCount: TextView = view.findViewById(R.id.usage_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_statistic, parent, false)
        return StatisticsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        val statistic = statistics[position]
        holder.itemName.text = statistic.itemName
        holder.usageCount.text = "Usage Count: ${statistic.usageCount}"
    }

    override fun getItemCount(): Int {
        return statistics.size
    }

    fun updateStatistics(newStatistics: List<PopularItemStatisticDto>) {
        statistics.clear()
        statistics.addAll(newStatistics)
        notifyDataSetChanged()
    }
}
