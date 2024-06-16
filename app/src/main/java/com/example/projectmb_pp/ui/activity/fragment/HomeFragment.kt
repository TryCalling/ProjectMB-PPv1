package com.example.projectmb_pp.ui.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.projectmb_pp.R
import com.example.projectmb_pp.adapter.MyAdapter
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
    private var showingAll = false

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
            SlideModel("https://twt-thumbs.washtimes.com/media/image/2022/12/04/gg22-movies-900_c2-0-3003-1750_s1770x1032.jpg?71b58e388ab29c2a7847401d3c7757678ee5e861"),
            SlideModel("https://www.soundandvision.com/images/styles/600_wide/public/516movies.promo_.jpg"),
            SlideModel("https://images.thedirect.com/media/article_full/spider-man-no-way-home-blu-ray.jpg"),
            SlideModel("https://i.ytimg.com/vi/RJoxvXQ7sJs/maxresdefault.jpg"),
            SlideModel("https://greenhouse.hulu.com/app/uploads/sites/11/Comedy-Movies-for-Kids-Hulu.jpg"),
            SlideModel("https://static1.srcdn.com/wordpress/wp-content/uploads/2023/11/despicable-me-movies.jpg"),
            SlideModel("https://i.ebayimg.com/images/g/DdIAAOSwMLdgznPF/s-l1200.webp"),
            SlideModel("https://img.freepik.com/premium-photo/4k-movie-concept_103577-3976.jpg"),

//            SlideModel("https://www.wooribank.com.kh/wp-content/uploads/2023/11/Major_Web-2-1.jpg"),
//            SlideModel("https://pbs.twimg.com/media/ECjugfFUIAILg7I.jpg"),
//            SlideModel("https://i.ytimg.com/vi/yNiA6mis7Nk/maxresdefault.jpg"),
//            SlideModel("https://www.retailnews.asia/wp-content/uploads/2015/08/major-cineplex.jpg"),
//            SlideModel("https://coolbeans.sgp1.digitaloceanspaces.com/legend-cinema-prod/c22d5817-ca3c-4e6d-b119-68fd0f15f5f3.jpeg")
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

        // Set up "Show All" button click
//        binding.txshowAll.setOnClickListener {
//            if (showingAll) {
//                getAllData()
//                binding.txshowAll.text = "Show All"
//            } else {
//                fetchHorrorMovies()
//            }
//            showingAll = !showingAll
//        }
        // Set up Spinner
        setupSpinner()

    }

    private fun setupSpinner() {
        val spinner: Spinner = binding.genreSpinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genre_array,
            R.layout.spinner_item // Custom layout for spinner items

        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> getAllData() // Show All
                    1 -> fetchMoviesByGenre(1, "Horror Movies") // Horror
                    2 -> fetchMoviesByGenre(2, "Love Story") // Funny
                    3 -> fetchMoviesByGenre(3, "Funny") // Funny
                    4 -> fetchMoviesByGenre(4, "Anime") // Anime
                    5 -> fetchMoviesByGenre(5, "Action") // Action
                    6 -> fetchMoviesByGenre(6, "Adventure") // Adventure
                    7 -> fetchMoviesByGenre(7, "Romantic") // Romantic
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
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

    private fun fetchMoviesByGenre(genreId: Int, genreName: String) {
        Api.retrofitService.getMoviesByGenre(genreId).enqueue(object : Callback<List<Property>> {
            override fun onResponse(call: Call<List<Property>>, response: Response<List<Property>>) {
                if (response.isSuccessful) {
                    val properties = response.body()
                    if (properties != null) {
                        Log.d("HomeFragment", "$genreName movies fetched successfully: ${properties.size} items")
                        myAdapter.updateData(properties.map { it.copy(isLoading = false) })
                    } else {
                        Log.e("HomeFragment", "Response body is null")
                    }
                } else {
                    Log.e("HomeFragment", "Failed to fetch $genreName movies: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
                Log.e("HomeFragment", "Error fetching $genreName movies", t)
            }
        })
    }

//    private fun fetchHorrorMovies() {
//        Api.retrofitService.getMoviesByGenre(1).enqueue(object : Callback<List<Property>> {
//            override fun onResponse(call: Call<List<Property>>, response: Response<List<Property>>) {
//                if (response.isSuccessful) {
//                    val properties = response.body()
//                    if (properties != null) {
//                        Log.d("HomeFragment", "Horror movies fetched successfully: ${properties.size} items")
//                        myAdapter.updateData(properties.map { it.copy(isLoading = false) })
//                        binding.txshowAll.text = "Horror MV"
//                    } else {
//                        Log.e("HomeFragment", "Response body is null")
//                    }
//                } else {
//                    Log.e("HomeFragment", "Failed to fetch horror movies: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
//                Log.e("HomeFragment", "Error fetching horror movies", t)
//            }
//        })
//    }

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
