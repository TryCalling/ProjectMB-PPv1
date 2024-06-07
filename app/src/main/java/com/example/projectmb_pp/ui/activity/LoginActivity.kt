package com.example.projectmb_pp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmb_pp.R
import com.example.projectmb_pp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // for set colorBar
        setNavigationBarColor(R.color.white)

        firebaseAuth = FirebaseAuth.getInstance()

        // Set focus listeners for form validation
        emailFocusListener()
        passwordFocusListener()

        //set Btn for next screen
        binding.BtnRegiNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.cirLoginButton.setOnClickListener {
            // Validate the form before attempting to login
            if (isValidForm()) {
                val textEmail = binding.editTextEmail.text.toString().trim()
                val textPassword = binding.editTextPassword.text.toString().trim()

                firebaseAuth.signInWithEmailAndPassword(textEmail, textPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful, retrieve additional data from Firestore if needed
                            retrieveUserDataFromFirestore()

                            Toast.makeText(this, "Successful...", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_LONG).show()
                        }
                    }

            }
        }
    }


    //Not yet
    private fun retrieveUserDataFromFirestore() {
        // Get the current user ID
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user ID is not null before proceeding
        userId?.let {
            // Retrieve user data from the "users" collection with the user ID as the document ID
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    // Handle retrieved user data if needed
                    val username = documentSnapshot.getString("username")
                    val email = documentSnapshot.getString("email")
                    // ... (handle other user data)
                    // Example: Print retrieved username
                    println("Username: $username")

                    // Create a UserProfile object
//                    val userProfile = DataProfile(username)

                    // Pass the user profile data to the next screen or update the UI as needed
//                    showUserProfileOnScreen(userProfile)

                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

//    private fun showUserProfileOnScreen(dataProfile: DataProfile) {
//        // Update the UI with the user profile data
//        // For example, you can set text views with the username and email
//        binding.textName.text = userProfile.name
//        binding.textEmail.text = userProfile.email
//
//    }


    private fun isValidForm(): Boolean {
        // Validate email and password using the existing validation functions
        val emailError = validEmail()
        val passwordError = validPassword()

        // Display errors if any
        if (emailError != null || passwordError != null) {
            showValidationErrors(emailError, passwordError)
            return false
        }

        return true
    }

    private fun showValidationErrors(emailError: String?, passwordError: String?) {
        // Display validation errors in an AlertDialog
        var message = ""
        if (emailError != null) {
            message += "\n\nEmail: $emailError"
        }
        if (passwordError != null) {
            message += "\n\nPassword: $passwordError"
        }

        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->
                // do nothing
            }
            .show()
    }

    private fun emailFocusListener() {
        binding.editTextEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.textInputEmail.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.editTextEmail.text.toString()
        return if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            "Invalid Email Address"
        } else null
    }

    private fun passwordFocusListener() {
        binding.editTextPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.textInputPassword.helperText = validPassword()
            }
        }
    }
    private fun validPassword(): String? {
        val passwordText = binding.editTextPassword.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }
        return null
    }

    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //Add new for testing
    override fun onPause() {
        super.onPause()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //Add new for testing
    override fun onResume() {
        super.onResume()
        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun setNavigationBarColor(colorId: Int) {
        window.navigationBarColor = resources.getColor(colorId, theme)
    }
}