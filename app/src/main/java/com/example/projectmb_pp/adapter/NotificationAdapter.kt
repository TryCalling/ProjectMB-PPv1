package com.example.projectmb_pp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectmb_pp.databinding.ItemViewNotificationBinding
import com.example.projectmb_pp.model.Property

class NotificationAdapter(
    private var notifications: List<Property>,
    private val onItemClick: (Property) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    companion object {
        private const val BASE_URL = "http://13.228.32.137:8888/"
    }

    inner class NotificationViewHolder(val binding: ItemViewNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Property) {
            binding.notificationTitle.text = notification.title
            binding.notificationContent.text = notification.synopsis
            binding.notificationTime.text = notification.release_date
//            binding.notificationTime.text = "${notification.release_date} Minutes ago"

            val fullUrl = BASE_URL + notification.poster_url
            Log.d("NotificationAdapter", "Loading image URL: $fullUrl")

            Glide.with(binding.root)
                .load(fullUrl)
                .centerCrop()
                .into(binding.notificationImage)

            binding.root.setOnClickListener {
                onItemClick(notification)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemViewNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun updateNotifications(newNotifications: List<Property>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }
}
