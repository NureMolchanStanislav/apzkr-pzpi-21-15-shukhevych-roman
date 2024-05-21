package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.activities.ItemDetailsActivity
import com.example.smartwardrobeanalytics.dtos.ItemDto

class ItemAdapter(
    private val context: Context,
    private val items: List<ItemDto>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.collection_name)
        val itemDescription: TextView = view.findViewById(R.id.collection_description)
        val detailsButton: Button = view.findViewById(R.id.details_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemDescription.text = item.description
        holder.detailsButton.setOnClickListener {
            val intent = Intent(context, ItemDetailsActivity::class.java).apply {
                putExtra("item_id", item.id)
                putExtra("item_name", item.name)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
