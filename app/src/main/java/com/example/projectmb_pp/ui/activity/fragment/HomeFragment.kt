package com.example.projectmb_pp.ui.activity.fragment

import MyAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.projectmb_pp.R
import com.example.projectmb_pp.databinding.FragmentHomeBinding
import com.example.projectmb_pp.model.Property
import com.example.projectmb_pp.service.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var myAdapter: MyAdapter

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var isLinearView = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ImageSliderView setup
        val imageList = arrayListOf(
            SlideModel("https://www.wooribank.com.kh/wp-content/uploads/2023/11/Major_Web-2-1.jpg"),
            SlideModel("https://pbs.twimg.com/media/ECjugfFUIAILg7I.jpg"),
            SlideModel("https://i.ytimg.com/vi/yNiA6mis7Nk/maxresdefault.jpg"),
            SlideModel("https://www.retailnews.asia/wp-content/uploads/2015/08/major-cineplex.jpg"),
            SlideModel("https://coolbeans.sgp1.digitaloceanspaces.com/legend-cinema-prod/c22d5817-ca3c-4e6d-b119-68fd0f15f5f3.jpeg")
        )
        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
//        // RecyclerView setup LinearView
//        recyclerView = binding.recyclerViewProduct
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        myAdapter = MyAdapter(emptyList()) { property ->
//            showDetailScreen(property)
//        }-----------------------------------

//        // RecyclerView setup old
//        binding.recyclerViewProduct.setHasFixedSize(true)
//        // Initially, set grid layout manager
//        binding.recyclerViewProduct.layoutManager = GridLayoutManager(requireContext(), 3)
//        myAdapter = MyAdapter(emptyList(), {property ->
//            showDetailScreen(property)
//        }, isLinearView)
//        binding.recyclerViewProduct.adapter = myAdapter
//        // Load data from API
//        getAllData()

        // RecyclerView setup new
        binding.recyclerViewProduct.setHasFixedSize(true)
        layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerViewProduct.layoutManager = layoutManager

        myAdapter = MyAdapter(emptyList(), { property ->
            showDetailScreen(property)
        }, isLinearView)

        binding.recyclerViewProduct.adapter = myAdapter

        // Load data from API
        getAllData()

        // Switch layout button setup
        binding.changeLayoutButton.setOnClickListener {
            isLinearView = !isLinearView
            toggleLayoutManager()
        }

    }

//    private fun toggleLayoutManager() {
//        binding.recyclerViewProduct.layoutManager = if (isLinearView) {
//            LinearLayoutManager(requireContext())
//        } else {
//            GridLayoutManager(requireContext(), 3)
//        }
//        binding.recyclerViewProduct.layoutManager = layoutManager
//    }

    private fun toggleLayoutManager() {
        binding.recyclerViewProduct.layoutManager = if (isLinearView) {
            LinearLayoutManager(requireContext())
        } else {
            GridLayoutManager(requireContext(), 3)
        }
        myAdapter.setLinearView(isLinearView)
    }


//    private fun getAllData() { old
//        Api.retrofitService.getAllData().enqueue(object : Callback<List<Property>> {
//            override fun onResponse(
//                call: Call<List<Property>>,
//                response: Response<List<Property>>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()?.let { properties ->
//                        myAdapter.updateData(properties.map { it.copy(isLoading = false) })
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//    }

    //new
    private fun getAllData() {
        Api.retrofitService.getAllData().enqueue(object : Callback<List<Property>> {
            override fun onResponse(
                call: Call<List<Property>>,
                response: Response<List<Property>>
            ) {
                if (response.isSuccessful) {
                    val properties = response.body()
                    if (properties != null) {
                        Log.d("HomeFragment", "Data fetched successfully: ${properties.size} items")
                        myAdapter.updateData(properties.map { it.copy(isLoading = false) })
                    } else {
                        Log.e("HomeFragment", "Response body is null")
                    }
                } else {
                    Log.e("HomeFragment", "Failed to fetch data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching data", t)
            }
        })
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
