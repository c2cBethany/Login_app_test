package com.example.login2.ui.newPost

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.login2.DataClass
import com.example.login2.databinding.FragmentPostBinding
import com.example.login2.datasource.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    var imageURL: String? = null
    var uri: Uri? = null
    private lateinit var user: User
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                uri = data!!.data
                binding.uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectButton.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }
        binding.uploadButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            saveData()
        }

        return root
    }

    private fun saveData() {
        val storageReference = FirebaseStorage.getInstance().reference.child("Post Images")
            .child(uri!!.lastPathSegment!!)
        storageReference.putFile(uri!!).addOnSuccessListener { taskSnapshot ->
            val uriTask = taskSnapshot.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            imageURL = urlImage.toString()
            uploadData()
        }.addOnFailureListener { e ->
            Toast.makeText(
                requireContext(), e.message.toString(), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun uploadData() {
        val caption = binding.postCaption.text.toString()
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val userId = firebaseAuth.currentUser?.uid
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        if (userId.toString().isNotEmpty()) {
            databaseReference.child(userId.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)!!
                    val dataClass = DataClass(caption, user.firstName + " " + user.lastName, imageURL)
                    FirebaseDatabase.getInstance().getReference("Posts").child(currentDate)
                        .setValue(dataClass).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Uploaded Successfully!", Toast.LENGTH_SHORT)
                                    .show()
                                binding.postCaption.setText("")
                                binding.progressBar.visibility = View.GONE
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(), e.message.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}