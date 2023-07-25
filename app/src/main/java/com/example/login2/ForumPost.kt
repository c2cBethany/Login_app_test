package com.example.login2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.login2.databinding.ActivityForumPostBinding
import com.example.login2.datasource.DataClassForum
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DateFormat
import java.util.Calendar

class ForumPost : AppCompatActivity() {

    private lateinit var binding: ActivityForumPostBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForumPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()

        binding.uploadButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            uploadData()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun uploadData() {
        val caption = binding.postForum.text.toString()
        val dataClassforum = DataClassForum(caption)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        FirebaseDatabase.getInstance().getReference("ForumPosts").child(currentDate)
            .setValue(dataClassforum).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Uploaded Successfully!", Toast.LENGTH_SHORT)
                        .show()
                    binding.postForum.setText("")
                    binding.progressBar.visibility = View.GONE
                }
            }.addOnFailureListener { e ->
                Toast.makeText(
                    this, e.message.toString(), Toast.LENGTH_SHORT
                ).show()
            }
    }
}