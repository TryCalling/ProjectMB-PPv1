package com.example.projectmb_pp.ui.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectmb_pp.databinding.FragmentContactusBinding

class ContactUsFragment : Fragment() {

    private lateinit var binding: FragmentContactusBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContactusBinding.inflate(inflater, container, false)
        return binding.root
    }
}
