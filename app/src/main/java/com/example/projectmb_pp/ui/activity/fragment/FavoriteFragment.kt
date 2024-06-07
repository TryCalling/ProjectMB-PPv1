package com.example.projectmb_pp.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectmb_pp.R
import com.example.projectmb_pp.adapter.FavoriteAdapter
import com.example.projectmb_pp.databinding.FragmentFavoriteBinding
import com.example.projectmb_pp.model.Property
import com.example.projectmb_pp.repository.LikedItemsRepository

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
//        favoriteAdapter = FavoriteAdapter(LikedItemsRepository.getLikedItems(), { property ->
//            showDetailScreen(property)
//        }, { property ->
//            updateList()
//        })

        // Initialize RecyclerView and Adapter
        favoriteAdapter = FavoriteAdapter(LikedItemsRepository.getLikedItems().toList(), { property ->
            showDetailScreen(property)
        }, { property ->
            updateList()
        })

        binding.recyclerviewLiked.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoriteAdapter
        }
    }

    private fun updateList() {
        favoriteAdapter.updateData(LikedItemsRepository.getLikedItems().toList())
    }

    private fun showDetailScreen(property: Property) {
        val detailFragment = Detail_MV_Fragment.newInstance(property)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
