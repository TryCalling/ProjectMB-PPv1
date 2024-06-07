package com.example.projectmb_pp.ui.activity.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projectmb_pp.databinding.FragmentProfileBinding
import com.example.projectmb_pp.model.DataProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference

    private var selectedImageUri: Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.let { intent ->
                val selectedImageUri = intent.data
                selectedImageUri?.let {
                    Log.d("ProfileFragment", "Selected Image URI: $it")
                    Glide.with(requireContext())
                        .load(it)
                        .into(binding.imageViewProfile)
                    this.selectedImageUri = it
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        // Check if user is authenticated
        currentUser?.let { user ->
            val userId: String = user.uid

            // Get reference to the user document in Firestore
            val userRef = db.collection("users").document(userId)

            // Retrieve user data
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Convert document snapshot to UserProfile object
                        val userProfile = documentSnapshot.toObject(DataProfile::class.java)
                        Log.d("ProfileFragment", "User data retrieved: $userProfile")

                        // Update UI with user data
                        userProfile?.let { profile ->
                            binding.apply {
                                textName.text = profile.name
                                textEmail.editText?.setText(profile.email)
                                textNBPhone.editText?.setText(profile.mobile)

                                profile.profileImageUrl?.let { imageUrl -> // Check and load existing profile image
                                    Glide.with(requireContext())
                                        .load(imageUrl)
                                        .into(binding.imageViewProfile)

                                }
                            }
                        }

                    } else {
                        Log.d("ProfileFragment", "User document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                    Log.e("ProfileFragment", "Error retrieving user data: ${exception.message}")
                    Toast.makeText(requireContext(), "Error retrieving user data: ${exception.message}", Toast.LENGTH_SHORT).show()

                }
        }

        // Set click listener for updating profile image
        binding.layoutUpdatePF.setOnClickListener {
            openImagePicker()
        }

        binding.btnUpdate.setOnClickListener {
            // Update email and mobile
            updateUserInfo()
            // Upload profile image
            uploadProfileImage()
        }

    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }


//Old
//    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val data = result.data
//            data?.let { intent ->
//                val selectedImageUri = intent.data
//                selectedImageUri?.let {
//                    binding.imageViewProfile.setImageURI(it)
//                    this.selectedImageUri = it
//                }
//            }
//        }
//    }



    private fun uploadProfileImage() {
        selectedImageUri?.let { uri ->
            val imageName = UUID.randomUUID().toString()
            val imageRef = storageRef.child("profileImages/$imageName")

            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    // Update profile image URL in Firestore
                    val currentUser = firebaseAuth.currentUser
                    currentUser?.let { user ->
                        val userId = user.uid
                        val userRef = db.collection("users").document(userId)
                        userRef.update("profileImageUrl", url.toString())
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Profile image updated successfully!", Toast.LENGTH_SHORT).show()
                                // Update UI with new profile image

//                                updateUserProfileUI(url.toString()) // Call updateUserProfileUI to update profile image

                                Glide.with(requireContext())
                                    .load(url)
                                    .into(binding.imageViewProfile) // Update the ImageView
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(requireContext(), "Failed to update profile image: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "No Data selected!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun updateUserInfo() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val userRef = db.collection("users").document(userId)
            val email = binding.textEmail.editText?.text.toString()
            val mobile = binding.textNBPhone.editText?.text.toString()

            userRef.update(mapOf(
                "email" to email,
                "mobile" to mobile
            )).addOnSuccessListener {
                Toast.makeText(requireContext(), "User info updated successfully!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Failed to update user info: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
