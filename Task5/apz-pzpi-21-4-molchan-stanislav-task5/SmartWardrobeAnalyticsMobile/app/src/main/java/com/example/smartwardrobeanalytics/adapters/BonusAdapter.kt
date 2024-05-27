package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.BonusDto

class BonusAdapter(
    private val context: Context,
    private var bonuses: MutableList<BonusDto>
) : RecyclerView.Adapter<BonusAdapter.BonusViewHolder>() {

    inner class BonusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val discount: TextView = view.findViewById(R.id.discount)
        val brandName: TextView = view.findViewById(R.id.brand_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BonusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bonus, parent, false)
        return BonusViewHolder(view)
    }

    override fun onBindViewHolder(holder: BonusViewHolder, position: Int) {
        val bonus = bonuses[position]
        holder.discount.text = "Discount: ${bonus.discount}%"
        holder.brandName.text = "Brand: ${bonus.brandName}"
    }

    override fun getItemCount(): Int {
        return bonuses.size
    }

    fun updateBonuses(newBonuses: List<BonusDto>) {
        bonuses.clear()
        bonuses.addAll(newBonuses)
        notifyDataSetChanged()
    }
}
