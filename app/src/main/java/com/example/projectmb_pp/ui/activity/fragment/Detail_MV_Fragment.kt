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
import com.example.projectmb_pp.repository.LikedItemsRepository

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
        LikedItemsRepository.initialize(requireContext())

        // Retrieve property object from arguments
        property = arguments?.getParcelable(ARG_PROPERTY) ?: return

        // Check if the property is liked
        val isLiked = LikedItemsRepository.getLikedItems().contains(property)
        updateLikeButton(isLiked)

        // Set title and description
        binding.textViewTitle.text = property.title
        binding.textViewDescription.text = property.synopsis

        // WebView Setup
        setupWebView(property.movie_url)

        // Handle like button click
        binding.likeButton.setOnClickListener {
            LikedItemsRepository.addLikedItem(property)
            updateLikeButton(true)
        }

        // Handle unlike button click
        binding.unlikeButton.setOnClickListener {
            LikedItemsRepository.removeLikedItem(property)
            updateLikeButton(false)
            // You might want to navigate back to the previous screen or update UI accordingly
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updateLikeButton(isLiked: Boolean) {
        if (isLiked) {
            binding.likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24) // Liked icon
            Toast.makeText(context, "Liked Successful", Toast.LENGTH_SHORT).show()
        } else {
            binding.likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24) // Unliked icon
            Toast.makeText(context, "Unliked Successful", Toast.LENGTH_SHORT).show()
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
