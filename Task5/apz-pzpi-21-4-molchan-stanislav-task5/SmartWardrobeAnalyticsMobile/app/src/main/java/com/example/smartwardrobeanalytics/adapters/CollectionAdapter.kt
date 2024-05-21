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
import com.example.smartwardrobeanalytics.activities.CollectionItemsActivity
import com.example.smartwardrobeanalytics.dtos.CollectionDto

class CollectionAdapter(private val context: Context, private val collections: List<CollectionDto>) :
    RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val collectionName: TextView = view.findViewById(R.id.collection_name)
        val collectionDescription: TextView = view.findViewById(R.id.collection_description)
        val detailsButton: Button = view.findViewById(R.id.details_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = collections[position]
        holder.collectionName.text = collection.name
        holder.collectionDescription.text = collection.description
        holder.detailsButton.setOnClickListener {
            val intent = Intent(context, CollectionItemsActivity::class.java).apply {
                putExtra("collection_id", collection.id)
                putExtra("collection_name", collection.name)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = collections.size
}
