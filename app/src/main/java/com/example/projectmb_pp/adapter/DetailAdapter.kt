package com.example.projectmb_pp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmb_pp.databinding.FragmentDetailMvBinding
import com.example.projectmb_pp.model.Property

class DetailAdapter(
    private var data: List<Property>
) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    inner class DetailViewHolder(private val binding: FragmentDetailMvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.textViewTitle.text = property.title
            binding.textViewDescription.text = property.synopsis
            // Load other details as needed

            // Example to load image if you have an image URL
//            Glide.with(binding.root)
//                .load(property.poster_url)
//                .centerCrop()
//                .into(binding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = FragmentDetailMvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val property = data[position]
        holder.bind(property)
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<Property>) {
        data = newData
        notifyDataSetChanged()
    }
}
