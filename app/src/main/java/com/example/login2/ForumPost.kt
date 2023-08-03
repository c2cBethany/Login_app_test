package com.example.login2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.login2.databinding.ActivityForumPostBinding
import com.example.login2.datasource.DataClassForum
import com.example.login2.datasource.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.util.Calendar

class ForumPost : AppCompatActivity() {

    private lateinit var binding: ActivityForumPostBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReferenceUser: DatabaseReference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForumPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val caption = binding.postForum.text.toString()
            firebaseAuth = FirebaseAuth.getInstance()
            val userId = firebaseAuth.currentUser?.uid
            databaseReferenceUser = FirebaseDatabase.getInstance().getReference("Users")

            if (userId.toString().isNotEmpty()) {
                databaseReferenceUser.child(userId.toString()).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        user = snapshot.getValue(User::class.java)!!

                        val dataClassforum = DataClassForum(caption, user.firstName + " " + user.lastName)
                        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
                        FirebaseDatabase.getInstance().getReference("ForumPosts").child(currentDate)
                            .setValue(dataClassforum).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@ForumPost, "Uploaded Successfully!", Toast.LENGTH_SHORT)
                                        .show()
                                    binding.postForum.setText("")
                                    binding.progressBar.visibility = View.GONE
                                }
                            }.addOnFailureListener { e ->
                                Toast.makeText(
                                    this@ForumPost, e.message.toString(), Toast.LENGTH_SHORT
                                ).show()
                            }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@ForumPost,
                            "Failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }

}