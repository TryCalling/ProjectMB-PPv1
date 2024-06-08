package com.example.projectmb_pp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmb_pp.databinding.ItemViewFavmoviesBinding
import com.example.projectmb_pp.model.Property

class FavoriteAdapter(
    private var data: List<Property>,
    private val onItemClick: (Property) -> Unit,
    private val onItemUnliked: (Property) -> Unit


) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val BASE_URL = "http://13.228.32.137:8888/" // Add your base URL here

    inner class FavoriteViewHolder(private val binding: ItemViewFavmoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.titleMV.text = property.title
            binding.textView4.text = property.synopsis

            val fullUrl = BASE_URL + property.poster_url
            Log.d("MyAdapter", "Loading image URL: ${property.poster_url}")


            // Load image if needed
            Glide.with(binding.root)
                .load(fullUrl)
                .centerCrop()
                .into(binding.imageView2)

            // Handle item click to show detail screen
            binding.root.setOnClickListener {
                onItemClick(property)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemViewFavmoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<Property>) {
        data = newData
        notifyDataSetChanged()

    }
}

