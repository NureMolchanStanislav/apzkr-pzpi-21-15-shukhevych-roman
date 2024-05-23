package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.ItemDto

class ItemAdapter(
    private val context: Context,
    private val items: List<ItemDto>,
    private val onEditClick: (ItemDto) -> Unit,
    private val onDeleteClick: (ItemDto) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemDescription: TextView = view.findViewById(R.id.item_description)
        val editIcon: ImageView = view.findViewById(R.id.edit_icon)
        val deleteIcon: ImageView = view.findViewById(R.id.delete_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.content_collection_items, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemDescription.text = item.description
        holder.editIcon.setOnClickListener { onEditClick(item) }
        holder.deleteIcon.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
