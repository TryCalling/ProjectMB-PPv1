package com.example.projectmb_pp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmb_pp.databinding.ActivityIntroscreenBinding

class IntroScreenActivity : AppCompatActivity() {

    private var binding : ActivityIntroscreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroscreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Simulate a delay using a Handler instead of Thread.sleep
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }, 1800)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null //Release the ViewBinding
    }

}