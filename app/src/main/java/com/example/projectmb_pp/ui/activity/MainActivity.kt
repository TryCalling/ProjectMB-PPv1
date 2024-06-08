package com.example.projectmb_pp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projectmb_pp.R
import com.example.projectmb_pp.databinding.ActivityMainBinding
import com.example.projectmb_pp.repository.SavedItemsRepository
import com.example.projectmb_pp.ui.activity.fragment.ContactUsFragment
import com.example.projectmb_pp.ui.activity.fragment.FavoriteFragment
import com.example.projectmb_pp.ui.activity.fragment.HomeFragment
import com.example.projectmb_pp.ui.activity.fragment.NotificationFragment
import com.example.projectmb_pp.ui.activity.fragment.ProfileFragment
import com.example.projectmb_pp.ui.activity.fragment.SettingFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var db: FirebaseFirestore
    private var callback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add callback for handling back press ========new
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Custom back pressed logic
            }
        }

        // Initialize LikedItemsRepository
        SavedItemsRepository.initialize(applicationContext)

        onBackPressedDispatcher.addCallback(this, callback!!)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up LogOut with Firebase
        firebaseAuth = FirebaseAuth.getInstance()

        // Initialize Firebase components
        db = FirebaseFirestore.getInstance()
//        Style ti 1
//            val bottomNavigationView = binding.btnnavView
//            bottomNavigationView.setOnItemSelectedListener { ... }

        // Style ti 2
        binding.btnnavView.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.iconHome -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.iconFav -> {
                    replaceFragment(FavoriteFragment())
                    true
                }
                R.id.iconProfile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.iconNotification -> {
                    replaceFragment(NotificationFragment())
                    true
                }
                else -> false
            }

        }
        replaceFragment(HomeFragment())

        //set click open navigationLayOut
        binding.navigationDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }

        // Initialize NavigationView and DrawerToggle
        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        // Initialize NavigationView and View Version App
        val menu = navigationView.menu
        val versionItem = menu.findItem(R.id.navD_Version)
        versionItem?.title = "Version 1.0.0"

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener(this)

        // Load the user's name and email from Firestore and update UI
        loadUserData()

    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences = newBase.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val locale = Locale(sharedPreferences.getString("My_Lang","en")!!)
        Log.d("language",sharedPreferences.getString("My_Lang","en")!!)
        Locale.setDefault(locale)
        val context = languageChange(newBase,locale)
        super.attachBaseContext(context)
    }

    private fun languageChange(context: Context, locale: Locale): Context {
        var tempContext = context
        val res = tempContext.resources
        val configuration =res.configuration

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(locale)
            val localList = LocaleList(locale)
            LocaleList.setDefault(localList)
            configuration.setLocales(localList)
            tempContext = tempContext.createConfigurationContext(configuration)
        }else{
            configuration.locale = locale
            res.updateConfiguration(configuration,res.displayMetrics)
        }
        return tempContext
    }

    private fun loadUserData() {
        // Get the current user ID
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user ID is not null before proceeding
        userId?.let { uid ->
            // Get reference to the user document in Firestore
            val userRef = db.collection("users").document(uid)

            // Retrieve user data
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Get user's name and email from Firestore
                        val name = documentSnapshot.getString("name")
                        val email = documentSnapshot.getString("email")
                        val profileImageUrl = documentSnapshot.getString("profileImageUrl")

                        // Update UI with user's name and email
                        updateNavigationHeader(name, email, profileImageUrl!!)
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateNavigationHeader(name: String?, email: String?, cardprofile: String) {
        // Update TextViews in the navigation header with user's name and email
        val headerView = binding.navView.getHeaderView(0)
        val txName: TextView = headerView.findViewById(R.id.txName)
        val txEmail: TextView = headerView.findViewById(R.id.txEmail)
        val imageViewProfile: ImageView = headerView.findViewById(R.id.cartPf)

        txName.text = name
        txEmail.text = email

        // Load profile image using Glide
        if (!cardprofile.isNullOrEmpty()) {
            Glide.with(this)
                .load(cardprofile)
                .placeholder(R.drawable.profile) // Placeholder image
                .into(imageViewProfile)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navD_Home -> {
                replaceFragment(HomeFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "It's Loading...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navD_Fav -> {
                replaceFragment(FavoriteFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "It's Loading...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navD_Help -> {
                replaceFragment(HomeFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "It's Loading...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navD_ContactUs -> {
                replaceFragment(ContactUsFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "It's Loading...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navD_Setting -> {
                replaceFragment(SettingFragment{
                    val intent = intent
                    finish()
                        startActivity(intent)

                })
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "It's Loading...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navD_Profile -> {
                replaceFragment(ProfileFragment())
                drawerLayout.closeDrawer(GravityCompat.START)
                Toast.makeText(this, "It's Loading...", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.navD_LogOut -> {
                val user = firebaseAuth.currentUser
                if (user != null)
                    firebaseAuth.signOut()
                Toast.makeText(this, "User Logged out. Please log in again.", Toast.LENGTH_SHORT).show()
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            // Add more cases for other menu items if needed

            else -> {
                return false
            }
        }

    }

    // Replace Fragment in the container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, close it
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // If the navigation drawer is not open, check if any fragments are in the back stack
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            if (backStackEntryCount > 0) {
                // If there are fragments in the back stack, pop the top fragment
                supportFragmentManager.popBackStack()
            } else {
                // If there are no fragments in the back stack, handle the back press normally
                super.onBackPressed()
            }
        }
    }

//    fun resetBackCallback() {
//        callback?.remove()
//        callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                // Custom back pressed logic
//            }
//        }
//        onBackPressedDispatcher.addCallback(this, callback!!)
//    }



}
