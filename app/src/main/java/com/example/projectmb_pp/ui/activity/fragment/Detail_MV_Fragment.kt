package com.example.projectmb_pp.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projectmb_pp.R
import com.example.projectmb_pp.databinding.FragmentDetailMvBinding
import com.example.projectmb_pp.model.Property
import com.example.projectmb_pp.repository.LikeItemsRepository
import com.example.projectmb_pp.repository.SavedItemsRepository

class Detail_MV_Fragment : Fragment() {

    private var _binding: FragmentDetailMvBinding? = null
    private val binding get() = _binding!!
    private lateinit var property: Property

    companion object {
        const val ARG_PROPERTY = "arg_property"

        fun newInstance(property: Property): Detail_MV_Fragment {
            val fragment = Detail_MV_Fragment()
            val args = Bundle()
            args.putParcelable(ARG_PROPERTY, property)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the repository
        LikeItemsRepository.initialize(requireContext())
        SavedItemsRepository.initialize(requireContext())

        // Retrieve property object from arguments
        property = arguments?.getParcelable(ARG_PROPERTY) ?: return

        // Restore the liked state and like count
        property.isLiked = LikeItemsRepository.isLiked(property.id)
        property.likeCount = LikeItemsRepository.getLikeCount(property.id)

        // Update UI with property details
        updateUI()

        // Check if the property is saved
        val isSaved = SavedItemsRepository.getSavedItems().contains(property)
        updateSaveButton(isSaved)

        // Handle like button click
        binding.likeButton.setOnClickListener {
            if (!property.isLiked) {
                property.likeCount++
                property.isLiked = true
                LikeItemsRepository.setLikeCount(property.id, property.likeCount)
                LikeItemsRepository.setLiked(property.id, true)
                updateLikeDislikeCounts()
                updateLikeButtonIcon()
                Toast.makeText(context, "Liked Successful", Toast.LENGTH_SHORT).show()

            }
        }

        // Handle unlike button click
        binding.unlikeButton.setOnClickListener {
            if (property.isLiked && property.likeCount > 0) {
                property.likeCount--
                property.isLiked = false
                LikeItemsRepository.setLikeCount(property.id, property.likeCount)
                LikeItemsRepository.setLiked(property.id, false)
                updateLikeDislikeCounts()
                updateLikeButtonIcon()
                Toast.makeText(context, "Unliked Successful", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle save button click
        binding.saveButton.setOnClickListener {
            if (isSaved) {
                SavedItemsRepository.removeSavedItem(property)
                updateSaveButton(false)
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            } else {
                SavedItemsRepository.addLikedItem(property)
                updateSaveButton(true)
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        // Set title and description
        binding.textViewTitle.text = property.title
        binding.textViewDescription.text = property.synopsis
        binding.releaseDate.text = property.release_date

        // Update like and dislike counts
        updateLikeDislikeCounts()
        updateLikeButtonIcon()
        // Update save button state
        updateSaveButton(SavedItemsRepository.getSavedItems().contains(property))
        // WebView Setup
        setupWebView(property.movie_url)
    }

    private fun updateLikeDislikeCounts() {
//        binding.txtCount.text = property.likeCount.toString()
        binding.txtCount.text = "${property.likeCount} Like"

        // Update dislike count TextView if you have one
        // binding.dislikeCountText.text = property.dislikeCount.toString()
    }

    private fun updateLikeButtonIcon() {
        if (property.isLiked) {
            binding.likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24) // Liked icon
        } else {
            binding.likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24) // Unliked icon
        }
    }

    private fun updateSaveButton(isSaved: Boolean) {
        if (isSaved) {
            binding.saveButton.setImageResource(R.drawable.baseline_favorite_24) // Liked icon
        } else {
            binding.saveButton.setImageResource(R.drawable.baseline_favorite_whrite_24) // Unliked icon
        }
    }

    private fun setupWebView(videoUrl: String) {
        val webView = binding.webView
        val progressBar = binding.progressBar

        if (videoUrl.isNotEmpty()) {
            webView.webViewClient = WebViewClient()
            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true

            // Enable full-screen video playback
            webView.webChromeClient = object : WebChromeClient() {
                private var customView: View? = null
                private var originalSystemUiVisibility: Int = 0
                private var originalOrientation: Int = 0
                private var customViewCallback: CustomViewCallback? = null

                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    if (customView != null) {
                        callback?.onCustomViewHidden()
                        return
                    }

                    customView = view
                    originalSystemUiVisibility = activity?.window?.decorView?.systemUiVisibility ?: 0
                    originalOrientation = activity?.requestedOrientation ?: 0
                    customViewCallback = callback

                    (activity?.window?.decorView as ViewGroup).addView(
                        customView,
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )

                    activity?.window?.decorView?.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

                    activity?.requestedOrientation = originalOrientation
                }

                override fun onHideCustomView() {
                    (activity?.window?.decorView as ViewGroup).removeView(customView)
                    customView = null
                    activity?.window?.decorView?.systemUiVisibility = originalSystemUiVisibility
                    activity?.requestedOrientation = originalOrientation
                    customViewCallback?.onCustomViewHidden()
                    customViewCallback = null
                }
            }

            webView.loadUrl(videoUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
