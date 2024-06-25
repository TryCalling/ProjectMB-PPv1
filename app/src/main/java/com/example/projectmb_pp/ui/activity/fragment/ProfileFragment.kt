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

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private var selectedImageUri: Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { uri ->
                Log.d("ProfileFragment", "Selected Image URI: $uri")
                if (isAdded && context != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding.imageViewProfile)
                }
                selectedImageUri = uri
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        currentUser?.let { user ->
            val userId: String = user.uid

            val userRef = db.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userProfile = documentSnapshot.toObject(DataProfile::class.java)
                        Log.d("ProfileFragment", "User data retrieved: $userProfile")

                        if (isAdded && context != null) {
                            userProfile?.let { profile ->
                                binding.apply {
                                    textName.text = profile.name
                                    textEmail.editText?.setText(profile.email)
                                    textNBPhone.editText?.setText(profile.mobile)

                                    profile.profileImageUrl?.let { imageUrl ->
                                        Glide.with(requireContext())
                                            .load(imageUrl)
                                            .into(binding.imageViewProfile)
                                    }
                                }
                            }
                        }
                    } else {
                        Log.d("ProfileFragment", "User document does not exist")
                    }
                }
                .addOnFailureListener { exception ->
                    if (isAdded && context != null) {
                        Log.e("ProfileFragment", "Error retrieving user data: ${exception.message}")
                        Toast.makeText(requireContext(), "Error retrieving user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.layoutUpdatePF.setOnClickListener {
            openImagePicker()
        }

        binding.btnUpdate.setOnClickListener {
            updateUserInfo()
            uploadProfileImage()
            if (isAdded && context != null) {
                Toast.makeText(requireContext(), "Updated your profile successfully...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private fun uploadProfileImage() {
        selectedImageUri?.let { uri ->
            val imageName = UUID.randomUUID().toString()
            val imageRef = storageRef.child("profileImages/$imageName")

            val uploadTask = imageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    val currentUser = firebaseAuth.currentUser
                    currentUser?.let { user ->
                        val userId = user.uid
                        val userRef = db.collection("users").document(userId)
                        userRef.update("profileImageUrl", url.toString())
                            .addOnSuccessListener {
                                if (isAdded && context != null) {
                                    Toast.makeText(requireContext(), "Profile image updated successfully!", Toast.LENGTH_SHORT).show()
                                    Glide.with(requireContext())
                                        .load(url)
                                        .into(binding.imageViewProfile)
                                }
                            }
                            .addOnFailureListener { exception ->
                                if (isAdded && context != null) {
                                    Toast.makeText(requireContext(), "Failed to update profile image: ${exception.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }.addOnFailureListener { exception ->
                if (isAdded && context != null) {
                    Toast.makeText(requireContext(), "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            if (isAdded && context != null) {
                Toast.makeText(requireContext(), "No Data selected!", Toast.LENGTH_SHORT).show()
            }
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
                if (isAdded && context != null) {
                    Toast.makeText(requireContext(), "User info updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                if (isAdded && context != null) {
                    Toast.makeText(requireContext(), "Failed to update user info: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
