package com.example.projectmb_pp.ui.activity.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projectmb_pp.databinding.FragmentSettingBinding


class SettingFragment(
    private val restartActivityUnit: ()-> Unit
) : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.languageValue.setOnClickListener {
            showLanguageDialog()
        }

        return view
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Khmer", "Chinese")
        val languageCodes = arrayOf("en", "km", "zh") // Corrected Chinese language code to 'zh'
        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setItems(languages) { _, which ->
                val selectedLanguage = languageCodes[which]
                changeLanguage(selectedLanguage)

            }
            .create()
            .show()
    }

    private fun changeLanguage(language: String) {
        Log.d("SettingFragment", "Changing language to: $language")
       // LocaleHelper.setLocale(requireContext(), language)
        saveLocale(language)
        restartActivity()
    }

    private fun saveLocale(language: String) {
        val prefs = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    private fun restartActivity() {
        restartActivityUnit()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
