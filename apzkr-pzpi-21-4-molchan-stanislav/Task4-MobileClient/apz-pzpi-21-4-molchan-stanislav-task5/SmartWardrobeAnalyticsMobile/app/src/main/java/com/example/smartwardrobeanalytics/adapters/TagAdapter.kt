package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.TagDto

class TagAdapter(
    private val context: Context,
    private var tags: MutableList<TagDto>
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    inner class TagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagId: TextView = view.findViewById(R.id.tag_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.tagId.text = tag.tagId
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    fun updateTags(newTags: List<TagDto>) {
        tags.clear()
        tags.addAll(newTags)
        notifyDataSetChanged()
    }
}
