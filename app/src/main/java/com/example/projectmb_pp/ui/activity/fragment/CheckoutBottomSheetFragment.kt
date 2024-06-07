package com.example.projectmb_pp.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmb_pp.databinding.BottomsheetPaymentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CheckoutBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the close button by its ID
        val closeButton1 = binding.imgClose
        val closeButton2 = binding.imgclose2

        // Set click listener for the close button
        closeButton1.setOnClickListener {
            dismiss() // Dismiss the bottom sheet when the second close button is clicked
        }

        // Set click listener for the second close button
        closeButton2.setOnClickListener {
            dismiss() // Dismiss the bottom sheet when the close button is clicked
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}