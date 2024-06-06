package com.example.smartwardrobeanalytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartwardrobeanalytics.R
import com.example.smartwardrobeanalytics.dtos.BrandDto

class BrandAdapter(
    private val context: Context,
    private var brands: MutableList<BrandDto>,
    private val onEditClick: (BrandDto) -> Unit,
    private val onDeleteClick: (BrandDto) -> Unit
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    inner class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandName: TextView = view.findViewById(R.id.brand_name)
        val editIcon: ImageView = view.findViewById(R.id.edit_icon)
        val deleteIcon: ImageView = view.findViewById(R.id.delete_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = brands[position]
        holder.brandName.text = brand.name

        holder.editIcon.setOnClickListener { onEditClick(brand) }
        holder.deleteIcon.setOnClickListener { onDeleteClick(brand) }
    }

    override fun getItemCount() = brands.size

    fun updateBrands(newBrands: List<BrandDto>) {
        brands.clear()
        brands.addAll(newBrands)
        notifyDataSetChanged()
    }
}