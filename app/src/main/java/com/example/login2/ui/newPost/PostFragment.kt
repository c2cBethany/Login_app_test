package com.example.login2.ui.newPost

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.login2.R
import com.example.login2.databinding.FragmentPostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null
    private lateinit var firebaseFirestore: FirebaseFirestore
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.uploadImage.setImageURI(it)
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseFirestore = FirebaseFirestore.getInstance()

        initVars()
        registerClickEvents()

        return root
    }

    private fun registerClickEvents() {
        binding.uploadButton.setOnClickListener {
            uploadImage()
        }

        binding.selectButton.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private fun uploadImage() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        binding.progressBar.visibility = View.VISIBLE
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        map["caption"] = binding.postCaption.text.toString()

                        firebaseFirestore.collection("images").add(map)
                            .addOnCompleteListener { firestoreTask ->

                                if (firestoreTask.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Uploaded successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        firestoreTask.exception?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                                binding.progressBar.visibility = View.GONE
                                binding.uploadImage.setImageResource(R.drawable.code2college)

                            }
                    }
                } else {
                    Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT)
                        .show()
                    binding.progressBar.visibility = View.GONE
                    binding.uploadImage.setImageResource(R.drawable.code2college)
                }
            }
        }
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}