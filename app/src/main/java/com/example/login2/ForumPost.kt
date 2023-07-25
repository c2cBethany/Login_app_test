package com.example.login2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.login2.databinding.ActivityForumPostBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ForumPost : AppCompatActivity() {

    private lateinit var binding: ActivityForumPostBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForumPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference.child("forum posts")

        binding.uploadButton.setOnClickListener {
            uploadText()
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun uploadText() {
        binding.progressBar.visibility = View.VISIBLE
        val text = hashMapOf("text post" to binding.postForum.text.toString(),
            "date" to FieldValue.serverTimestamp())
        firebaseFirestore.collection("forum posts").add(text)
            .addOnSuccessListener { Toast.makeText(
                this,
                "Uploaded text Successfully",
                Toast.LENGTH_SHORT
            ).show() }
            .addOnFailureListener { Toast.makeText(
                this,
                "Uploaded text unsuccessfully",
                Toast.LENGTH_SHORT
            ).show() }

        binding.progressBar.visibility = View.GONE
        binding.postForum.setText("")

    }
}