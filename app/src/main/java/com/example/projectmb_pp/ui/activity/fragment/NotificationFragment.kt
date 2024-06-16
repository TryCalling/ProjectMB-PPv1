package com.example.projectmb_pp.ui.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListenerAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectmb_pp.R
import com.example.projectmb_pp.adapter.NotificationAdapter
import com.example.projectmb_pp.databinding.FragmentNotificationBinding
import com.example.projectmb_pp.model.Property
import com.example.projectmb_pp.service.Api
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(context)
        val notificationAdapter = NotificationAdapter(emptyList()) { property ->
            onNotificationClicked(property)
        }
        binding.notificationRecyclerView.adapter = notificationAdapter

        fetchNotifications(notificationAdapter)

        // Smooth hide/show of toolbar on scroll
        binding.notificationRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isToolbarVisible = true

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && isToolbarVisible) {
                    hideToolbar()
                } else if (dy < 0 && !isToolbarVisible) {
                    showToolbar()
                }
            }

            private fun hideToolbar() {
                ViewCompat.animate(binding.tvNotification)
                    .translationY(-binding.tvNotification.height.toFloat())
                    .alpha(0f)
                    .setDuration(500)
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationEnd(view: View) {
                            isToolbarVisible = false
                        }
                    })
                    .start()
            }

            private fun showToolbar() {
                ViewCompat.animate(binding.tvNotification)
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(500)
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationEnd(view: View) {
                            isToolbarVisible = true
                        }
                    })
                    .start()
            }
        })
    }

    private fun fetchNotifications(adapter: NotificationAdapter) {
        lifecycleScope.launch {
            try {
                val response = Api.retrofitService.getAllData().awaitResponse()
                if (response.isSuccessful) {
                    val properties = response.body() ?: emptyList()
                    adapter.updateNotifications(properties)
                } else {
                    Log.e("NotificationFragment", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NotificationFragment", "Exception: ${e.message}", e)
            }
        }
    }

    private fun onNotificationClicked(property: Property) {
        val fragment = Detail_MV_Fragment.newInstance(property)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

