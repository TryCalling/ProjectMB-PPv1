package com.example.projectmb_pp.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectmb_pp.R
import com.example.projectmb_pp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // for set colorBar
        setNavigationBarColor(R.color.white)

        //Connect with firebase
        firebaseAuth = FirebaseAuth.getInstance()

        binding.BtnLoginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.cirRegisterButton.setOnClickListener {
            val textName = binding.editTextName.text.toString()
            val textEmail = binding.editTextEmail.text.toString()
            val textMobile = binding.editTextMobile.text.toString()
            val textPassword = binding.editTextPassword.text.toString()
            val textRetypePassword = binding.editTextRetypePassword.text.toString()

            if (isValidForm(textName, textEmail, textMobile, textPassword, textRetypePassword)) {
                if (textPassword == textRetypePassword) {

                    firebaseAuth.createUserWithEmailAndPassword(textEmail, textPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // User creation successful, now store additional data in Firestore
                                storeUserDataInFirestore(textName, textEmail, textMobile)

                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                            }
                        }

                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isValidForm(
        name: String,
        email: String,
        mobile: String,
        password: String,
        retypePassword: String
    ): Boolean {
        // Validate each field and show errors if any
        val nameError = validName(name)
        val emailError = validEmail(email)
        val mobileError = validMobile(mobile)
        val passwordError = validPassword(password)
        val retypePasswordError = validRetypePassword(password, retypePassword)

        // Display errors if any
        if (nameError != null || emailError != null || mobileError != null || passwordError != null || retypePasswordError != null) {
            showValidationErrors(nameError, emailError, mobileError, passwordError, retypePasswordError)
            return false
        }

        return true
    }

    private fun showValidationErrors(
        nameError: String?,
        emailError: String?,
        mobileError: String?,
        passwordError: String?,
        retypePasswordError: String?
    ) {
        // Display validation errors in an AlertDialog
        var message = ""
        if (nameError != null) {
            message += "\n\nName: $nameError"
        }
        if (emailError != null) {
            message += "\n\nEmail: $emailError"
        }
        if (mobileError != null) {
            message += "\n\nMobile: $mobileError"
        }
        if (passwordError != null) {
            message += "\n\nPassword: $passwordError"
        }
        if (retypePasswordError != null) {
            message += "\n\nRetype Password: $retypePasswordError"
        }

        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay") { _, _ ->

            }
            .show()
    }

    private fun validName(name: String): String? {
        return if (name.isEmpty()) {
            "Name is required"
        } else null
    }

    private fun validEmail(email: String): String? {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid Email Address. Please enter a valid email address."
        } else null
    }

    private fun validMobile(mobile: String): String? {
        return if (mobile.isEmpty()) {
            "Mobile is required"
        } else null
    }

    private fun validPassword(password: String): String? {
        val passwordLengthError = "Minimum 8 Character Password"
        val uppercaseError = "Must Contain 1 Upper-case Character"
        val lowercaseError = "Must Contain 1 Lower-case Character"
        val specialCharError = "Must Contain 1 Special Character (@#\$%^&+=)"

        return when {
            password.length < 8 -> passwordLengthError
            !password.matches(".*[A-Z].*".toRegex()) -> uppercaseError
            !password.matches(".*[a-z].*".toRegex()) -> lowercaseError
            !password.matches(".*[@#\$%^&+=].*".toRegex()) -> specialCharError
            else -> null
        }
    }

    private fun validRetypePassword(password: String, retypePassword: String): String? {
        return if (password != retypePassword) {
            "Passwords do not match"
        } else null
    }

    private fun storeUserDataInFirestore(name: String, email: String, mobile: String) {
        // Create a HashMap to store user data
        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "mobile" to mobile
        )

        // Get the current user ID
        val userId = firebaseAuth.currentUser?.uid

        // Check if the user ID is not null before proceeding
        userId?.let {
            // Set the user data in the "users" collection with the user ID as the document ID
            db.collection("users").document(userId)
                .set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "User data stored in Firestore.", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error storing user data: $e", Toast.LENGTH_LONG).show()
                }
        }
    }

    //Add new for testing
    override fun onDestroy() {
        super.onDestroy()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //Add new for testing
//    override fun onResume() {
//        super.onResume()
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }

////not yet
//private fun register(name: String, email: String, password: String, phoneNumber: String) {
//    val databaseReference = FirebaseDatabase.getInstance().getReference("users")
//
//    databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
//        ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            if (!dataSnapshot.exists()) {
//                // User with the same email does not exist, proceed with registration
//                val userId = databaseReference.push().key
//                val dataProfile = DataProfile(userId, name, email, password, phoneNumber)
//                databaseReference.child(userId!!).setValue(dataProfile)
//
//                Toast.makeText(this@RegisterActivity, "Registration successful!", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//                finish()
//            } else {
//                // User with the same email already exists
//                Toast.makeText(this@RegisterActivity, "User with this email already exists!", Toast.LENGTH_LONG).show()
//            }
//        }
//
//        override fun onCancelled(databaseError: DatabaseError) {
//            // Handle error
//            Toast.makeText(this@RegisterActivity, "Registration failed: " + databaseError.message, Toast.LENGTH_LONG).show()
//        }
//    })
//}

    fun setNavigationBarColor(colorId: Int) {
        window.navigationBarColor = resources.getColor(colorId, theme)
    }

}
